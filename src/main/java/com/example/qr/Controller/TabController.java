package com.example.qr.Controller;

import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class TabController {


    @GetMapping("")
    public String main(@RequestParam(required = false, defaultValue = "World") String name, Model model) {

        String[] numbers = new String[] { "1", "2", "3", "4" };
        String[] chars = new String[] { "A", "B", "C", "D" };


        model.addAttribute("numbers", numbers);
        model.addAttribute("chars", chars);
        return "index";
    }
}
