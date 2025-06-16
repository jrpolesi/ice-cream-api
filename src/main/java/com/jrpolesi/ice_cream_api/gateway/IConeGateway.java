package com.jrpolesi.ice_cream_api.gateway;

import com.jrpolesi.ice_cream_api.entities.Cone;

public interface IConeGateway {
  Cone findById(int id);
  Cone save(Cone cone);
}
