package com.sagag.services.copydb.batch.jobs;

import com.sagag.services.copydb.batch.*;
import com.sagag.services.copydb.batch.tasks.*;
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
public class CopyDbSQLJobConfig extends AbstractJobConfig {

  @Autowired
  private AadAccountsSQLCopiedTasklet aadAccountsSQLCopiedTasklet;
  
  @Autowired
  private AadAccountsTurnOffIdentityInsertion aadAccountsturnOffIdentityInsertion;

  @Autowired
  private AddressTypeSQLCopiedTasklet addressTypeSQLCopiedTasklet;
  
  @Autowired
  private AddressTypeTurnOffIdentityInsertion addressTypeturnOffIdentityInsertion;

  @Autowired
  private AddressSQLCopiedTasklet addressSQLCopiedTasklet;
  
  @Autowired
  private AddressTurnOffIdentityInsertion addressturnOffIdentityInsertion;

  @Autowired
  private AffiliatePermissionSQLCopiedTasklet affiliatePermissionSQLCopiedTasklet;
  
  @Autowired
  private AffiliatePermissionTurnOffIdentityInsertion affiliatePermissionturnOffIdentityInsertion;

  @Autowired
  private AllocationTypeSQLCopiedTasklet allocationTypeSQLCopiedTasklet;
  
  @Autowired
  private AllocationTypeTurnOffIdentityInsertion allocationTypeturnOffIdentityInsertion;

  @Autowired
  private ArticleHistorySQLCopiedTasklet articleHistorySQLCopiedTasklet;
  
  @Autowired
  private ArticleHistoryTurnOffIdentityInsertion articleHistoryturnOffIdentityInsertion;

  @Autowired
  private BasketHistorySQLCopiedTasklet basketHistorySQLCopiedTasklet;
  
  @Autowired
  private BasketHistoryTurnOffIdentityInsertion basketHistoryturnOffIdentityInsertion;

  @Autowired
  private BusinessLogSQLCopiedTasklet businessLogSQLCopiedTasklet;
  
  @Autowired
  private BusinessLogTurnOffIdentityInsertion businessLogturnOffIdentityInsertion;

  @Autowired
  private ClientRoleSQLCopiedTasklet clientRoleSQLCopiedTasklet;
  
  @Autowired
  private ClientRoleTurnOffIdentityInsertion clientRoleturnOffIdentityInsertion;

  @Autowired
  private CollectionPermissionSQLCopiedTasklet collectionPermissionSQLCopiedTasklet;
  
  @Autowired
  private CollectionPermissionTurnOffIdentityInsertion collectionPermissionturnOffIdentityInsertion;

  @Autowired
  private CollectiveDeliverySQLCopiedTasklet collectiveDeliverySQLCopiedTasklet;
  
  @Autowired
  private CollectiveDeliveryTurnOffIdentityInsertion collectiveDeliveryturnOffIdentityInsertion;

  @Autowired
  private CountrySQLCopiedTasklet countrySQLCopiedTasklet;
  
  @Autowired
  private CountryTurnOffIdentityInsertion countryturnOffIdentityInsertion;

  @Autowired
  private CouponConditionsSQLCopiedTasklet couponConditionsSQLCopiedTasklet;
  
  @Autowired
  private CouponConditionsTurnOffIdentityInsertion couponConditionsturnOffIdentityInsertion;

  @Autowired
  private CouponUseLogSQLCopiedTasklet couponUseLogSQLCopiedTasklet;
  
  @Autowired
  private CouponUseLogTurnOffIdentityInsertion couponUseLogturnOffIdentityInsertion;

  @Autowired
  private CurrencySQLCopiedTasklet currencySQLCopiedTasklet;
  
  @Autowired
  private CurrencyTurnOffIdentityInsertion currencyturnOffIdentityInsertion;

  @Autowired
  private DeliveryTypeSQLCopiedTasklet deliveryTypeSQLCopiedTasklet;
  
  @Autowired
  private DeliveryTypeTurnOffIdentityInsertion deliveryTypeturnOffIdentityInsertion;

  @Autowired
  private EshopCartItemSQLCopiedTasklet eshopCartItemSQLCopiedTasklet;
  
  @Autowired
  private EshopCartItemTurnOffIdentityInsertion eshopCartItemturnOffIdentityInsertion;

  @Autowired
  private EshopClientSQLCopiedTasklet eshopClientSQLCopiedTasklet;
  
  @Autowired
  private EshopClientTurnOffIdentityInsertion eshopClientturnOffIdentityInsertion;

  @Autowired
  private EshopClientResourceSQLCopiedTasklet eshopClientResourceSQLCopiedTasklet;
  
