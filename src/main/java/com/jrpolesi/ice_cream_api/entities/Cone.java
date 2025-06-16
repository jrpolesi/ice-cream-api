package com.jrpolesi.ice_cream_api.entities;

public class Cone {
  private Integer id;
  private String type;
  private String size;

  private Cone(Integer id, String type, String size) {
    this.id = id;
    this.type = type;
    this.size = size;
  }

  public static Cone with(String type, String size) {
    return new Cone(null, type, size);
  }

  public static Cone of(Integer id, String type, String size) {
    return new Cone(id, type, size);
  }

  public Integer getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getSize() {
    return size;
  }
}
