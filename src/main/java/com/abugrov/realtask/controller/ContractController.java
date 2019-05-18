package com.abugrov.realtask.controller;

import com.abugrov.realtask.model.Contract;
import com.abugrov.realtask.model.Task;
import com.abugrov.realtask.model.User;
import com.abugrov.realtask.service.ContractService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PreAuthorize("!hasAuthority('ADMIN') AND #user.id != #task.authorId")
    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal User user,
            @RequestParam("taskId") Task task,
            @RequestParam(required = false, defaultValue = "") String text
    ) {
        LocalDateTime time = LocalDateTime.now();
        Contract contract = new Contract(task, user, time, text);

        contractService.saveContract(contract);

        return "redirect:/task/" + task.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #contract.user.id == #user.id")
    @GetMapping("/{contractId}/delete")
    public String delete(
            @AuthenticationPrincipal User user,
            @PathVariable("contractId") Contract contract
    ) {
        contractService.deleteContract(contract);

        return "redirect:/notifications";
    }

    @PreAuthorize("hasAuthority('ADMIN') OR #contract.task.authorId == #user.id")
    @GetMapping("/{contractId}/accept")
    @Transactional
    public String accept(
            @AuthenticationPrincipal User user,
            @PathVariable("contractId") Contract contract
    ) {
        contractService.acceptContract(contract);
        contractService.declineAllExcept(contract);

        return "redirect:/task/" + contract.getTask().getId();
    }
}
