package com.sagag.eshop.repo.entity.client;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import lombok.Data;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "V_ESHOP_CLIENT")
public class VEshopClient implements Serializable {

  private static final long serialVersionUID = -8553401668985611770L;

  @Id
  private int id;

  private String clientName;

  private String clientSecret;

  private String resourceId;

  private String clientRoles;

  public List<EshopAuthority> getEshopAuthorities() {
    if (StringUtils.isBlank(clientRoles)) {
      return Collections.emptyList();
    }
    final String[] roles = StringUtils.split(clientRoles, SagConstants.COMMA_NO_SPACE);
    if (ArrayUtils.isEmpty(roles)) {
      return Collections.emptyList();
    }
    return Stream.of(roles)
        .map(role -> EshopAuthority.valueOf(StringUtils.trim(role)))
        .collect(Collectors.toList());
  }

}
