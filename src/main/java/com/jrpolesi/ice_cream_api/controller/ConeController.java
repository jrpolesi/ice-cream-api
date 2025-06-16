package com.jrpolesi.ice_cream_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jrpolesi.ice_cream_api.configurations.ConeConfigurations;
import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;
import com.jrpolesi.ice_cream_api.service.IConeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@RestController
@Validated
@RequestMapping("/cone")
public class ConeController {

  @Autowired
  private IConeService coneService;

  @Autowired
  private ConeConfigurations coneConfigs;

  @PostMapping
  public ResponseEntity<CreateConeResponseDto> createCone(
      @RequestBody @Valid CreateConeRequestDto coneRequestDto) {
    final var cone = coneService.createCone(coneRequestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(cone);
  }

  @GetMapping
  public ResponseEntity<List<GetConeResponseDto>> getCone(
      @RequestParam(required = false) @Size(min = 1, max = 1, message = "Size param must be a single character") String size) {

    List<GetConeResponseDto> cones;

    if (coneConfigs.coneSearchProperties().isEnabled() && size != null) {
      cones = coneService.searchAllConesBySize(size);
    } else {
      cones = coneService.getAllCones();
    }

    return ResponseEntity.status(HttpStatus.OK).body(cones);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetConeResponseDto> getConeById(@PathVariable int id) {
    final var cone = coneService.getConeById(id);

    return ResponseEntity.status(HttpStatus.OK).body(cone);
  }
}
