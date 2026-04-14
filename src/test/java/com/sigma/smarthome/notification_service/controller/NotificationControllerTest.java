package com.sigma.smarthome.notification_service.controller;

import com.sigma.smarthome.notification_service.dto.NotificationResponse;
import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationControllerTest {

    private NotificationService notificationService;
    private NotificationController notificationController;

    private Notification notification;
    private UUID userId;

    @BeforeEach
    void setUp() {
        notificationService = Mockito.mock(NotificationService.class);
        notificationController = new NotificationController(notificationService);

        userId = UUID.randomUUID();

        notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUserId(userId);
        notification.setTitle("Maintenance Update");
        notification.setMessage("Your request is now completed");
        notification.setType("STATUS_UPDATE");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnCreatedNotification() {
        Mockito.when(notificationService.createNotification(notification))
                .thenReturn(notification);

        NotificationResponse result = notificationController.create(notification);

        assertEquals(notification.getId(), result.getId());
        assertEquals(notification.getUserId(), result.getUserId());
    }

    @Test
    void getByUser_ShouldReturnPagedNotificationResponses() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);

        Mockito.when(notificationService.getNotificationsByUser(userId, pageable))
                .thenReturn(page);

        Page<NotificationResponse> response =
                notificationController.getByUser(userId, 0, 10);

        assertEquals(1, response.getContent().size());
        assertEquals(userId, response.getContent().get(0).getUserId());
        assertEquals("Maintenance Update", response.getContent().get(0).getTitle());
    }

    @Test
    void getByUser_ShouldReturnEmptyPage_WhenNoNotificationsExist() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Notification> emptyPage = Page.empty(pageable);

        Mockito.when(notificationService.getNotificationsByUser(userId, pageable))
                .thenReturn(emptyPage);

        Page<NotificationResponse> response =
                notificationController.getByUser(userId, 0, 10);

        assertEquals(0, response.getContent().size());
    }
    
    @Test
    void getByUser_ShouldUseRequestedPageAndSize() {
        PageRequest pageable = PageRequest.of(1, 5);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);

        Mockito.when(notificationService.getNotificationsByUser(userId, pageable))
                .thenReturn(page);

        Page<NotificationResponse> response =
                notificationController.getByUser(userId, 1, 5);

        assertEquals(1, response.getNumber());
        assertEquals(5, response.getSize());
        assertEquals(1, response.getContent().size());
    }
    
    @Test
    void getByUser_ShouldMapNotificationToResponseCorrectly() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);

        Mockito.when(notificationService.getNotificationsByUser(userId, pageable))
                .thenReturn(page);

        Page<NotificationResponse> response =
                notificationController.getByUser(userId, 0, 10);

        NotificationResponse dto = response.getContent().get(0);

        assertEquals(notification.getId(), dto.getId());
        assertEquals(notification.getUserId(), dto.getUserId());
        assertEquals(notification.getTitle(), dto.getTitle());
        assertEquals(notification.getMessage(), dto.getMessage());
        assertEquals(notification.getType(), dto.getType());
        assertEquals(notification.getIsRead(), dto.getIsRead());
    }
}