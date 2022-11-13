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
@Table(name = "ESHOP_CLIENT")
public class EshopClient implements Serializable {

  private static final long serialVersionUID = -422322643217505610L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String clientName;

  private String clientSecret;

  private int resourceId;

  private boolean active;
}
