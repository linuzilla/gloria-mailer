package com.example.mailer.utils;

import com.example.mailer.properties.MailerProperties;
import com.example.mailer.properties.SmtpProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Consumer;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private final InternetAddress systemDefaultEmailSender;
    private final Session session;
    private final MailerProperties mailerProperties;
    private final SmtpProperties smtpProperties;

    public MailServiceImpl(MailerProperties mailerProperties, SmtpProperties smtpProperties) throws UnsupportedEncodingException {
        this.mailerProperties = mailerProperties;
        this.smtpProperties = smtpProperties;

        Properties props = new Properties();
        props.put("mail.smtp.auth", smtpProperties.isAuth());
        props.put("mail.smtp.starttls.enable", smtpProperties.isStarttls());
        props.put("mail.smtp.host", smtpProperties.getHost());
        props.put("mail.smtp.port", smtpProperties.getPort());

        logger.info("SMTP: {}:{} (auth: {}, tls: {})", smtpProperties.getHost(), smtpProperties.getPort(), smtpProperties.isAuth(), smtpProperties.isStarttls());

        session = Session.getDefaultInstance(props, null);

        this.systemDefaultEmailSender = new InternetAddress(
                smtpProperties.getFrom(),
                smtpProperties.getFromName(),
                StandardCharsets.UTF_8.name());
    }

    @Override
    public void sendmail(String email, String name, String subject, Consumer<Multipart> consumer) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(systemDefaultEmailSender);

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, name, StandardCharsets.UTF_8.name()));

            if (this.smtpProperties.isUseReplyTo()) {
                message.setReplyTo(new Address[]{
                        new InternetAddress(
                                this.smtpProperties.getReplyTo(),
                                this.smtpProperties.getReplyToName(),
                                StandardCharsets.UTF_8.name())
                });
            }

            // Set Subject: header field
            message.setSubject(subject, StandardCharsets.UTF_8.name());

            Multipart multipart = new MimeMultipart();

            consumer.accept(multipart);

            message.setContent(multipart);

            // Send message
            if (!this.mailerProperties.isTestOnly()) {
                Transport.send(message);
                System.out.println("Sent message to [ " + email + " ] successfully");
            } else {
                System.out.println("Sent message to [ " + email + " ] (testing)");
            }
        } catch (UnsupportedEncodingException | MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
