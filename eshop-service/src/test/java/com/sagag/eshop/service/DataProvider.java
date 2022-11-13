package com.sagag.eshop.service;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.entity.VVirtualUser;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@UtilityClass
public class DataProvider {

  public List<VVirtualUser> buildVirtualUserList(long... userIds) {
    final LongFunction<VVirtualUser> virtualUserBuilder =
        id -> {
          final VVirtualUser user = new VVirtualUser();
          user.setId(id);
          user.setSetting((int) id);
          user.setOriginalUserId(id);
          return user;
        };
    if (ArrayUtils.isEmpty(userIds)) {
      return Lists.newArrayList(virtualUserBuilder.apply(RandomUtils.nextLong()));
    }
    return LongStream.of(userIds).mapToObj(virtualUserBuilder).collect(Collectors.toList());
  }

}
