package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Contract;
import com.abugrov.helpinghand.domain.Task;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.service.ContractService;
import com.abugrov.helpinghand.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    private final TaskService taskService;
    private final ContractService contractService;

    @Autowired
    public MainController(TaskService taskService, ContractService contractService) {
        this.taskService = taskService;
        this.contractService = contractService;
    }

    @GetMapping("/")
    public String index() { return "redirect:/main"; }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Task> pages;

        if (StringUtils.hasText(filter)) {
            pages = taskService.findByTitleActive(filter, pageable);
        } else {
            pages = taskService.findAllActive(pageable);
        }

        model.addAttribute("pages", pages);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @GetMapping("/archive")
    public String archive(
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Task> pages = taskService.findAllNotActive(pageable);

        model.addAttribute("pages", pages);
        model.addAttribute("url", "/archive");

        return "archive";
    }

    @GetMapping("notifications")
    public String notifications(
            Model model,
            @AuthenticationPrincipal User user
    ) {
        List<Contract> contracts = contractService.check(contractService.findByUser(user));

        if (contracts == null) {
            contracts = Collections.emptyList();
        }

        model.addAttribute("contracts", contracts);

        return "notifications";
    }

    @GetMapping("mytasks")
    public String mytasks(
            Model model,
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Task> pages = taskService.findByAuthor(user, pageable);

        model.addAttribute("pages", pages);
        model.addAttribute("url", "/mytasks");

        return "mytasks";
    }
}