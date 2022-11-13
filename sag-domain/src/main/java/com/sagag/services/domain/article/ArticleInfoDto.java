package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleInfoDto implements Serializable {

  private static final long serialVersionUID = 2975958107678558894L;

  @JsonProperty("id_info")
  private String idInfo;

  @JsonProperty("info_type")
  private String infoType;

  @JsonProperty("info_txt")
  private String infoTxtDe;

}
