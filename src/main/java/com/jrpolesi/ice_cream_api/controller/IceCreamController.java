package com.jrpolesi.ice_cream_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jrpolesi.ice_cream_api.dto.CreateIceCreamRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.service.IIceCreamService;

import jakarta.validation.Valid;

@RestController
public class IceCreamController {

  @Autowired
  private IIceCreamService iceCreamService;

  // public IceCreamController(IIceCreamService iceCreamService) {
  //   this.iceCreamService = iceCreamService;
  // }

  @GetMapping("/ice-cream")
  public ResponseEntity<List<GetIceCreamResponseDto>> getIceCream() {

    final var iceCreams = iceCreamService.getAllIceCreams();

    return ResponseEntity.status(HttpStatus.OK).body(iceCreams);
  }

  @PostMapping("/ice-cream")
  public ResponseEntity<CreateIceCreamResponseDto> createIceCream(@RequestBody @Valid CreateIceCreamRequestDto iceCreamRequestDto) {

    final var createdIceCream = iceCreamService.createIceCream(iceCreamRequestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdIceCream);
  }
}
