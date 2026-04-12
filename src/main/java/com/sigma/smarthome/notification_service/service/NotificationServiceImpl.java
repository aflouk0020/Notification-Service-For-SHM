package com.sigma.smarthome.notification_service.service;

import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

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
    public Page<Notification> getNotificationsByUser(UUID userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Override
    public Notification markAsRead(UUID id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification not found: " + id));

        notification.setIsRead(true);
        return repository.save(notification);
    }
}