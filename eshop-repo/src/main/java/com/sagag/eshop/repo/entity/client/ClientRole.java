package com.sagag.eshop.repo.entity.client;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CLIENT_ROLE")
public class ClientRole implements Serializable {

  private static final long serialVersionUID = 1355656509612834623L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private int clientId;

  private int roleId;

  public ClientRole(int clientId, int roleId) {
    setClientId(clientId);
    setRoleId(roleId);
  }

}