  @Autowired
  private EshopClientResourceTurnOffIdentityInsertion eshopClientResourceturnOffIdentityInsertion;

  @Autowired
  private EshopFunctionSQLCopiedTasklet eshopFunctionSQLCopiedTasklet;
  
  @Autowired
  private EshopFunctionTurnOffIdentityInsertion eshopFunctionturnOffIdentityInsertion;

  @Autowired
  private EshopGroupSQLCopiedTasklet eshopGroupSQLCopiedTasklet;
  
  @Autowired
  private EshopGroupTurnOffIdentityInsertion eshopGroupturnOffIdentityInsertion;

  @Autowired
  private EshopPermissionSQLCopiedTasklet eshopPermissionSQLCopiedTasklet;
  
  @Autowired
  private EshopPermissionTurnOffIdentityInsertion eshopPermissionturnOffIdentityInsertion;

  @Autowired
  private EshopReleaseSQLCopiedTasklet eshopReleaseSQLCopiedTasklet;
  
  @Autowired
  private EshopReleaseTurnOffIdentityInsertion eshopReleaseturnOffIdentityInsertion;

  @Autowired
  private ExternalOrganisationSQLCopiedTasklet externalOrganisationSQLCopiedTasklet;
  
  @Autowired
  private ExternalOrganisationTurnOffIdentityInsertion externalOrganisationturnOffIdentityInsertion;

  @Autowired
  private ExternalUserSQLCopiedTasklet externalUserSQLCopiedTasklet;
  
  @Autowired
  private ExternalUserTurnOffIdentityInsertion externalUserturnOffIdentityInsertion;

  @Autowired
  private FeedbackDepartmentSQLCopiedTasklet feedbackDepartmentSQLCopiedTasklet;
  
  @Autowired
  private FeedbackDepartmentTurnOffIdentityInsertion feedbackDepartmentturnOffIdentityInsertion;

  @Autowired
  private FeedbackDepartmentContactSQLCopiedTasklet feedbackDepartmentContactSQLCopiedTasklet;
  
  @Autowired
  private FeedbackDepartmentContactTurnOffIdentityInsertion feedbackDepartmentContactturnOffIdentityInsertion;

  @Autowired
  private FeedbackStatusSQLCopiedTasklet feedbackStatusSQLCopiedTasklet;
  
  @Autowired
  private FeedbackStatusTurnOffIdentityInsertion feedbackStatusturnOffIdentityInsertion;

  @Autowired
  private FeedbackTopicSQLCopiedTasklet feedbackTopicSQLCopiedTasklet;
  
  @Autowired
  private FeedbackTopicTurnOffIdentityInsertion feedbackTopicturnOffIdentityInsertion;

  @Autowired
  private FeedbackTopicDepartmentSQLCopiedTasklet feedbackTopicDepartmentSQLCopiedTasklet;
  
  @Autowired
  private FeedbackTopicDepartmentTurnOffIdentityInsertion feedbackTopicDepartmentturnOffIdentityInsertion;

  @Autowired
  private FinalCustomerOrderSQLCopiedTasklet finalCustomerOrderSQLCopiedTasklet;
  
  @Autowired
  private FinalCustomerOrderTurnOffIdentityInsertion finalCustomerOrderturnOffIdentityInsertion;

  @Autowired
  private FinalCustomerOrderItemSQLCopiedTasklet finalCustomerOrderItemSQLCopiedTasklet;
  
  @Autowired
  private FinalCustomerOrderItemTurnOffIdentityInsertion finalCustomerOrderItemturnOffIdentityInsertion;

  @Autowired
  private FinalCustomerPropertySQLCopiedTasklet finalCustomerPropertySQLCopiedTasklet;
  
  @Autowired
  private FinalCustomerPropertyTurnOffIdentityInsertion finalCustomerPropertyturnOffIdentityInsertion;

  @Autowired
  private GroupPermissionSQLCopiedTasklet groupPermissionSQLCopiedTasklet;
  
  @Autowired
  private GroupPermissionTurnOffIdentityInsertion groupPermissionturnOffIdentityInsertion;

  @Autowired
  private InvoiceTypeSQLCopiedTasklet invoiceTypeSQLCopiedTasklet;
  
  @Autowired
  private InvoiceTypeTurnOffIdentityInsertion invoiceTypeturnOffIdentityInsertion;

  @Autowired
  private LanguagesSQLCopiedTasklet languagesSQLCopiedTasklet;
  
  @Autowired
  private LanguagesTurnOffIdentityInsertion languagesturnOffIdentityInsertion;

