package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapping;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.Id;

@Data
// @formatter:off
@Document(
		indexName = "ch_unitree",
		type = "tree",
		shards = 5,
		replicas = 1,
		refreshInterval = "-1",
		createIndex = false,
		useServerConfiguration = true
)
@JsonPropertyOrder({
		"tree_id",
		"tree_name",
		"tree_image",
		"tiles",
		"nodes",
		"tree_sort",
		"tree_external_service",
		"tree_external_service_attribute"
})
// @formatter:on
@EqualsAndHashCode(of = "id")
public class UnitreeDoc implements Serializable {

  private static final long serialVersionUID = 1233321274512124426L;

  @Id
  @Mapping("treeId")
  @JsonProperty("tree_id")
  private String id;

  @JsonProperty("tree_name")
  private String treeName;

  @JsonProperty("tree_image")
  private String treeImage;

  @JsonProperty("tiles")
  @Field(type = FieldType.Nested)
  private List<Tile> tiles;

  @JsonProperty("nodes")
  @Field(type = FieldType.Nested)
  private List<UnitreeNode> unitreeNodes;

  @JsonProperty("tree_sort")
  private String treeSort;

  @JsonProperty("tree_external_service")
  private String treeExternalService;

  @JsonProperty("tree_external_service_attribute")
  private String treeExternalServiceAttribute;

  @JsonIgnore
  public Optional<UnitreeNode> findNodeById(String nodeId) {
    if (CollectionUtils.isEmpty(unitreeNodes)) {
      return Optional.empty();
    }
    return unitreeNodes.stream()
        .filter(node -> StringUtils.equalsIgnoreCase(nodeId, node.getLeafId())).findFirst();
  }
}
