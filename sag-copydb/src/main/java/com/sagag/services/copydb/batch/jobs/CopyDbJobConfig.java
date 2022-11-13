package com.sagag.services.copydb.batch.jobs;

import com.sagag.services.copydb.batch.*;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.steps.*;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.transaction.Transactional;

@Configuration
@CopyDbProfile
@DependsOn(value = "copyDbBatchConfig")
public class CopyDbJobConfig extends AbstractJobConfig {

  @Autowired
  private AadAccountsCopySteps aadAccountsCopySteps;
  
  @Autowired
  private AadAccountsTurnOffIdentityInsertion aadAccountsturnOffIdentityInsertion;

  @Autowired
  private AddressTypeCopySteps addressTypeCopySteps;
  
  @Autowired
  private AddressTypeTurnOffIdentityInsertion addressTypeturnOffIdentityInsertion;

  @Autowired
  private AddressCopySteps addressCopySteps;
  
  @Autowired
  private AddressTurnOffIdentityInsertion addressturnOffIdentityInsertion;

  @Autowired
  private AffiliatePermissionCopySteps affiliatePermissionCopySteps;
  
  @Autowired
  private AffiliatePermissionTurnOffIdentityInsertion affiliatePermissionturnOffIdentityInsertion;

  @Autowired
  private AllocationTypeCopySteps allocationTypeCopySteps;
  
  @Autowired
  private AllocationTypeTurnOffIdentityInsertion allocationTypeturnOffIdentityInsertion;

  @Autowired
  private ArticleHistoryCopySteps articleHistoryCopySteps;
  
  @Autowired
  private ArticleHistoryTurnOffIdentityInsertion articleHistoryturnOffIdentityInsertion;

  @Autowired
  private BasketHistoryCopySteps basketHistoryCopySteps;
  
  @Autowired
  private BasketHistoryTurnOffIdentityInsertion basketHistoryturnOffIdentityInsertion;

  @Autowired
  private BusinessLogCopySteps businessLogCopySteps;
  
  @Autowired
  private BusinessLogTurnOffIdentityInsertion businessLogturnOffIdentityInsertion;

  @Autowired
  private ClientRoleCopySteps clientRoleCopySteps;
  
  @Autowired
  private ClientRoleTurnOffIdentityInsertion clientRoleturnOffIdentityInsertion;

  @Autowired
  private CollectionPermissionCopySteps collectionPermissionCopySteps;
  
  @Autowired
  private CollectionPermissionTurnOffIdentityInsertion collectionPermissionturnOffIdentityInsertion;

  @Autowired
  private CollectiveDeliveryCopySteps collectiveDeliveryCopySteps;
  
  @Autowired
  private CollectiveDeliveryTurnOffIdentityInsertion collectiveDeliveryturnOffIdentityInsertion;

  @Autowired
  private CountryCopySteps countryCopySteps;
  
  @Autowired
  private CountryTurnOffIdentityInsertion countryturnOffIdentityInsertion;

  @Autowired
  private CouponConditionsCopySteps couponConditionsCopySteps;
  
  @Autowired
  private CouponConditionsTurnOffIdentityInsertion couponConditionsturnOffIdentityInsertion;

  @Autowired
  private CouponUseLogCopySteps couponUseLogCopySteps;
  
  @Autowired
  private CouponUseLogTurnOffIdentityInsertion couponUseLogturnOffIdentityInsertion;

  @Autowired
  private CurrencyCopySteps currencyCopySteps;
  
  @Autowired
  private CurrencyTurnOffIdentityInsertion currencyturnOffIdentityInsertion;

  @Autowired
  private DeliveryTypeCopySteps deliveryTypeCopySteps;
  
  @Autowired
  private DeliveryTypeTurnOffIdentityInsertion deliveryTypeturnOffIdentityInsertion;

  @Autowired
  private EshopCartItemCopySteps eshopCartItemCopySteps;
  
  @Autowired
  private EshopCartItemTurnOffIdentityInsertion eshopCartItemturnOffIdentityInsertion;

  @Autowired
  private EshopClientCopySteps eshopClientCopySteps;
  
  @Autowired
  private EshopClientTurnOffIdentityInsertion eshopClientturnOffIdentityInsertion;

  @Autowired
  private EshopClientResourceCopySteps eshopClientResourceCopySteps;
  
  @Autowired
  private EshopClientResourceTurnOffIdentityInsertion eshopClientResourceturnOffIdentityInsertion;

