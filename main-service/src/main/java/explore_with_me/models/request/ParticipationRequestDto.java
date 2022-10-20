package explore_with_me.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    @NotNull @NotBlank
    private String created;
    @NotNull @Positive
    private Long event;
    @NotNull @Positive
    private Long requester;
    private String status;
}
