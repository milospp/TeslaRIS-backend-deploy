package rs.teslaris.core.dto.commontypes;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.teslaris.core.util.notificationhandling.NotificationAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Integer id;

    private String notificationText;

    private List<NotificationAction> possibleActions;
}
