package com.jrpolesi.ice_cream_api.gateway;

import java.util.List;

import com.jrpolesi.ice_cream_api.entities.Cone;

public interface IConeGateway {
  Cone findById(int id);

  List<Cone> findAll();

  List<Cone> findAllBySize(String size);

  Cone save(Cone cone);
}
