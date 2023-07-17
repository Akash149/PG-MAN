package com.pgman.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.dao.NotificationRepository;
import com.pgman.entities.Notification;
import com.pgman.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepo;

    @Override
    public boolean createNotifications(Notification notification) {
        notificationRepo.save(notification);
        return false;
    }

    @Override
    public void deleteNotifications(Notification notification) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteNotificationsById(Notification notification) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Notification getANotiFicationsById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Notification> getAllNotifications() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Notification updateNotifications(int id, Notification notifications) {
        // TODO Auto-generated method stub
        return null;
    }

    
    
}
