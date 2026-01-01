package org.example.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.client.NotificationClient;
import org.example.model.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceCaller {

    @Autowired
    private NotificationClient notificationClient;

    @CircuitBreaker(
            name = "notificationServiceCB",
            fallbackMethod = "notificationFallback"
    )
    public void send(NotificationDTO request) {
        notificationClient.sendNotification(request);
    }

    public String notificationFallback(NotificationDTO request, Throwable ex) {
        return "Notification failed, skipping email";
    }
}

