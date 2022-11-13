package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.criteria.OrderHistorySearchCriteria;
import com.sagag.eshop.repo.entity.order.VOrderHistory;
import com.sagag.eshop.repo.specification.VOrderHistorySpecifications;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for View order history repository.
 *
 * <pre>
 * (@tuan): prepare test data at axtest and enable all methods in this class)s
 * </pre>
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Waiting for data test")
public class VOrderHistoryRepositoryIT {

  private static Long saleId = 30L;
  private static Long affiliateId = 2L;
  private static Integer pageNumber = 0;
  private static Integer pageSize = 3;

  @Autowired
  private VOrderHistoryRepository vOrderHistoryRepo;

  @Test
  public void testFindBysaleIdAndAffiliateId() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdWithCustomerNr() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setCustomerNumber("1109");

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdWithCustomerName() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setCustomerName(" R. GesmbH");

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdWithOrderNumber() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderNumber("AU3010001077");

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdWithTotalPrice() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setTotalPrice("2.85");

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdWithCreatedDate() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDate("2018-02-28");

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdWithSaleName() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
  }


  @Test
  public void testFindBysaleIdAndAffiliateIdOrderDescByCustomerNr() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDescByCustomerNumber(true);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
    Assert.assertThat(orders.getContent().get(0).getCustomerNumber(),
        Matchers.greaterThanOrEqualTo(orders.getContent().get(1).getCustomerNumber()));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdOrderDescByCustomerName() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDescByCustomerName(true);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
    Assert.assertThat(orders.getContent().get(0).getCustomerName(),
        Matchers.greaterThanOrEqualTo(orders.getContent().get(1).getCustomerName()));
  }


  @Test
  public void testFindBysaleIdAndAffiliateIdOrderDescByOrderNumber() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDescByOrderNumber(true);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
    Assert.assertThat(orders.getContent().get(0).getOrderNumber(),
        Matchers.greaterThanOrEqualTo(orders.getContent().get(1).getOrderNumber()));
  }


  @Test
  public void testFindBysaleIdAndAffiliateIdOrderDescByTotalPrice() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDescByTotalPrice(true);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
    Assert.assertThat(orders.getContent().get(0).getTotalPrice(),
        Matchers.greaterThanOrEqualTo(orders.getContent().get(1).getTotalPrice()));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdOrderDescByCreatedDate() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDescByOrderDate(true);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
    Assert.assertThat(orders.getContent().get(0).getCreatedDate(),
        Matchers.greaterThanOrEqualTo(orders.getContent().get(1).getCreatedDate()));
  }

  @Test
  public void testFindBysaleIdAndAffiliateIdOrderDescBySaleName() {

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    criteria.setAffiliateId(affiliateId);
    criteria.setSaleId(saleId);
    criteria.setOrderDescByOrderDate(true);

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageRequest);
    Assert.assertThat(orders.getTotalElements(), Matchers.greaterThan(0L));
    Assert.assertThat(orders.getContent().get(0).getSaleName(),
        Matchers.greaterThanOrEqualTo(orders.getContent().get(1).getSaleName()));
  }
}
