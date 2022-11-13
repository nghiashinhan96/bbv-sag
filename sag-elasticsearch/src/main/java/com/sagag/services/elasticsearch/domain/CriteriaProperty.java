package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.persistence.Id;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "cid", "cvp" })
public class CriteriaProperty implements Serializable {

  private static final long serialVersionUID = 4820513219627793272L;

  @Id
  @JsonIgnore
  private long id;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("cvp")
  private String cvp;

}
