package vn.huy.service.account.service;

import vn.huy.service.account.dto.ResponseDto;
import vn.huy.service.account.entity.UserPost;

import java.security.Principal;

public interface UserPostService {

    ResponseDto saveUser(UserPost dto, boolean update, Principal principal);
}
