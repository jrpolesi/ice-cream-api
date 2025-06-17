package com.jrpolesi.ice_cream_api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;
import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.gateway.IConeGateway;
import com.jrpolesi.ice_cream_api.service.IConeService;

@Service
public class ConeServiceImpl implements IConeService {

  @Autowired
  private IConeGateway coneGateway;

  @Override
  public CreateConeResponseDto createCone(CreateConeRequestDto coneRequestDto) {
    final var coneIntent = Cone.create(
        coneRequestDto.type(),
        coneRequestDto.size());

    final var createCone = coneGateway.save(coneIntent);

    return CreateConeResponseDto.fromEntity(createCone);
  }

  @Override
  public List<GetConeResponseDto> getAllCones() {
    final var cones = coneGateway.findAll();

    return cones.stream().map(GetConeResponseDto::fromEntity).toList();
  }

  @Override
  public List<GetConeResponseDto> searchAllConesBySize(String size) {
    final var cones = coneGateway.findAllBySize(size);

    return cones.stream().map(GetConeResponseDto::fromEntity).toList();
  }

  @Override
  public GetConeResponseDto getConeById(int id) {
    final var cone = coneGateway.findById(id);

    return GetConeResponseDto.fromEntity(cone);
  }
}
