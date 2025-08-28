package com.github.gianlucafattarsi.liberapi.application.config.locale;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@RequiredArgsConstructor
@Component("localeUtilsMessage")
public class LocaleUtilsMessage {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    public String getMessage(String keyMessage, Object[] params) {
        return getMessage(keyMessage, params, Locale.of("en"));
    }

    public String getMessage(String keyMessage, Object[] params, Locale locale) {
        try {
            return messageSource.getMessage(keyMessage, params, locale);
        } catch (final NoSuchMessageException ex) {
            return "??" + keyMessage + "??";
        }
    }

    public String getMessage(String keyMessage, Object[] params, HttpServletRequest request) {
        try {
            return messageSource.getMessage(keyMessage,
                    params,
                    localeResolver.resolveLocale(request));
        } catch (final NoSuchMessageException ex) {
            return "??" + keyMessage + "??";
        }
    }

    public String getMessage(String keyMessage, Object[] params, WebRequest request) {
        try {
            return messageSource.getMessage(keyMessage,
                    params,
                    localeResolver.resolveLocale(request));
        } catch (final NoSuchMessageException ex) {
            return "??" + keyMessage + "??";
        }
    }
}