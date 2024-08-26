package vn.huyl.service.auth.service;

import vn.huyl.service.auth.dto.ResponseDto;
import vn.huyl.service.auth.dto.UserDto;

import java.security.Principal;
import java.util.List;

public interface AuthService {

    ResponseDto saveUser(UserDto dto, boolean update, Principal principal);

    ResponseDto deleteUser(List<String> userIds, Principal principal);
}
