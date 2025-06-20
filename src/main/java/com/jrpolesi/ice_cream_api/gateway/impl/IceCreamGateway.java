package com.jrpolesi.ice_cream_api.gateway.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jrpolesi.ice_cream_api.entities.IceCream;
import com.jrpolesi.ice_cream_api.gateway.IIceCreamGateway;
import com.jrpolesi.ice_cream_api.repository.IIceCreamRepository;
import com.jrpolesi.ice_cream_api.repository.model.IceCreamModel;

@Component
public class IceCreamGateway implements IIceCreamGateway {

  @Autowired
  private IIceCreamRepository iceCreamRepository;

  @Override
  public List<IceCream> findAll(String size) {
    return iceCreamRepository.findAllBySizeNullable(size)
        .stream()
        .map(iceCream -> iceCream.toEntity())
        .toList();
  }

  @Override
  public IceCream save(IceCream iceCream) {
    final var iceCreamModel = IceCreamModel.fromEntity(iceCream);

    return iceCreamRepository
        .save(iceCreamModel)
        .toEntity();
  }

  @Override
  public IceCream findById(Integer id) {
    return iceCreamRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Ice cream not found with id: " + id))
        .toEntity();
  }
}
