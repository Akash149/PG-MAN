package com.pgman.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgman.entities.Notifications;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer>{
    
}
