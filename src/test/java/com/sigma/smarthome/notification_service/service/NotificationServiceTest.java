package com.sigma.smarthome.notification_service.service;

import com.sigma.smarthome.notification_service.entity.Notification;
import com.sigma.smarthome.notification_service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUserId(userId);
        notification.setTitle("Maintenance Update");
        notification.setMessage("Your request is now in progress");
        notification.setType("STATUS_UPDATE");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createNotification_ShouldSaveAndReturnNotification() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification saved = notificationService.createNotification(notification);

        assertEquals(notification.getId(), saved.getId());
        assertEquals(notification.getUserId(), saved.getUserId());
        verify(notificationRepository).save(notification);
    }

    @Test
    void getNotificationsByUser_ShouldReturnPagedNotifications() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(page);

        Page<Notification> result = notificationService.getNotificationsByUser(userId, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(userId, result.getContent().get(0).getUserId());

        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Test
    void getNotificationsByUser_ShouldReturnEmptyPage_WhenNoNotificationsExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> emptyPage = Page.empty(pageable);

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(emptyPage);

        Page<Notification> result = notificationService.getNotificationsByUser(userId, pageable);

        assertEquals(0, result.getContent().size());

        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Test
    void getNotificationsByUser_ShouldPassCorrectPageableToRepository() {
        Pageable pageable = PageRequest.of(2, 3);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(page);

        Page<Notification> result = notificationService.getNotificationsByUser(userId, pageable);

        assertEquals(2, result.getNumber());
        assertEquals(3, result.getSize());

        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
}