package com.dexwin.notesapp.controller.view;

import com.dexwin.notesapp.dtos.AuthDtos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new AuthDtos.SignupRequest());
        return "signup";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new AuthDtos.LoginRequest());
        return "login";
    }

}