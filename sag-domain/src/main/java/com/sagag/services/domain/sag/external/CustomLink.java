package com.sagag.services.domain.sag.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties("templated")
public class CustomLink extends Link {

  private static final long serialVersionUID = 3518541164350040733L;

  @JsonIgnore
  public CustomLink(String href) {
    super(href);
  }

  @Override
  public String getRel() {
    return StringUtils.EMPTY;
  }
}
