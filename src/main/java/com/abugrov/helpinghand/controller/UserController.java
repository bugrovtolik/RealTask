package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Role;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) throws IOException {
        userService.saveUser(user, username, form, file);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("updateAvatar")
    public String updateAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttrs
    ) throws IOException {
        if (userService.updateAvatar(user, file)) {
            redirectAttrs.addFlashAttribute("avatarMessage", "Изображение профиля успешно изменено!");
            redirectAttrs.addFlashAttribute("messageType", "success");
        } else {
            redirectAttrs.addFlashAttribute("avatarMessage", "Это файл не подходит!");
            redirectAttrs.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/user/profile";
    }

    @PostMapping("updatePassword")
    public String updatePassword(
            @AuthenticationPrincipal User user,
            @RequestParam String oldpass,
            @RequestParam String newpass,
            @RequestParam String newpass2,
            RedirectAttributes redirectAttrs
    ) {
        if (newpass.equals(newpass2)) {
            if (userService.isActualPassword(user, oldpass)) {
                userService.updatePassword(user, newpass);
                redirectAttrs.addFlashAttribute("passwordMessage", "Пароль успешно изменён!");
                redirectAttrs.addFlashAttribute("messageType", "success");
            } else {
                redirectAttrs.addFlashAttribute("passwordMessage","Текущий пароль введён неверно!");
                redirectAttrs.addFlashAttribute("messageType", "danger");
            }
        } else {
            redirectAttrs.addFlashAttribute("passwordMessage", "Пароли не совпадают!");
            redirectAttrs.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/user/profile";
    }

    @GetMapping("recover/{code}")
    public String recoverCode(Model model, @PathVariable String code) {
        UserDetails user = userService.findByActivationCode(code);

        if (user == null) {
            model.addAttribute("messageType","danger");
            model.addAttribute("message", "Ссылка недействительна или устарела!");
            return "index";
        }

        model.addAttribute("username", user.getUsername());
        return "recover";
    }

    @PostMapping("recover")
    public String recover(
            Model model,
            @RequestParam String newpass,
            @RequestParam String newpass2,
            @RequestParam String username
    ) {
        User user = userService.findByUsername(username);

        if (newpass.equals(newpass2)) {
            userService.updatePassword(user, newpass);
            userService.cleanActivationCode(user);
            model.addAttribute("message", "Пароль успешно изменён!");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Пароли не совпадают!");
            model.addAttribute("messageType", "danger");
            model.addAttribute("username", user.getUsername());

            return "recover";
        }

        return "login";
    }

    @GetMapping("lostPassword")
    public String forgotPassword() {
        return "lostPassword";
    }

    @PostMapping("lostPassword")
    public String sendNewPassword(Model model, @RequestParam String email) {
        if (StringUtils.isEmpty(email)) {
            return "lostPassword";
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("messageType","danger");
            model.addAttribute("message", "Пользователь не найден!");
        } else {
            userService.recover(user);
            model.addAttribute("messageType","success");
            model.addAttribute("message", "Письмо с ссылкой для создания нового пароля отправлено Вам на почту!");
        }

        return "index";
    }
}