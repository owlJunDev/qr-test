package com.example.qr.Controller;

import java.util.*;
import java.util.regex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.qr.DTO.CellRequest;
import com.example.qr.Entity.Cell;
import com.example.qr.Repository.CellRepository;

@RestController
public class TabRestController {

    public static Iterable<MatchResult> allMatches(final Pattern p, final CharSequence input) {
        return new Iterable<MatchResult>() {
            public Iterator<MatchResult> iterator() {
                return new Iterator<MatchResult>() {

                    final Matcher matcher = p.matcher(input);
                    MatchResult pending;

                    public boolean hasNext() {

                        if (pending == null && matcher.find()) {
                            pending = matcher.toMatchResult();
                        }
                        return pending != null;
                    }

                    public MatchResult next() {

                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }

                        MatchResult next = pending;
                        pending = null;
                        return next;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public String firstExp(String exp) {
        int i;
        boolean isRetry;

        Double[] param = new Double[2];
        do {
            isRetry = false;
            for (MatchResult matchs : allMatches(Pattern.compile("([\\d\\.\\d]+[\\/\\*][\\d\\.\\d]+)"), exp)) {
                isRetry = true;
                i = 0;
                for (MatchResult match : allMatches(Pattern.compile("[\\d\\.\\d]+"), matchs.group())) {
                    param[i++] = Double.parseDouble(match.group());
                }

                if (matchs.group().contains("*")) {
                    exp = exp.replace(matchs.group(), String.valueOf(param[0] * param[1]));
                } else {
                    exp = exp.replace(matchs.group(), String.valueOf(param[0] / param[1]));
                    System.out.println(exp);
                }
            }
        } while (isRetry);
        return exp;
    }

    public String secondExp(String exp) {
        int i = 0;

        Double[] param = new Double[2];
        for (MatchResult matchs : allMatches(Pattern.compile("([\\d\\.\\d]+[\\+\\-][\\d\\.\\d]+)"), exp)) {
            for (MatchResult match : allMatches(Pattern.compile("[\\d\\.\\d]+"), matchs.group())) {
                param[i++] = Double.parseDouble(match.group());
            }
            if (matchs.group().contains("+")) {
                exp = exp.replace(matchs.group(), String.valueOf(param[0] + param[1]));
            } else {
                exp = exp.replace(matchs.group(), String.valueOf(param[0] - param[1]));
            }
        }
        return exp;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Autowired
    private CellRepository cellRepository;

    @PostMapping(value = "/cells")
    public void updateCells(@RequestBody CellRequest dataCell) {
        Cell cell = new Cell(dataCell.cell, dataCell.formula);
        String exp = (dataCell.formula).replace("=", "");
        String cellsExp = new String();

        for (MatchResult match : allMatches(Pattern.compile("([A-Z]\\d)"), exp)) {
            cellsExp += match.group() + " ";
        }
        if (!cellsExp.isEmpty()) {

            Iterable<Cell> cellsRep = cellRepository.findByNameIn(Arrays.asList(cellsExp.split("(\\s)")));

            for (Cell c : cellsRep) {
                exp = exp.replace(c.getName(), c.getValue());
            }
        }

        do {
            exp = firstExp(exp);
            exp = secondExp(exp);

            for (MatchResult match : allMatches(Pattern.compile("\\([\\d\\.\\d]+\\)"), exp)) {
                String str = match.group();
                str = str.replace("(", "");
                str = str.replace(")", "");

                exp = exp.replace(match.group(), str);
            }
        } while (!isNumeric(exp));

        cell.setValue(exp);
        cellRepository.save(cell);
    }

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public Object getCells() {
        Iterable<Cell> cells = cellRepository.findAll();

        return cells;
    }
}