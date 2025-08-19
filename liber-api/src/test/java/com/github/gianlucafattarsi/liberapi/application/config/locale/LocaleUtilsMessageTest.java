package com.github.gianlucafattarsi.liberapi.application.config.locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LocaleUtilsMessageTest {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private LocaleUtilsMessage localeUtilsMessage;

    @Test
    @DisplayName("Get message (no param)")
    public void whenGetMessage_withoutParam_thenOk() {

        String messageKey = "NoSuchEntityException.detail";

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey, null);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("The resource may not exist anymore");

        // IT locale
        message = localeUtilsMessage.getMessage(messageKey, null, Locale.ITALIAN);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("Il dato cercato potrebbe non esistere più");

        // EN locale
        message = localeUtilsMessage.getMessage(messageKey, null, Locale.ENGLISH);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("The resource may not exist anymore");
    }

    @Test
    @DisplayName("Get message (with param)")
    public void whenGetMessage_withParam_thenOk() {

        String messageKey = "UserAlreadyExistsException.detail";
        String[] messageParams = new String[]{"testUser", "testUser@email.com"};

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey, messageParams);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("User with userName: testUser or email: testUser@email.com already exists");

        // IT locale
        message = localeUtilsMessage.getMessage(messageKey,
                messageParams,
                Locale.ITALIAN);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("Utente con userName: testUser o email: testUser@email.com già esistente");

        // EN locale
        message = localeUtilsMessage.getMessage(messageKey,
                messageParams,
                Locale.ENGLISH);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("User with userName: testUser or email: testUser@email.com already exists");
    }

    @Test
    @DisplayName("Get message (no message key)")
    public void whenGetMessage_withNoMessageKey_thenOk() {

        String messageKey = "no.key.found";

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey, null);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");

        // IT locale
        message = localeUtilsMessage.getMessage(messageKey, null, Locale.ITALIAN);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");

        // EN locale
        message = localeUtilsMessage.getMessage(messageKey, null, Locale.ENGLISH);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");
    }

    @Test
    @DisplayName("Get message (from http request)")
    public void whenGetMessage_fromHttpRequest_thenOk() {

        String messageKey = "UserAlreadyExistsException.detail";
        String[] messageParams = new String[]{"testUser", "testUser@email.com"};

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "aa");

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey,
                messageParams,
                request);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("User with userName: testUser or email: testUser@email.com already exists");

        // IT locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "it");
        message = localeUtilsMessage.getMessage(messageKey,
                messageParams,
                request);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("Utente con userName: testUser o email: testUser@email.com già esistente");

        // EN locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "en");
        message = localeUtilsMessage.getMessage(messageKey,
                messageParams,
                request);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("User with userName: testUser or email: testUser@email.com already exists");
    }

    @Test
    @DisplayName("Get message (from http request, no message key)")
    public void whenGetMessage_fromHttpRequest_noMessageKey_thenOk() {

        String messageKey = "no.key.found";

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "aa");

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey,
                null,
                request);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");

        // IT locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "it");
        message = localeUtilsMessage.getMessage(messageKey,
                null,
                request);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");

        // EN locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "en");
        message = localeUtilsMessage.getMessage(messageKey,
                null,
                request);

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");
    }

    @Test
    @DisplayName("Get message (from web request)")
    public void whenGetMessage_fromWebRequest_thenOk() {

        String messageKey = "UserAlreadyExistsException.detail";
        String[] messageParams = new String[]{"testUser", "testUser@email.com"};

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "aa");

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey, messageParams, new ServletWebRequest(request));

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("User with userName: testUser or email: testUser@email.com already exists");

        // IT locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "it");
        message = localeUtilsMessage.getMessage(messageKey, messageParams, new ServletWebRequest(request));

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("Utente con userName: testUser o email: testUser@email.com già esistente");

        // EN locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "en");
        message = localeUtilsMessage.getMessage(messageKey,
                messageParams,
                new ServletWebRequest(request));

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("User with userName: testUser or email: testUser@email.com already exists");
    }

    @Test
    @DisplayName("Get message (from web request, no message key)")
    public void whenGetMessage_fromWebRequest_noMessageKey_thenOk() {

        String messageKey = "no.key.found";

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept-Language", "aa");

        // Default lang
        String message = localeUtilsMessage.getMessage(messageKey, null, new ServletWebRequest(request));

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");

        // IT locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "it");
        message = localeUtilsMessage.getMessage(messageKey, null, new ServletWebRequest(request));

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");

        // EN locale
        request.removeHeader("Accept-Language");
        request.addHeader("Accept-Language", "en");
        message = localeUtilsMessage.getMessage(messageKey,
                null,
                new ServletWebRequest(request));

        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("??no.key.found??");
    }
}
