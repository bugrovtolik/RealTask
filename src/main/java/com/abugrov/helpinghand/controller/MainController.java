package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Service;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.repos.ServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final ServiceRepo serviceRepo;

    @Autowired
    public MainController(ServiceRepo serviceRepo) {
        this.serviceRepo = serviceRepo;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Service> services;

        if (filter != null && !filter.isEmpty()) {
            services = serviceRepo.findByTitle(filter);
        } else {
            services = serviceRepo.findAll();
        }

        model.addAttribute("services", services);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String title,
                      @RequestParam String description, Model model) {
        Service service = new Service(title, description, user);
        serviceRepo.save(service);

        Iterable<Service> services = serviceRepo.findAll();
        model.addAttribute("services", services);

        return "main";
    }
}