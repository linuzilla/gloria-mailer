package com.example.mailer.utils;

import javax.mail.Multipart;
import java.util.function.Consumer;

public interface MailService {
    void sendmail(String email, String name, String subject, Consumer<Multipart> consumer);
}