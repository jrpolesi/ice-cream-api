package com.jrpolesi.ice_cream_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jrpolesi.ice_cream_api.repository.model.IceCreamModel;

public interface IIceCreamRepository extends JpaRepository<IceCreamModel, Integer> {

  @Query("SELECT i FROM IceCreamModel i WHERE (:size IS NULL OR i.size = :size)")
  List<IceCreamModel> findAllBySizeNullable(@Param("size") String size);
}