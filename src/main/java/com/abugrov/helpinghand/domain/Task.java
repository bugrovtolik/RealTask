package com.abugrov.helpinghand.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Transient
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm")
    private Date execfrom;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm")
    private Date execto;
    @NotNull
    @PositiveOrZero
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Task() {}

    public Task(String title, String description, String secret, String lat, String lng, String execfrom, String execto, Integer price, User author) throws ParseException {
        this.title = title;
        this.description = description;
        this.secret = secret;
        this.lat = lat;
        this.lng = lng;
        this.execfrom = format.parse(execfrom);
        this.execto = format.parse(execto);
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

    public Date getExecfrom() {
        return execfrom;
    }

    public void setExecfrom(String execfrom) throws ParseException {
        this.execfrom = format.parse(execfrom);
    }

    public Date getExecto() {
        return execto;
    }

    public void setExecto(String execto) throws ParseException {
        this.execto = format.parse(execto);
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
