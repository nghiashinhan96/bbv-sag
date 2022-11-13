package com.sagag.services.article.api.request;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalVendorRequestBody {

  private String companyName;

  private List<String> articleIds;

  private List<VendorDto> vendors;
}
