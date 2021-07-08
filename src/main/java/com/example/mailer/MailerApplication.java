package com.example.mailer;

import com.example.mailer.properties.MailerProperties;
import com.example.mailer.utils.MailService;
import com.example.mailer.utils.ReadFileUtil;
import com.example.mailer.utils.ReceptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MailerApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MailerApplication.class);
    private final MailService mailService;
    private final MailerProperties properties;
    private final ReceptionService receptionService;

    private record PartContent(String content, String contentType) {
    }

    public MailerApplication(MailService mailService, MailerProperties properties, ReceptionService receptionService) {
        this.mailService = mailService;
        this.properties = properties;
        this.receptionService = receptionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MailerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = ReadFileUtil.readFrom(properties.getTemplate());

        final var props = System.getProperties();
        final var session = Session.getInstance(props, null);

        final var message = new MimeMessage(session, inputStream);
        final var subject = message.getSubject();
        System.out.println("Subject: " + subject);

        final var messageParts = new ArrayList<PartContent>();

        if (message.getContent() instanceof MimeMultipart mimeMultipart) {
            for (var i = 0; i < mimeMultipart.getCount(); i++) {
                final var content = mimeMultipart.getBodyPart(i).getContent();

                if (content instanceof String body) {
                    messageParts.add(new PartContent(body,
                            Arrays.stream(mimeMultipart.getBodyPart(i).getHeader("Content-Type"))
                                    .findFirst()
                                    .orElse("")));
                }
            }
        }

        receptionService.processing((counter, email, name, placeholderMap) -> {
            System.out.println("Name: " + name + ", Email: " + email);
            final var recipient = properties.isDebug() ? properties.getRecipientWhenDebug() : email;

            if (properties.isDebug() && counter > 5) {
                return;
            }

            mailService.sendmail(recipient, name, subject, multipart -> {
                for (var messagePart : messageParts) {
                    try {
                        BodyPart messageBodyPart = new MimeBodyPart();

                        String text = messagePart.content();

                        final var html = messagePart.contentType().matches(".*text/html.*");

                        if (html) {
                            for (var entry : placeholderMap.entrySet()) {
                                final var regex = html ? "&lt;&lt;\\s*" + entry.getKey() + "\\s*&gt;&gt;"
                                        : "<<\\s*" + entry.getKey() + "\\s*>>";

                                logger.trace("Regular express: {}", regex);
                                text = text.replaceAll(regex, entry.getValue());
                            }
                            messageBodyPart.setContent(text, messagePart.contentType());

                            multipart.addBodyPart(messageBodyPart);
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
