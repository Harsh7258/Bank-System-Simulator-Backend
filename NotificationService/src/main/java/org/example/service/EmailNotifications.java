package org.example.service;

import org.example.model.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailNotifications {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Notifications request) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(request.getToEmail());
        mail.setSubject("Transaction Alert");
        mail.setText(
                "Account: " + request.getAccountNo() + "\n" +
                        "Type: " + request.getTransactionType() + "\n" +
                        "Amount: " + request.getAmount() + "\n" +
                        "Message: " + request.getMessage() + "\n"+
                        "Date: " + new Date()
        );

        mailSender.send(mail);
    }
}
