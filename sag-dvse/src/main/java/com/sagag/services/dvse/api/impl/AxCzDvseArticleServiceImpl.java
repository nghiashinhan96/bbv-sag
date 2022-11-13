package com.sagag.services.dvse.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.dvse.api.AxCzDvseArticleService;
import com.sagag.services.dvse.builder.AxCzDvseObjectHelper;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.profiles.AxCzDvseProfile;
import com.sagag.services.dvse.utils.DvseArticleUtils;
import com.sagag.services.dvse.wsdl.tmconnect.ArticleTmf;
import com.sagag.services.dvse.wsdl.tmconnect.ErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;
import com.sagag.services.dvse.wsdl.tmconnect.MasterData;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.DeliveryProfileCacheService;
import com.sagag.services.hazelcast.api.ExternalVendorCacheService;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import com.sagag.services.ivds.executor.impl.AbstractIvdsArticleTaskExecutor;


/**
 * The service for getting article information from SAG Connect API using for DVSE TMConnect.
 *
 */
@Service
@AxCzDvseProfile
public class AxCzDvseArticleServiceImpl extends DvseProcessor implements AxCzDvseArticleService {

  @Autowired
  private ArticleSearchExternalExecutor axArticleSearchExecutor;

  @Autowired
  private NextWorkingDateCacheService nextWorkingDateCacheService;

  @Autowired
  private ContextService contextService;

  @Autowired
  private ExternalVendorCacheService externalVendorCacheService;

  @Autowired
  private DeliveryProfileCacheService deliveryProfileCacheService;

  @Autowired
  private AbstractIvdsArticleTaskExecutor abstractIvdsArticleTaskExecutor;

  @Override
  public GetErpInformationResponse getArticleInformation(ConnectUser user,
      GetErpInformation request) {
    Optional<MasterData> masterDataOpt = Optional.ofNullable(request.getRequest().getMasterData());
    Map<String, String> articleIdAndUUIDMap =
        AxCzDvseObjectHelper.extractArticleIdUUIDMap(masterDataOpt);
    List<ArticleTmf> artTmfs = AxCzDvseObjectHelper.extractArticleTmfs(masterDataOpt);
    List<ErpInformation> erpInfos = AxCzDvseObjectHelper.extractErpInformation(request);
    List<ArticleDocDto> requestedArticles = AxCzDvseObjectHelper.buildArticleFromGetInformationRequest(articleIdAndUUIDMap, artTmfs, erpInfos);

    Map<String, Optional<Integer>> articleAndRequestQuantity =
        DvseArticleUtils.buildArticleAndRequestedQuantityMap(requestedArticles);

    final SupportedAffiliate affiliate = user.getAffiliate();

    final List<ArticleDocDto> articles =
        searchArticles(affiliate, articleAndRequestQuantity, user.isSaleOnBehalf());

    final Customer customer = user.getCustomer();

    final ErpSendMethodEnum sendMethodEnum =
        contextService.getEshopContext(user.key()).getDeliveryType();
    final String pickupBranchId = contextService.getPickupBranchId(user);
    final NextWorkingDates nextWorkingDate = nextWorkingDateCacheService.get(user, pickupBranchId);
    boolean caculateVatRate = abstractIvdsArticleTaskExecutor.calculateVatPriceRequired(user);
    abstractIvdsArticleTaskExecutor.updateVatRate(articles, caculateVatRate);

    ArticleSearchCriteria criteria = ArticleSearchCriteria.builder().affiliate(affiliate)
        .custNr(user.getCustNrStr()).companyName(user.getCompanyName())
        .availabilityUrl(customer.getAvailabilityRelativeUrl()).filterArticleBefore(true)
        .updatePrice(true).updateAvailability(true).isCartMode(false)
        .calculateVatPriceRequired(caculateVatRate)
        .addressId(user.getDeliveryAddressId()).pickupBranchId(pickupBranchId)
        .defaultBrandId(user.getDefaultBranchId()).deliveryType(sendMethodEnum.name())
        .articles(articles).nextWorkingDate(nextWorkingDate)
        .externalVendors(externalVendorCacheService.findAll())
        .deliveryProfiles(deliveryProfileCacheService.findAll())
        .priceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum())
        .isDvseMode(true).build();

    final List<ArticleDocDto> updatedArticles = axArticleSearchExecutor.execute(criteria);

    GetErpInformationResponse response = AxCzDvseObjectHelper.buildGetArticleInformationResponse(request,
        articleIdAndUUIDMap, artTmfs, erpInfos, updatedArticles, user);
    return response;
  }

}
