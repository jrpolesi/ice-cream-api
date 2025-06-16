package com.jrpolesi.ice_cream_api.gateway.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.gateway.IConeGateway;
import com.jrpolesi.ice_cream_api.repository.IConeRepository;
import com.jrpolesi.ice_cream_api.repository.model.ConeModel;

@Component
public class ConeGatewayImpl implements IConeGateway {

  @Autowired
  private IConeRepository coneRepository;

  @Override
  public Cone findById(int id) {
    return coneRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cone not found with id: " + id))
        .toEntity();
  }

  @Override
  public Cone save(Cone cone) {
    final var coneModel = ConeModel.fromEntity(cone);

    return coneRepository.save(coneModel).toEntity();
  }

  @Override
  public List<Cone> findAll() {
    return coneRepository.findAll().stream()
        .map(ConeModel::toEntity)
        .toList();
  }

  @Override
  public List<Cone> findAllBySize(String size) {
    return coneRepository.findAllBySize(size)
        .stream()
        .map(ConeModel::toEntity)
        .toList();
  }
}
