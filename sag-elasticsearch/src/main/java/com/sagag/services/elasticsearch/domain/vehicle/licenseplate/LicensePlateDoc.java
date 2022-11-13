package com.sagag.services.elasticsearch.domain.vehicle.licenseplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(
  indexName = "astra",
  type = "doc",
  shards = 5,
  replicas = 1,
  refreshInterval = "-1",
  createIndex = false,
  useServerConfiguration = true
)
public class LicensePlateDoc implements Serializable {

  private static final long serialVersionUID = -4598593323801723674L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("lp")
  private String lp;

  @JsonProperty("snr")
  private String snr;

  @JsonProperty("tsn")
  private String tsn;
}
