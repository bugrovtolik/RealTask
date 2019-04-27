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
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("JAVA_TOOL_OPTIONS");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put("JAVA_TOOL_OPTIONS", System.getenv("JAVA_TOOL_OPTIONS") + " -Djdk.http.auth.proxying.disabledSchemes= -Djdk.http.auth.tunneling.disabledSchemes=");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
        System.out.println("JAVA_TOOL_OPTIONS: " + System.getenv("JAVA_TOOL_OPTIONS"));
        SpringApplication.run(Application.class, args);
    }
}