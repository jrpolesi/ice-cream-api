package com.jrpolesi.ice_cream_api.repository.model;

import java.util.List;

import com.jrpolesi.ice_cream_api.entities.Cone;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cones")
public class ConeModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @Column(name = "type", nullable = false)
  String type;

  @Column(name = "size", nullable = false, length = 1)
  String size;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "ice_cream_id")
  List<IceCreamModel> iceCreams;

  public ConeModel() {
  }

  private ConeModel(Integer id, String type, String size) {
    this.id = id;
    this.type = type;
    this.size = size;
  }

  public Cone toEntity() {
    return Cone.with(id, type, size);
  }

  public static ConeModel fromEntity(Cone cone) {
    return new ConeModel(
        cone.getId(),
        cone.getType(),
        cone.getSize());
  }
}
