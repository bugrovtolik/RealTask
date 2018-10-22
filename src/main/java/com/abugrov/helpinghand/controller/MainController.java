package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final TaskRepo taskRepo;

    @Autowired
    public MainController(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Task> tasks;

        if (filter != null && !filter.isEmpty()) {
            tasks = taskRepo.findByTitle(filter);
        } else {
            tasks = taskRepo.findAll();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);

        return "main";
    }
}