  @Autowired
  private LegalDocumentAffiliateAssignedLogSQLCopiedTasklet legalDocumentAffiliateAssignedLogSQLCopiedTasklet;
  
  @Autowired
  private LegalDocumentAffiliateAssignedLogTurnOffIdentityInsertion legalDocumentAffiliateAssignedLogturnOffIdentityInsertion;

  @Autowired
  private LegalDocumentCustomerAcceptedLogSQLCopiedTasklet legalDocumentCustomerAcceptedLogSQLCopiedTasklet;
  
  @Autowired
  private LegalDocumentCustomerAcceptedLogTurnOffIdentityInsertion legalDocumentCustomerAcceptedLogturnOffIdentityInsertion;

  @Autowired
  private LegalDocumentMasterSQLCopiedTasklet legalDocumentMasterSQLCopiedTasklet;
  
  @Autowired
  private LegalDocumentMasterTurnOffIdentityInsertion legalDocumentMasterturnOffIdentityInsertion;

  @Autowired
  private LicenseSettingsSQLCopiedTasklet licenseSettingsSQLCopiedTasklet;
  
  @Autowired
  private LicenseSettingsTurnOffIdentityInsertion licenseSettingsturnOffIdentityInsertion;

  @Autowired
  private LicenseSQLCopiedTasklet licenseSQLCopiedTasklet;
  
  @Autowired
  private LicenseTurnOffIdentityInsertion licenseturnOffIdentityInsertion;

  @Autowired
  private MappingUserIdEblConnectSQLCopiedTasklet mappingUserIdEblConnectSQLCopiedTasklet;
  
  @Autowired
  private MappingUserIdEblConnectTurnOffIdentityInsertion mappingUserIdEblConnectturnOffIdentityInsertion;

  @Autowired
  private MessageSQLCopiedTasklet messageSQLCopiedTasklet;
  
  @Autowired
  private MessageTurnOffIdentityInsertion messageturnOffIdentityInsertion;

  @Autowired
  private MessageAccessRightSQLCopiedTasklet messageAccessRightSQLCopiedTasklet;
  
  @Autowired
  private MessageAccessRightTurnOffIdentityInsertion messageAccessRightturnOffIdentityInsertion;

  @Autowired
  private MessageAccessRightAreaSQLCopiedTasklet messageAccessRightAreaSQLCopiedTasklet;
  
  @Autowired
  private MessageAccessRightAreaTurnOffIdentityInsertion messageAccessRightAreaturnOffIdentityInsertion;

  @Autowired
  private MessageAccessRightRoleSQLCopiedTasklet messageAccessRightRoleSQLCopiedTasklet;
  
  @Autowired
  private MessageAccessRightRoleTurnOffIdentityInsertion messageAccessRightRoleturnOffIdentityInsertion;

  @Autowired
  private MessageAreaSQLCopiedTasklet messageAreaSQLCopiedTasklet;
  
  @Autowired
  private MessageAreaTurnOffIdentityInsertion messageAreaturnOffIdentityInsertion;

  @Autowired
  private MessageHidingSQLCopiedTasklet messageHidingSQLCopiedTasklet;
  
  @Autowired
  private MessageHidingTurnOffIdentityInsertion messageHidingturnOffIdentityInsertion;

  @Autowired
  private MessageLanguageSQLCopiedTasklet messageLanguageSQLCopiedTasklet;
  
  @Autowired
  private MessageLanguageTurnOffIdentityInsertion messageLanguageturnOffIdentityInsertion;

  @Autowired
  private MessageLocationSQLCopiedTasklet messageLocationSQLCopiedTasklet;
  
  @Autowired
  private MessageLocationTurnOffIdentityInsertion messageLocationturnOffIdentityInsertion;

  @Autowired
  private MessageLocationTypeSQLCopiedTasklet messageLocationTypeSQLCopiedTasklet;
  
  @Autowired
  private MessageLocationTypeTurnOffIdentityInsertion messageLocationTypeturnOffIdentityInsertion;

  @Autowired
  private MessageLocationTypeRoleTypeSQLCopiedTasklet messageLocationTypeRoleTypeSQLCopiedTasklet;
  
  @Autowired
  private MessageLocationTypeRoleTypeTurnOffIdentityInsertion messageLocationTypeRoleTypeturnOffIdentityInsertion;

  @Autowired
  private MessageRoleTypeSQLCopiedTasklet messageRoleTypeSQLCopiedTasklet;
  
  @Autowired
  private MessageRoleTypeTurnOffIdentityInsertion messageRoleTypeturnOffIdentityInsertion;

  @Autowired
  private MessageStyleSQLCopiedTasklet messageStyleSQLCopiedTasklet;
  
