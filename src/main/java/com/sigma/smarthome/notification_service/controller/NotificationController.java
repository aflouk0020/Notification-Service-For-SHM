package com.sigma.smarthome.notification_service.controller;

import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<Notification> getByUser(@PathVariable UUID userId) {
        return service.getNotificationsByUser(userId);
    }
}