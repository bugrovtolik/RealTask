package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Role;
import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskRepo taskRepo;

    @Autowired
    public TaskController(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @GetMapping("/create")
    public String create() {
        return "taskCreate";
    }

    @PostMapping("/create")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Task task,
                      BindingResult bindingResult,
                      Model model
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtility.getErrors(bindingResult);
            model.mergeAttributes(errors);

            return "taskCreate";
        }

        task.setAuthor(user);

        taskRepo.save(task);

        Iterable<Task> tasks = taskRepo.findAll();
        model.addAttribute("tasks", tasks);

        return "main";
    }

    @GetMapping("{task}")
    public String view(@PathVariable Task task, Model model) {
        model.addAttribute("task", task);

        return "task";
    }
}
