package com.github.gianlucafattarsi.liberapi.context.account.user.event;

import org.springframework.context.ApplicationEvent;

import java.io.Serial;

public class UserDeletedEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = -4182019932984499591L;

    private long id;

    public UserDeletedEvent(final Object source) {
        super(source);
    }
}
