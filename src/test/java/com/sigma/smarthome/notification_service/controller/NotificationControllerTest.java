package com.sigma.smarthome.notification_service.controller;

import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NotificationControllerTest {

    private NotificationService notificationService;
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        notificationService = Mockito.mock(NotificationService.class);
        notificationController = new NotificationController(notificationService);
    }

    @Test
    void create_ShouldReturnSavedNotification() {
        UUID userId = UUID.randomUUID();

        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUserId(userId);
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test");
        notification.setType("INFO");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        when(notificationService.createNotification(notification)).thenReturn(notification);

        Notification result = notificationController.create(notification);

        assertEquals("Test Notification", result.getTitle());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getByUser_ShouldReturnNotificationsList() {
        UUID userId = UUID.randomUUID();

        Notification first = new Notification();
        first.setId(UUID.randomUUID());
        first.setUserId(userId);
        first.setTitle("First");
        first.setMessage("Message 1");
        first.setType("INFO");
        first.setIsRead(false);
        first.setCreatedAt(LocalDateTime.now());

        Notification second = new Notification();
        second.setId(UUID.randomUUID());
        second.setUserId(userId);
        second.setTitle("Second");
        second.setMessage("Message 2");
        second.setType("INFO");
        second.setIsRead(false);
        second.setCreatedAt(LocalDateTime.now().minusMinutes(10));

        when(notificationService.getNotificationsByUser(userId))
                .thenReturn(List.of(first, second));

        List<Notification> result = notificationController.getByUser(userId);

        assertEquals(2, result.size());
        assertEquals("First", result.get(0).getTitle());
        assertEquals("Second", result.get(1).getTitle());
    }
}