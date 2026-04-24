package com.arnor4eck.medicinenotes.service.mail_sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile({"dev"})
public class DevSender implements SimpleMailSender {
    @Override
    public void send(String to, String subject, String text) {
        log.info("Sending email to {}, subject {}, text {}", to, subject, text);
    }
}
