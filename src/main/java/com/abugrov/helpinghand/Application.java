package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("JAVA_TOOL_OPTIONS: " + System.getenv("JAVA_TOOL_OPTIONS"));
        SpringApplication.run(Application.class, args);
    }
}