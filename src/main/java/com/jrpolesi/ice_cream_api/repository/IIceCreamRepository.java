package com.jrpolesi.ice_cream_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrpolesi.ice_cream_api.repository.model.IceCreamModel;

public interface IIceCreamRepository extends JpaRepository<IceCreamModel, Integer> {

} 