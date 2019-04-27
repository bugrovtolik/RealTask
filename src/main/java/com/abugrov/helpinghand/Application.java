package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        System.out.println("JAVA_TOOL_OPTIONS:   " + System.getenv("JAVA_TOOL_OPTIONS"));
        ProcessBuilder pb = new ProcessBuilder(args);
        Map<String, String> env = pb.environment();
        env.put("JAVA_TOOL_OPTIONS", System.getenv("JAVA_TOOL_OPTIONS") + " -Djdk.http.auth.proxying.disabledSchemes= -Djdk.http.auth.tunneling.disabledSchemes=");
        Process process = pb.start();
        System.out.println("JAVA_TOOL_OPTIONS:   " + System.getenv("JAVA_TOOL_OPTIONS"));

        SpringApplication.run(Application.class, args);
        System.out.println("DESTROYEDD");
        process.destroy();
    }
}