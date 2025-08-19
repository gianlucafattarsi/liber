package com.github.gianlucafattarsi.liberapi.application.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final Environment environment;

    private final JavaMailSender javaMailSender;

    @Value("${alert.mail.from}")
    private String from;

    public void send(MailMessage message) {
        if (Arrays.stream(environment.getActiveProfiles())
                  .noneMatch(p -> p.equals("dev"))) {
            sendMail(message);
        } else {
            // In development mode, we log the password instead of sending an email
            logMail(message);
        }

    }

    private void sendMail(MailMessage message) {
        try {

            final MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

            helper.setFrom(from);
            helper.setTo(message.getRecipients()
                                .toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(message.getBody(), true);

            javaMailSender.send(mailMessage);

            log.debug("Mail sent successfully to: {}", String.join(", ", message.getRecipients()));
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e);
        }
    }

    private void logMail(MailMessage message) {
        log.debug("Dev profile active: not sending email");
        log.debug("Message: subject -> {}, bodi -> {}, recipients -> {}",
                message.getSubject(), message.getBody(), String.join(", ", message.getRecipients()));
    }
}
