package com.sagag.eshop.service.virtualuser;

import com.sagag.eshop.repo.entity.VVirtualUser;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public interface VirtualUserHandler extends Consumer<List<VVirtualUser>> {

  default Function<List<VVirtualUser>, Map<Long, Long> > userIdAndOrgIdMapExtractor() {
    return virtualUsers -> CollectionUtils.emptyIfNull(virtualUsers).stream()
      .collect(Collectors.toMap(VVirtualUser::getId, VVirtualUser::getOriginalUserId));
  }

  default UnaryOperator<List<VVirtualUser>> userLoginBeforeInputHourBackExtractor(
    final Date inputHourBack) {
    final Predicate<VVirtualUser> beforeInputHourBackPredicate =
      v -> Objects.isNull(v.getFirstLoginDate()) || v.getFirstLoginDate().before(inputHourBack);
    return virtualUsers -> CollectionUtils.emptyIfNull(virtualUsers).stream()
      .filter(beforeInputHourBackPredicate).collect(Collectors.toList());
  }

  default Function<List<VVirtualUser>, List<Long>> userIdListExtractor() {
    return virtualUsers -> CollectionUtils.emptyIfNull(virtualUsers).stream()
      .map(VVirtualUser::getId).collect(Collectors.toList());
  }

  default Function<List<VVirtualUser>, List<Integer>> userSettingIdListExtractor() {
    return virtualUsers -> CollectionUtils.emptyIfNull(virtualUsers).stream()
      .map(VVirtualUser::getSetting).collect(Collectors.toList());
  }


}
