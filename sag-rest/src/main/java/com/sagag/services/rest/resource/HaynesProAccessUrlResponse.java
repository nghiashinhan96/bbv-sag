package com.sagag.services.rest.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.hateoas.ResourceSupport;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class HaynesProAccessUrlResponse extends ResourceSupport {

  @NonNull
  private String accessUrl;
}
