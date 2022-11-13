package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnOrderDto implements Serializable {

  private static final long serialVersionUID = -4516271584735924027L;

  private String returnOrderUrl;

  private String journalId;

  private String batchJobId;

  private List<ReturnOrderPositionDto> returnOrderPositions;

  public List<String> getQuarantineOrderPositions() {
    return getOrderNrsByFilter(returnOrderPositions,
        pos -> BooleanUtils.isTrue(pos.getQuarantineOrder()));
  }

  public List<String> getNonQuarantineOrderPositions() {
    return getOrderNrsByFilter(returnOrderPositions,
        pos -> BooleanUtils.isFalse(pos.getQuarantineOrder()));
  }

  private static List<String> getOrderNrsByFilter(final List<ReturnOrderPositionDto> positions,
      final Predicate<ReturnOrderPositionDto> predicate) {
    if (CollectionUtils.isEmpty(positions)) {
      return Collections.emptyList();
    }
    return positions.stream().filter(predicate).map(ReturnOrderPositionDto::getOrderNr)
        .collect(Collectors.toList());
  }
}
