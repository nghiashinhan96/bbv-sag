package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
@JsonPropertyOrder(
    {
     "umarId",
     "articleId",
     "leadingArticleId",
     "articleAvailability"
    })
public class Availability implements Serializable {

  private static final long serialVersionUID = 649336307275822014L;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long umarId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long articleId;

  /**
   * The identifier of the leading article of the deprecated UMAR. Note: The attribute is included
   * in serialized JSON response only for requests upon the deprecated UMAR.
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long leadingArticleId;

  private AvailabilityArticleAvailability articleAvailability;

}
