package org.example.controller;

import org.example.model.Notifications;
import org.example.service.EmailNotifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private EmailNotifications emailNotifications;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody Notifications request) {
        emailNotifications.sendEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");
    }
}

