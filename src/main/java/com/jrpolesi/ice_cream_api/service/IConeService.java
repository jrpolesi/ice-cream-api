package com.jrpolesi.ice_cream_api.service;

import java.util.List;

import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;

public interface IConeService {
  CreateConeResponseDto createCone(CreateConeRequestDto coneRequestDto);

  List<GetConeResponseDto> getAllCones();

  GetConeResponseDto getConeById(int id);
}
