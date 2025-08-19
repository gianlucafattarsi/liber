package com.github.gianlucafattarsi.liberapi.application.security.service;

import com.github.gianlucafattarsi.liberapi.application.exception.type.NoSuchEntityException;
import com.github.gianlucafattarsi.liberapi.application.security.JwtTokenManager;
import com.github.gianlucafattarsi.liberapi.application.security.JwtUser;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.Session;
import com.github.gianlucafattarsi.liberapi.context.account.session.dto.UserInfo;
import com.github.gianlucafattarsi.liberapi.context.account.session.mapper.UserInfoMapper;
import com.github.gianlucafattarsi.liberapi.context.account.session.payload.UserLogin;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.RefreshToken;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import com.github.gianlucafattarsi.liberapi.context.account.user.repository.RefreshTokenRepository;
import com.github.gianlucafattarsi.liberapi.context.account.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserInfoMapper userInfoMapper;

    //@Value("${spring.profiles.active}")
    //private String environment;

    @Autowired
    Environment environment;

    @Transactional
    public Session authenticate(final UserLogin userLogin) {
        final User user = userService.findByUsername(userLogin.username())
                                     .orElseThrow(NoSuchEntityException::new);
        userService.validateUser(user.getUserName());

        final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(userLogin.username(),
                userLogin.password());
        final var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);

        // Generate JWT access token
        final UserDetails userDetails = JwtUser.createInstance(user);
        final String accessToken = jwtTokenManager.generateAccessToken(userDetails);

        // Create refresh token
        refreshTokenRepository.deleteByUserId(user.getId());
        RefreshToken refreshToken = jwtTokenManager.generateRefreshToken(user);
        refreshToken = refreshTokenRepository.save(refreshToken);

        UserInfo userInfo = userInfoMapper.fromUser(user);

        final Session session = new Session();
        session.setAccessToken(accessToken);
        session.setRefreshToken(refreshToken.getId());
        session.setUserInfo(userInfo);
        session.setEnvironment(StringUtils.join(environment.getActiveProfiles(), ","));

        return session;
    }

    @Transactional
    public Session refreshToken(UUID refreshToken) {
        final var refreshTokenEntity = refreshTokenRepository.findByIdAndExpiresAtAfter(refreshToken,
                                                                     Instant.now())
                                                             .orElseThrow(() -> new BadCredentialsException(
                                                                     "Invalid or expired refresh token"));

        final UserDetails userDetails = JwtUser.createInstance(refreshTokenEntity.getUser());
        final String newAccessToken = jwtTokenManager.generateAccessToken(userDetails);

        UserInfo userInfo = userInfoMapper.fromUser(refreshTokenEntity.getUser());

        RefreshToken newRefreshToken = jwtTokenManager.generateRefreshToken(refreshTokenEntity.getUser());
        refreshTokenRepository.deleteByUserId(refreshTokenEntity.getUser()
                                                                .getId());
        newRefreshToken = refreshTokenRepository.save(newRefreshToken);

        final Session session = new Session();
        session.setAccessToken(newAccessToken);
        session.setRefreshToken(newRefreshToken.getId());
        session.setUserInfo(userInfo);
        session.setEnvironment(StringUtils.join(environment.getActiveProfiles(), ","));

        return session;
    }

    @Transactional
    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}