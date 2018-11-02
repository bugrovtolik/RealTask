package com.abugrov.helpinghand.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Comment {
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
    private User author;

    @NotNull
    private LocalDateTime posted;

    @Length(max = 255, message = "Комментарий слишком длинный (более 255 символов)")
    private String text;

    public Comment(Task task, User author, LocalDateTime posted, String text) {
        this.task = task;
        this.author = author;
        this.posted = posted;
        this.text = text;
    }

    public Comment() {}

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

    public User getAuthor() {
        return author;
    }

    public String getAuthorName() { return author.getUsername(); }

    public String getAuthorAvatar() { return author.getAvatar(); }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getPostedFormatted() {
        return DomainUtility.toString(posted);
    }

    public LocalDateTime getPosted() {
        return posted;
    }

    public void setPosted(LocalDateTime posted) {
        this.posted = posted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
