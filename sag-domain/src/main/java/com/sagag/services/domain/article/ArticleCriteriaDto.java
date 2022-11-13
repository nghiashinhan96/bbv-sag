package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

@Data
public class ArticleCriteriaDto implements Serializable {

  private static final long serialVersionUID = -5170358546828822739L;

  @JsonProperty("cvp")
  private String cvp;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("cn")
  private String cndech;

  @JsonProperty("csort")
  private int csort;

  @JsonIgnore
  public Long getCidLong() {
    if (StringUtils.isBlank(cid)) {
      return NumberUtils.LONG_MINUS_ONE;
    }
    return NumberUtils.toLong(cid);
  }

}
