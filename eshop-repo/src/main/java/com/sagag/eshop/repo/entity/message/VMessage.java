package com.sagag.eshop.repo.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Message view class which maps to view V_MESSAGE.
 */
@Entity
@Table(name = "V_MESSAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private long id;

  private String title;

  private String type;

  private String area;

  private String subArea;

  private String locationValue;

  private boolean active;

  private Date createdDate;

  private Date dateValidFrom;

  private Date dateValidTo;
}
