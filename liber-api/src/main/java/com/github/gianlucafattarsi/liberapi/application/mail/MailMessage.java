package com.github.gianlucafattarsi.liberapi.application.mail;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MailMessage {

    private List<String> recipients;
    private String subject;
    private String body;
}
