package com.sagag.services.tools.domain.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class POJO for contact of customer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo implements Serializable {

  private static final long serialVersionUID = 217509458821633569L;

  private String value;

  private String description;

  private String type;

  private boolean isPrimary;
}
