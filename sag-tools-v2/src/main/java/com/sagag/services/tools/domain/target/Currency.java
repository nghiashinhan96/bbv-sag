package com.sagag.services.tools.domain.target;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable {

  private static final long serialVersionUID = -3578474556330690291L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ISO_CODE")
  private String isoCode;

  private String description;
}
