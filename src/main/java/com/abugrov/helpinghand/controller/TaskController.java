package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.config.PaymentConfig;
import com.abugrov.helpinghand.domain.Contract;
import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.domain.dto.PaymentResponseDto;
import com.abugrov.helpinghand.service.ContractService;
import com.abugrov.helpinghand.service.TaskService;
import com.liqpay.LiqPay;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final PaymentConfig paymentConfig;
    private final TaskService taskService;
    private final ContractService contractService;

    @Autowired
    public TaskController(PaymentConfig paymentConfig, TaskService taskService, ContractService contractService) {
        this.paymentConfig = paymentConfig;
        this.taskService = taskService;
        this.contractService = contractService;
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

    @RequestMapping( value = "/callback", consumes = "application/x-www-form-urlencoded", method = RequestMethod.POST )
    public void callback(
            @RequestParam String data,
            @RequestParam String signature
    ) throws IOException {
        System.out.println("inside");
        if (!paymentConfig.isValidSignature(data, signature)) {
            PaymentResponseDto resp = paymentConfig.read(data);
            System.out.println("valid");
//            if (resp.getOrderId().equals(task.getAuthorId() + "_" + task.getId())) {
//                if (resp.getStatus() == PaymentResponseDto.Status.sandbox) {
//                    task.setPaid(true);
//                    taskService.saveTask(task);
//                }
//            }
        }
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

        taskService.updateTask(oldTask, newTask);
        contractService.deleteByTask(oldTask);

        return "redirect:/task/" + oldTask.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #user.id == #task.authorId")
    @PostMapping("/{taskId}/delete")
    @Transactional
    public String delete(@AuthenticationPrincipal User user,
                         @PathVariable("taskId") Task task) throws Exception {
        if (paymentConfig.payTo(user, task)) {
            contractService.deleteByTask(task);
            taskService.deleteTask(task);

            return "redirect:/main";
        }

        return "redirect:/task/" + task.getId();
    }

    @GetMapping("/{taskId}")
    public String view(@AuthenticationPrincipal User user,
                       @PathVariable("taskId") Task task,
                       Model model) throws Exception {
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
        } else if (!task.isPaid()) {
            if (paymentConfig.isPaid(task)) {
                System.out.println("isPaid");
//                task.setPaid(true);
//                task.setActive(true);
//                taskService.saveTask(task);
                model.addAttribute("payment", paymentConfig.getHref(task));
            } else {
                model.addAttribute("payment", paymentConfig.getHref(task));
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

        return "redirect:/task/" + task.getId();
    }
}
