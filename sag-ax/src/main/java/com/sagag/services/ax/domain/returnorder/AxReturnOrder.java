package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderPositionDto;

import lombok.Data;

@Data
public class AxReturnOrder implements Serializable {

  private static final long serialVersionUID = -4516271584735924027L;

  private String returnOrderUrl;

  private String journalId;

  private String batchJobId;

  private List<AxReturnOrderPosition> returnOrderPositionList;

  public ReturnOrderDto toDto() {
    final ReturnOrderDto returnOrder = new ReturnOrderDto();
    returnOrder.setReturnOrderUrl(returnOrderUrl);
    returnOrder.setJournalId(journalId);
    returnOrder.setBatchJobId(batchJobId);
    if (!CollectionUtils.isEmpty(returnOrderPositionList)) {
      final List<ReturnOrderPositionDto> positions = returnOrderPositionList.stream()
          .map(pos -> SagBeanUtils.map(pos, ReturnOrderPositionDto.class))
          .collect(Collectors.toList());
      returnOrder.setReturnOrderPositions(positions);
    }
    return returnOrder;
  }

}

