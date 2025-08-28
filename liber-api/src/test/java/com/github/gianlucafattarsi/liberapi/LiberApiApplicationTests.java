package com.github.gianlucafattarsi.liberapi;

import com.github.gianlucafattarsi.liberapi.application.mail.TestConfigForMail;
import com.github.gianlucafattarsi.liberapi.context.account.session.controller.impl.SessionController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LiberApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestConfigForMail.class})
class LiberApiApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SessionController sessionController;

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(sessionController).isNotNull();
    }

    @Test
    @DisplayName("Main method runs without exceptions")
    public void testMain() {
        LiberApiApplication.main(new String[]{});
    }
}
