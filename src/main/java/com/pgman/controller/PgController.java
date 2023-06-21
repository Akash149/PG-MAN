package com.pgman.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgman.entities.Owner;
import com.pgman.entities.pg.Flat;
import com.pgman.entities.pg.Floor;
import com.pgman.entities.pg.PgDetails;
import com.pgman.service.PgService;
import com.pgman.service.pg.FlatService;
import com.pgman.service.pg.FloorService;
import com.pgman.service.pg.RoomService;

@RestController
@RequestMapping("/pg")
public class PgController {

    @Autowired
    PgService pgService;

    @Autowired
    FloorService floorService;

    @Autowired
    FlatService flatService;

    @Autowired
    RoomService roomService;

    private Owner owner = new Owner();
    
    Logger LOGGER = LoggerFactory.getLogger(PgController.class);

    @GetMapping("/{pgId}")
    public ResponseEntity<PgDetails> getPgDetails(@PathVariable("pgId") String pgId) {

        try {
            PgDetails pgDetails = pgService.getPgById(pgId);
            return ResponseEntity.ok().body(pgDetails);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    } 

    @GetMapping("/floor/{flId}")
    public ResponseEntity<List<Flat>> getFlat(@PathVariable("flId") int floorid) {
        Floor floor = null;
        List<Flat> flats = null;
        try {
            floor = floorService.getAFloor(floorid);
            flats = flatService.getFlatByFloor(floor);
            return ResponseEntity.ok().body(flats);
        } catch (Exception e) {
            LOGGER.error("{}",e.getMessage());
            return null;
        }
    }
    
}
