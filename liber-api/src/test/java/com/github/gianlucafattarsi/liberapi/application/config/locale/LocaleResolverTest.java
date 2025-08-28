package com.github.gianlucafattarsi.liberapi.application.config.locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LocaleResolverTest {

    @Autowired
    private LocaleResolver localeResolver;

    @Test
    @DisplayName("Resolve from HttpServletRequest")
    public void whenResolveLocale_frmHttpServletRequest_thenOk() {

        // No lang in header
        MockHttpServletRequest request = new MockHttpServletRequest();
        Locale locale = localeResolver.resolveLocale(request);
        assertThat(locale).isNotNull();
        assertThat(locale).isEqualTo(Locale.of("en"));

        // Empty lang in header
        request.addHeader("Accept-Language", "");
        locale = localeResolver.resolveLocale(request);
        assertThat(locale).isNotNull();
        assertThat(locale).isEqualTo(Locale.of("en"));

        // IT lang in header
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "it");
        locale = localeResolver.resolveLocale(request);
        assertThat(locale).isNotNull();
        assertThat(locale).isEqualTo(Locale.of("it"));
    }

    @Test
    @DisplayName("Resolve from WebRequest")
    public void whenResolveLocale_frmWebRequest_thenOk() {

        // No lang in header
        MockHttpServletRequest request = new MockHttpServletRequest();
        Locale locale = localeResolver.resolveLocale(new ServletWebRequest(request));
        assertThat(locale).isNotNull();
        assertThat(locale).isEqualTo(Locale.of("en"));

        // Empty lang in header
        request.addHeader("Accept-Language", "");
        locale = localeResolver.resolveLocale(new ServletWebRequest(request));
        assertThat(locale).isNotNull();
        assertThat(locale).isEqualTo(Locale.of("en"));

        // IT lang in header
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "it");
        locale = localeResolver.resolveLocale(new ServletWebRequest(request));
        assertThat(locale).isNotNull();
        assertThat(locale).isEqualTo(Locale.of("it"));
    }
}
