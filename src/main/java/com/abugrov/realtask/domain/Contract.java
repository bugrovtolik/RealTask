package com.abugrov.realtask.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private LocalDateTime time;

    @Length(max = 255, message = "Комментарий слишком длинный (более 255 символов)")
    private String text;

    private Boolean accepted;

    private Boolean completed;

    public Contract(Task task, User user, LocalDateTime time, String text) {
        this.task = task;
        this.user = user;
        this.time = time;
        this.text = text;
    }

    public Contract() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public String getUserName() { return user.getUsername(); }

    public String getUserAvatar() { return user.getAvatar(); }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTimeFormatted() {
        return DomainUtility.toString(time);
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