  @Autowired
  private EshopFunctionCopySteps eshopFunctionCopySteps;
  
  @Autowired
  private EshopFunctionTurnOffIdentityInsertion eshopFunctionturnOffIdentityInsertion;

  @Autowired
  private EshopGroupCopySteps eshopGroupCopySteps;
  
  @Autowired
  private EshopGroupTurnOffIdentityInsertion eshopGroupturnOffIdentityInsertion;

  @Autowired
  private EshopPermissionCopySteps eshopPermissionCopySteps;
  
  @Autowired
  private EshopPermissionTurnOffIdentityInsertion eshopPermissionturnOffIdentityInsertion;

  @Autowired
  private EshopReleaseCopySteps eshopReleaseCopySteps;
  
  @Autowired
  private EshopReleaseTurnOffIdentityInsertion eshopReleaseturnOffIdentityInsertion;

  @Autowired
  private ExternalOrganisationCopySteps externalOrganisationCopySteps;
  
  @Autowired
  private ExternalOrganisationTurnOffIdentityInsertion externalOrganisationturnOffIdentityInsertion;

  @Autowired
  private ExternalUserCopySteps externalUserCopySteps;
  
  @Autowired
  private ExternalUserTurnOffIdentityInsertion externalUserturnOffIdentityInsertion;

  @Autowired
  private FeedbackDepartmentCopySteps feedbackDepartmentCopySteps;
  
  @Autowired
  private FeedbackDepartmentTurnOffIdentityInsertion feedbackDepartmentturnOffIdentityInsertion;

  @Autowired
  private FeedbackDepartmentContactCopySteps feedbackDepartmentContactCopySteps;
  
  @Autowired
  private FeedbackDepartmentContactTurnOffIdentityInsertion feedbackDepartmentContactturnOffIdentityInsertion;

  @Autowired
  private FeedbackStatusCopySteps feedbackStatusCopySteps;
  
  @Autowired
  private FeedbackStatusTurnOffIdentityInsertion feedbackStatusturnOffIdentityInsertion;

  @Autowired
  private FeedbackTopicCopySteps feedbackTopicCopySteps;
  
  @Autowired
  private FeedbackTopicTurnOffIdentityInsertion feedbackTopicturnOffIdentityInsertion;

  @Autowired
  private FeedbackTopicDepartmentCopySteps feedbackTopicDepartmentCopySteps;
  
  @Autowired
  private FeedbackTopicDepartmentTurnOffIdentityInsertion feedbackTopicDepartmentturnOffIdentityInsertion;

  @Autowired
  private FinalCustomerOrderCopySteps finalCustomerOrderCopySteps;
  
  @Autowired
  private FinalCustomerOrderTurnOffIdentityInsertion finalCustomerOrderturnOffIdentityInsertion;

  @Autowired
  private FinalCustomerOrderItemCopySteps finalCustomerOrderItemCopySteps;
  
  @Autowired
  private FinalCustomerOrderItemTurnOffIdentityInsertion finalCustomerOrderItemturnOffIdentityInsertion;

  @Autowired
  private FinalCustomerPropertyCopySteps finalCustomerPropertyCopySteps;
  
  @Autowired
  private FinalCustomerPropertyTurnOffIdentityInsertion finalCustomerPropertyturnOffIdentityInsertion;

  @Autowired
  private GroupPermissionCopySteps groupPermissionCopySteps;
  
  @Autowired
  private GroupPermissionTurnOffIdentityInsertion groupPermissionturnOffIdentityInsertion;

  @Autowired
  private InvoiceTypeCopySteps invoiceTypeCopySteps;
  
  @Autowired
  private InvoiceTypeTurnOffIdentityInsertion invoiceTypeturnOffIdentityInsertion;

  @Autowired
  private LanguagesCopySteps languagesCopySteps;
  
  @Autowired
  private LanguagesTurnOffIdentityInsertion languagesturnOffIdentityInsertion;

  @Autowired
  private LegalDocumentAffiliateAssignedLogCopySteps legalDocumentAffiliateAssignedLogCopySteps;
  
  @Autowired
  private LegalDocumentAffiliateAssignedLogTurnOffIdentityInsertion legalDocumentAffiliateAssignedLogturnOffIdentityInsertion;

  @Autowired
  private LegalDocumentCustomerAcceptedLogCopySteps legalDocumentCustomerAcceptedLogCopySteps;
  
