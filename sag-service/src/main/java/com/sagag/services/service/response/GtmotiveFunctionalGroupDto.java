package com.sagag.services.service.response;

import com.sagag.services.service.response.gtmotive.GtmotivePartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GtmotiveFunctionalGroupDto implements Serializable {

  private static final long serialVersionUID = -5627000404808730862L;

  private String functionalGroup;
  private String functionalGroupDescription;
  private List<GtmotivePartItem> parts;
}
