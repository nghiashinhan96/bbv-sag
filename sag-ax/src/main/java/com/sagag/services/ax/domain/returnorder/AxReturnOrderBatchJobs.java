package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobInfoDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;

import lombok.Data;

@Data
public class AxReturnOrderBatchJobs implements Serializable {

  private static final long serialVersionUID = -2099464807000359224L;

  private List<AxReturnOrderBatchJobInfo> batchJobs;

  public ReturnOrderBatchJobsDto toDto() {
    final ReturnOrderBatchJobsDto batchJobsDto = new ReturnOrderBatchJobsDto();
    final List<ReturnOrderBatchJobInfoDto> batchJobInfoDtos = CollectionUtils.emptyIfNull(batchJobs)
        .stream().map(batchJob -> SagBeanUtils.map(batchJob, ReturnOrderBatchJobInfoDto.class))
        .collect(Collectors.toList());
    batchJobsDto.setBatchJobs(batchJobInfoDtos);
    return batchJobsDto;
  }

}

