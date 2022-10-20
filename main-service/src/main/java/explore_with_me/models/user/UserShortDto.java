package explore_with_me.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserShortDto {

    private Long id;
    private String name;
}