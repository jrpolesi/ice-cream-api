package com.jrpolesi.ice_cream_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;
import com.jrpolesi.ice_cream_api.service.IConeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cone")
public class ConeController {

  @Autowired
  private IConeService coneService;

  @PostMapping
  public ResponseEntity<CreateConeResponseDto> createCone(@RequestBody @Valid CreateConeRequestDto coneRequestDto) {
    final var cone = coneService.createCone(coneRequestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(cone);
  }

  @GetMapping
  public ResponseEntity<List<GetConeResponseDto>> getCone() {
    final var cones = coneService.getAllCones();

    return ResponseEntity.status(HttpStatus.OK).body(cones);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetConeResponseDto> getConeById(@PathVariable int id) {
    final var cone = coneService.getConeById(id);
    
    return ResponseEntity.status(HttpStatus.OK).body(cone);
  }
}
