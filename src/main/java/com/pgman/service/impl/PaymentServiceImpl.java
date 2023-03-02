package com.pgman.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgman.entities.Guest;
import com.pgman.entities.Payments;
import com.pgman.service.PaymentService;
import com.pgman.dao.PaymentsRepository;

public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentsRepository paymentRepo;

    @Override
    public Payments createPayment(Payments payments) {
        return paymentRepo.save(payments);
    }

    @Override
    public void deletePayment(int id) {
        Payments pay = paymentRepo.findById(id).get();
       try {
            if(pay != null) {
                paymentRepo.deleteById(id);
            } else {
                throw new Exception("Resource Not found");
            }
       } catch (Exception e) {
            e.printStackTrace();
       }
    }

    @Override
    public List<Payments> getAllPayments() {
        return paymentRepo.findAll();
    }

    @Override
    public void updatePayment(int id, Payments payments) {
        Payments pay = paymentRepo.findById(id).get();
        try {
            if(pay != null) {
                paymentRepo.deleteById(id);
            } else {
                throw new Exception("Resource Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }

    @Override
    public List<Payments> getAllPaymentByGuest(Guest guest) throws Exception {   
        List<Payments> payments = null;
        payments = paymentRepo.findByGuest(guest);
        
        if(payments != null) {
            return payments;
        } else {
            throw new Exception("Payments not found");
        }
    }  
     
}
