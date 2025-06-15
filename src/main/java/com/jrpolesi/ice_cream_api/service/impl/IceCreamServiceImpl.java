package com.jrpolesi.ice_cream_api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.entities.IceCream;
import com.jrpolesi.ice_cream_api.repository.IIceCreamRepository;
import com.jrpolesi.ice_cream_api.service.IIceCreamService;

@Service
public class IceCreamServiceImpl implements IIceCreamService {

  @Autowired
  private IIceCreamRepository iceCreamRepository;

  public List<GetIceCreamResponseDto> getAllIceCreams() {

    final var iceCreams = iceCreamRepository.findAll().stream().map(m -> m.toEntity()).toList();

    return iceCreams.stream()
        .map(this::mapToDto)
        .toList();
  }

  private GetIceCreamResponseDto mapToDto(IceCream iceCream) {
    return new GetIceCreamResponseDto(
        iceCream.getId(),
        iceCream.getFlavor(),
        iceCream.getSize(),
        iceCream.getPrice());
  }
}
