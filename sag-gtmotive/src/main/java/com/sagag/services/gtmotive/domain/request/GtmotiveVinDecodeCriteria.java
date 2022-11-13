package com.sagag.services.gtmotive.domain.request;

import com.sagag.services.gtmotive.utils.GtmotiveUtils;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

@Data
public class GtmotiveVinDecodeCriteria {

  protected String vin;

  protected String makeCode;

  private String modifiedVin;

  /**
   * Returns the modified vin code.
   *
   * <pre>#5288: [CH/AT]:Convert an entered "o" to a "0" in an entered VIN</pre>
   *
   * @return the modified vin code
   */
  public String getModifiedVin() {
    if (StringUtils.isBlank(this.modifiedVin)) {
      this.modifiedVin = GtmotiveUtils.modifyVinCode(this.getVin());
    }
    return this.modifiedVin;
  }
}
