package com.abugrov.helpinghand.controller;

import com.abugrov.helpinghand.domain.User;
import com.abugrov.helpinghand.domain.dto.CaptchaResponseDto;
import com.abugrov.helpinghand.utility.UserUtility;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    private final UserUtility userUtility;
    private final RestTemplate restTemplate;

    @Autowired
    public RegistrationController(UserUtility userUtility, RestTemplate restTemplate) {
        this.userUtility = userUtility;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttrs
    ) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Вы забыли поставить галочку!");
        }

        boolean isConfirmedEmpty = StringUtils.isEmpty(passwordConfirm);

        if (isConfirmedEmpty) {
            model.addAttribute("password2Error", "Вы забыли подтвердить пароль!");
        } else if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают!");
        }
        if (isConfirmedEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtility.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }

        if (userUtility.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Пользователь с таким email уже зарегистрирован!");
            return "registration";
        }

        if (!userUtility.addUser(user)) {
            model.addAttribute("usernameError", "Такой пользователь уже зарегистрирован!");
            return "registration";
        }

        redirectAttrs.addFlashAttribute("messageType","success");
        redirectAttrs.addFlashAttribute("message","Письмо с кодом подтверждения отправлено вам на почту!");

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userUtility.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType","success");
            model.addAttribute("message","Активация прошла успешно!");
        } else {
            model.addAttribute("messageType","danger");
            model.addAttribute("message","Код активации недействителен или устарел!");
        }

        return "index";
    }
}
