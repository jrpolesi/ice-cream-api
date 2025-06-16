package com.jrpolesi.ice_cream_api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrpolesi.ice_cream_api.dto.CreateIceCreamRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.entities.IceCream;
import com.jrpolesi.ice_cream_api.gateway.IConeGateway;
import com.jrpolesi.ice_cream_api.gateway.IIceCreamGateway;
import com.jrpolesi.ice_cream_api.service.IIceCreamService;

@Service
public class IceCreamServiceImpl implements IIceCreamService {

  @Autowired
  private IIceCreamGateway iceCreamGateway;

  @Autowired
  private IConeGateway coneGateway;

  @Override
  public List<GetIceCreamResponseDto> getAllIceCreams() {

    final var iceCreams = iceCreamGateway.findAll();

    return iceCreams.stream()
        .map(GetIceCreamResponseDto::fromEntity)
        .toList();
  }

  @Override
  public GetIceCreamResponseDto getIceCreamById(Integer id) {
    final var iceCream = iceCreamGateway.findById(id);

    return GetIceCreamResponseDto.fromEntity(iceCream);
  }

  @Override
  public CreateIceCreamResponseDto createIceCream(CreateIceCreamRequestDto iceCreamRequestDto) {

    final var cone = coneGateway.findById(iceCreamRequestDto.coneId());

    final var iceCream = IceCream.with(
        iceCreamRequestDto.flavor(),
        iceCreamRequestDto.size(),
        iceCreamRequestDto.price(),
        cone);

    final var savedIceCream = iceCreamGateway.save(iceCream);

    return CreateIceCreamResponseDto.fromEntity(savedIceCream);
  }
}
