package com.pgman.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgman.entities.Guest;
import com.pgman.entities.Payments;

@Service
public interface PaymentService {

    //1. create a new payments
    public Payments createPayment(Payments payments);

    //2. Get all payments
    public List<Payments> getAllPayments();

    //3. Delete a payments
    public void deletePayment(int id);

    //4. update a payment
    public void updatePayment(int id, Payments payment);

    //5. get all payments by guest
    public List<Payments> getAllPaymentByGuest(Guest guest) throws Exception;
    
}
