package com.sagag.services.ax.callable.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.sag.erp.ArticleStock;

public class AxStockAsyncCallableCreatorUtilTest {

  private final ArticleSearchCriteria searchCriteria = initSearchCriteria();

  private final List<ArticleStock> erpStocks = initStock();

  @Test
  public void testMissingOwnVendorFromDatabase() {
    List<ArticleStock> findDeliverableStockByDistributedBranch = AxStockAsyncCallableCreatorUtil
        .findDeliverableStockByDistributedBranch(searchCriteria, erpStocks, "200");
    Assert.assertNotNull(findDeliverableStockByDistributedBranch);
    Assert.assertEquals(2, findDeliverableStockByDistributedBranch.size());
    List<String> branchIds = findDeliverableStockByDistributedBranch.stream()
        .map(ArticleStock::getBranchId).collect(Collectors.toList());
    Assert.assertTrue(branchIds.contains("201"));
    Assert.assertTrue(branchIds.contains("202"));
    Assert.assertFalse(branchIds.contains("200"));
    
  }

  private ArticleSearchCriteria initSearchCriteria() {
    return ArticleSearchCriteria.builder().defaultBrandId("200")
        .externalVendors(externalVendorData()).deliveryProfiles(deliveryProfileData()).build();
  }

  private List<ExternalVendorDto> externalVendorData() {
    List<ExternalVendorDto> data = new ArrayList<>();
    data.add(ExternalVendorDto.builder().id(1).availabilityTypeId("OWN").deliveryProfileId(0)
        .vendorId("0").sagArticleGroup(null).brandId(null).vendorPriority(0).build());
    data.add(ExternalVendorDto.builder().id(3).availabilityTypeId("VEN").deliveryProfileId(1)
        .vendorId("859067").sagArticleGroup("RE").brandId(1638L).vendorPriority(1).build());
    data.add(ExternalVendorDto.builder().id(2).availabilityTypeId("CON").deliveryProfileId(2)
        .vendorId("8156").sagArticleGroup(null).brandId(null).vendorPriority(2).build());
    return data;
  }

  private List<DeliveryProfileDto> deliveryProfileData() {
    List<DeliveryProfileDto> data = new ArrayList<>();
    data.add(createSingleDeliveryProfile(1, 200, 0, 200));
    data.add(createSingleDeliveryProfile(2, 200, 0, 201));
    data.add(createSingleDeliveryProfile(3, 200, 0, 202));
    data.add(createSingleDeliveryProfile(4, 200, 0, 203));

    data.add(createSingleDeliveryProfile(5, 300, 1, 204));
    data.add(createSingleDeliveryProfile(6, 300, 1, 205));

    data.add(createSingleDeliveryProfile(7, 400, 2, 206));
    data.add(createSingleDeliveryProfile(8, 400, 2, 207));

    return data;
  }

  private DeliveryProfileDto createSingleDeliveryProfile(Integer id, Integer branchId,
      Integer deliveryProfileId, Integer distributionBranchid) {
    return DeliveryProfileDto.builder().id(id).deliveryBranchId(branchId)
        .deliveryProfileId(deliveryProfileId).distributionBranchId(distributionBranchid).build();
  }

  private List<ArticleStock> initStock() {
    return Arrays.asList(
        ArticleStock.builder().articleId("1001136873").branchId("200").stock(100d).build(),
        ArticleStock.builder().articleId("1001136873").branchId("201").stock(100d).build(),
        ArticleStock.builder().articleId("1001136873").branchId("202").stock(1d).build());
  }

}
