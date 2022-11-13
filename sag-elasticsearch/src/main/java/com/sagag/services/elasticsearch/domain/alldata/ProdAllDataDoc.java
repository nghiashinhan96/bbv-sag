package com.sagag.services.elasticsearch.domain.alldata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(
  indexName = "prod_alldata",
  type = "ARTICLE",
  shards = 5,
  replicas = 1,
  refreshInterval = "-1",
  createIndex = false,
  useServerConfiguration = true
)
public class ProdAllDataDoc implements Serializable {

  private static final long serialVersionUID = -565620587064550737L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("original_article_number")
  private String originalArticleNumber;

  @JsonProperty("gaid")
  private String gaId;

  @JsonProperty("EAN")
  private List<String> eanNumbers;

  @JsonProperty("oe_number")
  private List<String> oeNumbers;

  @JsonProperty("Artikelnummer")
  private List<String> articleNumbers;

  @JsonProperty("reference_number")
  private List<String> referenceNumbers;

  @JsonProperty("Gebrauchsnummer")
  private List<String> usageNumbers;
}
