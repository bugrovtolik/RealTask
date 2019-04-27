package com.abugrov.helpinghand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        String[] newArgs = new String[args.length + 1];
        for (int i = 0; i < args.length; i++) {
            newArgs[i] = args[i];
        }
        newArgs[args.length] = "-Djdk.http.auth.tunneling.disabledSchemes=";
        System.out.println("newArgs is: " + newArgs[0] + " " + newArgs[1] + " " + args.length);
        SpringApplication.run(Application.class, newArgs);
    }
}