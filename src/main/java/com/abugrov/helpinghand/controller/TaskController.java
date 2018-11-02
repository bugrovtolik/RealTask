package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Comment;
import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.service.CommentService;
import com.abugrov.helpinghand.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final CommentService commentService;

    @Autowired
    public TaskController(TaskService taskService, CommentService commentService) {
        this.taskService = taskService;
        this.commentService = commentService;
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
    @Transactional
    public String delete(@AuthenticationPrincipal User user,
                         @PathVariable("taskId") Task task) {
        commentService.deleteByTask(task);
        taskService.deleteTask(task);

        return "redirect:/main";
    }

    @GetMapping("/{taskId}")
    public String view(@AuthenticationPrincipal User user,
                       @PathVariable("taskId") Task task,
                       Model model) {
        List<Comment> comments = Collections.emptyList();

        if (user.getId().equals(task.getAuthorId())) {
            comments = commentService.findAll();
        }

        model.addAttribute("task", task);
        model.addAttribute("comments", comments);

        return "task";
    }

    @PreAuthorize("!hasAuthority('ADMIN') AND #user.id != #task.authorId")
    @PostMapping("/{taskId}/execute")
    public String execute(
            @AuthenticationPrincipal User user,
            @PathVariable("taskId") Task task,
            @RequestParam(required = false, defaultValue = "") String text
    ) {
        Comment comment = new Comment(task, user, LocalDateTime.now(), text);

        commentService.saveComment(comment);

        return "redirect:/task/" + task.getId();
    }
}
