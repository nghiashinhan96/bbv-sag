package com.sagag.services.domain.eshop.dto.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OrganisationCollectionDto implements Serializable {

  private static final long serialVersionUID = -4508540212180383262L;

  private String affiliateShortName;
  private String name;
  private String description;
  private List<PermissionConfigurationDto> permissions;
  private Map<String, String> settings;
  private String collectionShortName;
  @JsonProperty("default")
  private Boolean isDefault;

  @JsonIgnore
  private List<Integer> customerSettingIds;

  @JsonIgnore
  private List<Integer> finalCustomerSettingIds;

  public Map<String, String> getSettings() {
    if (this.settings == null) {
      this.settings = new HashMap<>();
    }
    return this.settings;
  }

  @JsonIgnore
  public void addSetting(String key, String value) {
    if (this.settings == null) {
      this.settings = new HashMap<>();
    }
    this.settings.put(key, value);
  }
}
