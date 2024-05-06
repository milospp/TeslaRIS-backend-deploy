package rs.teslaris.core.service.impl.commontypes;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.dto.commontypes.NotificationDTO;
import rs.teslaris.core.model.commontypes.Notification;
import rs.teslaris.core.repository.commontypes.NotificationRepository;
import rs.teslaris.core.service.impl.JPAServiceImpl;
import rs.teslaris.core.service.interfaces.commontypes.NotificationService;
import rs.teslaris.core.util.exceptionhandling.exception.NotificationException;
import rs.teslaris.core.util.notificationhandling.NewOtherNameNotificationHandler;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl extends JPAServiceImpl<Notification>
    implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NewOtherNameNotificationHandler newOtherNameNotificationHandler;

    @Override
    protected JpaRepository<Notification, Integer> getEntityRepository() {
        return notificationRepository;
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Integer userId) {
        var notificationList = notificationRepository.getNotificationsForUser(userId);

        return notificationList.stream().map(
            notification -> new NotificationDTO(notification.getId(),
                notification.getNotificationText())).collect(
            Collectors.toList());
    }

    @Override
    public long getUserNotificationCount(Integer userId) {
        return notificationRepository.getNotificationCountForUser(userId);
    }

    @Override
    public void approve(Integer notificationId, Integer userId) {
        var notification = findOne(notificationId);

        if (!notification.getUser().getId().equals(userId)) {
            throw new NotificationException(
                "Exception you are trying to approve does not belong to you.");
        }

        switch (notification.getNotificationType()) {
            case NEW_PAPER_HARVESTED:
                //TODO: To be implemented...
                break;
            case NEW_OTHER_NAME_DETECTED:
                newOtherNameNotificationHandler.handle(notification);
                break;
        }
    }

    @Override
    public void reject(Integer notificationId, Integer userId) {
        var notification = findOne(notificationId);

        if (!notification.getUser().getId().equals(userId)) {
            throw new NotificationException(
                "Exception you are trying to approve does not belong to you.");
        }

        delete(notificationId);
    }

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
