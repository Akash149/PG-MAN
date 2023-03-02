package com.pgman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.pgman.config.impl.UserDetailsServiceImpl;
import com.pgman.service.AddressService;
import com.pgman.service.GuestService;
import com.pgman.service.OwnerService;
import com.pgman.service.PaymentService;
import com.pgman.service.PgService;
import com.pgman.service.TransactionService;

import org.springframework.security.core.userdetails.*;

import com.pgman.service.impl.AddressServiceImpl;
import com.pgman.service.impl.GuestServiceImpl;
import com.pgman.service.impl.OwnerServiceImpl;
import com.pgman.service.impl.PaymentServiceImpl;
import com.pgman.service.impl.PgServiceImpl;
import com.pgman.service.impl.TransactionServiceImpl;

@Configuration
@Component
public class MyConfig {
    
    // guest service Implementation class bean
    @Bean
    public GuestService getGuestService() {
        return new GuestServiceImpl();
    }

    // Owner service Implementation class bean
    @Bean
    public OwnerService getOwnerService() {
        return new OwnerServiceImpl();
    }

    // PG service Implementation class bean
    @Bean
    public PgService getPgService() {
        return new PgServiceImpl();
    }

    // Address service Implementation class bean
    @Bean
    public AddressService getAddressService() {
        return new AddressServiceImpl();
    }

    // User details service impl bean
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }
    
    // Payment service impl bean
    @Bean
    public PaymentService getPaymentService() {
        return new PaymentServiceImpl();
    }

    // Transaction service impl bean
    @Bean
    public TransactionService getTransactionService() {
        return new TransactionServiceImpl();
    }

    // TODO: DAO Anuthentication provider


    // TODO: Authentication manager
}