  @Autowired
  private LegalDocumentCustomerAcceptedLogTurnOffIdentityInsertion legalDocumentCustomerAcceptedLogturnOffIdentityInsertion;

  @Autowired
  private LegalDocumentMasterCopySteps legalDocumentMasterCopySteps;
  
  @Autowired
  private LegalDocumentMasterTurnOffIdentityInsertion legalDocumentMasterturnOffIdentityInsertion;

  @Autowired
  private LicenseSettingsCopySteps licenseSettingsCopySteps;
  
  @Autowired
  private LicenseSettingsTurnOffIdentityInsertion licenseSettingsturnOffIdentityInsertion;

  @Autowired
  private LicenseCopySteps licenseCopySteps;
  
  @Autowired
  private LicenseTurnOffIdentityInsertion licenseturnOffIdentityInsertion;

  @Autowired
  private MappingUserIdEblConnectCopySteps mappingUserIdEblConnectCopySteps;
  
  @Autowired
  private MappingUserIdEblConnectTurnOffIdentityInsertion mappingUserIdEblConnectturnOffIdentityInsertion;

  @Autowired
  private MessageCopySteps messageCopySteps;
  
  @Autowired
  private MessageTurnOffIdentityInsertion messageturnOffIdentityInsertion;

  @Autowired
  private MessageAccessRightCopySteps messageAccessRightCopySteps;
  
  @Autowired
  private MessageAccessRightTurnOffIdentityInsertion messageAccessRightturnOffIdentityInsertion;

  @Autowired
  private MessageAccessRightAreaCopySteps messageAccessRightAreaCopySteps;
  
  @Autowired
  private MessageAccessRightAreaTurnOffIdentityInsertion messageAccessRightAreaturnOffIdentityInsertion;

  @Autowired
  private MessageAccessRightRoleCopySteps messageAccessRightRoleCopySteps;
  
  @Autowired
  private MessageAccessRightRoleTurnOffIdentityInsertion messageAccessRightRoleturnOffIdentityInsertion;

  @Autowired
  private MessageAreaCopySteps messageAreaCopySteps;
  
  @Autowired
  private MessageAreaTurnOffIdentityInsertion messageAreaturnOffIdentityInsertion;

  @Autowired
  private MessageHidingCopySteps messageHidingCopySteps;
  
  @Autowired
  private MessageHidingTurnOffIdentityInsertion messageHidingturnOffIdentityInsertion;

  @Autowired
  private MessageLanguageCopySteps messageLanguageCopySteps;
  
  @Autowired
  private MessageLanguageTurnOffIdentityInsertion messageLanguageturnOffIdentityInsertion;

  @Autowired
  private MessageLocationCopySteps messageLocationCopySteps;
  
  @Autowired
  private MessageLocationTurnOffIdentityInsertion messageLocationturnOffIdentityInsertion;

  @Autowired
  private MessageLocationTypeCopySteps messageLocationTypeCopySteps;
  
  @Autowired
  private MessageLocationTypeTurnOffIdentityInsertion messageLocationTypeturnOffIdentityInsertion;

  @Autowired
  private MessageLocationTypeRoleTypeCopySteps messageLocationTypeRoleTypeCopySteps;
  
  @Autowired
  private MessageLocationTypeRoleTypeTurnOffIdentityInsertion messageLocationTypeRoleTypeturnOffIdentityInsertion;

  @Autowired
  private MessageRoleTypeCopySteps messageRoleTypeCopySteps;
  
  @Autowired
  private MessageRoleTypeTurnOffIdentityInsertion messageRoleTypeturnOffIdentityInsertion;

  @Autowired
  private MessageStyleCopySteps messageStyleCopySteps;
  
  @Autowired
  private MessageStyleTurnOffIdentityInsertion messageStyleturnOffIdentityInsertion;

  @Autowired
  private MessageSubAreaCopySteps messageSubAreaCopySteps;
  
  @Autowired
  private MessageSubAreaTurnOffIdentityInsertion messageSubAreaturnOffIdentityInsertion;

  @Autowired
  private MessageTypeCopySteps messageTypeCopySteps;
  
  @Autowired
  private MessageTypeTurnOffIdentityInsertion messageTypeturnOffIdentityInsertion;

  @Autowired
  private MessageVisibilityCopySteps messageVisibilityCopySteps;
  
  @Autowired
  private MessageVisibilityTurnOffIdentityInsertion messageVisibilityturnOffIdentityInsertion;

