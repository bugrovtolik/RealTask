package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.Role;
import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.domain.dto.LiqPayResponseDto;
import com.abugrov.helpinghand.service.PaymentService;
import com.abugrov.helpinghand.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PaymentService paymentService;

    public UserController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
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

    @PostMapping("{userId}/paid")
    public void paymentCallback(
            @RequestParam("data") String data,
            @RequestParam("signature") String signature,
            @PathVariable("userId") User user
    ) throws IOException {
        System.out.println("callback " + data + " " + signature);
        if (paymentService.isValidSignature(data, signature)) {
            LiqPayResponseDto resp = paymentService.read(data);
            if (resp.getStatus() == LiqPayResponseDto.Status.sandbox ||
                    resp.getStatus() == LiqPayResponseDto.Status.success) {
                userService.updateCredit(user, user.getCredit() + resp.getAmount());
            }
        }
    }

    @PostMapping("payToUser")
    @Transactional
    public String payToUser(
            @AuthenticationPrincipal User user,
            @RequestParam("amount") Integer amount,
            @RequestParam("by") String by,
            RedirectAttributes redirectAttrs
    ) throws Exception {
        if (user.getCredit() >= amount) {
            boolean success = false;

            if (by.equals("phone")) {
                System.out.println("trying by phone");
                success = paymentService.transferToPhone(user, amount);
                by = " номер телефона ";
            } else if (by.equals("card")) {
                System.out.println("trying by card");
                success = paymentService.transferToPrivatCard(user, amount);
                by = "у кредитную карту ";
            }

            if (success && userService.updateCredit(user, user.getCredit() + amount)) {
                redirectAttrs.addFlashAttribute("paymentMessage", "На Ваш" + by + "успешно переведены " + amount + " грн");
                redirectAttrs.addFlashAttribute("messageType", "success");
            } else {
                redirectAttrs.addFlashAttribute("paymentMessage", "Ошибка!");
                redirectAttrs.addFlashAttribute("messageType", "danger");
            }
        } else {
            redirectAttrs.addFlashAttribute("paymentMessage", "Недостаточно денег!");
            redirectAttrs.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/user/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) throws Exception {
        userService.saveUser(user, username, form, file);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("hasCreditCard", StringUtils.hasText(user.getCreditCardNumber()));

        return "profile";
    }

    @PostMapping("payToService")
    public RedirectView getPayToService(@RequestParam("amount") Integer amount,
                                  @AuthenticationPrincipal User user) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(paymentService.getHref(user, amount));

        return redirectView;
    }

    @PostMapping("updateAvatar")
    public String updateAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttrs
    ) throws Exception {
        if (userService.updateAvatar(user, file)) {
            redirectAttrs.addFlashAttribute("avatarMessage", "Изображение профиля успешно изменено!");
            redirectAttrs.addFlashAttribute("messageType", "success");
        } else {
            redirectAttrs.addFlashAttribute("avatarMessage", "Этот файл не подходит!");
            redirectAttrs.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/user/profile";
    }

    @PostMapping("updateUsername")
    public String updateUsername(
            @AuthenticationPrincipal User user,
            @RequestParam String username,
            RedirectAttributes redirectAttrs
    ) {
        if (userService.updateUsername(user, username)) {
            redirectAttrs.addFlashAttribute("usernameMessage", "Имя и фамилия успешно изменены!");
            redirectAttrs.addFlashAttribute("messageType", "success");
        } else {
            redirectAttrs.addFlashAttribute("usernameMessage", "Вы ничего не ввели!");
            redirectAttrs.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/user/profile";
    }

    @PostMapping("updatePhoneNumber")
    public String updatePhoneNumber(
            @AuthenticationPrincipal User user,
            @RequestParam String phoneNumber,
            RedirectAttributes redirectAttrs
    ) {
        if (userService.updatePhoneNumber(user, phoneNumber)) {
            redirectAttrs.addFlashAttribute("phoneMessage", "Номер телефона успешно изменён!");
            redirectAttrs.addFlashAttribute("messageType", "success");
        } else {
            redirectAttrs.addFlashAttribute("phoneMessage", "Вы ничего не ввели!");
            redirectAttrs.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/user/profile";
    }

    @PostMapping("updateCreditCardNumber")
    public String updateCreditCardNumber(
            @AuthenticationPrincipal User user,
            @RequestParam String creditCardNumber,
            RedirectAttributes redirectAttrs
    ) {
        if (userService.updateCreditCardNumber(user, creditCardNumber)) {
            redirectAttrs.addFlashAttribute("creditCardMessage", "Номер кредитной карты успешно изменён!");
            redirectAttrs.addFlashAttribute("messageType", "success");
        } else {
            redirectAttrs.addFlashAttribute("creditCardMessage", "Номер кредитной карты введён неверно!");
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
            if (userService.updatePassword(user, oldpass, newpass)) {
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
        User user = userService.findByActivationCode(code);

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
            if (userService.setNewPassword(user, newpass)) {
                model.addAttribute("message", "Пароль успешно изменён!");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Вы ничего не ввели!");
                model.addAttribute("messageType", "danger");
                model.addAttribute("username", user.getUsername());

                return "recover";
            }
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
        if (!StringUtils.hasText(email)) {
            model.addAttribute("messageType","danger");
            model.addAttribute("message", " Вы ничего не ввели!");

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