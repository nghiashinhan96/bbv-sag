package com.sagag.services.ax.domain.vendor;

import java.io.Serializable;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.common.utils.SagBeanUtils;

import lombok.Data;

@Data
public class AxVendor implements Serializable {

  private static final long serialVersionUID = -918646970977985299L;

  private String vendorId;

  private String vendorName;

  private String leadTime;

  private String articleId;

  private String externalArticleId;

  public VendorDto toDto() {
    return SagBeanUtils.map(this, VendorDto.class);
  }

}
