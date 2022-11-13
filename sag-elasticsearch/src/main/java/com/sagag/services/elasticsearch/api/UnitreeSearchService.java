package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.criteria.unitree.KeywordUnitreeSearchCriteria;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UnitreeSearchService {

  /**
   * Returns a list of Unitree Compact.
   *
   * @param
   * @return a list of {@link UnitreeDoc}.
   */
  List<UnitreeDoc> getAllUnitreeCompact();

  /**
   * Returns a list of UnitreeDoc belongs to the selected tab on screen.
   *
   * @param
   * @return a Optional {@link UnitreeDoc}.
   */
  Optional<UnitreeDoc> getUnitreeByUnitreeId(String unitreeId);
  
  /**
   * 
   * 
   * @param criteria
   * @param pageable
   * @return Page of {@link UnitreeDoc}.
   */
  Page<UnitreeDoc> search(KeywordUnitreeSearchCriteria criteria, Pageable pageable);

  /**
   * Returns a UnitreeDoc contain the leafId
   *
   * @param
   * @return a Optional {@link UnitreeDoc}.
   */
  public Optional<UnitreeDoc> getUnitreeByLeafId(final String leafId);

}
