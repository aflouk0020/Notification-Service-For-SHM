package com.sigma.smarthome.notification_service.service;

import com.sigma.smarthome.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {

    Notification createNotification(Notification notification);

    Page<Notification> getNotificationsByUser(UUID userId, Pageable pageable);
    
    Notification markAsRead(UUID id);
}