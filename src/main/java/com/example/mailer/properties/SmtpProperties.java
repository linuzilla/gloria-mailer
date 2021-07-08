package com.example.mailer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("smtp")
public class SmtpProperties {
    private String from;
    private String fromName;
    private String replyTo;
    private String replyToName;
    private boolean useReplyTo;
    private boolean auth;
    private boolean starttls;
    private String host;
    private int port;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyToName() {
        return replyToName;
    }

    public void setReplyToName(String replyToName) {
        this.replyToName = replyToName;
    }

    public boolean isUseReplyTo() {
        return useReplyTo;
    }

    public void setUseReplyTo(boolean useReplyTo) {
        this.useReplyTo = useReplyTo;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isStarttls() {
        return starttls;
    }

    public void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

