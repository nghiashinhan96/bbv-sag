package com.sagag.services.ivds.filter.gtmotive;

import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationItem;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GtmotiveLeadingZeroIdsFilter {

  public List<String> filter(final List<GtmotiveOperationItem> operations) {

    if (CollectionUtils.isEmpty(operations)) {
      return Collections.emptyList();
    }

    final List<String> strippedNonAlphaNumericCharsPartNumbers = operations.stream()
        .map(operation -> SagStringUtils.stripNonAlphaNumericChars(operation.getReference()))
        .collect(Collectors.toList());

    // #991: Interface:GTMotive- leading zeros
    final List<String> modifiedPartNumbers = strippedNonAlphaNumericCharsPartNumbers.stream()
        .map(GtmotiveUtils.stripStartPartNumberConverter()).collect(Collectors.toList());

    return Stream
        .concat(strippedNonAlphaNumericCharsPartNumbers.stream(), modifiedPartNumbers.stream())
        .distinct().collect(Collectors.toList());
  }

}
