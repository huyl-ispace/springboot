package vn.huyl.service.auth.service.security;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtService {

    private final JwtEncoder jwtEncoder;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final RegisteredClientRepository registeredClientRepository;

    public JwtService(JwtEncoder jwtEncoder, OAuth2AuthorizationService oAuth2AuthorizationService, RegisteredClientRepository registeredClientRepository) {
        this.jwtEncoder = jwtEncoder;
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.registeredClientRepository = registeredClientRepository;
    }

    public String genToken(String username) {
        String tokenValue = "";
        RegisteredClient registeredClient = registeredClientRepository.findByClientId("browser");
        if(null != registeredClient){
            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .id(UUID.randomUUID().toString())
                    .issuer(registeredClient.getId())
                    .notBefore(now)
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(registeredClient
                            .getTokenSettings().getAccessTokenTimeToLive().getSeconds()))
                    .subject(username)
                    .audience(Collections.singletonList(registeredClient.getClientId()))
                    .claim("scope", registeredClient.getScopes())
                    .build();
            Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt()
            );
            OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .id(UUID.randomUUID().toString())
                    .authorizedScopes(registeredClient.getScopes())
                    .principalName(username)
                    .authorizationGrantType(new AuthorizationGrantType("jwt-password"))
                    .token(accessToken)
                    .build();
            oAuth2AuthorizationService.save(authorization);
            tokenValue = jwt.getTokenValue();
        }
        return tokenValue;
    }

    public void revokeToken(String token) {
        try {
            OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            if(oAuth2Authorization != null)
                oAuth2AuthorizationService.remove(oAuth2Authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
