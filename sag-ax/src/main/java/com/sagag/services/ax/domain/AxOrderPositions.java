package com.sagag.services.ax.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.sag.erp.ExternalOrderPosition;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import lombok.Data;
import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class to receive the list of order positions from Dynamic AX ERP.
 *
 */
@Data
public class AxOrderPositions implements Serializable {

  private static final long serialVersionUID = 6184149771939942247L;

  @JsonProperty("_links")
  private Map<String, Link> links;

  private List<AxOrderPosition> orderPositions;

  public ExternalOrderPositions toExternalOrderPositionsDto() {
    List<ExternalOrderPosition> externalOrderPositions = new ArrayList<>();
    if (!Objects.isNull(this.orderPositions)) {
      externalOrderPositions.addAll(this.orderPositions.stream()
          .map(AxOrderPosition::toExternalOrderPositionDto).collect(Collectors.toList()));
    }
    return ExternalOrderPositions.builder()
        .positions(externalOrderPositions)
        .links(this.links).build();
  }
}
