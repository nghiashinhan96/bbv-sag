package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * License setting Dto class.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseSettingsDto implements Serializable {

  private static final long serialVersionUID = 2791579103318793264L;

  private int id;

  private long packId;

  private String packName;

  @JsonSerialize(using = ToStringSerializer.class)
  private long packArticleId;

  private String packArticleNo;

  @JsonSerialize(using = ToStringSerializer.class)
  private String packUmarId;

  private int quantity;

  private Date lastUpdate;

  private String lastUpdatedBy;

  private String typeOfLicense;

  private String productText;

  private String optionalParameters;

  /**
   * Constructs the License settings Dto.
   */
  public LicenseSettingsDto(final int id, final long packId, final String packName) {
    this.id = id;
    this.packId = packId;
    this.packName = packName;
  }
}
