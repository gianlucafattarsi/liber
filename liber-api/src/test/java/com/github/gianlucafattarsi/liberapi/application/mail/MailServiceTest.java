package com.github.gianlucafattarsi.liberapi.application.mail;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MailServiceTest {

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment environment;

    @TestConfiguration
    public class MailServiceTestConfig {
        @Bean
        @Primary
        public MailService mailService() {
            return new MailService(environment, javaMailSender);
        }
    }
}
