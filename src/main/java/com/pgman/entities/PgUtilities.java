package com.pgman.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pgman.service.GuestService;
import com.pgman.service.TransactionService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PgUtilities implements Serializable{

    @Autowired
    private GuestService guestService;

    @Autowired
    private TransactionService transactionService;

    private int month = LocalDateTime.now().getMonthValue();
    private int year = LocalDateTime.now().getYear();
    private float collectedAmount = 0;
    private float totalRent = 0;
    private float percent = 0;
    private float remainingAmount = 0;
    private List<Transactions> tr = new ArrayList<Transactions>();

    private LocalDate startDate = LocalDate.of(year, month, 01);
    private LocalDate endDate = LocalDate.now();

    private static Logger logger = LoggerFactory.getLogger(PgUtilities.class);

    public int getRentCollectionPercentage(Owner owners) {
        try {       
            tr = transactionService.getTransactionsBetween(startDate,endDate,owners,"rent");
            totalRent = guestService.getTotalRent(owners);
            logger.info("Transaction between {}",startDate +" to " + endDate + ", Transaction count is " + tr.size());
            collectedAmount = 0;
            for (Transactions transactions : tr) {
                collectedAmount += transactions.getPayments().getAmount();  
            } 
            // Get percentage of collected amount
            percent = (collectedAmount/totalRent) * 100;
            remainingAmount = totalRent-collectedAmount;

            logger.info("Total collected amount of this month is " + collectedAmount);
            logger.info("Total rent should be collected: " + totalRent);
            logger.info("Collected percentage is " + percent + "%");
            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("{}", e.getMessage());
        }
        return (int) percent;
    }
}
