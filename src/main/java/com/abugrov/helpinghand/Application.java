package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    static {
        System.out.println("before " + System.getProperty("jdk.http.auth.tunneling.disabledSchemes",""));
        System.out.println("JAVA before " + System.getProperty("JAVA_TOOL_OPTIONS"));
        System.setProperty("JAVA_TOOL_OPTIONS","jdk.http.auth.tunneling.disabledSchemes=");
        System.out.println("JAVA after " + System.getProperty("JAVA_TOOL_OPTIONS"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}