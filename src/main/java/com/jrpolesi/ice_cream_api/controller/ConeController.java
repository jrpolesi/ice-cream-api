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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Create a new cone")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cone created successfully")
  })
  @PostMapping
  public ResponseEntity<CreateConeResponseDto> createCone(
      @RequestBody @Valid CreateConeRequestDto coneRequestDto) {
    final var cone = coneService.createCone(coneRequestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(cone);
  }

  @Operation(summary = "Get all cones or filter by size")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of cones returned successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid size parameter"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
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

  @Operation(summary = "Get cone by ID")
  @GetMapping("/{id}")
  public ResponseEntity<GetConeResponseDto> getConeById(@PathVariable int id) {
    final var cone = coneService.getConeById(id);

    return ResponseEntity.status(HttpStatus.OK).body(cone);
  }
}
