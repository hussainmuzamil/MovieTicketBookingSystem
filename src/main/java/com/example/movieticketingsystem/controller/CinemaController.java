package com.example.movieticketingsystem.controller;

import com.example.movieticketingsystem.dto.CinemaRequest;
import com.example.movieticketingsystem.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cinema")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;
    @GetMapping
    public ResponseEntity<?> getCinemaById(@RequestParam(name = "id") long id){
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/cinemas")
    public ResponseEntity<?> getAllCinemas(){
        return ResponseEntity.accepted().build();
    }
    @PostMapping
    public ResponseEntity<?> addCinema(@RequestBody CinemaRequest cinemaRequest){
        return ResponseEntity.accepted().build();
    }
    @PutMapping
    public ResponseEntity<?> updateCinema(
            @RequestParam(name = "id") long id,
            @RequestBody CinemaRequest cinemaRequest
    ){
        return ResponseEntity.accepted().build();
    }
    @DeleteMapping
    public ResponseEntity<?> deleteCinema(@RequestParam(name = "id") long id){
        return ResponseEntity.accepted().build();
    }
}
