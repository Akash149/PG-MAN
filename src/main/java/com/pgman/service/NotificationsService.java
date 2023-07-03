package com.pgman.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.Notifications;

@Service
public interface NotificationsService {
    
    public boolean createNotifications(Notifications notifications);

    public List<Notifications> getAllNotifications();

    public Notifications getANotiFicationsById(int id);

    public Notifications getANotifications(Notifications notifications);

    public Notifications updateNotifications(int id, Notifications notifications);

    public void deleteNotifications(Notifications notifications);

    public void deleteNotificationsById(Notifications notifications);

}
