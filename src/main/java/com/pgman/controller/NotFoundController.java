package com.pgman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class NotFoundController {

    @RequestMapping("/error-404")
    public String notFound404(Model model) {
        model.addAttribute("title","Page not found");
        return "resource/error404";
    }
    
}
