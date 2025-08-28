package com.github.gianlucafattarsi.liberapi.context.importers.websocket.controller;

import com.github.gianlucafattarsi.liberapi.application.config.locale.LocaleUtilsMessage;
import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.AuthenticationFacade;
import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.Principal;
import com.github.gianlucafattarsi.liberapi.context.importers.websocket.message.LibraryImportMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Locale;

@RequiredArgsConstructor
@Controller
public class LibraryImporterMessageHandler {

    private static final String TOPIC = "/topic/library-import";

    private final SimpMessagingTemplate template;
    private final LocaleUtilsMessage localeUtilsMessage;
    private final AuthenticationFacade authenticationFacade;

    public void sendMessage(boolean isError, String messageKey) {
        sendMessage(isError, messageKey, null);
    }

    public void sendMessage(boolean isError, String messageKey, String[] params) {
        final Principal principal = authenticationFacade.getCurrentPrincipal();

        final String username = principal.username();
        final String message = localeUtilsMessage.getMessage(messageKey, params, Locale.of(principal.lang()));

        template.convertAndSend(TOPIC, new LibraryImportMessage(isError, message, username));
    }
}
