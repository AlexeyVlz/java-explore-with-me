package explore_with_me.models.event;

import explore_with_me.models.category.CategoryDto;
import explore_with_me.models.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;
    private CategoryDto categoryDto;
    private Integer confirmedRequests;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
