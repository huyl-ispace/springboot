package vn.huyl.service.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import vn.huyl.service.auth.client.AccountServiceClient;
import vn.huyl.service.auth.dto.ResponseDto;
import vn.huyl.service.auth.dto.UserDto;
import vn.huyl.service.auth.service.AuthService;
import vn.huyl.service.auth.service.security.JwtService;

import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    //private final KeyPair keyPair;

    private final AccountServiceClient accountServiceClient;

    @PostMapping(value = "/user")
    public ResponseDto createUser(@RequestBody @Valid UserDto dto, Principal principal) {
        return authService.saveUser(dto, false, principal);
    }

    @PutMapping(value = "/user")
    public ResponseDto updateUser(@RequestBody @Valid UserDto dto, Principal principal) {
        return authService.saveUser(dto, true, principal);
    }

    @DeleteMapping(value = "/user")
    public ResponseDto deleteUser(@RequestBody List<String> userIds, Principal principal) {
        return authService.deleteUser(userIds, principal);
    }

    @GetMapping(value = "/token/{username}")
    public String genToken(@PathVariable String username) {
        return jwtService.genToken(username);
    }

    @DeleteMapping(value = "/revoke-token")
    public void revokeToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Lấy token sau "Bearer "
            jwtService.revokeToken(token);
        }
    }

    /*@GetMapping(value = "/.well-known/jwks.json")
    public Map<String, Object> getJwks() {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return jwkSet.toJSONObject();
    }*/

    @GetMapping(value = "/test")
    public String test(Principal principal) {
        try {
            return accountServiceClient.test();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
