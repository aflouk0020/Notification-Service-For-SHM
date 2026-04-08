package com.sigma.smarthome.notification_service.service;

import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUser(UUID userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}