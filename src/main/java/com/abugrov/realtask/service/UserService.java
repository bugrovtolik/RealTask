package com.abugrov.realtask.service;

import com.abugrov.realtask.model.Comment;
import com.abugrov.realtask.model.Payment;
import com.abugrov.realtask.model.Role;
import com.abugrov.realtask.model.User;
import com.abugrov.realtask.repos.PaymentRepo;
import com.abugrov.realtask.repos.UserRepo;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Value("${upload.path}")
    private String uploadPath;

    @Value("${hostname}")
    private String hostname;

    private final UserRepo userRepo;
    private final PaymentRepo paymentRepo;
    private final CommentService commentService;
    private final MailSender mailSender;
    private final Cloudinary cloudinary;
    public final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PaymentRepo paymentRepo,
                       CommentService commentService, MailSender mailSender,
                       Cloudinary cloudinary, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.paymentRepo = paymentRepo;
        this.commentService = commentService;
        this.mailSender = mailSender;
        this.cloudinary = cloudinary;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден!");
        }

        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByEmail(user.getEmail());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        String message = String.format(
                "Приветсвуем Вас, %s!\n" +
                "Благодарим за регистрацию на сайте RealTask.\n" +
                "Пожалуйста, проследуйте по следующей ссылке для завершения " +
                "регистрации: http://%s/activate/%s",
                user.getUsername(), hostname, user.getActivationCode()
        );

        sendMessage(user,"Код активации для RealTask", message);

        return true;
    }

    public User findByActivationCode(String code) {
        return userRepo.findByActivationCode(code);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public boolean activateUser(String code) {
        User user = findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        user.setCredit(0);

        userRepo.save(user);

        Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user,
                         String username,
                         Map<String, String> form,
                         MultipartFile file
    ) throws Exception {

        user.setUsername(username);

        updateAvatar(user, file);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);

        Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendMessage(User user, String topic, String message) {
        if (StringUtils.hasText(user.getEmail())) {
            mailSender.send(user.getEmail(), topic, message);
        }
    }

    public void recover(User user) {
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        String message = String.format(
            "Приветсвуем Вас, %s!\n" +
            "Пройдите по ссылке для создания нового пароля: http://%s/user/recover/%s",
            user.getUsername(), hostname, user.getActivationCode()
        );

        sendMessage(user,"Восстановление пароля на RealTask", message);
    }

    public boolean updatePassword(User user, String oldpass, String newpass) {
        if (StringUtils.hasText(newpass) && passwordEncoder.matches(oldpass, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newpass));
            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }

        return false;
    }

    public boolean setNewPassword(User user, String newpass) {
        if (StringUtils.hasText(newpass)) {
            user.setActivationCode(null);
            user.setPassword(passwordEncoder.encode(newpass));
            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }

        return false;
    }

    public boolean updateAvatar(User user, MultipartFile multipartFile) throws Exception {
        if (multipartFile != null && multipartFile.getOriginalFilename() != null
                && !multipartFile.getOriginalFilename().isEmpty()) {

            if (user.getAvatar() != null) {
                cloudinary.api().deleteResources(Collections.singleton(user.getAvatar()), null);
            }

            user.setAvatar(UUID.randomUUID().toString());

            cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.asMap(
                "public_id", user.getAvatar(),
                "transformation", new Transformation().crop("limit").width(50).height(50)
            ));

            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }

        return false;
    }

    public boolean updateUsername(User user, String username) {
        if (StringUtils.hasText(username)) {
            user.setUsername(username);
            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }
        
        return false;
    }

    public boolean updatePhoneNumber(User user, String phoneNumber) {
        if (StringUtils.hasText(phoneNumber)) {
            user.setPhoneNumber(phoneNumber);
            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }

        return false;
    }

    public boolean updateCreditCardNumber(User user, String creditCardNumber) {
        if (StringUtils.hasText(creditCardNumber)) {
            user.setCreditCardNumber(creditCardNumber);
            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }

        return false;
    }

    public boolean updateCredit(User user, Integer credit) {
        if (credit >= 0) {
            user.setCredit(credit);
            userRepo.save(user);

            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }

        return false;
    }

    public boolean existComment(User author, User receiver) {
        return commentService.findByAuthorAndReceiver(author, receiver) != null;
    }

    public void deleteComment(Comment comment) {
        commentService.deleteComment(comment);
    }

    public boolean addComment(User author, User receiver, String text, Integer rating, LocalDateTime time) {
        Comment comment = commentService.findByAuthorAndReceiver(author, receiver);

        if (comment != null) {
            return false;
        }

        comment = new Comment();

        comment.setAuthor(author);
        comment.setReceiver(receiver);
        comment.setText(text);
        comment.setRating(rating);
        comment.setTime(time);

        commentService.saveComment(comment);

        return true;
    }

    public List<Comment> getComments(User user) {
        return commentService.findByReceiver(user);
    }

    public String getRating(List<Comment> comments) {
        int sum = 0;
        for (Comment comment : comments) {
            sum += comment.getRating();
        }

        String result = String.valueOf((double) sum / comments.size());
        return result.length() > 3 ? result.substring(0,3) : result;
    }

    public void savePayment(Payment request) {
        paymentRepo.save(request);
    }

    public void deletePayment(Payment request) {
        paymentRepo.delete(request);
    }

    public List<Payment> getPayments() {
        return paymentRepo.findAll();
    }
}