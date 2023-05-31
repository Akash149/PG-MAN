package com.pgman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.pgman.config.impl.UserDetailsServiceImpl;
import com.pgman.entities.PgUtilities;
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
import com.pgman.service.pg.FlatService;
import com.pgman.service.pg.FloorService;
import com.pgman.service.pg.PolicyService;
import com.pgman.service.pg.RoomService;
import com.pgman.service.pg.impl.FlatServiceImpl;
import com.pgman.service.pg.impl.FloorServiceImpl;
import com.pgman.service.pg.impl.PolicyServiceImpl;
import com.pgman.service.pg.impl.RoomServiceImpl;

@Configuration
@Component
public class MyConfig {

    // guest service Implementation class bean
    @Bean
    GuestService getGuestService() {
        return new GuestServiceImpl();
    }

    // Owner service Implementation class bean
    @Bean
    OwnerService getOwnerService() {
        return new OwnerServiceImpl();
    }

    // PG service Implementation class bean
    @Bean
    PgService getPgService() {
        return new PgServiceImpl();
    }

    // Address service Implementation class bean
    @Bean
    AddressService getAddressService() {
        return new AddressServiceImpl();
    }

    // User details service impl bean
    @Bean
    UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Payment service impl bean
    @Bean
    PaymentService getPaymentService() {
        return new PaymentServiceImpl();
    }

    // Transaction service impl bean
    @Bean
    TransactionService getTransactionService() {
        return new TransactionServiceImpl();
    }

    // Floor service impl bean
    @Bean
    FloorService getFloorService() {
        return new FloorServiceImpl();
    }

    // Flat service impl bean
    @Bean
    FlatService getFlatService() {
        return new FlatServiceImpl();
    }

    // Room Service impl bean
    @Bean
    RoomService getRoomService() {
        return new RoomServiceImpl();
    }

    // Policy Service impl bean
    @Bean
    PolicyService getPolicyService() {
        return new PolicyServiceImpl();
    }

    // pg utility class 
    @Bean
    PgUtilities getPgUtilities() {
        return new PgUtilities();
    }

}
