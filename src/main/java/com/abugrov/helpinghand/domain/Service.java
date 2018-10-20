package com.abugrov.helpinghand.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Вы забыли указать заголовок!")
    @Length(max = 255, message = "Заголовок слишком длинный (более 255 символов)")
    private String title;
    @NotBlank(message = "Вы забыли указать описание!")
    @Length(max = 2000, message = "Описание слишком длинное (более 2000 символов)")
    private String description;
    @Length(max = 500, message = "Постарайтесь уложиться в 500 символов!")
    private String secret;
    @NotBlank
    private String lat;
    @NotBlank
    private String lng;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Service() {}

    public Service(String title, String description, String secret, String lat, String lng, User user) {
        this.title = title;
        this.description = description;
        this.secret = secret;
        this.lat = lat;
        this.lng = lng;
        this.author = user;
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
}
