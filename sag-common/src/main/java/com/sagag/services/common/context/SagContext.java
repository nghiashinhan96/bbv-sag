package com.sagag.services.common.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The SAG context entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagContext {

  private String requestId;

}
