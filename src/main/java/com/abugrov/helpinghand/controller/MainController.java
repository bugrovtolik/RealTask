package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.repos.TaskRepo;
import com.abugrov.helpinghand.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    private final TaskService taskService;

    @Autowired
    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String index() { return "redirect:/main"; }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        List<Task> tasks;

        if (StringUtils.hasText(filter)) {
            tasks = taskService.findByTitle(filter);
        } else {
            tasks = taskService.findAll();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);

        return "main";
    }
}