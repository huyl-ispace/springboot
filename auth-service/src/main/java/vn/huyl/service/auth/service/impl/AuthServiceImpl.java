package vn.huyl.service.auth.service.impl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.huyl.service.auth.dto.ResponseDto;
import vn.huyl.service.auth.dto.UserDto;
import vn.huyl.service.auth.service.AuthService;
import vn.huyl.service.auth.util.ConstantUtil;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realmName;

    private boolean userExists(String username, String email, String userId) {
        UsersResource usersResource = keycloak.realm(realmName).users();
        List<UserRepresentation> users = usersResource.list();
        return users.stream().anyMatch(user -> (username.
                equals(user.getUsername()) || email.equals(user.getEmail()))
                && user.isEnabled() && !userId.equals(user.getId()));
    }

    private Optional<UserRepresentation> findDisableByUsernameAndEmail(String username, String email) {
        UsersResource usersResource = keycloak.realm(realmName).users();
        return usersResource.list().stream()
                .filter(userRepresentation ->
                        (username.equals(userRepresentation.getUsername()) || email.equals(userRepresentation.getEmail()))
                                && !userRepresentation.isEnabled())
                .findFirst();
    }

    private Optional<UserRepresentation> findById(String userId) {
        UsersResource usersResource = keycloak.realm(realmName).users();
        try {
            UserRepresentation userRepresentation = usersResource.get(userId).toRepresentation();
            return Optional.of(userRepresentation);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseDto saveUser(UserDto dto, boolean update, Principal principal) {
        ResponseDto responseDto = new ResponseDto();
        try {
            if (userExists(dto.getUsername(), dto.getEmail(), update ? dto.getUserId() : "")) {
                responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                responseDto.setMessage(String.format(ConstantUtil.ErrorMessage.EXISTED.getValue(),
                        "username hoặc email"));
                return responseDto;
            }
            String userId = "";
            UsersResource usersResource = keycloak.realm(realmName).users();
            UserRepresentation userRepresentation;
            if (update) {
                Optional<UserRepresentation> optional = findById(dto.getUserId());
                if (optional.isEmpty()) {
                    responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
                    responseDto.setMessage(String.format(ConstantUtil.ErrorMessage.NOT_EXISTED.getValue(),
                            "userId"));
                    return responseDto;
                }
                userRepresentation = optional.get();
                userRepresentation.setUsername(dto.getUsername());
                userRepresentation.setEmail(dto.getEmail());
                userRepresentation.setFirstName(dto.getFirstName());
                userRepresentation.setLastName(dto.getLastName());
                usersResource.get(userRepresentation.getId()).update(userRepresentation);
                userId = userRepresentation.getId();
            } else {
                Optional<UserRepresentation> optional = findDisableByUsernameAndEmail(dto.getUsername(), dto.getEmail());
                if (optional.isPresent()) {
                    userRepresentation = optional.get();
                    userRepresentation.setEnabled(true);
                    userRepresentation.setFirstName(dto.getFirstName());
                    userRepresentation.setLastName(dto.getLastName());
                    usersResource.get(userRepresentation.getId()).update(userRepresentation);
                    userId = userRepresentation.getId();
                } else {
                    userRepresentation = new UserRepresentation();
                    userRepresentation.setUsername(dto.getUsername());
                    userRepresentation.setEmail(dto.getEmail());
                    userRepresentation.setEnabled(true);
                    userRepresentation.setFirstName(dto.getFirstName());
                    userRepresentation.setLastName(dto.getLastName());
                    Response response = usersResource.create(userRepresentation);
                    if (response.getStatus() == HttpStatus.CREATED.value()) {
                        userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                    }
                    response.close();
                }
            }
            if (!userId.isEmpty()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(dto.getPassword());
                credential.setTemporary(false);
                usersResource.get(userId).resetPassword(credential);
            }
            dto.setUserId(userId);
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage(ConstantUtil.ErrorMessage.SUCCESS.getValue());
            responseDto.setResult(dto);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());;
            responseDto.setMessage(ConstantUtil.ErrorMessage.INTERNAL_SEVER_ERROR.getValue());
        }
        return responseDto;
    }

    @Override
    public ResponseDto deleteUser(List<String> userIds, Principal principal) {
        if (null == userIds || userIds.isEmpty()) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(),
                    String.format(ConstantUtil.ErrorMessage.ARRAY_NOT_EMPTY.getValue(), "userIds"), null);
        }
        UsersResource usersResource = keycloak.realm(realmName).users();
        for (String userId : userIds) {
            Optional<UserRepresentation> optional = findById(userId);
            if (optional.isPresent()) {
                UserRepresentation user = optional.get();
                user.setEnabled(false);
                usersResource.get(userId).update(user);
            }
        }
        return new ResponseDto(HttpStatus.OK.value(), ConstantUtil.ErrorMessage.SUCCESS.getValue(), userIds);
    }
}
