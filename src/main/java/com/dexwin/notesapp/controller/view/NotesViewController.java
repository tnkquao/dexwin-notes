package com.dexwin.notesapp.controller.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotesViewController {

    @GetMapping("/notes")
    public String notesPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged in as: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return "notes";
    }
}