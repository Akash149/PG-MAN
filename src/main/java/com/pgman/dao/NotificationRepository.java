package com.pgman.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
}
