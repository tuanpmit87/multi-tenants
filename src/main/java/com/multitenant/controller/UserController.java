package com.multitenant.controller;

import com.multitenant.entities.User;
import com.multitenant.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/v1/api")
public class UserController {
    @Value("${tenant.file.path}")
    private String filePath;

    private final UserService userService;
    private final RefreshEndpoint refreshEndpoint;

    public UserController(UserService userService, RefreshEndpoint refreshEndpoint) {
        this.userService = userService;
        this.refreshEndpoint = refreshEndpoint;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User request) {
        User user = userService.saveUser(request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create-tenant-property/{tenantName}")
    public String createTenantProperty(@PathVariable String tenantName) {
        try {
            Path path = Paths.get(filePath + "/" + tenantName + ".properties");
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            Properties properties = new Properties();
            properties.setProperty("name", tenantName);
            properties.setProperty("datasource.url", "jdbc:mysql://localhost:3306/" + tenantName);
            properties.setProperty("datasource.username", "root");
            properties.setProperty("datasource.password", "root");
            properties.setProperty("datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");

            try (FileWriter fileWriter = new FileWriter(path.toFile())) {
                properties.store(fileWriter, "User Properties File");
            }

            refreshEndpoint.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Tenant properties file created and refresh triggered.";
    }
}
