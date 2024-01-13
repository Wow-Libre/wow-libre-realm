package com.auth.wow.libre.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Benefit {
  public final Integer id;
  public final String name;
  public final String description;
  @JsonProperty("background_image")
  public final String backgroundImage;
  public final String icon;

  public Benefit(Integer id, String name, String description, String backgroundImage, String icon) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.backgroundImage = backgroundImage;
    this.icon = icon;
  }
}
