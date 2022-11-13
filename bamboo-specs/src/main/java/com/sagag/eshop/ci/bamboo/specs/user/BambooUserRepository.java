package com.sagag.eshop.ci.bamboo.specs.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BambooUserRepository {

  private static final BambooDeveloperUser[] BAMBOO_USERS = new BambooDeveloperUser[] {
      new BambooDeveloperUser("ext_thi"),
      new BambooDeveloperUser("ext_tuan"),
      new BambooDeveloperUser("ext_kiet"),
  };

  public List<BambooDeveloperUser> findUsers() {
    return Collections.unmodifiableList(Arrays.asList(BAMBOO_USERS));
  }

}
