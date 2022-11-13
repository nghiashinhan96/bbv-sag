package com.sagag.eshop.repo.entity.message;

import com.sagag.eshop.repo.entity.EshopRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_ACCESS_RIGHT_ROLE.
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT_ROLE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAccessRightRole implements Serializable {

  private static final long serialVersionUID = -7858558113920106265L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ACCESS_RIGHT_ID", nullable = false)
  private MessageAccessRight messageAccessRight;

  @ManyToOne
  @JoinColumn(name = "ESHOP_ROLE_ID", nullable = false)
  private EshopRole eshopRole;
}
