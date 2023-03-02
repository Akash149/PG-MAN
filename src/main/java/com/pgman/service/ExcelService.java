package com.pgman.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgman.entities.Guest;
import com.pgman.entities.Owner;
import com.pgman.helper.Generator;

@Service
public class ExcelService {

    @Autowired
    private OwnerService ownerService;

    public ByteArrayInputStream getAllGuestByOwner(Owner owner) throws Exception {
        List<Guest> guests = ownerService.getAllGuest(owner);

        ByteArrayInputStream bais = Generator.dataToExcel(guests);
        return bais;
    }
    
}
