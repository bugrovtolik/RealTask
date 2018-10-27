package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("task", "null");
        return "taskCreate";
    }

    @PostMapping("/create")
    public String create(@AuthenticationPrincipal User user,
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

        taskService.saveTask(task);

        return "redirect:/main";
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #task.authorId")
    @GetMapping("/{taskId}/edit")
    public String getEdit(@AuthenticationPrincipal User user,
                          @PathVariable("taskId") Task task, Model model) {
        model.addAttribute(task);
        model.addAttribute("taskId", task.getId());

        return "taskEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #oldTask.authorId")
    @PostMapping("/{taskId}/edit")
    public String edit(@AuthenticationPrincipal User user,
                       @PathVariable("taskId") Task oldTask,
                       @Valid Task newTask,
                       BindingResult bindingResult,
                       Model model
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtility.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("taskId", oldTask.getId());

            return "taskEdit";
        }

        taskService.updateTask(oldTask, newTask);

        return "redirect:/task/" + oldTask.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #task.authorId")
    @PostMapping("/{taskId}/delete")
    public String delete(@AuthenticationPrincipal User user,
                         @PathVariable("taskId") Task task) {
        taskService.deleteTask(task);

        return "redirect:/main";
    }

    @GetMapping("{taskId}")
    public String view(@PathVariable("taskId") Task task, Model model) {
        model.addAttribute("task", task);

        return "task";
    }
}
