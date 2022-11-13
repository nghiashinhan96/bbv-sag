package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.TourTime;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interfacing for {@link TourTime}.
 *
 */
public interface TourTimeRepository extends JpaRepository<TourTime, Integer> {

  /**
   * Returns tour time list by customer nr.
   *
   */
  List<TourTime> findByCustomerNumber(String customerNumber);

}
