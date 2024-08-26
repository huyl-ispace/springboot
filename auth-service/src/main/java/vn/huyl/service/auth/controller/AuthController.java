package vn.huyl.service.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.huyl.service.auth.dto.ResponseDto;
import vn.huyl.service.auth.dto.UserDto;
import vn.huyl.service.auth.service.AuthService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
}
