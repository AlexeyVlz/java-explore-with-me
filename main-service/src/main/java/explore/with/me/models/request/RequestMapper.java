package explore.with.me.models.request;

import explore.with.me.models.State;
import explore.with.me.models.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestMapper {

    public static Request toRequest(ParticipationRequestDto participationRequestDto, User requester) {
        return new Request(LocalDateTime.parse(participationRequestDto.getCreated(), getFormatter()),
                participationRequestDto.getEvent(),
                requester,
                State.valueOf(participationRequestDto.getStatus().toUpperCase()));
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                request.getCreated().format(getFormatter()),
                request.getEventId(),
                request.getRequester().getId(),
                request.getStatus().toString());
    }

    private static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}
