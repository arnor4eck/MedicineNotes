package com.arnor4eck.medicinenotes.service.mail_sender;

public interface SimpleMailSender {
    void send(String to, String subject, String text);
}
