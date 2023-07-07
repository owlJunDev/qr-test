package com.example.qr.Controller;

import org.springframework.ui.Model;

import java.util.*;
import java.util.regex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.qr.Entity.Cell;
import com.example.qr.Repository.CellRepository;

@Controller
public class TabController {
    @Autowired
    private CellRepository cellRepository;


    @GetMapping("")
    public String main(@RequestParam(required = false, defaultValue = "World") String name, Model model) {

        String[] numbers = new String[] { "1", "2", "3", "4" };
        String[] chars = new String[] { "A", "B", "C", "D" };


        model.addAttribute("numbers", numbers);
        model.addAttribute("chars", chars);
        return "index";
    }
}
