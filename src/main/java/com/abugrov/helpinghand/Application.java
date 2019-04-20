package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {
    static {
        System.out.println("before " + System.getProperty("jdk.http.auth.tunneling.disabledSchemes"));
        try {
            Runtime.getRuntime().exec("cmd /c set JAVA_TOOL_OPTIONS=-Djdk.http.auth.tunneling.disabledSchemes=");
        } catch (IOException e) {
            System.out.println("EXCEPTION");
        }
        System.out.println("after " + System.getProperty("jdk.http.auth.tunneling.disabledSchemes"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}