package org.example.service;

import org.example.model.Notifications;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationTest {

    @InjectMocks
    private EmailNotifications emailNotifications;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void sendEmail_success() {

        Notifications request = new Notifications();
        request.setToEmail("test@gmail.com");
        request.setAccountNo("ACC123");
        request.setTransactionType("CREDIT");
        request.setAmount(1000.0);
        request.setMessage("Amount credited successfully");

        // when
        emailNotifications.sendEmail(request);

        // then
        verify(mailSender, times(1))
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_verifyEmailContent() {

        Notifications request = new Notifications();
        request.setToEmail("test@gmail.com");
        request.setAccountNo("ACC123");
        request.setTransactionType("DEBIT");
        request.setAmount(500.0);
        request.setMessage("Amount debited successfully");

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailNotifications.sendEmail(request);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage mail = captor.getValue();

        assertEquals("test@gmail.com", mail.getTo()[0]);
        assertEquals("Transaction Alert", mail.getSubject());
        assertTrue(mail.getText().contains("ACC123"));
        assertTrue(mail.getText().contains("DEBIT"));
        assertTrue(mail.getText().contains("500.0"));
        assertTrue(mail.getText().contains("Amount debited successfully"));
    }

}
