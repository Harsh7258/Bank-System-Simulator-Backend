package org.example.client;

import org.example.model.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NotificationService")
public interface NotificationClient {
    @PostMapping("notifications/send")
    void sendNotification(@RequestBody NotificationDTO request);
}
