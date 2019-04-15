package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    static {
        System.out.println("before " + System.getProperty("jdk.http.auth.tunneling.disabledSchemes"));
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}