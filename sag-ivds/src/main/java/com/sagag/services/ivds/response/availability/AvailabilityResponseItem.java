package com.sagag.services.ivds.response.availability;

import com.sagag.services.domain.sag.erp.Availability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Availability response item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponseItem implements Serializable {

  private static final long serialVersionUID = 268870125689638807L;

  private List<Availability> availabilities;

  public int getTotalQuantity() {
    if (CollectionUtils.isEmpty(availabilities)) {
      return 0;
    }
    return availabilities.stream().mapToInt(Availability::getDefaultQuantity).sum();
  }
}
