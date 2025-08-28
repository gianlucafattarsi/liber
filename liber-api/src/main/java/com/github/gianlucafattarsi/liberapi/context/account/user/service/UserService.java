package com.github.gianlucafattarsi.liberapi.context.account.user.service;

import com.github.gianlucafattarsi.liberapi.application.config.security.authentication.AuthenticationFacade;
import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.application.exception.type.UserAlreadyExistsException;
import com.github.gianlucafattarsi.liberapi.application.mail.MailMessage;
import com.github.gianlucafattarsi.liberapi.application.mail.MailService;
import com.github.gianlucafattarsi.liberapi.application.security.JwtUser;
import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import com.github.gianlucafattarsi.liberapi.context.account.permission.repository.Permissions;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.NewUserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.controller.payload.UserPayload;
import com.github.gianlucafattarsi.liberapi.context.account.user.dto.UserDTO;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import com.github.gianlucafattarsi.liberapi.context.account.user.event.UserDeletedEvent;
import com.github.gianlucafattarsi.liberapi.context.account.user.mapper.UserMapper;
import com.github.gianlucafattarsi.liberapi.context.account.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository users;
    private final Permissions permissions;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper mapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final MailService mailService;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = users.findByUserNameIgnoreCase(username);

        // Converting UserInfo to UserDetails
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.",
                    username));
        } else {
            return JwtUser.createInstance(user.get());
        }
    }

    public List<UserDTO> loadAllUsers() {
        return users.findAll()
                    .stream()
                    .map(mapper::toDTO)
                    .toList();
    }

    public void validateUser(String username) {
        final Optional<User> userOpt = findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.",
                    username));
        }
    }

    public Optional<User> findByUsername(String username) {
        return users.findByUserNameIgnoreCase(username);
    }

    @Transactional
    public void createNewUser(NewUserPayload userPayload) {

        users.findByUserNameIgnoreCase(userPayload.userName())
             .ifPresent(_ -> {
                 throw new UserAlreadyExistsException(userPayload.userName(), userPayload.email());
             });

        String password = RandomStringUtils.secure()
                                           .nextAlphanumeric(10);

        User user = mapper.toEntity(userPayload);
        user.updateId(null);
        user.updatePassword(passwordEncoder.encode(password));
        assignUserPermissions(user, false);

        users.save(user);


        MailMessage mailMessage = MailMessage.builder()
                                             .recipients(List.of(userPayload.email()))
                                             .subject("Liber - New User Created")
                                             .body("A new user has been created for you in Liber. The generated password is: " + password)
                                             .build();
        mailService.send(mailMessage);

        log.debug("A new user has been created for you in Liber. The generated password is: {}", password);
    }

    private void assignUserPermissions(User user, boolean administrator) {
        final List<Permission> allPermissions = permissions.findAll();

        if (administrator) {
            allPermissions.forEach(user::addPermission);
        } else {
            allPermissions.stream()
                          .filter(p -> p.getCode()
                                        .equals("USER_DEFAULT"))
                          .findFirst()
                          .ifPresent(user::addPermission);
        }
    }

    public UserDTO editUser(Long id, UserPayload userPayload) {

        User user = users.findById(id)
                         .orElseThrow(NoSuchEntityException::new);
        user.changeEmail(userPayload.email());
        user.changeLang(userPayload.lang());
        user.updatePassword(passwordEncoder.encode(userPayload.password()));
        user.revokePermissions();
        assignUserPermissions(user, userPayload.administrator());

        user = users.save(user);

        return mapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(long id) {

        if (authenticationFacade.getCurrentPrincipal() == null) {
            throw new IllegalStateException("You cannot delete the user.");
        }

        if (!authenticationFacade.getCurrentPrincipal()
                                 .id()
                                 .equals(id)) {
            throw new IllegalStateException("You cannot delete a different user than yourself.");
        }

        applicationEventPublisher.publishEvent(new UserDeletedEvent(id));

        users.deleteById(id);
    }
}