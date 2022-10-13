package explore_with_me.models.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewUserRequest {

    @NotBlank
    String name;
    @NotBlank @Email
    String email;
}
