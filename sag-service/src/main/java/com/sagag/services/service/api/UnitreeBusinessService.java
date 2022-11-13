package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeCompactDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;

import java.util.List;
import java.util.Optional;

public interface UnitreeBusinessService {

  /**
   * Returns the unitree node parent on the selected tab.
   *
   * @param unitreeId the Tree id.
   * @param userInfo user info
   * @return a list of unitreenode.
   */
  Optional<UnitreeNodeDto> getUnitreeNodesByUnitreeId(String unitreeId, UserInfo userInfo);

  /**
   * Returns the all Unitree Docs
   *
   * @param affiliate the affiliate of user.
   * @return a list of Unitree Docs.
   */
  List<UnitreeCompactDto> getAllUnitreeCompact(SupportedAffiliate affiliate);

  /**
   * Returns a UnitreeDoc contain the leafId
   *
   * @param
   * @return a Optional {@link UnitreeDoc}.
   */
  public Optional<UnitreeCompactDto> getUnitreeByLeafId(final String leafId);

}