  @Autowired
  private MessageStyleTurnOffIdentityInsertion messageStyleturnOffIdentityInsertion;

  @Autowired
  private MessageSubAreaSQLCopiedTasklet messageSubAreaSQLCopiedTasklet;
  
  @Autowired
  private MessageSubAreaTurnOffIdentityInsertion messageSubAreaturnOffIdentityInsertion;

  @Autowired
  private MessageTypeSQLCopiedTasklet messageTypeSQLCopiedTasklet;
  
  @Autowired
  private MessageTypeTurnOffIdentityInsertion messageTypeturnOffIdentityInsertion;

  @Autowired
  private MessageVisibilitySQLCopiedTasklet messageVisibilitySQLCopiedTasklet;
  
  @Autowired
  private MessageVisibilityTurnOffIdentityInsertion messageVisibilityturnOffIdentityInsertion;

  @Autowired
  private OfferSQLCopiedTasklet offerSQLCopiedTasklet;
  
  @Autowired
  private OfferTurnOffIdentityInsertion offerturnOffIdentityInsertion;

  @Autowired
  private OfferAddressSQLCopiedTasklet offerAddressSQLCopiedTasklet;
  
  @Autowired
  private OfferAddressTurnOffIdentityInsertion offerAddressturnOffIdentityInsertion;

  @Autowired
  private OfferPersonSQLCopiedTasklet offerPersonSQLCopiedTasklet;
  
  @Autowired
  private OfferPersonTurnOffIdentityInsertion offerPersonturnOffIdentityInsertion;

  @Autowired
  private OfferPersonPropertySQLCopiedTasklet offerPersonPropertySQLCopiedTasklet;
  
  @Autowired
  private OfferPositionSQLCopiedTasklet offerPositionSQLCopiedTasklet;
  
  @Autowired
  private OfferPositionTurnOffIdentityInsertion offerPositionturnOffIdentityInsertion;

  @Autowired
  private OrderStatusSQLCopiedTasklet orderStatusSQLCopiedTasklet;
  
  @Autowired
  private OrderStatusTurnOffIdentityInsertion orderStatusturnOffIdentityInsertion;

  @Autowired
  private OrderTypeSQLCopiedTasklet orderTypeSQLCopiedTasklet;
  
  @Autowired
  private OrderTypeTurnOffIdentityInsertion orderTypeturnOffIdentityInsertion;

  @Autowired
  private OrganisationPropertySQLCopiedTasklet organisationPropertySQLCopiedTasklet;
  
  @Autowired
  private OrganisationPropertyTurnOffIdentityInsertion organisationPropertyturnOffIdentityInsertion;

  @Autowired
  private OrganisationTypeSQLCopiedTasklet organisationTypeSQLCopiedTasklet;
  
  @Autowired
  private OrganisationTypeTurnOffIdentityInsertion organisationTypeturnOffIdentityInsertion;

  @Autowired
  private PaymentMethodSQLCopiedTasklet paymentMethodSQLCopiedTasklet;
  
  @Autowired
  private PaymentMethodTurnOffIdentityInsertion paymentMethodturnOffIdentityInsertion;

  @Autowired
  private CustomerSettingsSQLCopiedTasklet customerSettingsSQLCopiedTasklet;
  
  @Autowired
  private CustomerSettingsTurnOffIdentityInsertion customerSettingsturnOffIdentityInsertion;

  @Autowired
  private OrganisationSQLCopiedTasklet organisationSQLCopiedTasklet;
  
  @Autowired
  private OrganisationTurnOffIdentityInsertion organisationturnOffIdentityInsertion;

  @Autowired
  private OrganisationCollectionSQLCopiedTasklet organisationCollectionSQLCopiedTasklet;
  
  @Autowired
  private OrganisationCollectionTurnOffIdentityInsertion organisationCollectionturnOffIdentityInsertion;

  @Autowired
  private CollectionRelationSQLCopiedTasklet collectionRelationSQLCopiedTasklet;
  
  @Autowired
  private CollectionRelationTurnOffIdentityInsertion collectionRelationturnOffIdentityInsertion;

  @Autowired
  private OrganisationAddressSQLCopiedTasklet organisationAddressSQLCopiedTasklet;
  
  @Autowired
  private OrganisationAddressTurnOffIdentityInsertion organisationAddressturnOffIdentityInsertion;

  @Autowired
  private OrganisationGroupSQLCopiedTasklet organisationGroupSQLCopiedTasklet;
  
  @Autowired
  private OrganisationGroupTurnOffIdentityInsertion organisationGroupturnOffIdentityInsertion;

