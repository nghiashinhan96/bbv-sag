package com.sagag.services.service.uniparts.unitree;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeCompactDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface TreeUnitreeBuilder extends BiFunction<UnitreeDoc, UserInfo, UnitreeNodeDto> {

  /**
   * Returns UnitreeNode parent was built include itself children.
   *
   * @param unitreeDoc
   * @param userInfo user info
   * @return the result of {@link UnitreeNodeDto}, otherwise is null
   */
  @Override
  UnitreeNodeDto apply(UnitreeDoc unitreeDoc, UserInfo userInfo);


  /**
   * Returns UnitreeCompactDto updated with TreeSort follow by affiliate
   *
   * @param affiliate
   * @return the result of {@link UnitreeCompactDto}
   */
  Function<UnitreeCompactDto, UnitreeCompactDto> updateTreeSortByAffiliate(
      SupportedAffiliate affiliate);
}
