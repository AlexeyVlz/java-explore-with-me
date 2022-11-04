package explore.with.me.models.request;

import explore.with.me.UtilClass;
import explore.with.me.models.user.User;

import java.time.LocalDateTime;

public class RequestMapper {

    public static Request toRequest(ParticipationRequestDto participationRequestDto, User requester) {
        return new Request(LocalDateTime.parse(participationRequestDto.getCreated(), UtilClass.getFormat()),
                participationRequestDto.getEvent(),
                requester,
                RequestStatus.valueOf(participationRequestDto.getStatus().toUpperCase()));
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                request.getCreated().format(UtilClass.getFormat()),
                request.getEventId(),
                request.getRequester().getId(),
                request.getStatus().toString());
    }
}
