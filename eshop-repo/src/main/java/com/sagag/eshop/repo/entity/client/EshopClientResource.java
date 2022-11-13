package com.sagag.eshop.repo.entity.client;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ESHOP_CLIENT_RESOURCE")
public class EshopClientResource implements Serializable {

  private static final long serialVersionUID = 1750632332654053129L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  private String description;

  private boolean active;
}
