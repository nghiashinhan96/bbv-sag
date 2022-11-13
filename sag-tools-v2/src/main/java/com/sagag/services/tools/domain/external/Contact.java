package com.sagag.services.tools.domain.external;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact implements Serializable {

  private static final long serialVersionUID = 8721479582199436436L;

  public static final String TYPE_PHONE = "Phone";
  public static final String TYPE_FAX = "Fax";
  public static final String TYPE_EMAIL = "Email";

  private String contactValue;

  private String contactDescription;

  private String contactType;

}