  @Autowired
  private OfferCopySteps offerCopySteps;
  
  @Autowired
  private OfferTurnOffIdentityInsertion offerturnOffIdentityInsertion;

  @Autowired
  private OfferAddressCopySteps offerAddressCopySteps;
  
  @Autowired
  private OfferAddressTurnOffIdentityInsertion offerAddressturnOffIdentityInsertion;

  @Autowired
  private OfferPersonCopySteps offerPersonCopySteps;
  
  @Autowired
  private OfferPersonTurnOffIdentityInsertion offerPersonturnOffIdentityInsertion;

  @Autowired
  private OfferPersonPropertyCopySteps offerPersonPropertyCopySteps;
  
  @Autowired
  private OfferPositionCopySteps offerPositionCopySteps;
  
  @Autowired
  private OfferPositionTurnOffIdentityInsertion offerPositionturnOffIdentityInsertion;

  @Autowired
  private OrderStatusCopySteps orderStatusCopySteps;
  
  @Autowired
  private OrderStatusTurnOffIdentityInsertion orderStatusturnOffIdentityInsertion;

  @Autowired
  private OrderTypeCopySteps orderTypeCopySteps;
  
  @Autowired
  private OrderTypeTurnOffIdentityInsertion orderTypeturnOffIdentityInsertion;

  @Autowired
  private OrganisationPropertyCopySteps organisationPropertyCopySteps;
  
  @Autowired
  private OrganisationPropertyTurnOffIdentityInsertion organisationPropertyturnOffIdentityInsertion;

  @Autowired
  private OrganisationTypeCopySteps organisationTypeCopySteps;
  
  @Autowired
  private OrganisationTypeTurnOffIdentityInsertion organisationTypeturnOffIdentityInsertion;

  @Autowired
  private PaymentMethodCopySteps paymentMethodCopySteps;
  
  @Autowired
  private PaymentMethodTurnOffIdentityInsertion paymentMethodturnOffIdentityInsertion;

  @Autowired
  private CustomerSettingsCopySteps customerSettingsCopySteps;
  
  @Autowired
  private CustomerSettingsTurnOffIdentityInsertion customerSettingsturnOffIdentityInsertion;

  @Autowired
  private OrganisationCopySteps organisationCopySteps;
  
  @Autowired
  private OrganisationTurnOffIdentityInsertion organisationturnOffIdentityInsertion;

  @Autowired
  private OrganisationCollectionCopySteps organisationCollectionCopySteps;
  
  @Autowired
  private OrganisationCollectionTurnOffIdentityInsertion organisationCollectionturnOffIdentityInsertion;

  @Autowired
  private CollectionRelationCopySteps collectionRelationCopySteps;
  
  @Autowired
  private CollectionRelationTurnOffIdentityInsertion collectionRelationturnOffIdentityInsertion;

  @Autowired
  private OrganisationAddressCopySteps organisationAddressCopySteps;
  
  @Autowired
  private OrganisationAddressTurnOffIdentityInsertion organisationAddressturnOffIdentityInsertion;

  @Autowired
  private OrganisationGroupCopySteps organisationGroupCopySteps;
  
  @Autowired
  private OrganisationGroupTurnOffIdentityInsertion organisationGroupturnOffIdentityInsertion;

  @Autowired
  private OrgCollectionSettingsCopySteps orgCollectionSettingsCopySteps;
  
  @Autowired
  private OrgCollectionSettingsTurnOffIdentityInsertion orgCollectionSettingsturnOffIdentityInsertion;

  @Autowired
  private UserSettingsCopySteps userSettingsCopySteps;
  
  @Autowired
  private UserSettingsTurnOffIdentityInsertion userSettingsturnOffIdentityInsertion;

  @Autowired
  private OrganisationSettingsCopySteps organisationSettingsCopySteps;
  
  @Autowired
  private OrganisationSettingsTurnOffIdentityInsertion organisationSettingsturnOffIdentityInsertion;

  @Autowired
  private PermFunctionCopySteps permFunctionCopySteps;
  
  @Autowired
  private PermFunctionTurnOffIdentityInsertion permFunctionturnOffIdentityInsertion;

  @Autowired
  private RolePermissionCopySteps rolePermissionCopySteps;
  
  @Autowired
  private RolePermissionTurnOffIdentityInsertion rolePermissionturnOffIdentityInsertion;

