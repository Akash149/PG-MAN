package com.pgman.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.Notification;

@Service
public interface NotificationService {
    
    public boolean createNotifications(Notification notifications);

    public List<Notification> getAllNotifications();

    public Notification getANotiFicationsById(int id);

    public Notification updateNotifications(int id, Notification notifications);

    public void deleteNotifications(Notification notifications);

    public void deleteNotificationsById(Notification notifications);

}
