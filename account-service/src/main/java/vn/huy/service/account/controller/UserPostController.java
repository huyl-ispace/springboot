package vn.huy.service.account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PreAuthorize("hasAuthority('SCOPE_client')")
    @GetMapping(value = "/test")
    public String test(Principal principal){
        return "Hello acc: " + principal.getName();
    }
}
