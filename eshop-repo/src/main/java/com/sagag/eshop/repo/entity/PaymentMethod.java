package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity for {@link PaymentMethod} table.
 */
@Table(name = "PAYMENT_METHOD")
@Entity
@NamedQueries(
    value = {
        @NamedQuery(name = "PaymentMethod.findAll", query = "SELECT p FROM PaymentMethod p"),
        @NamedQuery(
            name = "PaymentMethod.findByDescCode",
            query = "select p from PaymentMethod p where p.descCode= :descCode") })
@Data
public class PaymentMethod implements Serializable {

  private static final long serialVersionUID = -3506928523760404340L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String descCode;

  private String payMethod;

  private String description;

  private Integer orderDisplay;

}
