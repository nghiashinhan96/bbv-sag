package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobOrderNumberDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;

import lombok.Data;

@Data
public class AxReturnOrderBatchJobResult implements Serializable {

  private static final long serialVersionUID = 5329078286915855156L;
  
  private List<AxReturnOrderBatchJobOrderNumber> orderNumbers;

  public ReturnOrderBatchJobsResultDto toDto() {
    final ReturnOrderBatchJobsResultDto batchJobsResultDto = new ReturnOrderBatchJobsResultDto();
    final List<ReturnOrderBatchJobOrderNumberDto> orderNumbersDtos = CollectionUtils
        .emptyIfNull(orderNumbers).stream().map(batchJobOrderNumber -> SagBeanUtils
            .map(batchJobOrderNumber, ReturnOrderBatchJobOrderNumberDto.class))
        .collect(Collectors.toList());
    batchJobsResultDto.setOrderNumbers(orderNumbersDtos);
    return batchJobsResultDto;
  }

}

