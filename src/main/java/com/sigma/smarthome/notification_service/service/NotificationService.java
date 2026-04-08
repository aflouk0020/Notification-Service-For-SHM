package com.sigma.smarthome.notification_service.service;

import com.sigma.smarthome.notification_service.entity.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

    Notification createNotification(Notification notification);

    List<Notification> getNotificationsByUser(UUID userId);
}