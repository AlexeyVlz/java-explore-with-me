package explore_with_me.models.request;

import lombok.Data;

@Data
public class ParticipationRequestDto {

    String created;
    Integer event;
    Integer id;
    Integer requester;
    String status;
}
