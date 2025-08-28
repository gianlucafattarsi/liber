package com.github.gianlucafattarsi.liberapi.application.security;

import com.github.gianlucafattarsi.liberapi.context.account.user.entity.RefreshToken;
import com.github.gianlucafattarsi.liberapi.context.account.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtTokenManager {

    public static final String CLAIM_KEY_MAIL = "mail";
    public static final String CLAIM_KEY_USERID = "id";
    public static final String CLAIM_KEY_LANG = "lang";
    static final String CLAIM_KEY_AUTHORITIES = "roles";
    static final String CLAIM_KEY_IS_ENABLED = "isEnabled";
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration-ttl}")
    private Duration accessTokenTtl;
    @Value("${jwt.refresh.expiration-ttl}")
    private Duration refreshTokenTtl;

    public String generateAccessToken(UserDetails userDetails) {
        final List<String> auth = userDetails.getAuthorities()
                                             .stream()
                                             .map(GrantedAuthority::getAuthority)
                                             .collect(Collectors.toList());

        Consumer<Map<String, Object>> claimsToken = ((claims) -> {
            claims.put(CLAIM_KEY_AUTHORITIES, auth);
            claims.put(CLAIM_KEY_IS_ENABLED, userDetails.isEnabled());
            claims.put(CLAIM_KEY_MAIL, ((JwtUser) userDetails).getEmail());
            claims.put(CLAIM_KEY_USERID, ((JwtUser) userDetails).getId());
            claims.put(CLAIM_KEY_LANG, ((JwtUser) userDetails).getLang());
        });

        return generateToken(userDetails.getUsername(), claimsToken);
    }

    private String generateToken(String username, Consumer<Map<String, Object>> claims) {
        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                                                   .claims(claims)
                                                   .subject(username)
                                                   .issuedAt(Instant.now())
                                                   .expiresAt(generateAccessTokenExpirationDate())
                                                   .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                         .getTokenValue();
    }

    private Instant generateAccessTokenExpirationDate() {
        return Instant.now()
                      .plus(accessTokenTtl);
    }

    public UserDetails resolveUser(String token) {
        final Map<String, Object> claims = extractAllClaims(token);
        final Long userId = Long.valueOf(claims.get(CLAIM_KEY_USERID)
                                               .toString());
        final String username = claims.get(JwtClaimNames.SUB)
                                      .toString();
        final String email = claims.get(CLAIM_KEY_MAIL)
                                   .toString();
        final String lang = claims.get(CLAIM_KEY_LANG)
                                  .toString();
        final List<String> roles = (List<String>) claims.get(CLAIM_KEY_AUTHORITIES);

        return JwtUser.builder()
                      .id(userId)
                      .username(username)
                      .email(email)
                      .lang(lang)
                      .roles(roles)
                      .build();
    }

    private Map<String, Object> extractAllClaims(String token) {
        return jwtDecoder.decode(token)
                         .getClaims();
    }

    public RefreshToken generateRefreshToken(User user) {
        return RefreshToken.builder()
                           .user(user)
                           .expiresAt(Instant.now()
                                             .plus(refreshTokenTtl))
                           .build();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get(JwtClaimNames.SUB)
                                      .toString();
    }
}
