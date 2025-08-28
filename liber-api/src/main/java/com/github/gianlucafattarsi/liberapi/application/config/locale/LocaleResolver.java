package com.github.gianlucafattarsi.liberapi.application.config.locale;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;
import java.util.Map;

@Component
public class LocaleResolver extends AcceptHeaderLocaleResolver {

    private static final Map<String, Locale> LOCALES = Map.of("it",
            Locale.of("it"),
            "en",
            Locale.of("en"));

    @Nonnull
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        final String language = request.getHeader("Accept-Language");
        return resolveLanguage(language);
    }

    private Locale resolveLanguage(String language) {
        if (StringUtils.defaultIfBlank(language, "")
                       .contains("it")) {
            return LOCALES.get("it");
        }
        return LOCALES.get("en");
    }

    public Locale resolveLocale(WebRequest request) {
        final String language = request.getHeader("Accept-Language");
        return resolveLanguage(language);
    }

}