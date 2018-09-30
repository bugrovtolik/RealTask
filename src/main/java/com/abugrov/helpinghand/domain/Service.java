package com.abugrov.helpinghand.domain;

import javax.persistence.*;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String description;
    private String secret;
    private String lat;
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

    public Integer getId() {
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

    public void setId(Integer id) {
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
