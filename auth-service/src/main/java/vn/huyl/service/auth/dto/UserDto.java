package vn.huyl.service.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.huyl.service.auth.util.ConstantUtil;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String userId;

    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    private String username;

    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    private String password;

    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    @Email(message = ConstantUtil.INVALID)
    private String email;

    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    private String firstName;

    @NotBlank(message = ConstantUtil.NOT_EMPTY)
    private String lastName;
}
