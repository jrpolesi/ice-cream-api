package com.jrpolesi.ice_cream_api.service;

import java.util.List;

import com.jrpolesi.ice_cream_api.dto.CreateIceCreamRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;

public interface IIceCreamService {
  List<GetIceCreamResponseDto> getAllIceCreams();

  CreateIceCreamResponseDto createIceCream(CreateIceCreamRequestDto iceCreamRequestDto);
}
