package org.example.service;

import org.example.client.NotificationClient;
import org.example.model.NotificationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceCallerTest {

    @InjectMocks
    private NotificationServiceCaller notificationServiceCaller;

    @Mock
    private NotificationClient notificationClient;

    @Test
    void sendNotification_success() {

        NotificationDTO dto = new NotificationDTO();
        dto.setToEmail("test@gmail.com");

        doNothing().when(notificationClient)
                .sendNotification(dto);

        notificationServiceCaller.send(dto);

        verify(notificationClient, times(1))
                .sendNotification(dto);
    }

}