  @Autowired
  private RoleTypeCopySteps roleTypeCopySteps;
  
  @Autowired
  private RoleTypeTurnOffIdentityInsertion roleTypeturnOffIdentityInsertion;

  @Autowired
  private EshopRoleCopySteps eshopRoleCopySteps;
  
  @Autowired
  private EshopRoleTurnOffIdentityInsertion eshopRoleturnOffIdentityInsertion;

  @Autowired
  private GroupRoleCopySteps groupRoleCopySteps;
  
  @Autowired
  private GroupRoleTurnOffIdentityInsertion groupRoleturnOffIdentityInsertion;

  @Autowired
  private SalutationCopySteps salutationCopySteps;
  
  @Autowired
  private SalutationTurnOffIdentityInsertion salutationturnOffIdentityInsertion;

  @Autowired
  private EshopUserCopySteps eshopUserCopySteps;
  
  @Autowired
  private EshopUserTurnOffIdentityInsertion eshopUserturnOffIdentityInsertion;

  @Autowired
  private FeedbackCopySteps feedbackCopySteps;
  
  @Autowired
  private FeedbackTurnOffIdentityInsertion feedbackturnOffIdentityInsertion;

  @Autowired
  private VinLoggingCopySteps vinLoggingCopySteps;
  
  @Autowired
  private VinLoggingTurnOffIdentityInsertion vinLoggingturnOffIdentityInsertion;

  @Autowired
  private LoginCopySteps loginCopySteps;
  
  @Autowired
  private LoginTurnOffIdentityInsertion loginturnOffIdentityInsertion;

  @Autowired
  private GroupUserCopySteps groupUserCopySteps;
  
  @Autowired
  private GroupUserTurnOffIdentityInsertion groupUserturnOffIdentityInsertion;

  @Autowired
  private OrderHistoryCopySteps orderHistoryCopySteps;
  
  @Autowired
  private OrderHistoryTurnOffIdentityInsertion orderHistoryturnOffIdentityInsertion;

  @Autowired
  private UserOrderHistoryCopySteps userOrderHistoryCopySteps;
  
  @Autowired
  private UserOrderHistoryTurnOffIdentityInsertion userOrderHistoryturnOffIdentityInsertion;

  @Autowired
  private ShopArticleCopySteps shopArticleCopySteps;
  
  @Autowired
  private ShopArticleTurnOffIdentityInsertion shopArticleturnOffIdentityInsertion;

  @Autowired
  private SupportedAffiliateCopySteps supportedAffiliateCopySteps;
  
  @Autowired
  private SupportedAffiliateTurnOffIdentityInsertion supportedAffiliateturnOffIdentityInsertion;

  @Autowired
  private SupportedBrandPromotionCopySteps supportedBrandPromotionCopySteps;
  
  @Autowired
  private SupportedBrandPromotionTurnOffIdentityInsertion supportedBrandPromotionturnOffIdentityInsertion;

  @Autowired
  private VehicleHistoryCopySteps vehicleHistoryCopySteps;
  
  @Autowired
  private VehicleHistoryTurnOffIdentityInsertion vehicleHistoryturnOffIdentityInsertion;

  @Autowired
  private UserVehicleHistoryCopySteps userVehicleHistoryCopySteps;
  
  @Autowired
  private UserVehicleHistoryTurnOffIdentityInsertion userVehicleHistoryturnOffIdentityInsertion;

