package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.TargetOffer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetOfferRepository extends CrudRepository<TargetOffer, Long> {

  List<TargetOffer> findByIdIn(List<Long> offerIds);

}
