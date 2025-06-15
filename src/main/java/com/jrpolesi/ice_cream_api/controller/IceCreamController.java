package com.jrpolesi.ice_cream_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.service.IIceCreamService;

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
}
