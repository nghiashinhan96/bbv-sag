package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.TargetOfferPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetOfferPersonRepository extends JpaRepository<TargetOfferPerson, Long> {

  @Query("select p.id from TargetOfferPerson p")
  List<Long> findPersonIdList();

}