  @Autowired
  private OrgCollectionSettingsSQLCopiedTasklet orgCollectionSettingsSQLCopiedTasklet;
  
  @Autowired
  private OrgCollectionSettingsTurnOffIdentityInsertion orgCollectionSettingsturnOffIdentityInsertion;

  @Autowired
  private UserSettingsSQLCopiedTasklet userSettingsSQLCopiedTasklet;
  
  @Autowired
  private UserSettingsTurnOffIdentityInsertion userSettingsturnOffIdentityInsertion;

  @Autowired
  private OrganisationSettingsSQLCopiedTasklet organisationSettingsSQLCopiedTasklet;
  
  @Autowired
  private OrganisationSettingsTurnOffIdentityInsertion organisationSettingsturnOffIdentityInsertion;

  @Autowired
  private PermFunctionSQLCopiedTasklet permFunctionSQLCopiedTasklet;
  
  @Autowired
  private PermFunctionTurnOffIdentityInsertion permFunctionturnOffIdentityInsertion;

  @Autowired
  private RolePermissionSQLCopiedTasklet rolePermissionSQLCopiedTasklet;
  
  @Autowired
  private RolePermissionTurnOffIdentityInsertion rolePermissionturnOffIdentityInsertion;

  @Autowired
  private RoleTypeSQLCopiedTasklet roleTypeSQLCopiedTasklet;
  
  @Autowired
  private RoleTypeTurnOffIdentityInsertion roleTypeturnOffIdentityInsertion;

  @Autowired
  private EshopRoleSQLCopiedTasklet eshopRoleSQLCopiedTasklet;
  
  @Autowired
  private EshopRoleTurnOffIdentityInsertion eshopRoleturnOffIdentityInsertion;

  @Autowired
  private GroupRoleSQLCopiedTasklet groupRoleSQLCopiedTasklet;
  
  @Autowired
  private GroupRoleTurnOffIdentityInsertion groupRoleturnOffIdentityInsertion;

  @Autowired
  private SalutationSQLCopiedTasklet salutationSQLCopiedTasklet;
  
  @Autowired
  private SalutationTurnOffIdentityInsertion salutationturnOffIdentityInsertion;

  @Autowired
  private EshopUserSQLCopiedTasklet eshopUserSQLCopiedTasklet;
  
  @Autowired
  private EshopUserTurnOffIdentityInsertion eshopUserturnOffIdentityInsertion;

  @Autowired
  private FeedbackSQLCopiedTasklet feedbackSQLCopiedTasklet;
  
  @Autowired
  private FeedbackTurnOffIdentityInsertion feedbackturnOffIdentityInsertion;

  @Autowired
  private VinLoggingSQLCopiedTasklet vinLoggingSQLCopiedTasklet;
  
  @Autowired
  private VinLoggingTurnOffIdentityInsertion vinLoggingturnOffIdentityInsertion;

  @Autowired
  private LoginSQLCopiedTasklet loginSQLCopiedTasklet;
  
  @Autowired
  private LoginTurnOffIdentityInsertion loginturnOffIdentityInsertion;

  @Autowired
  private GroupUserSQLCopiedTasklet groupUserSQLCopiedTasklet;
  
  @Autowired
  private GroupUserTurnOffIdentityInsertion groupUserturnOffIdentityInsertion;

  @Autowired
  private OrderHistorySQLCopiedTasklet orderHistorySQLCopiedTasklet;
  
  @Autowired
  private OrderHistoryTurnOffIdentityInsertion orderHistoryturnOffIdentityInsertion;

  @Autowired
  private UserOrderHistorySQLCopiedTasklet userOrderHistorySQLCopiedTasklet;
  
  @Autowired
  private UserOrderHistoryTurnOffIdentityInsertion userOrderHistoryturnOffIdentityInsertion;

  @Autowired
  private ShopArticleSQLCopiedTasklet shopArticleSQLCopiedTasklet;
  
  @Autowired
  private ShopArticleTurnOffIdentityInsertion shopArticleturnOffIdentityInsertion;

  @Autowired
  private SupportedAffiliateSQLCopiedTasklet supportedAffiliateSQLCopiedTasklet;
  
  @Autowired
  private SupportedAffiliateTurnOffIdentityInsertion supportedAffiliateturnOffIdentityInsertion;

  @Autowired
  private SupportedBrandPromotionSQLCopiedTasklet supportedBrandPromotionSQLCopiedTasklet;
  
  @Autowired
  private SupportedBrandPromotionTurnOffIdentityInsertion supportedBrandPromotionturnOffIdentityInsertion;

  @Autowired
  private VehicleHistorySQLCopiedTasklet vehicleHistorySQLCopiedTasklet;
  
