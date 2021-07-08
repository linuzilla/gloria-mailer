package com.example.mailer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("application")
public class MailerProperties {
    private String template;
    private String dataFile;
    private String emailField;
    private String nameField;
    private boolean testOnly;
    private boolean debug;
    private String recipientWhenDebug;


    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getEmailField() {
        return emailField;
    }

    public void setEmailField(String emailField) {
        this.emailField = emailField;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public boolean isTestOnly() {
        return testOnly;
    }

    public void setTestOnly(boolean testOnly) {
        this.testOnly = testOnly;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getRecipientWhenDebug() {
        return recipientWhenDebug;
    }

    public void setRecipientWhenDebug(String recipientWhenDebug) {
        this.recipientWhenDebug = recipientWhenDebug;
    }
}
