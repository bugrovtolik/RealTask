package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Contract;
import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.service.ContractService;
import com.abugrov.helpinghand.service.TaskService;
import com.abugrov.helpinghand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final ContractService contractService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, ContractService contractService, UserService userService) {
        this.taskService = taskService;
        this.contractService = contractService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String getCreate(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("task", "null");
        model.addAttribute("hasCreditCard", StringUtils.hasText(user.getCreditCardNumber()));

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

        if (!task.isCashless()) {
            task.setActive(true);
        }

        task.setAuthor(user);

        taskService.saveTask(task);

        return "redirect:/main";
    }

    @PreAuthorize("#user.id == #task.authorId")
    @GetMapping("/{taskId}/pay")
    @Transactional
    public String pay(@AuthenticationPrincipal User user,
                      @PathVariable("taskId") Task task) {
        if (userService.updateCredit(user, user.getCredit() - task.getPrice())) {
            task.setPaid(true);
            task.setActive(true);
        }

        return "redirect:/task/" + task.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #task.authorId")
    @GetMapping("/{taskId}/edit")
    public String getEdit(@AuthenticationPrincipal User user,
                          @PathVariable("taskId") Task task, Model model) {
        model.addAttribute(task);
        model.addAttribute("taskId", task.getId());
        model.addAttribute("hasCreditCard", StringUtils.hasText(user.getCreditCardNumber()));

        return "taskEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #oldTask.authorId")
    @PostMapping("/{taskId}/edit")
    @Transactional
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

        if (!oldTask.isCashless() && newTask.isCashless()) {
            oldTask.setActive(false);
        } else if (oldTask.isCashless() && !newTask.isCashless()) {
            if (oldTask.isPaid()) {
                if (userService.updateCredit(user, user.getCredit() + oldTask.getPrice())) {
                    oldTask.setPaid(false);
                }
            }
            oldTask.setActive(true);
        }

        taskService.updateTask(oldTask, newTask);
        contractService.deleteByTask(oldTask);

        return "redirect:/task/" + oldTask.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #task.authorId")
    @PostMapping("/{taskId}/delete")
    @Transactional
    public String delete(@AuthenticationPrincipal User user,
                         @PathVariable("taskId") Task task) {
        if (task.isPaid()) {
            userService.updateCredit(user,user.getCredit() + task.getPrice());
        }

        contractService.deleteByTask(task);
        taskService.deleteTask(task);

        return "redirect:/main";
    }

    @GetMapping("/{taskId}")
    public String view(@AuthenticationPrincipal User user,
                       @PathVariable("taskId") Task task,
                       Model model) {
        if (task.isActive()) {
            List<Contract> contracts;
            Contract accepted = contractService.findByTaskAndAccepted(task);

            if (task.isActive() && accepted == null && (user.getId().equals(task.getAuthorId()) || user.isAdmin())) {
                contracts = contractService.findByTask(task);
                if (contracts != null && !contracts.isEmpty()) {
                    model.addAttribute("contracts", contracts);
                }
            }

            if (accepted == null && contractService.findByUserAndTask(user, task) == null && task.isActive()) {
                model.addAttribute("allowExec", true);
            }

            model.addAttribute("accepted", accepted);
            if (accepted != null && (user.isAdmin() || user.getId().equals(accepted.getUser().getId()))) {
                model.addAttribute("secret", true);
            }
        } else {
            Contract completed = contractService.findByTaskAndCompleted(task);
            if (completed != null) {
                model.addAttribute("completed", completed);
            }
        }
        model.addAttribute("task", task);

        return "task";
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #task.authorId == #user.id")
    @GetMapping("/{taskId}/complete")
    @Transactional
    public String complete(
            @AuthenticationPrincipal User user,
            @PathVariable("taskId") Task task
    ) {
        Contract contract = contractService.findByTaskAndAccepted(task);

        contract.setTime(LocalDateTime.now());
        contract.setCompleted(true);
        contractService.saveContract(contract);

        contractService.deleteByTaskAndNotAccepted(task);
        taskService.deactivate(task);

        if (task.isPaid()) {
            contract.getUser().setCredit(contract.getUser().getCredit() + task.getPrice());
        }

        return "redirect:/task/" + task.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #task.authorId == #user.id")
    @GetMapping("/{taskId}/incomplete")
    @Transactional
    public String incomplete(
            @AuthenticationPrincipal User user,
            @PathVariable("taskId") Task task
    ) {
        Contract contract = contractService.findByTaskAndAccepted(task);

        contract.setTime(LocalDateTime.now());
        contract.setCompleted(false);
        contractService.saveContract(contract);

        contractService.deleteByTaskAndNotAccepted(task);
        taskService.deactivate(task);

        if (task.isPaid()) {
            user.setCredit(user.getCredit() + task.getPrice());
        }

        return "redirect:/task/" + task.getId();
    }
}
