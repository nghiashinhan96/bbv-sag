package com.sagag.services.ivds.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Request body class for Category search.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchRequest implements Serializable {

  private static final long serialVersionUID = -8859375367633549602L;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<String> genArtIds;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<String> vehIds;

  private String selectedTypeId;

  private int size;

  private boolean selectedFromQuickClick;

  @JsonIgnore
  private boolean usingVersion2;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<String> selectedOilIds;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<String> selectedCategoryIds;

  @JsonIgnore
  public List<String> getSelectedOilIds() {
    if (StringUtils.isBlank(selectedTypeId)) {
      return this.selectedOilIds;
    }
    return Arrays.asList(this.selectedTypeId);
  }
}
