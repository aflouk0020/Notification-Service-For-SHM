package com.sigma.smarthome.notification_service.controller;

import com.sigma.smarthome.notification_service.dto.NotificationResponse;
import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public Notification create(@RequestBody Notification notification) {
        return service.createNotification(notification);
    }

    @GetMapping("/{userId}")
    public Page<NotificationResponse> getByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return service.getNotificationsByUser(userId, pageable)
                .map(notification -> new NotificationResponse(
                        notification.getId(),
                        notification.getUserId(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getType(),
                        notification.getIsRead(),
                        notification.getCreatedAt()
                ));
    }
}