package com.sagag.services.haynespro.dto;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data(staticConstructor = "of")
public class HaynesProOptionDto implements Serializable {

  private static final long serialVersionUID = -6624699420953712287L;

  @NonNull
  private String code;

  @NonNull
  private String name;

}