  @Autowired
  private VehicleHistoryTurnOffIdentityInsertion vehicleHistoryturnOffIdentityInsertion;

  @Autowired
  private UserVehicleHistorySQLCopiedTasklet userVehicleHistorySQLCopiedTasklet;
  
  @Autowired
  private UserVehicleHistoryTurnOffIdentityInsertion userVehicleHistoryturnOffIdentityInsertion;

  @Bean(name = "CopyDatabaseUsingSQL")
  @Transactional
  public Job createSQLCopyDataBaseJob(JobNotificationListener listener) {
    return jobBuilderFactory.get(Jobs.COPY_DATABASE_BY_SQL.getName())
        .listener(listener)
        .start(toStep(aadAccountsSQLCopiedTasklet))
        .next(toStep(aadAccountsturnOffIdentityInsertion))
        .next(toStep(addressTypeSQLCopiedTasklet))
        .next(toStep(addressTypeturnOffIdentityInsertion))
        .next(toStep(addressSQLCopiedTasklet))
        .next(toStep(addressturnOffIdentityInsertion))
        .next(toStep(affiliatePermissionSQLCopiedTasklet))
        .next(toStep(affiliatePermissionturnOffIdentityInsertion))
        .next(toStep(allocationTypeSQLCopiedTasklet))
        .next(toStep(allocationTypeturnOffIdentityInsertion))
        .next(toStep(articleHistorySQLCopiedTasklet))
        .next(toStep(articleHistoryturnOffIdentityInsertion))
        .next(toStep(basketHistorySQLCopiedTasklet))
        .next(toStep(basketHistoryturnOffIdentityInsertion))
        .next(toStep(businessLogSQLCopiedTasklet))
        .next(toStep(businessLogturnOffIdentityInsertion))
        .next(toStep(clientRoleSQLCopiedTasklet))
        .next(toStep(clientRoleturnOffIdentityInsertion))
        .next(toStep(collectionPermissionSQLCopiedTasklet))
        .next(toStep(collectionPermissionturnOffIdentityInsertion))
        .next(toStep(collectiveDeliverySQLCopiedTasklet))
        .next(toStep(collectiveDeliveryturnOffIdentityInsertion))
        .next(toStep(countrySQLCopiedTasklet))
        .next(toStep(countryturnOffIdentityInsertion))
        .next(toStep(couponConditionsSQLCopiedTasklet))
        .next(toStep(couponConditionsturnOffIdentityInsertion))
        .next(toStep(couponUseLogSQLCopiedTasklet))
        .next(toStep(couponUseLogturnOffIdentityInsertion))
        .next(toStep(currencySQLCopiedTasklet))
        .next(toStep(currencyturnOffIdentityInsertion))
        .next(toStep(deliveryTypeSQLCopiedTasklet))
        .next(toStep(deliveryTypeturnOffIdentityInsertion))
        .next(toStep(eshopCartItemSQLCopiedTasklet))
        .next(toStep(eshopCartItemturnOffIdentityInsertion))
        .next(toStep(eshopClientSQLCopiedTasklet))
        .next(toStep(eshopClientturnOffIdentityInsertion))
        .next(toStep(eshopClientResourceSQLCopiedTasklet))
        .next(toStep(eshopClientResourceturnOffIdentityInsertion))
        .next(toStep(eshopFunctionSQLCopiedTasklet))
        .next(toStep(eshopFunctionturnOffIdentityInsertion))
        .next(toStep(eshopGroupSQLCopiedTasklet))
        .next(toStep(eshopGroupturnOffIdentityInsertion))
        .next(toStep(eshopPermissionSQLCopiedTasklet))
        .next(toStep(eshopPermissionturnOffIdentityInsertion))
        .next(toStep(eshopReleaseSQLCopiedTasklet))
        .next(toStep(eshopReleaseturnOffIdentityInsertion))
        .next(toStep(externalOrganisationSQLCopiedTasklet))
        .next(toStep(externalOrganisationturnOffIdentityInsertion))
        .next(toStep(externalUserSQLCopiedTasklet))
        .next(toStep(externalUserturnOffIdentityInsertion))
        .next(toStep(feedbackDepartmentSQLCopiedTasklet))
        .next(toStep(feedbackDepartmentturnOffIdentityInsertion))
        .next(toStep(feedbackDepartmentContactSQLCopiedTasklet))
        .next(toStep(feedbackDepartmentContactturnOffIdentityInsertion))
        .next(toStep(feedbackStatusSQLCopiedTasklet))
        .next(toStep(feedbackStatusturnOffIdentityInsertion))
        .next(toStep(feedbackTopicSQLCopiedTasklet))
        .next(toStep(feedbackTopicturnOffIdentityInsertion))
        .next(toStep(feedbackTopicDepartmentSQLCopiedTasklet))
        .next(toStep(feedbackTopicDepartmentturnOffIdentityInsertion))
        .next(toStep(finalCustomerOrderSQLCopiedTasklet))
        .next(toStep(finalCustomerOrderturnOffIdentityInsertion))
        .next(toStep(finalCustomerOrderItemSQLCopiedTasklet))
        .next(toStep(finalCustomerOrderItemturnOffIdentityInsertion))
        .next(toStep(finalCustomerPropertySQLCopiedTasklet))
        .next(toStep(finalCustomerPropertyturnOffIdentityInsertion))
        .next(toStep(groupPermissionSQLCopiedTasklet))
        .next(toStep(groupPermissionturnOffIdentityInsertion))
        .next(toStep(invoiceTypeSQLCopiedTasklet))
        .next(toStep(invoiceTypeturnOffIdentityInsertion))
        .next(toStep(languagesSQLCopiedTasklet))
        .next(toStep(languagesturnOffIdentityInsertion))
        .next(toStep(legalDocumentAffiliateAssignedLogSQLCopiedTasklet))
        .next(toStep(legalDocumentAffiliateAssignedLogturnOffIdentityInsertion))
        .next(toStep(legalDocumentCustomerAcceptedLogSQLCopiedTasklet))
        .next(toStep(legalDocumentCustomerAcceptedLogturnOffIdentityInsertion))
        .next(toStep(legalDocumentMasterSQLCopiedTasklet))
        .next(toStep(legalDocumentMasterturnOffIdentityInsertion))
        .next(toStep(licenseSettingsSQLCopiedTasklet))
        .next(toStep(licenseSettingsturnOffIdentityInsertion))
        .next(toStep(licenseSQLCopiedTasklet))
        .next(toStep(licenseturnOffIdentityInsertion))
        .next(toStep(mappingUserIdEblConnectSQLCopiedTasklet))
        .next(toStep(mappingUserIdEblConnectturnOffIdentityInsertion))
        .next(toStep(messageSQLCopiedTasklet))
        .next(toStep(messageturnOffIdentityInsertion))
        .next(toStep(messageAccessRightSQLCopiedTasklet))
        .next(toStep(messageAccessRightturnOffIdentityInsertion))
        .next(toStep(messageAccessRightAreaSQLCopiedTasklet))
        .next(toStep(messageAccessRightAreaturnOffIdentityInsertion))
        .next(toStep(messageAccessRightRoleSQLCopiedTasklet))
        .next(toStep(messageAccessRightRoleturnOffIdentityInsertion))
        .next(toStep(messageAreaSQLCopiedTasklet))
        .next(toStep(messageAreaturnOffIdentityInsertion))
        .next(toStep(messageHidingSQLCopiedTasklet))
        .next(toStep(messageHidingturnOffIdentityInsertion))
        .next(toStep(messageLanguageSQLCopiedTasklet))
        .next(toStep(messageLanguageturnOffIdentityInsertion))
        .next(toStep(messageLocationSQLCopiedTasklet))
        .next(toStep(messageLocationturnOffIdentityInsertion))
        .next(toStep(messageLocationTypeSQLCopiedTasklet))
        .next(toStep(messageLocationTypeturnOffIdentityInsertion))
        .next(toStep(messageLocationTypeRoleTypeSQLCopiedTasklet))
        .next(toStep(messageLocationTypeRoleTypeturnOffIdentityInsertion))
        .next(toStep(messageRoleTypeSQLCopiedTasklet))
        .next(toStep(messageRoleTypeturnOffIdentityInsertion))
        .next(toStep(messageStyleSQLCopiedTasklet))
        .next(toStep(messageStyleturnOffIdentityInsertion))
        .next(toStep(messageSubAreaSQLCopiedTasklet))
        .next(toStep(messageSubAreaturnOffIdentityInsertion))
        .next(toStep(messageTypeSQLCopiedTasklet))
        .next(toStep(messageTypeturnOffIdentityInsertion))
        .next(toStep(messageVisibilitySQLCopiedTasklet))
        .next(toStep(messageVisibilityturnOffIdentityInsertion))
        .next(toStep(offerSQLCopiedTasklet))
        .next(toStep(offerturnOffIdentityInsertion))
        .next(toStep(offerAddressSQLCopiedTasklet))
        .next(toStep(offerAddressturnOffIdentityInsertion))
        .next(toStep(offerPersonSQLCopiedTasklet))
        .next(toStep(offerPersonturnOffIdentityInsertion))
        .next(toStep(offerPersonPropertySQLCopiedTasklet))
        .next(toStep(offerPositionSQLCopiedTasklet))
        .next(toStep(offerPositionturnOffIdentityInsertion))
        .next(toStep(orderStatusSQLCopiedTasklet))
        .next(toStep(orderStatusturnOffIdentityInsertion))
        .next(toStep(orderTypeSQLCopiedTasklet))
        .next(toStep(orderTypeturnOffIdentityInsertion))
        .next(toStep(organisationPropertySQLCopiedTasklet))
        .next(toStep(organisationPropertyturnOffIdentityInsertion))
        .next(toStep(organisationTypeSQLCopiedTasklet))
        .next(toStep(organisationTypeturnOffIdentityInsertion))
        .next(toStep(paymentMethodSQLCopiedTasklet))
        .next(toStep(paymentMethodturnOffIdentityInsertion))
        .next(toStep(customerSettingsSQLCopiedTasklet))
        .next(toStep(customerSettingsturnOffIdentityInsertion))
        .next(toStep(organisationSQLCopiedTasklet))
        .next(toStep(organisationturnOffIdentityInsertion))
        .next(toStep(organisationCollectionSQLCopiedTasklet))
        .next(toStep(organisationCollectionturnOffIdentityInsertion))
        .next(toStep(collectionRelationSQLCopiedTasklet))
        .next(toStep(collectionRelationturnOffIdentityInsertion))
        .next(toStep(organisationAddressSQLCopiedTasklet))
        .next(toStep(organisationAddressturnOffIdentityInsertion))
        .next(toStep(organisationGroupSQLCopiedTasklet))
        .next(toStep(organisationGroupturnOffIdentityInsertion))
        .next(toStep(orgCollectionSettingsSQLCopiedTasklet))
        .next(toStep(orgCollectionSettingsturnOffIdentityInsertion))
        .next(toStep(userSettingsSQLCopiedTasklet))
        .next(toStep(userSettingsturnOffIdentityInsertion))
        .next(toStep(organisationSettingsSQLCopiedTasklet))
        .next(toStep(organisationSettingsturnOffIdentityInsertion))
        .next(toStep(permFunctionSQLCopiedTasklet))
        .next(toStep(permFunctionturnOffIdentityInsertion))
        .next(toStep(rolePermissionSQLCopiedTasklet))
        .next(toStep(rolePermissionturnOffIdentityInsertion))
        .next(toStep(roleTypeSQLCopiedTasklet))
        .next(toStep(roleTypeturnOffIdentityInsertion))
        .next(toStep(eshopRoleSQLCopiedTasklet))
        .next(toStep(eshopRoleturnOffIdentityInsertion))
        .next(toStep(groupRoleSQLCopiedTasklet))
        .next(toStep(groupRoleturnOffIdentityInsertion))
        .next(toStep(salutationSQLCopiedTasklet))
        .next(toStep(salutationturnOffIdentityInsertion))
        .next(toStep(eshopUserSQLCopiedTasklet))
        .next(toStep(eshopUserturnOffIdentityInsertion))
        .next(toStep(feedbackSQLCopiedTasklet))
        .next(toStep(feedbackturnOffIdentityInsertion))
        .next(toStep(vinLoggingSQLCopiedTasklet))
        .next(toStep(vinLoggingturnOffIdentityInsertion))
        .next(toStep(loginSQLCopiedTasklet))
        .next(toStep(loginturnOffIdentityInsertion))
        .next(toStep(groupUserSQLCopiedTasklet))
        .next(toStep(groupUserturnOffIdentityInsertion))
        .next(toStep(orderHistorySQLCopiedTasklet))
        .next(toStep(orderHistoryturnOffIdentityInsertion))
        .next(toStep(userOrderHistorySQLCopiedTasklet))
        .next(toStep(userOrderHistoryturnOffIdentityInsertion))
        .next(toStep(shopArticleSQLCopiedTasklet))
        .next(toStep(shopArticleturnOffIdentityInsertion))
        .next(toStep(supportedAffiliateSQLCopiedTasklet))
        .next(toStep(supportedAffiliateturnOffIdentityInsertion))
        .next(toStep(supportedBrandPromotionSQLCopiedTasklet))
        .next(toStep(supportedBrandPromotionturnOffIdentityInsertion))
        .next(toStep(vehicleHistorySQLCopiedTasklet))
        .next(toStep(vehicleHistoryturnOffIdentityInsertion))
        .next(toStep(userVehicleHistorySQLCopiedTasklet))
        .next(toStep(userVehicleHistoryturnOffIdentityInsertion))
        .build();
  }

}
