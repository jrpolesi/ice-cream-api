package com.jrpolesi.ice_cream_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrpolesi.ice_cream_api.repository.model.ConeModel;

public interface IConeRepository extends JpaRepository<ConeModel, Integer> {
  
}
