package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Notification;
import com.bezkoder.spring.security.postgresql.payload.response.NotificationResponse;
import com.bezkoder.spring.security.postgresql.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(Notification notification) {
        // Add any additional business logic or validation here
        return notificationRepository.save(notification);
    }

    public Page<Notification> getAllNotificationsByUserId(Long id, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedDateDesc(id, pageable);
    }

    public List<NotificationResponse> getNotificationResponses(Page<Notification> notifications) {
        List<NotificationResponse> notificationResponses = new ArrayList<>();

        for (Notification notification: notifications.getContent()) {
            NotificationResponse notificationResponse = mapNotificationToNotificationResponse(notification);
            notificationResponses.add(notificationResponse);
        }
        return notificationResponses;
    }

    private NotificationResponse mapNotificationToNotificationResponse(Notification notification) {
        NotificationResponse notificationResponse = new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getDescription(),
                notification.getName(),
                notification.isReadStatus(),
                notification.getObjId(),
                notification.getUserId(),
                notification.getCreatedDate().getTime(),
                notification.getUpdatedDate().getTime(),
                notification.getSenderId(),
                notification.getSenderImg()
        );
        return notificationResponse;
    }

    public Notification getNotificationById(Long id) throws ChangeSetPersister.NotFoundException {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    public Notification updateReadStatus(Long id, boolean readStatus) throws ChangeSetPersister.NotFoundException {
        Notification existingNotification = getNotificationById(id);
        existingNotification.setReadStatus(readStatus);
        existingNotification.setUpdatedDate(new Date());
        return notificationRepository.save(existingNotification);
    }

    public void markAllNotificationsAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        notifications.forEach(notification -> notification.setReadStatus(true));
        notificationRepository.saveAll(notifications);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndReadStatus(userId, false);
    }

    public void updateReadStatusForMultipleNotifications(List<Long> notificationIds, boolean newReadStatus) {
        // Assuming you have a method in your repository to update read status
        // Implement the logic to update the read status for each notification
        Date currentDate = new Date();
        for (Long notificationId : notificationIds) {
            Optional<Notification> notification = notificationRepository.findById(notificationId);
            if (notification.isPresent()) {
                Notification noti = notification.get();
                noti.setReadStatus(newReadStatus);
                noti.setUpdatedDate(currentDate);
                notificationRepository.save(noti);
            }
        }
    }
}
