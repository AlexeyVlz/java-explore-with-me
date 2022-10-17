package explore_with_me.models.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewUserRequest {

    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank @Email
    private String email;
}
