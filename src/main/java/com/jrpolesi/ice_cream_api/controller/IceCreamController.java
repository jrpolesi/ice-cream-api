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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jrpolesi.ice_cream_api.dto.CreateIceCreamRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.service.IIceCreamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Validated // This annotation is used to enable validation on RequestParam
@RestController
public class IceCreamController {

  @Autowired
  private IIceCreamService iceCreamService;

  // public IceCreamController(IIceCreamService iceCreamService) {
  // this.iceCreamService = iceCreamService;
  // }

  @Operation(summary = "Create a new ice cream")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Ice cream created successfully")
  })
  @PostMapping("/ice-cream")
  public ResponseEntity<CreateIceCreamResponseDto> createIceCream(
      @RequestBody @Valid CreateIceCreamRequestDto iceCreamRequestDto) {

    final var createdIceCream = iceCreamService.createIceCream(iceCreamRequestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdIceCream);
  }

  @Operation(summary = "Get all ice creams or filter by size")
  @GetMapping("/ice-cream")
  public ResponseEntity<List<GetIceCreamResponseDto>> getIceCream(
      @RequestParam(required = false) @Size(min = 1, max = 1, message = "Size param must be a single character") String size) {

    final var iceCreams = iceCreamService.getAllIceCreams(size);

    return ResponseEntity.status(HttpStatus.OK).body(iceCreams);
  }

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ice cream returned successfully")
  })
  @GetMapping("/ice-cream/{id}")
  public ResponseEntity<GetIceCreamResponseDto> getIceCreamById(@PathVariable Integer id) {

    final var iceCream = iceCreamService.getIceCreamById(id);

    return ResponseEntity.status(HttpStatus.OK).body(iceCream);
  }
}
