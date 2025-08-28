package com.github.gianlucafattarsi.liberapi.application.exception.type;

import lombok.Getter;

import java.io.Serial;

/**
 * Exception thrown when a user already exists in the system.
 * <p>
 * This exception is typically used in scenarios where a user tries to register or create an account
 * with a username or email that is already associated with an existing account.
 * </p>
 */
@Getter
public class UserAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5171604478922892116L;

    private final String userName;
    private final String email;

    /**
     * Constructs a new UserAlreadyExistsException with the specified username and email.
     *
     * @param userName the username that already exists
     * @param email    the email that already exists
     */
    public UserAlreadyExistsException(String userName, String email) {
        super("User with userName: " + userName + " or email: " + email + " already exists");
        this.userName = userName;
        this.email = email;
    }
}
