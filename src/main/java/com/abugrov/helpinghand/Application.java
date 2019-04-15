package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        System.out.println("before " + System.getProperty("http.auth.tunneling.disabledSchemes"));
//        System.setProperty("http.auth.tunneling.disabledSchemes","");
//        System.setProperty("http.auth.proxying.disabledSchemes","");
//        System.out.println("after " + System.getProperty("http.auth.tunneling.disabledSchemes"));
        SpringApplication.run(Application.class, args);
    }
}