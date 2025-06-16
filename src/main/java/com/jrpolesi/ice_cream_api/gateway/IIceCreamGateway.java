package com.jrpolesi.ice_cream_api.gateway;

import java.util.List;

import com.jrpolesi.ice_cream_api.entities.IceCream;

public interface IIceCreamGateway {
  List<IceCream> findAll();

  IceCream findById(Integer id);

  IceCream save(IceCream iceCream);
}
