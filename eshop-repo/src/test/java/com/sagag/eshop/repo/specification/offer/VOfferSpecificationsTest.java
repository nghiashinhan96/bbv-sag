package com.sagag.eshop.repo.specification.offer;

import com.sagag.eshop.repo.criteria.offer.OfferSearchCriteria;
import com.sagag.eshop.repo.entity.offer.ViewOffer;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RunWith(MockitoJUnitRunner.class)
public class VOfferSpecificationsTest {

  @Mock
  private Root<ViewOffer> root;
  @Mock
  private CriteriaQuery<ViewOffer> query;
  @Mock
  private CriteriaBuilder criteriaBuilder;

  @Test
  public void testBuildEmptyQuery() {
    final OfferSearchCriteria criteria = new OfferSearchCriteria();
    VOfferSpecifications.of(criteria).toPredicate(root, query, criteriaBuilder);
  }

  @Test
  public void testBuildFullQuery() {
    final OfferSearchCriteria criteria = new OfferSearchCriteria();
    // Search criteria
    criteria.setOfferNumber("offerNr");
    criteria.setCustomerName("customerName");
    criteria.setRemark("remark");
    criteria.setVehicleDesc("vehDesc");
    criteria.setOfferDate(StringUtils.EMPTY);
    criteria.setPrice(StringUtils.EMPTY);
    criteria.setStatus("status");
    criteria.setUsername("username");

    // Order criteria
    criteria.setOrderDescByOfferNumber(true);
    criteria.setOrderDescByCustomerName(true);
    criteria.setOrderDescByRemark(true);
    criteria.setOrderDescByVehicleDesc(true);
    criteria.setOrderDescByOfferDate(null);
    criteria.setOrderDescByPrice(null);
    criteria.setOrderDescByStatus(true);
    criteria.setOrderDescByUsername(true);

    VOfferSpecifications.of(criteria).toPredicate(root, query, criteriaBuilder);
  }

}
