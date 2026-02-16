package com.example.webapp.controller;

import com.example.webapp.dto.SignupDTO;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.service.SignupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final SignupService signupService;
    private final DeptRepository deptRepository;

    public AuthController(SignupService signupService, DeptRepository deptRepository) {
        this.signupService = signupService;
        this.deptRepository = deptRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signup", new SignupDTO());
        model.addAttribute("departments", deptRepository.findAll());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signup") SignupDTO signupDTO, Model model) {
        try {
            signupService.signup(signupDTO);
            return "redirect:/login?signup=success";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("departments", deptRepository.findAll());
            return "signup";
        }
    }
}
