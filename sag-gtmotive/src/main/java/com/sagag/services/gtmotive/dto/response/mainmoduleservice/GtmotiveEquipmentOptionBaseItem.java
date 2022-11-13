package com.sagag.services.gtmotive.dto.response.mainmoduleservice;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GtmotiveEquipmentOptionBaseItem implements Serializable {

  private static final long serialVersionUID = -8911738766003683125L;

  private String code;
  private String description;

  @JsonIgnore
  public boolean isValidItem() {
    return !isEmpty();
  }

  @JsonIgnore
  public boolean isEmpty() {
    return StringUtils.isBlank(code) && StringUtils.isBlank(description);
  }
}
