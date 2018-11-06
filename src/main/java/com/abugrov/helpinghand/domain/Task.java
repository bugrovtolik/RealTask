package com.abugrov.helpinghand.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Length(max = 255, message = "Заголовок слишком длинный (более 255 символов)")
    private String title;

    @NotBlank
    @Length(max = 2000, message = "Описание слишком длинное (более 2000 символов)")
    private String description;

    @Length(max = 500, message = "Постарайтесь уложиться в 500 символов!")
    private String secret;

    @NotBlank
    private String lat;

    @NotBlank
    private String lng;

    @NotNull
    @FutureOrPresent(message = "Эта дата уже прошла!")
    private LocalDateTime execFrom;

    @NotNull
    @FutureOrPresent(message = "Эта дата уже прошла!")
    private LocalDateTime execTo;

    @NotNull
    @PositiveOrZero
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @AssertTrue(message = "Дата начала выполнения должна быть раньше даты завершения!")
    private boolean isFromBeforeTo() {
        if (execFrom != null && execTo != null) {
            return execFrom.isBefore(execTo);
        }
        return false;
    }

    public Task() {}

    public Task(String title, String description, String secret, String lat, String lng, String execFrom, String execTo, Integer price, User author) {
        this.title = title;
        this.description = description;
        this.secret = secret;
        this.lat = lat;
        this.lng = lng;
        this.execFrom = DomainUtility.toDate(execFrom);
        this.execTo = DomainUtility.toDate(execTo);
        this.price = price;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    
    public String getSecret() {
        return secret;
    }
    
    public String getLat() {
        return lat;
    }
    
    public String getLng() {
        return lng;
    }
    
    public User getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public Long getAuthorId() {
        return author.getId();
    }

    public String getAuthorAvatar() {
        return author.getAvatar();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public void setLat(String lat) {
        this.lat = lat;
    }
    
    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getExecFromFormatted() {
        return DomainUtility.toString(execFrom);
    }

    public void setExecFrom(String execFrom) {
        this.execFrom = DomainUtility.toDate(execFrom);
    }

    public String getExecToFormatted() {
        return DomainUtility.toString(execTo);
    }

    public void setExecTo(String execTo) {
        this.execTo = DomainUtility.toDate(execTo);
    }

    public LocalDateTime getExecFrom() {
        return execFrom;
    }

    public LocalDateTime getExecTo() {
        return execTo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
