package com.jrpolesi.ice_cream_api.service;

import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;

public interface IConeService {
  CreateConeResponseDto createCone(CreateConeRequestDto coneRequestDto);
}
