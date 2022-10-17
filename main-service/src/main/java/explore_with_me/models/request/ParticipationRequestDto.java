package explore_with_me.models.request;

import lombok.Data;

@Data
public class ParticipationRequestDto {

    private String created;
    private Integer event;
    private Long id;
    private Integer requester;
    private String status;
}