  @Bean(name = "CopyDatabase")
  @Transactional
  public Job createCopyDataBaseJob(JobNotificationListener listener) {
    return jobBuilderFactory.get(Jobs.COPY_DATABASE.getName())
        .listener(listener)
        .start(aadAccountsCopySteps.copyAadAccounts())
        .next(toStep(aadAccountsturnOffIdentityInsertion))
        .next(addressTypeCopySteps.copyAddressType())
        .next(toStep(addressTypeturnOffIdentityInsertion))
        .next(addressCopySteps.copyAddress())
        .next(toStep(addressturnOffIdentityInsertion))
        .next(affiliatePermissionCopySteps.copyAffiliatePermission())
        .next(toStep(affiliatePermissionturnOffIdentityInsertion))
        .next(allocationTypeCopySteps.copyAllocationType())
        .next(toStep(allocationTypeturnOffIdentityInsertion))
        .next(articleHistoryCopySteps.copyArticleHistory())
        .next(toStep(articleHistoryturnOffIdentityInsertion))
        .next(basketHistoryCopySteps.copyBasketHistory())
        .next(toStep(basketHistoryturnOffIdentityInsertion))
        .next(businessLogCopySteps.copyBusinessLog())
        .next(toStep(businessLogturnOffIdentityInsertion))
        .next(clientRoleCopySteps.copyClientRole())
        .next(toStep(clientRoleturnOffIdentityInsertion))
        .next(collectionPermissionCopySteps.copyCollectionPermission())
        .next(toStep(collectionPermissionturnOffIdentityInsertion))
        .next(collectiveDeliveryCopySteps.copyCollectiveDelivery())
        .next(toStep(collectiveDeliveryturnOffIdentityInsertion))
        .next(countryCopySteps.copyCountry())
        .next(toStep(countryturnOffIdentityInsertion))
        .next(couponConditionsCopySteps.copyCouponConditions())
        .next(toStep(couponConditionsturnOffIdentityInsertion))
        .next(couponUseLogCopySteps.copyCouponUseLog())
        .next(toStep(couponUseLogturnOffIdentityInsertion))
        .next(currencyCopySteps.copyCurrency())
        .next(toStep(currencyturnOffIdentityInsertion))
        .next(deliveryTypeCopySteps.copyDeliveryType())
        .next(toStep(deliveryTypeturnOffIdentityInsertion))
        .next(eshopCartItemCopySteps.copyEshopCartItem())
        .next(toStep(eshopCartItemturnOffIdentityInsertion))
        .next(eshopClientCopySteps.copyEshopClient())
        .next(toStep(eshopClientturnOffIdentityInsertion))
        .next(eshopClientResourceCopySteps.copyEshopClientResource())
        .next(toStep(eshopClientResourceturnOffIdentityInsertion))
        .next(eshopFunctionCopySteps.copyEshopFunction())
        .next(toStep(eshopFunctionturnOffIdentityInsertion))
        .next(eshopGroupCopySteps.copyEshopGroup())
        .next(toStep(eshopGroupturnOffIdentityInsertion))
        .next(eshopPermissionCopySteps.copyEshopPermission())
        .next(toStep(eshopPermissionturnOffIdentityInsertion))
        .next(eshopReleaseCopySteps.copyEshopRelease())
        .next(toStep(eshopReleaseturnOffIdentityInsertion))
        .next(externalOrganisationCopySteps.copyExternalOrganisation())
        .next(toStep(externalOrganisationturnOffIdentityInsertion))
        .next(externalUserCopySteps.copyExternalUser())
        .next(toStep(externalUserturnOffIdentityInsertion))
        .next(feedbackDepartmentCopySteps.copyFeedbackDepartment())
        .next(toStep(feedbackDepartmentturnOffIdentityInsertion))
        .next(feedbackDepartmentContactCopySteps.copyFeedbackDepartmentContact())
        .next(toStep(feedbackDepartmentContactturnOffIdentityInsertion))
        .next(feedbackStatusCopySteps.copyFeedbackStatus())
        .next(toStep(feedbackStatusturnOffIdentityInsertion))
        .next(feedbackTopicCopySteps.copyFeedbackTopic())
        .next(toStep(feedbackTopicturnOffIdentityInsertion))
        .next(feedbackTopicDepartmentCopySteps.copyFeedbackTopicDepartment())
        .next(toStep(feedbackTopicDepartmentturnOffIdentityInsertion))
        .next(finalCustomerOrderCopySteps.copyFinalCustomerOrder())
        .next(toStep(finalCustomerOrderturnOffIdentityInsertion))
        .next(finalCustomerOrderItemCopySteps.copyFinalCustomerOrderItem())
        .next(toStep(finalCustomerOrderItemturnOffIdentityInsertion))
        .next(finalCustomerPropertyCopySteps.copyFinalCustomerProperty())
        .next(toStep(finalCustomerPropertyturnOffIdentityInsertion))
        .next(groupPermissionCopySteps.copyGroupPermission())
        .next(toStep(groupPermissionturnOffIdentityInsertion))
        .next(invoiceTypeCopySteps.copyInvoiceType())
        .next(toStep(invoiceTypeturnOffIdentityInsertion))
        .next(languagesCopySteps.copyLanguages())
        .next(toStep(languagesturnOffIdentityInsertion))
        .next(legalDocumentAffiliateAssignedLogCopySteps.copyLegalDocumentAffiliateAssignedLog())
        .next(toStep(legalDocumentAffiliateAssignedLogturnOffIdentityInsertion))
        .next(legalDocumentCustomerAcceptedLogCopySteps.copyLegalDocumentCustomerAcceptedLog())
        .next(toStep(legalDocumentCustomerAcceptedLogturnOffIdentityInsertion))
        .next(legalDocumentMasterCopySteps.copyLegalDocumentMaster())
        .next(toStep(legalDocumentMasterturnOffIdentityInsertion))
        .next(licenseSettingsCopySteps.copyLicenseSettings())
        .next(toStep(licenseSettingsturnOffIdentityInsertion))
        .next(licenseCopySteps.copyLicense())
        .next(toStep(licenseturnOffIdentityInsertion))
        .next(mappingUserIdEblConnectCopySteps.copyMappingUserIdEblConnect())
        .next(toStep(mappingUserIdEblConnectturnOffIdentityInsertion))
        .next(messageCopySteps.copyMessage())
        .next(toStep(messageturnOffIdentityInsertion))
        .next(messageAccessRightCopySteps.copyMessageAccessRight())
        .next(toStep(messageAccessRightturnOffIdentityInsertion))
        .next(messageAccessRightAreaCopySteps.copyMessageAccessRightArea())
        .next(toStep(messageAccessRightAreaturnOffIdentityInsertion))
        .next(messageAccessRightRoleCopySteps.copyMessageAccessRightRole())
        .next(toStep(messageAccessRightRoleturnOffIdentityInsertion))
        .next(messageAreaCopySteps.copyMessageArea())
        .next(toStep(messageAreaturnOffIdentityInsertion))
        .next(messageHidingCopySteps.copyMessageHiding())
        .next(toStep(messageHidingturnOffIdentityInsertion))
        .next(messageLanguageCopySteps.copyMessageLanguage())
        .next(toStep(messageLanguageturnOffIdentityInsertion))
        .next(messageLocationCopySteps.copyMessageLocation())
        .next(toStep(messageLocationturnOffIdentityInsertion))
        .next(messageLocationTypeCopySteps.copyMessageLocationType())
        .next(toStep(messageLocationTypeturnOffIdentityInsertion))
        .next(messageLocationTypeRoleTypeCopySteps.copyMessageLocationTypeRoleType())
        .next(toStep(messageLocationTypeRoleTypeturnOffIdentityInsertion))
        .next(messageRoleTypeCopySteps.copyMessageRoleType())
        .next(toStep(messageRoleTypeturnOffIdentityInsertion))
        .next(messageStyleCopySteps.copyMessageStyle())
        .next(toStep(messageStyleturnOffIdentityInsertion))
        .next(messageSubAreaCopySteps.copyMessageSubArea())
        .next(toStep(messageSubAreaturnOffIdentityInsertion))
        .next(messageTypeCopySteps.copyMessageType())
        .next(toStep(messageTypeturnOffIdentityInsertion))
        .next(messageVisibilityCopySteps.copyMessageVisibility())
        .next(toStep(messageVisibilityturnOffIdentityInsertion))
        .next(offerCopySteps.copyOffer())
        .next(toStep(offerturnOffIdentityInsertion))
        .next(offerAddressCopySteps.copyOfferAddress())
        .next(toStep(offerAddressturnOffIdentityInsertion))
        .next(offerPersonCopySteps.copyOfferPerson())
        .next(toStep(offerPersonturnOffIdentityInsertion))
        .next(offerPersonPropertyCopySteps.copyOfferPersonProperty())
        .next(offerPositionCopySteps.copyOfferPosition())
        .next(toStep(offerPositionturnOffIdentityInsertion))
        .next(orderStatusCopySteps.copyOrderStatus())
        .next(toStep(orderStatusturnOffIdentityInsertion))
        .next(orderTypeCopySteps.copyOrderType())
        .next(toStep(orderTypeturnOffIdentityInsertion))
        .next(organisationPropertyCopySteps.copyOrganisationProperty())
        .next(toStep(organisationPropertyturnOffIdentityInsertion))
        .next(organisationTypeCopySteps.copyOrganisationType())
        .next(toStep(organisationTypeturnOffIdentityInsertion))
        .next(paymentMethodCopySteps.copyPaymentMethod())
        .next(toStep(paymentMethodturnOffIdentityInsertion))
        .next(customerSettingsCopySteps.copyCustomerSettings())
        .next(toStep(customerSettingsturnOffIdentityInsertion))
        .next(organisationCopySteps.copyOrganisation())
        .next(toStep(organisationturnOffIdentityInsertion))
        .next(organisationCollectionCopySteps.copyOrganisationCollection())
        .next(toStep(organisationCollectionturnOffIdentityInsertion))
        .next(collectionRelationCopySteps.copyCollectionRelation())
        .next(toStep(collectionRelationturnOffIdentityInsertion))
        .next(organisationAddressCopySteps.copyOrganisationAddress())
        .next(toStep(organisationAddressturnOffIdentityInsertion))
        .next(organisationGroupCopySteps.copyOrganisationGroup())
        .next(toStep(organisationGroupturnOffIdentityInsertion))
        .next(orgCollectionSettingsCopySteps.copyOrgCollectionSettings())
        .next(toStep(orgCollectionSettingsturnOffIdentityInsertion))
        .next(userSettingsCopySteps.copyUserSettings())
        .next(toStep(userSettingsturnOffIdentityInsertion))
        .next(organisationSettingsCopySteps.copyOrganisationSettings())
        .next(toStep(organisationSettingsturnOffIdentityInsertion))
        .next(permFunctionCopySteps.copyPermFunction())
        .next(toStep(permFunctionturnOffIdentityInsertion))
        .next(rolePermissionCopySteps.copyRolePermission())
        .next(toStep(rolePermissionturnOffIdentityInsertion))
        .next(roleTypeCopySteps.copyRoleType())
        .next(toStep(roleTypeturnOffIdentityInsertion))
        .next(eshopRoleCopySteps.copyEshopRole())
        .next(toStep(eshopRoleturnOffIdentityInsertion))
        .next(groupRoleCopySteps.copyGroupRole())
        .next(toStep(groupRoleturnOffIdentityInsertion))
        .next(salutationCopySteps.copySalutation())
        .next(toStep(salutationturnOffIdentityInsertion))
        .next(eshopUserCopySteps.copyEshopUser())
        .next(toStep(eshopUserturnOffIdentityInsertion))
        .next(feedbackCopySteps.copyFeedback())
        .next(toStep(feedbackturnOffIdentityInsertion))
        .next(vinLoggingCopySteps.copyVinLogging())
        .next(toStep(vinLoggingturnOffIdentityInsertion))
        .next(loginCopySteps.copyLogin())
        .next(toStep(loginturnOffIdentityInsertion))
        .next(groupUserCopySteps.copyGroupUser())
        .next(toStep(groupUserturnOffIdentityInsertion))
        .next(orderHistoryCopySteps.copyOrderHistory())
        .next(toStep(orderHistoryturnOffIdentityInsertion))
        .next(userOrderHistoryCopySteps.copyUserOrderHistory())
        .next(toStep(userOrderHistoryturnOffIdentityInsertion))
        .next(shopArticleCopySteps.copyShopArticle())
        .next(toStep(shopArticleturnOffIdentityInsertion))
        .next(supportedAffiliateCopySteps.copySupportedAffiliate())
        .next(toStep(supportedAffiliateturnOffIdentityInsertion))
        .next(supportedBrandPromotionCopySteps.copySupportedBrandPromotion())
        .next(toStep(supportedBrandPromotionturnOffIdentityInsertion))
        .next(vehicleHistoryCopySteps.copyVehicleHistory())
        .next(toStep(vehicleHistoryturnOffIdentityInsertion))
        .next(userVehicleHistoryCopySteps.copyUserVehicleHistory())
        .next(toStep(userVehicleHistoryturnOffIdentityInsertion))
        .build();
  }

  @Bean(name = "DropBatchTables")
  @Transactional
  public Job dropBatchTables(JobNotificationListener listener,
      DropBatchTablesTasklet dropBatchTablesTasklet) {
    return jobBuilderFactory.get(Jobs.DROP_BATCH_TABLES.getName())
        .listener(listener)
        .start(toStep(dropBatchTablesTasklet))
        .build();
  }
  
  @Bean(name = "TruncateAllDestTables")
  @Transactional
  public Job truncateAllDestTables(JobNotificationListener listener,
      TruncateAllDestTablesTasklet truncateAllDestTablesTasklet) {
    return jobBuilderFactory.get(Jobs.TRUNCATE_ALL_DEST_TABLES.getName())
        .listener(listener)
        .start(toStep(truncateAllDestTablesTasklet))
        .build();
  }

}
