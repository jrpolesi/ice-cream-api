package com.jrpolesi.ice_cream_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;
import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.gateway.IConeGateway;
import com.jrpolesi.ice_cream_api.service.IConeService;

@Service
public class ConeServiceImpl implements IConeService {

  @Autowired
  private IConeGateway coneGateway;

  @Override
  public CreateConeResponseDto createCone(CreateConeRequestDto coneRequestDto) {
    final var coneIntent = Cone.with(
        coneRequestDto.type(),
        coneRequestDto.size());

    final var createCone = coneGateway.save(coneIntent);

    return mapToDto(createCone);
  }

  private CreateConeResponseDto mapToDto(Cone cone) {
    return new CreateConeResponseDto(
        cone.getId(),
        cone.getType(),
        cone.getSize());
  }
}
