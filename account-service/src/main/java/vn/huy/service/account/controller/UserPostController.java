package vn.huy.service.account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.huy.service.account.dto.ResponseDto;
import vn.huy.service.account.entity.UserPost;
import vn.huy.service.account.service.UserPostService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserPostController {

    private final UserPostService userPostService;

    @PostMapping(value = "/create")
    public ResponseDto createUserPost(@RequestBody @Valid UserPost dto, Principal principal) {
        return userPostService.saveUser(dto, false, principal);
    }

    @PutMapping(value = "/update")
    public ResponseDto updateUserPost(@RequestBody @Valid UserPost dto, Principal principal) {
        return userPostService.saveUser(dto, true, principal);
    }
}
