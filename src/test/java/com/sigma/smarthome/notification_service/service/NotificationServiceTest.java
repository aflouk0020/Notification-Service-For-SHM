package com.sigma.smarthome.notification_service.service;

import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private UUID userId;
    private Notification notification;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUserId(userId);
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test");
        notification.setType("INFO");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createNotification_ShouldSaveAndReturnNotification() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.createNotification(notification);

        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
        assertEquals("Test Notification", result.getTitle());
        verify(notificationRepository).save(notification);
    }

    @Test
    void getNotificationsByUser_ShouldReturnOrderedNotifications() {
        Notification second = new Notification();
        second.setId(UUID.randomUUID());
        second.setUserId(userId);
        second.setTitle("Second Notification");
        second.setMessage("Second message");
        second.setType("INFO");
        second.setIsRead(false);
        second.setCreatedAt(LocalDateTime.now().minusHours(1));

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(List.of(notification, second));

        List<Notification> result = notificationService.getNotificationsByUser(userId);

        assertEquals(2, result.size());
        assertEquals("Test Notification", result.get(0).getTitle());
        assertEquals("Second Notification", result.get(1).getTitle());
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }
}