package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
//@formatter:off
@JsonPropertyOrder(
    {
      "seq_no",
      "lnk_type",
      "lnk_val",
      "accessory_lists_text"
      })
//@formatter:on
public class ArticleAccessory implements Serializable {

  private static final long serialVersionUID = 1887518134221262009L;

  @JsonProperty("seq_no")
  private Integer seqNo;

  @JsonProperty("lnk_type")
  private Integer linkType;

  @JsonProperty("lnk_val")
  private String linkVal;

  @JsonProperty("accessory_lists_text")
  private String accesoryListsText;

  @JsonProperty("acc_list_items")
  @Field(type = FieldType.Nested)
  private List<ArticleAccessoryItem> accesoryListItems;

}
