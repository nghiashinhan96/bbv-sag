
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sagag.services.stakis.erp.wsdl.tmconnect package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindItemsResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsResponse");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _ArrayOfArticleTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfArticleTmf");
    private final static QName _Customer_QNAME = new QName("http://topmotive.eu/TMConnect", "Customer");
    private final static QName _Address_QNAME = new QName("http://topmotive.eu/TMConnect", "Address");
    private final static QName _ArrayOfDispatchType_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfDispatchType");
    private final static QName _FindItemsReply_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsReply");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _GetAccountDataRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountDataRequestBody");
    private final static QName _GetAccountDataResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountDataResponse");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _GetErpInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformation");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _ArrayOfItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfItemsCollection");
    private final static QName _SendOrderResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderResponseBody");
    private final static QName _DeleteSessionRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "DeleteSessionRequestBody");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _ArrayOfNotification_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfNotification");
    private final static QName _SendOrderDocumentRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocumentRequest");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _SendOrderDocumentResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocumentResponseBody");
    private final static QName _LinkedItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedItemsCollection");
    private final static QName _GetNotificationRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationRequestBody");
    private final static QName _GetErpInformationRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationRequest");
    private final static QName _ArrayOfAddress_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAddress");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _GetItemsCollectionsResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionsResponse");
    private final static QName _AdditionalIdentifier_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalIdentifier");
    private final static QName _BaseResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "BaseResponse");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _AccountData_QNAME = new QName("http://topmotive.eu/TMConnect", "AccountData");
    private final static QName _SendOrderRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderRequest");
    private final static QName _GetVersion_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersion");
    private final static QName _FindItemsRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsRequest");
    private final static QName _AvailabilityState_QNAME = new QName("http://topmotive.eu/TMConnect", "AvailabilityState");
    private final static QName _ArrayOfAvailabilityState_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAvailabilityState");
    private final static QName _ArrayOfAdditionalIdentifier_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAdditionalIdentifier");
    private final static QName _Tour_QNAME = new QName("http://topmotive.eu/TMConnect", "Tour");
    private final static QName _SendOrderDocumentResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocumentResponse");
    private final static QName _GetAccountDataResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountDataResponseBody");
    private final static QName _GetSessionResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetSessionResponseBody");
    private final static QName _Article_QNAME = new QName("http://topmotive.eu/TMConnect", "Article");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _BaseDto_QNAME = new QName("http://topmotive.eu/TMConnect", "BaseDto");
    private final static QName _ArrayOfControlIndicator_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfControlIndicator");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _Notification_QNAME = new QName("http://topmotive.eu/TMConnect", "Notification");
    private final static QName _ArrayOfSelectionListItem_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfSelectionListItem");
    private final static QName _ErpInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "ErpInformation");
    private final static QName _MasterData_QNAME = new QName("http://topmotive.eu/TMConnect", "MasterData");
    private final static QName _GetVersionRequestBody_QNAME = new QName("http://schemas.datacontract.org/2004/07/STG.DVSE.TMC", "GetVersionRequestBody");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _ItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "ItemsCollection");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _ArrayOfIcon_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfIcon");
    private final static QName _GetErpInformationReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationReply");
    private final static QName _ProductGroupTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ProductGroupTmf");
    private final static QName _GetAccountInformationReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountInformationReply");
    private final static QName _GetItemsCollectionsRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionsRequestBody");
    private final static QName _Credentials_QNAME = new QName("http://topmotive.eu/TMConnect", "Credentials");
    private final static QName _Memo_QNAME = new QName("http://topmotive.eu/TMConnect", "Memo");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _KeyValueItem_QNAME = new QName("http://topmotive.eu/TMConnect", "KeyValueItem");
    private final static QName _GetItemsCollectionsResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionsResponseBody");
    private final static QName _SendOrderReply_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderReply");
    private final static QName _ArrayOfArticle_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfArticle");
    private final static QName _ControlIndicator_QNAME = new QName("http://topmotive.eu/TMConnect", "ControlIndicator");
    private final static QName _GetSession_QNAME = new QName("http://topmotive.eu/TMConnect", "GetSession");
    private final static QName _ArrayOfEntityLink_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfEntityLink");
    private final static QName _Vehicle_QNAME = new QName("http://topmotive.eu/TMConnect", "Vehicle");
    private final static QName _ArrayOfOrder_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfOrder");
    private final static QName _DispatchType_QNAME = new QName("http://topmotive.eu/TMConnect", "DispatchType");
    private final static QName _ServiceConfiguration_QNAME = new QName("http://topmotive.eu/TMConnect", "ServiceConfiguration");
    private final static QName _ArrayOfOrderPosition_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfOrderPosition");
    private final static QName _ArrayOfRepairTime_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfRepairTime");
    private final static QName _ArrayOfTour_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfTour");
    private final static QName _GetSessionResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetSessionResponse");
    private final static QName _ArrayOfPrice_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfPrice");
    private final static QName _ValidationMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "ValidationMessage");
    private final static QName _DeleteSessionResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "DeleteSessionResponseBody");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _ArrayOfVehicle_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfVehicle");
    private final static QName _SendOrderResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderResponse");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _GetErpInformationResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationResponse");
    private final static QName _GetErpInformationResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationResponseBody");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _ArrayOfAccountData_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAccountData");
    private final static QName _Price_QNAME = new QName("http://topmotive.eu/TMConnect", "Price");
    private final static QName _OrderPosition_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderPosition");
    private final static QName _ArrayOfMemo_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfMemo");
    private final static QName _ArrayOfErpInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfErpInformation");
    private final static QName _SendOrderRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderRequestBody");
    private final static QName _FindItems_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItems");
    private final static QName _BaseRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "BaseRequest");
    private final static QName _Icon_QNAME = new QName("http://topmotive.eu/TMConnect", "Icon");
    private final static QName _SelectionList_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionList");
    private final static QName _TextBlock_QNAME = new QName("http://topmotive.eu/TMConnect", "TextBlock");
    private final static QName _ArrayOfProductGroupTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfProductGroupTmf");
    private final static QName _SupplierTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "SupplierTmf");
    private final static QName _GetNotification_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotification");
    private final static QName _SendOrder_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrder");
    private final static QName _ArrayOfLinkedItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfLinkedItemsCollection");
    private final static QName _Order_QNAME = new QName("http://topmotive.eu/TMConnect", "Order");
    private final static QName _ArrayOfTextBlock_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfTextBlock");
    private final static QName _GetNotificationReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationReply");
    private final static QName _GetErpInformationRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationRequestBody");
    private final static QName _GetSessionReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetSessionReply");
    private final static QName _OrderId_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderId");
    private final static QName _SelectionListItem_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionListItem");
    private final static QName _GetVersionResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersionResponse");
    private final static QName _ArrayOfKeyValueItem_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfKeyValueItem");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _SendOrderDocument_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocument");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _GetAccountData_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountData");
    private final static QName _GetVersionResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersionResponseBody");
    private final static QName _ArrayOfSelectionList_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfSelectionList");
    private final static QName _FindItemsRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsRequestBody");
    private final static QName _GetSessionRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetSessionRequestBody");
    private final static QName _FindItemsResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsResponseBody");
    private final static QName _GetNotificationRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationRequest");
    private final static QName _User_QNAME = new QName("http://topmotive.eu/TMConnect", "User");
    private final static QName _Warehouse_QNAME = new QName("http://topmotive.eu/TMConnect", "Warehouse");
    private final static QName _ArrayOfUserDefinedData_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfUserDefinedData");
    private final static QName _GetItemsCollectionReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionReply");
    private final static QName _ArrayOfOrderId_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfOrderId");
    private final static QName _GetItemsCollections_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollections");
    private final static QName _GetNotificationResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationResponseBody");
    private final static QName _ArrayOfCustomer_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfCustomer");
    private final static QName _GetItemsCollectionRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionRequest");
    private final static QName _GetNotificationResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationResponse");
    private final static QName _OrderCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderCollection");
    private final static QName _ArticleTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleTmf");
    private final static QName _ArrayOfQuantity_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfQuantity");
    private final static QName _DeleteSession_QNAME = new QName("http://topmotive.eu/TMConnect", "DeleteSession");
    private final static QName _RepairTimesCostRate_QNAME = new QName("http://topmotive.eu/TMConnect", "RepairTimesCostRate");
    private final static QName _EntityLink_QNAME = new QName("http://topmotive.eu/TMConnect", "EntityLink");
    private final static QName _DeleteSessionResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "DeleteSessionResponse");
    private final static QName _EMasterDataType_QNAME = new QName("http://topmotive.eu/TMConnect", "EMasterDataType");
    private final static QName _RepairTime_QNAME = new QName("http://topmotive.eu/TMConnect", "RepairTime");
    private final static QName _ArrayOfValidationMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfValidationMessage");
    private final static QName _UserDefinedData_QNAME = new QName("http://topmotive.eu/TMConnect", "UserDefinedData");
    private final static QName _ArrayOfWarehouse_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfWarehouse");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Quantity_QNAME = new QName("http://topmotive.eu/TMConnect", "Quantity");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _SendOrderDocumentRequestBody_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocumentRequestBody");
    private final static QName _GetErpInformationRequestErpArticleInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "ErpArticleInformation");
    private final static QName _CredentialsSalt_QNAME = new QName("http://topmotive.eu/TMConnect", "Salt");
    private final static QName _CredentialsSecurityToken_QNAME = new QName("http://topmotive.eu/TMConnect", "SecurityToken");
    private final static QName _CredentialsSalesAdvisorCredentials_QNAME = new QName("http://topmotive.eu/TMConnect", "SalesAdvisorCredentials");
    private final static QName _CredentialsExternalIdentityProviderId_QNAME = new QName("http://topmotive.eu/TMConnect", "ExternalIdentityProviderId");
    private final static QName _CredentialsCatalogUserCredentials_QNAME = new QName("http://topmotive.eu/TMConnect", "CatalogUserCredentials");
    private final static QName _RepairTimeTypeOfCostRate_QNAME = new QName("http://topmotive.eu/TMConnect", "TypeOfCostRate");
    private final static QName _RepairTimeDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "Description");
    private final static QName _RepairTimeId_QNAME = new QName("http://topmotive.eu/TMConnect", "Id");
    private final static QName _SendOrderRequestBodyRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "request");
    private final static QName _ArticleArticleIdErp_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleIdErp");
    private final static QName _ControlIndicatorLinkedEntity_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedEntity");
    private final static QName _ControlIndicatorParameters_QNAME = new QName("http://topmotive.eu/TMConnect", "Parameters");
    private final static QName _GetNotificationReplyItems_QNAME = new QName("http://topmotive.eu/TMConnect", "Items");
    private final static QName _GetErpInformationResponseBodyGetErpInformationResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationResult");
    private final static QName _LinkedItemsCollectionMemos_QNAME = new QName("http://topmotive.eu/TMConnect", "Memos");
    private final static QName _LinkedItemsCollectionLinkedItems_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedItems");
    private final static QName _AdditionalIdentifierValue_QNAME = new QName("http://topmotive.eu/TMConnect", "Value");
    private final static QName _SendOrderResponseBodySendOrderResult_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderResult");
    private final static QName _GetVersionResponseBodyGetVersionResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersionResult");
    private final static QName _ErpInformationItem_QNAME = new QName("http://topmotive.eu/TMConnect", "Item");
    private final static QName _ErpInformationPrices_QNAME = new QName("http://topmotive.eu/TMConnect", "Prices");
    private final static QName _ErpInformationAdditionalDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalDescription");
    private final static QName _ErpInformationLinkedItemsCollections_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedItemsCollections");
    private final static QName _ErpInformationControlIndicators_QNAME = new QName("http://topmotive.eu/TMConnect", "ControlIndicators");
    private final static QName _ErpInformationAdditionalDescriptionExtended_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalDescriptionExtended");
    private final static QName _ErpInformationWarehouses_QNAME = new QName("http://topmotive.eu/TMConnect", "Warehouses");
    private final static QName _ErpInformationStatusInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "StatusInformation");
    private final static QName _ErpInformationSpecialIcons_QNAME = new QName("http://topmotive.eu/TMConnect", "SpecialIcons");
    private final static QName _BaseDtoAdditionalIdentifiers_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalIdentifiers");
    private final static QName _OrderPositionSelectionLists_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionLists");
    private final static QName _OrderPositionLinkedEntities_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedEntities");
    private final static QName _PriceCurrencyCodeIso4217_QNAME = new QName("http://topmotive.eu/TMConnect", "CurrencyCode_Iso_4217");
    private final static QName _PriceCurrencySymbol_QNAME = new QName("http://topmotive.eu/TMConnect", "CurrencySymbol");
    private final static QName _SendOrderDocumentResponseBodySendOrderDocumentResult_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocumentResult");
    private final static QName _BaseResponseErrorCode_QNAME = new QName("http://topmotive.eu/TMConnect", "ErrorCode");
    private final static QName _BaseResponseNotifications_QNAME = new QName("http://topmotive.eu/TMConnect", "Notifications");
    private final static QName _BaseResponseErrorMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "ErrorMessage");
    private final static QName _BaseResponseValidationMessages_QNAME = new QName("http://topmotive.eu/TMConnect", "ValidationMessages");
    private final static QName _BaseResponseAdditionalIds_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalIds");
    private final static QName _IconLinkUrl_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkUrl");
    private final static QName _IconUrl_QNAME = new QName("http://topmotive.eu/TMConnect", "Url");
    private final static QName _IconTopmotiveId_QNAME = new QName("http://topmotive.eu/TMConnect", "TopmotiveId");
    private final static QName _SelectionListItemText_QNAME = new QName("http://topmotive.eu/TMConnect", "Text");
    private final static QName _TourExpectedDelivery_QNAME = new QName("http://topmotive.eu/TMConnect", "ExpectedDelivery");
    private final static QName _MasterDataItemsCollections_QNAME = new QName("http://topmotive.eu/TMConnect", "ItemsCollections");
    private final static QName _MasterDataAddresses_QNAME = new QName("http://topmotive.eu/TMConnect", "Addresses");
    private final static QName _MasterDataArticleTmfs_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleTmfs");
    private final static QName _MasterDataCustomers_QNAME = new QName("http://topmotive.eu/TMConnect", "Customers");
    private final static QName _MasterDataRepairTimes_QNAME = new QName("http://topmotive.eu/TMConnect", "RepairTimes");
    private final static QName _MasterDataSelectionListItems_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionListItems");
    private final static QName _MasterDataAvailabilityStates_QNAME = new QName("http://topmotive.eu/TMConnect", "AvailabilityStates");
    private final static QName _MasterDataDispatchTypes_QNAME = new QName("http://topmotive.eu/TMConnect", "DispatchTypes");
    private final static QName _MasterDataArticles_QNAME = new QName("http://topmotive.eu/TMConnect", "Articles");
    private final static QName _MasterDataIcons_QNAME = new QName("http://topmotive.eu/TMConnect", "Icons");
    private final static QName _MasterDataTours_QNAME = new QName("http://topmotive.eu/TMConnect", "Tours");
    private final static QName _MasterDataTextBlocks_QNAME = new QName("http://topmotive.eu/TMConnect", "TextBlocks");
    private final static QName _MasterDataVehicles_QNAME = new QName("http://topmotive.eu/TMConnect", "Vehicles");
    private final static QName _GetSessionResponseBodyGetSessionResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetSessionResult");
    private final static QName _ArticleTmfArticleIdSupplier_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleIdSupplier");
    private final static QName _ArticleTmfProductGroups_QNAME = new QName("http://topmotive.eu/TMConnect", "ProductGroups");
    private final static QName _ArticleTmfSupplier_QNAME = new QName("http://topmotive.eu/TMConnect", "Supplier");
    private final static QName _RepairTimesCostRatePricePerUnit_QNAME = new QName("http://topmotive.eu/TMConnect", "PricePerUnit");
    private final static QName _AddressCompanyName_QNAME = new QName("http://topmotive.eu/TMConnect", "CompanyName");
    private final static QName _AddressPostOfficeBox_QNAME = new QName("http://topmotive.eu/TMConnect", "PostOfficeBox");
    private final static QName _AddressType_QNAME = new QName("http://topmotive.eu/TMConnect", "Type");
    private final static QName _AddressAddressDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "AddressDescription");
    private final static QName _AddressPhone_QNAME = new QName("http://topmotive.eu/TMConnect", "Phone");
    private final static QName _AddressStreet_QNAME = new QName("http://topmotive.eu/TMConnect", "Street");
    private final static QName _AddressCountry_QNAME = new QName("http://topmotive.eu/TMConnect", "Country");
    private final static QName _AddressLastName_QNAME = new QName("http://topmotive.eu/TMConnect", "LastName");
    private final static QName _AddressDistrict_QNAME = new QName("http://topmotive.eu/TMConnect", "District");
    private final static QName _AddressFax_QNAME = new QName("http://topmotive.eu/TMConnect", "Fax");
    private final static QName _AddressAdditionalInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalInformation");
    private final static QName _AddressZIP_QNAME = new QName("http://topmotive.eu/TMConnect", "ZIP");
    private final static QName _AddressStreetExt_QNAME = new QName("http://topmotive.eu/TMConnect", "StreetExt");
    private final static QName _AddressEmail_QNAME = new QName("http://topmotive.eu/TMConnect", "Email");
    private final static QName _AddressFirstName_QNAME = new QName("http://topmotive.eu/TMConnect", "FirstName");
    private final static QName _AddressTitle_QNAME = new QName("http://topmotive.eu/TMConnect", "Title");
    private final static QName _AddressCity_QNAME = new QName("http://topmotive.eu/TMConnect", "City");
    private final static QName _AddressContact_QNAME = new QName("http://topmotive.eu/TMConnect", "Contact");
    private final static QName _AddressMobilePhone_QNAME = new QName("http://topmotive.eu/TMConnect", "MobilePhone");
    private final static QName _ValidationMessageMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "Message");
    private final static QName _DispatchTypeDispatchConditions_QNAME = new QName("http://topmotive.eu/TMConnect", "DispatchConditions");
    private final static QName _OrderTourInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "TourInformation");
    private final static QName _OrderWarehouseInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "WarehouseInformation");
    private final static QName _OrderOrderIds_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderIds");
    private final static QName _GetSessionReplySessionId_QNAME = new QName("http://topmotive.eu/TMConnect", "SessionId");
    private final static QName _VehicleModelGroup_QNAME = new QName("http://topmotive.eu/TMConnect", "ModelGroup");
    private final static QName _VehicleTypeCertificateNumber_QNAME = new QName("http://topmotive.eu/TMConnect", "TypeCertificateNumber");
    private final static QName _VehicleMileageShortIndicator_QNAME = new QName("http://topmotive.eu/TMConnect", "MileageShortIndicator");
    private final static QName _VehicleMotorCode_QNAME = new QName("http://topmotive.eu/TMConnect", "MotorCode");
    private final static QName _VehicleModel_QNAME = new QName("http://topmotive.eu/TMConnect", "Model");
    private final static QName _VehicleVehicleOwner_QNAME = new QName("http://topmotive.eu/TMConnect", "VehicleOwner");
    private final static QName _VehicleMake_QNAME = new QName("http://topmotive.eu/TMConnect", "Make");
    private final static QName _VehiclePlateId_QNAME = new QName("http://topmotive.eu/TMConnect", "PlateId");
    private final static QName _VehicleVin_QNAME = new QName("http://topmotive.eu/TMConnect", "Vin");
    private final static QName _GetNotificationResponseBodyGetNotificationResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationResult");
    private final static QName _SupplierTmfName_QNAME = new QName("http://topmotive.eu/TMConnect", "Name");
    private final static QName _OrderCollectionOrders_QNAME = new QName("http://topmotive.eu/TMConnect", "Orders");
    private final static QName _OrderCollectionShortDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "ShortDescription");
    private final static QName _QuantityExpectedDeliveryTime_QNAME = new QName("http://topmotive.eu/TMConnect", "ExpectedDeliveryTime");
    private final static QName _QuantityLotSizes_QNAME = new QName("http://topmotive.eu/TMConnect", "LotSizes");
    private final static QName _QuantityPackagingUnit_QNAME = new QName("http://topmotive.eu/TMConnect", "PackagingUnit");
    private final static QName _QuantityQuantityUnit_QNAME = new QName("http://topmotive.eu/TMConnect", "QuantityUnit");
    private final static QName _MemoLabel_QNAME = new QName("http://topmotive.eu/TMConnect", "Label");
    private final static QName _FindItemsRequestSearchTerm_QNAME = new QName("http://topmotive.eu/TMConnect", "SearchTerm");
    private final static QName _GetItemsCollectionsResponseBodyGetItemsCollectionsResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionsResult");
    private final static QName _KeyValueItemKey_QNAME = new QName("http://topmotive.eu/TMConnect", "Key");
    private final static QName _WarehouseQuantities_QNAME = new QName("http://topmotive.eu/TMConnect", "Quantities");
    private final static QName _BaseRequestExternalSessionId_QNAME = new QName("http://topmotive.eu/TMConnect", "ExternalSessionId");
    private final static QName _BaseRequestClientIp_QNAME = new QName("http://topmotive.eu/TMConnect", "ClientIp");
    private final static QName _BaseRequestLanguageCodeIso6391_QNAME = new QName("http://topmotive.eu/TMConnect", "LanguageCodeIso639_1");
    private final static QName _DeleteSessionResponseBodyDeleteSessionResult_QNAME = new QName("http://topmotive.eu/TMConnect", "DeleteSessionResult");
    private final static QName _FindItemsResponseBodyFindItemsResult_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsResult");
    private final static QName _UserUsername_QNAME = new QName("http://topmotive.eu/TMConnect", "Username");
    private final static QName _UserMandatorId_QNAME = new QName("http://topmotive.eu/TMConnect", "MandatorId");
    private final static QName _UserCustomerId_QNAME = new QName("http://topmotive.eu/TMConnect", "CustomerId");
    private final static QName _UserPassword_QNAME = new QName("http://topmotive.eu/TMConnect", "Password");
    private final static QName _SendOrderDocumentRequestDocument_QNAME = new QName("http://topmotive.eu/TMConnect", "Document");
    private final static QName _GetAccountDataResponseBodyGetAccountDataResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountDataResult");
    private final static QName _CustomerDetails_QNAME = new QName("http://topmotive.eu/TMConnect", "Details");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sagag.services.stakis.erp.wsdl.tmconnect
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link ArrayOfDispatchType }
     * 
     */
    public ArrayOfDispatchType createArrayOfDispatchType() {
        return new ArrayOfDispatchType();
    }

    /**
     * Create an instance of {@link Customer }
     * 
     */
    public Customer createCustomer() {
        return new Customer();
    }

    /**
     * Create an instance of {@link FindItemsReply }
     * 
     */
    public FindItemsReply createFindItemsReply() {
        return new FindItemsReply();
    }

    /**
     * Create an instance of {@link GetAccountDataRequestBody }
     * 
     */
    public GetAccountDataRequestBody createGetAccountDataRequestBody() {
        return new GetAccountDataRequestBody();
    }

    /**
     * Create an instance of {@link FindItemsResponseBody }
     * 
     */
    public FindItemsResponseBody createFindItemsResponseBody() {
        return new FindItemsResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfArticleTmf }
     * 
     */
    public ArrayOfArticleTmf createArrayOfArticleTmf() {
        return new ArrayOfArticleTmf();
    }

    /**
     * Create an instance of {@link SendOrderDocumentRequest }
     * 
     */
    public SendOrderDocumentRequest createSendOrderDocumentRequest() {
        return new SendOrderDocumentRequest();
    }

    /**
     * Create an instance of {@link SendOrderDocumentResponseBody }
     * 
     */
    public SendOrderDocumentResponseBody createSendOrderDocumentResponseBody() {
        return new SendOrderDocumentResponseBody();
    }

    /**
     * Create an instance of {@link LinkedItemsCollection }
     * 
     */
    public LinkedItemsCollection createLinkedItemsCollection() {
        return new LinkedItemsCollection();
    }

    /**
     * Create an instance of {@link GetNotificationRequestBody }
     * 
     */
    public GetNotificationRequestBody createGetNotificationRequestBody() {
        return new GetNotificationRequestBody();
    }

    /**
     * Create an instance of {@link GetErpInformationRequest }
     * 
     */
    public GetErpInformationRequest createGetErpInformationRequest() {
        return new GetErpInformationRequest();
    }

    /**
     * Create an instance of {@link ArrayOfAddress }
     * 
     */
    public ArrayOfAddress createArrayOfAddress() {
        return new ArrayOfAddress();
    }

    /**
     * Create an instance of {@link GetItemsCollectionsResponseBody }
     * 
     */
    public GetItemsCollectionsResponseBody createGetItemsCollectionsResponseBody() {
        return new GetItemsCollectionsResponseBody();
    }

    /**
     * Create an instance of {@link GetAccountDataResponseBody }
     * 
     */
    public GetAccountDataResponseBody createGetAccountDataResponseBody() {
        return new GetAccountDataResponseBody();
    }

    /**
     * Create an instance of {@link GetErpInformationRequestBody }
     * 
     */
    public GetErpInformationRequestBody createGetErpInformationRequestBody() {
        return new GetErpInformationRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfItemsCollection }
     * 
     */
    public ArrayOfItemsCollection createArrayOfItemsCollection() {
        return new ArrayOfItemsCollection();
    }

    /**
     * Create an instance of {@link SendOrderResponseBody }
     * 
     */
    public SendOrderResponseBody createSendOrderResponseBody() {
        return new SendOrderResponseBody();
    }

    /**
     * Create an instance of {@link DeleteSessionRequestBody }
     * 
     */
    public DeleteSessionRequestBody createDeleteSessionRequestBody() {
        return new DeleteSessionRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfNotification }
     * 
     */
    public ArrayOfNotification createArrayOfNotification() {
        return new ArrayOfNotification();
    }

    /**
     * Create an instance of {@link GetSessionResponseBody }
     * 
     */
    public GetSessionResponseBody createGetSessionResponseBody() {
        return new GetSessionResponseBody();
    }

    /**
     * Create an instance of {@link Article }
     * 
     */
    public Article createArticle() {
        return new Article();
    }

    /**
     * Create an instance of {@link BaseDto }
     * 
     */
    public BaseDto createBaseDto() {
        return new BaseDto();
    }

    /**
     * Create an instance of {@link ArrayOfControlIndicator }
     * 
     */
    public ArrayOfControlIndicator createArrayOfControlIndicator() {
        return new ArrayOfControlIndicator();
    }

    /**
     * Create an instance of {@link Notification }
     * 
     */
    public Notification createNotification() {
        return new Notification();
    }

    /**
     * Create an instance of {@link ArrayOfSelectionListItem }
     * 
     */
    public ArrayOfSelectionListItem createArrayOfSelectionListItem() {
        return new ArrayOfSelectionListItem();
    }

    /**
     * Create an instance of {@link AdditionalIdentifier }
     * 
     */
    public AdditionalIdentifier createAdditionalIdentifier() {
        return new AdditionalIdentifier();
    }

    /**
     * Create an instance of {@link BaseResponse }
     * 
     */
    public BaseResponse createBaseResponse() {
        return new BaseResponse();
    }

    /**
     * Create an instance of {@link AccountData }
     * 
     */
    public AccountData createAccountData() {
        return new AccountData();
    }

    /**
     * Create an instance of {@link GetVersionRequestBody }
     * 
     */
    public GetVersionRequestBody createGetVersionRequestBody() {
        return new GetVersionRequestBody();
    }

    /**
     * Create an instance of {@link SendOrderRequest }
     * 
     */
    public SendOrderRequest createSendOrderRequest() {
        return new SendOrderRequest();
    }

    /**
     * Create an instance of {@link FindItemsRequest }
     * 
     */
    public FindItemsRequest createFindItemsRequest() {
        return new FindItemsRequest();
    }

    /**
     * Create an instance of {@link AvailabilityState }
     * 
     */
    public AvailabilityState createAvailabilityState() {
        return new AvailabilityState();
    }

    /**
     * Create an instance of {@link ArrayOfAvailabilityState }
     * 
     */
    public ArrayOfAvailabilityState createArrayOfAvailabilityState() {
        return new ArrayOfAvailabilityState();
    }

    /**
     * Create an instance of {@link ArrayOfAdditionalIdentifier }
     * 
     */
    public ArrayOfAdditionalIdentifier createArrayOfAdditionalIdentifier() {
        return new ArrayOfAdditionalIdentifier();
    }

    /**
     * Create an instance of {@link Tour }
     * 
     */
    public Tour createTour() {
        return new Tour();
    }

    /**
     * Create an instance of {@link KeyValueItem }
     * 
     */
    public KeyValueItem createKeyValueItem() {
        return new KeyValueItem();
    }

    /**
     * Create an instance of {@link SendOrderReply }
     * 
     */
    public SendOrderReply createSendOrderReply() {
        return new SendOrderReply();
    }

    /**
     * Create an instance of {@link ArrayOfArticle }
     * 
     */
    public ArrayOfArticle createArrayOfArticle() {
        return new ArrayOfArticle();
    }

    /**
     * Create an instance of {@link ControlIndicator }
     * 
     */
    public ControlIndicator createControlIndicator() {
        return new ControlIndicator();
    }

    /**
     * Create an instance of {@link GetSessionRequestBody }
     * 
     */
    public GetSessionRequestBody createGetSessionRequestBody() {
        return new GetSessionRequestBody();
    }

    /**
     * Create an instance of {@link ErpInformation }
     * 
     */
    public ErpInformation createErpInformation() {
        return new ErpInformation();
    }

    /**
     * Create an instance of {@link MasterData }
     * 
     */
    public MasterData createMasterData() {
        return new MasterData();
    }

    /**
     * Create an instance of {@link ItemsCollection }
     * 
     */
    public ItemsCollection createItemsCollection() {
        return new ItemsCollection();
    }

    /**
     * Create an instance of {@link ArrayOfIcon }
     * 
     */
    public ArrayOfIcon createArrayOfIcon() {
        return new ArrayOfIcon();
    }

    /**
     * Create an instance of {@link GetErpInformationReply }
     * 
     */
    public GetErpInformationReply createGetErpInformationReply() {
        return new GetErpInformationReply();
    }

    /**
     * Create an instance of {@link GetItemsCollectionsRequestBody }
     * 
     */
    public GetItemsCollectionsRequestBody createGetItemsCollectionsRequestBody() {
        return new GetItemsCollectionsRequestBody();
    }

    /**
     * Create an instance of {@link ProductGroupTmf }
     * 
     */
    public ProductGroupTmf createProductGroupTmf() {
        return new ProductGroupTmf();
    }

    /**
     * Create an instance of {@link GetAccountInformationReply }
     * 
     */
    public GetAccountInformationReply createGetAccountInformationReply() {
        return new GetAccountInformationReply();
    }

    /**
     * Create an instance of {@link Credentials }
     * 
     */
    public Credentials createCredentials() {
        return new Credentials();
    }

    /**
     * Create an instance of {@link Memo }
     * 
     */
    public Memo createMemo() {
        return new Memo();
    }

    /**
     * Create an instance of {@link ArrayOfPrice }
     * 
     */
    public ArrayOfPrice createArrayOfPrice() {
        return new ArrayOfPrice();
    }

    /**
     * Create an instance of {@link ValidationMessage }
     * 
     */
    public ValidationMessage createValidationMessage() {
        return new ValidationMessage();
    }

    /**
     * Create an instance of {@link DeleteSessionResponseBody }
     * 
     */
    public DeleteSessionResponseBody createDeleteSessionResponseBody() {
        return new DeleteSessionResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfVehicle }
     * 
     */
    public ArrayOfVehicle createArrayOfVehicle() {
        return new ArrayOfVehicle();
    }

    /**
     * Create an instance of {@link GetErpInformationResponseBody }
     * 
     */
    public GetErpInformationResponseBody createGetErpInformationResponseBody() {
        return new GetErpInformationResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfEntityLink }
     * 
     */
    public ArrayOfEntityLink createArrayOfEntityLink() {
        return new ArrayOfEntityLink();
    }

    /**
     * Create an instance of {@link Vehicle }
     * 
     */
    public Vehicle createVehicle() {
        return new Vehicle();
    }

    /**
     * Create an instance of {@link ArrayOfOrder }
     * 
     */
    public ArrayOfOrder createArrayOfOrder() {
        return new ArrayOfOrder();
    }

    /**
     * Create an instance of {@link ServiceConfiguration }
     * 
     */
    public ServiceConfiguration createServiceConfiguration() {
        return new ServiceConfiguration();
    }

    /**
     * Create an instance of {@link DispatchType }
     * 
     */
    public DispatchType createDispatchType() {
        return new DispatchType();
    }

    /**
     * Create an instance of {@link ArrayOfOrderPosition }
     * 
     */
    public ArrayOfOrderPosition createArrayOfOrderPosition() {
        return new ArrayOfOrderPosition();
    }

    /**
     * Create an instance of {@link ArrayOfRepairTime }
     * 
     */
    public ArrayOfRepairTime createArrayOfRepairTime() {
        return new ArrayOfRepairTime();
    }

    /**
     * Create an instance of {@link ArrayOfTour }
     * 
     */
    public ArrayOfTour createArrayOfTour() {
        return new ArrayOfTour();
    }

    /**
     * Create an instance of {@link ArrayOfLinkedItemsCollection }
     * 
     */
    public ArrayOfLinkedItemsCollection createArrayOfLinkedItemsCollection() {
        return new ArrayOfLinkedItemsCollection();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link SendOrderRequestBody }
     * 
     */
    public SendOrderRequestBody createSendOrderRequestBody() {
        return new SendOrderRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfTextBlock }
     * 
     */
    public ArrayOfTextBlock createArrayOfTextBlock() {
        return new ArrayOfTextBlock();
    }

    /**
     * Create an instance of {@link GetNotificationReply }
     * 
     */
    public GetNotificationReply createGetNotificationReply() {
        return new GetNotificationReply();
    }

    /**
     * Create an instance of {@link GetSessionReply }
     * 
     */
    public GetSessionReply createGetSessionReply() {
        return new GetSessionReply();
    }

    /**
     * Create an instance of {@link OrderId }
     * 
     */
    public OrderId createOrderId() {
        return new OrderId();
    }

    /**
     * Create an instance of {@link GetVersionResponseBody }
     * 
     */
    public GetVersionResponseBody createGetVersionResponseBody() {
        return new GetVersionResponseBody();
    }

    /**
     * Create an instance of {@link SelectionListItem }
     * 
     */
    public SelectionListItem createSelectionListItem() {
        return new SelectionListItem();
    }

    /**
     * Create an instance of {@link ArrayOfAccountData }
     * 
     */
    public ArrayOfAccountData createArrayOfAccountData() {
        return new ArrayOfAccountData();
    }

    /**
     * Create an instance of {@link Price }
     * 
     */
    public Price createPrice() {
        return new Price();
    }

    /**
     * Create an instance of {@link OrderPosition }
     * 
     */
    public OrderPosition createOrderPosition() {
        return new OrderPosition();
    }

    /**
     * Create an instance of {@link BaseRequest }
     * 
     */
    public BaseRequest createBaseRequest() {
        return new BaseRequest();
    }

    /**
     * Create an instance of {@link ArrayOfMemo }
     * 
     */
    public ArrayOfMemo createArrayOfMemo() {
        return new ArrayOfMemo();
    }

    /**
     * Create an instance of {@link ArrayOfErpInformation }
     * 
     */
    public ArrayOfErpInformation createArrayOfErpInformation() {
        return new ArrayOfErpInformation();
    }

    /**
     * Create an instance of {@link FindItemsRequestBody }
     * 
     */
    public FindItemsRequestBody createFindItemsRequestBody() {
        return new FindItemsRequestBody();
    }

    /**
     * Create an instance of {@link SelectionList }
     * 
     */
    public SelectionList createSelectionList() {
        return new SelectionList();
    }

    /**
     * Create an instance of {@link Icon }
     * 
     */
    public Icon createIcon() {
        return new Icon();
    }

    /**
     * Create an instance of {@link SupplierTmf }
     * 
     */
    public SupplierTmf createSupplierTmf() {
        return new SupplierTmf();
    }

    /**
     * Create an instance of {@link TextBlock }
     * 
     */
    public TextBlock createTextBlock() {
        return new TextBlock();
    }

    /**
     * Create an instance of {@link ArrayOfProductGroupTmf }
     * 
     */
    public ArrayOfProductGroupTmf createArrayOfProductGroupTmf() {
        return new ArrayOfProductGroupTmf();
    }

    /**
     * Create an instance of {@link Warehouse }
     * 
     */
    public Warehouse createWarehouse() {
        return new Warehouse();
    }

    /**
     * Create an instance of {@link ArrayOfUserDefinedData }
     * 
     */
    public ArrayOfUserDefinedData createArrayOfUserDefinedData() {
        return new ArrayOfUserDefinedData();
    }

    /**
     * Create an instance of {@link GetItemsCollectionReply }
     * 
     */
    public GetItemsCollectionReply createGetItemsCollectionReply() {
        return new GetItemsCollectionReply();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link ArrayOfOrderId }
     * 
     */
    public ArrayOfOrderId createArrayOfOrderId() {
        return new ArrayOfOrderId();
    }

    /**
     * Create an instance of {@link GetNotificationResponseBody }
     * 
     */
    public GetNotificationResponseBody createGetNotificationResponseBody() {
        return new GetNotificationResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfCustomer }
     * 
     */
    public ArrayOfCustomer createArrayOfCustomer() {
        return new ArrayOfCustomer();
    }

    /**
     * Create an instance of {@link GetItemsCollectionRequest }
     * 
     */
    public GetItemsCollectionRequest createGetItemsCollectionRequest() {
        return new GetItemsCollectionRequest();
    }

    /**
     * Create an instance of {@link ArrayOfKeyValueItem }
     * 
     */
    public ArrayOfKeyValueItem createArrayOfKeyValueItem() {
        return new ArrayOfKeyValueItem();
    }

    /**
     * Create an instance of {@link SendOrderDocumentRequestBody }
     * 
     */
    public SendOrderDocumentRequestBody createSendOrderDocumentRequestBody() {
        return new SendOrderDocumentRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfSelectionList }
     * 
     */
    public ArrayOfSelectionList createArrayOfSelectionList() {
        return new ArrayOfSelectionList();
    }

    /**
     * Create an instance of {@link GetNotificationRequest }
     * 
     */
    public GetNotificationRequest createGetNotificationRequest() {
        return new GetNotificationRequest();
    }

    /**
     * Create an instance of {@link ArrayOfValidationMessage }
     * 
     */
    public ArrayOfValidationMessage createArrayOfValidationMessage() {
        return new ArrayOfValidationMessage();
    }

    /**
     * Create an instance of {@link UserDefinedData }
     * 
     */
    public UserDefinedData createUserDefinedData() {
        return new UserDefinedData();
    }

    /**
     * Create an instance of {@link ArrayOfWarehouse }
     * 
     */
    public ArrayOfWarehouse createArrayOfWarehouse() {
        return new ArrayOfWarehouse();
    }

    /**
     * Create an instance of {@link Quantity }
     * 
     */
    public Quantity createQuantity() {
        return new Quantity();
    }

    /**
     * Create an instance of {@link ArrayOfQuantity }
     * 
     */
    public ArrayOfQuantity createArrayOfQuantity() {
        return new ArrayOfQuantity();
    }

    /**
     * Create an instance of {@link OrderCollection }
     * 
     */
    public OrderCollection createOrderCollection() {
        return new OrderCollection();
    }

    /**
     * Create an instance of {@link ArticleTmf }
     * 
     */
    public ArticleTmf createArticleTmf() {
        return new ArticleTmf();
    }

    /**
     * Create an instance of {@link RepairTimesCostRate }
     * 
     */
    public RepairTimesCostRate createRepairTimesCostRate() {
        return new RepairTimesCostRate();
    }

    /**
     * Create an instance of {@link EntityLink }
     * 
     */
    public EntityLink createEntityLink() {
        return new EntityLink();
    }

    /**
     * Create an instance of {@link RepairTime }
     * 
     */
    public RepairTime createRepairTime() {
        return new RepairTime();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsResponse")
    public JAXBElement<FindItemsResponseBody> createFindItemsResponse(FindItemsResponseBody value) {
        return new JAXBElement<FindItemsResponseBody>(_FindItemsResponse_QNAME, FindItemsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticleTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfArticleTmf")
    public JAXBElement<ArrayOfArticleTmf> createArrayOfArticleTmf(ArrayOfArticleTmf value) {
        return new JAXBElement<ArrayOfArticleTmf>(_ArrayOfArticleTmf_QNAME, ArrayOfArticleTmf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Customer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Customer")
    public JAXBElement<Customer> createCustomer(Customer value) {
        return new JAXBElement<Customer>(_Customer_QNAME, Customer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Address")
    public JAXBElement<Address> createAddress(Address value) {
        return new JAXBElement<Address>(_Address_QNAME, Address.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDispatchType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfDispatchType")
    public JAXBElement<ArrayOfDispatchType> createArrayOfDispatchType(ArrayOfDispatchType value) {
        return new JAXBElement<ArrayOfDispatchType>(_ArrayOfDispatchType_QNAME, ArrayOfDispatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsReply")
    public JAXBElement<FindItemsReply> createFindItemsReply(FindItemsReply value) {
        return new JAXBElement<FindItemsReply>(_FindItemsReply_QNAME, FindItemsReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountDataRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountDataRequestBody")
    public JAXBElement<GetAccountDataRequestBody> createGetAccountDataRequestBody(GetAccountDataRequestBody value) {
        return new JAXBElement<GetAccountDataRequestBody>(_GetAccountDataRequestBody_QNAME, GetAccountDataRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountDataResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountDataResponse")
    public JAXBElement<GetAccountDataResponseBody> createGetAccountDataResponse(GetAccountDataResponseBody value) {
        return new JAXBElement<GetAccountDataResponseBody>(_GetAccountDataResponse_QNAME, GetAccountDataResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformation")
    public JAXBElement<GetErpInformationRequestBody> createGetErpInformation(GetErpInformationRequestBody value) {
        return new JAXBElement<GetErpInformationRequestBody>(_GetErpInformation_QNAME, GetErpInformationRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfItemsCollection")
    public JAXBElement<ArrayOfItemsCollection> createArrayOfItemsCollection(ArrayOfItemsCollection value) {
        return new JAXBElement<ArrayOfItemsCollection>(_ArrayOfItemsCollection_QNAME, ArrayOfItemsCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderResponseBody")
    public JAXBElement<SendOrderResponseBody> createSendOrderResponseBody(SendOrderResponseBody value) {
        return new JAXBElement<SendOrderResponseBody>(_SendOrderResponseBody_QNAME, SendOrderResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSessionRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DeleteSessionRequestBody")
    public JAXBElement<DeleteSessionRequestBody> createDeleteSessionRequestBody(DeleteSessionRequestBody value) {
        return new JAXBElement<DeleteSessionRequestBody>(_DeleteSessionRequestBody_QNAME, DeleteSessionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNotification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfNotification")
    public JAXBElement<ArrayOfNotification> createArrayOfNotification(ArrayOfNotification value) {
        return new JAXBElement<ArrayOfNotification>(_ArrayOfNotification_QNAME, ArrayOfNotification.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocumentRequest")
    public JAXBElement<SendOrderDocumentRequest> createSendOrderDocumentRequest(SendOrderDocumentRequest value) {
        return new JAXBElement<SendOrderDocumentRequest>(_SendOrderDocumentRequest_QNAME, SendOrderDocumentRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocumentResponseBody")
    public JAXBElement<SendOrderDocumentResponseBody> createSendOrderDocumentResponseBody(SendOrderDocumentResponseBody value) {
        return new JAXBElement<SendOrderDocumentResponseBody>(_SendOrderDocumentResponseBody_QNAME, SendOrderDocumentResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LinkedItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItemsCollection")
    public JAXBElement<LinkedItemsCollection> createLinkedItemsCollection(LinkedItemsCollection value) {
        return new JAXBElement<LinkedItemsCollection>(_LinkedItemsCollection_QNAME, LinkedItemsCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationRequestBody")
    public JAXBElement<GetNotificationRequestBody> createGetNotificationRequestBody(GetNotificationRequestBody value) {
        return new JAXBElement<GetNotificationRequestBody>(_GetNotificationRequestBody_QNAME, GetNotificationRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationRequest")
    public JAXBElement<GetErpInformationRequest> createGetErpInformationRequest(GetErpInformationRequest value) {
        return new JAXBElement<GetErpInformationRequest>(_GetErpInformationRequest_QNAME, GetErpInformationRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAddress")
    public JAXBElement<ArrayOfAddress> createArrayOfAddress(ArrayOfAddress value) {
        return new JAXBElement<ArrayOfAddress>(_ArrayOfAddress_QNAME, ArrayOfAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionsResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionsResponse")
    public JAXBElement<GetItemsCollectionsResponseBody> createGetItemsCollectionsResponse(GetItemsCollectionsResponseBody value) {
        return new JAXBElement<GetItemsCollectionsResponseBody>(_GetItemsCollectionsResponse_QNAME, GetItemsCollectionsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalIdentifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIdentifier")
    public JAXBElement<AdditionalIdentifier> createAdditionalIdentifier(AdditionalIdentifier value) {
        return new JAXBElement<AdditionalIdentifier>(_AdditionalIdentifier_QNAME, AdditionalIdentifier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "BaseResponse")
    public JAXBElement<BaseResponse> createBaseResponse(BaseResponse value) {
        return new JAXBElement<BaseResponse>(_BaseResponse_QNAME, BaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AccountData")
    public JAXBElement<AccountData> createAccountData(AccountData value) {
        return new JAXBElement<AccountData>(_AccountData_QNAME, AccountData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderRequest")
    public JAXBElement<SendOrderRequest> createSendOrderRequest(SendOrderRequest value) {
        return new JAXBElement<SendOrderRequest>(_SendOrderRequest_QNAME, SendOrderRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersion")
    public JAXBElement<GetVersionRequestBody> createGetVersion(GetVersionRequestBody value) {
        return new JAXBElement<GetVersionRequestBody>(_GetVersion_QNAME, GetVersionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsRequest")
    public JAXBElement<FindItemsRequest> createFindItemsRequest(FindItemsRequest value) {
        return new JAXBElement<FindItemsRequest>(_FindItemsRequest_QNAME, FindItemsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AvailabilityState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityState")
    public JAXBElement<AvailabilityState> createAvailabilityState(AvailabilityState value) {
        return new JAXBElement<AvailabilityState>(_AvailabilityState_QNAME, AvailabilityState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAvailabilityState")
    public JAXBElement<ArrayOfAvailabilityState> createArrayOfAvailabilityState(ArrayOfAvailabilityState value) {
        return new JAXBElement<ArrayOfAvailabilityState>(_ArrayOfAvailabilityState_QNAME, ArrayOfAvailabilityState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalIdentifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAdditionalIdentifier")
    public JAXBElement<ArrayOfAdditionalIdentifier> createArrayOfAdditionalIdentifier(ArrayOfAdditionalIdentifier value) {
        return new JAXBElement<ArrayOfAdditionalIdentifier>(_ArrayOfAdditionalIdentifier_QNAME, ArrayOfAdditionalIdentifier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tour }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour")
    public JAXBElement<Tour> createTour(Tour value) {
        return new JAXBElement<Tour>(_Tour_QNAME, Tour.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocumentResponse")
    public JAXBElement<SendOrderDocumentResponseBody> createSendOrderDocumentResponse(SendOrderDocumentResponseBody value) {
        return new JAXBElement<SendOrderDocumentResponseBody>(_SendOrderDocumentResponse_QNAME, SendOrderDocumentResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountDataResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountDataResponseBody")
    public JAXBElement<GetAccountDataResponseBody> createGetAccountDataResponseBody(GetAccountDataResponseBody value) {
        return new JAXBElement<GetAccountDataResponseBody>(_GetAccountDataResponseBody_QNAME, GetAccountDataResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSessionResponseBody")
    public JAXBElement<GetSessionResponseBody> createGetSessionResponseBody(GetSessionResponseBody value) {
        return new JAXBElement<GetSessionResponseBody>(_GetSessionResponseBody_QNAME, GetSessionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Article }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Article")
    public JAXBElement<Article> createArticle(Article value) {
        return new JAXBElement<Article>(_Article_QNAME, Article.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseDto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "BaseDto")
    public JAXBElement<BaseDto> createBaseDto(BaseDto value) {
        return new JAXBElement<BaseDto>(_BaseDto_QNAME, BaseDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfControlIndicator")
    public JAXBElement<ArrayOfControlIndicator> createArrayOfControlIndicator(ArrayOfControlIndicator value) {
        return new JAXBElement<ArrayOfControlIndicator>(_ArrayOfControlIndicator_QNAME, ArrayOfControlIndicator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Notification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Notification")
    public JAXBElement<Notification> createNotification(Notification value) {
        return new JAXBElement<Notification>(_Notification_QNAME, Notification.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfSelectionListItem")
    public JAXBElement<ArrayOfSelectionListItem> createArrayOfSelectionListItem(ArrayOfSelectionListItem value) {
        return new JAXBElement<ArrayOfSelectionListItem>(_ArrayOfSelectionListItem_QNAME, ArrayOfSelectionListItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErpInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpInformation")
    public JAXBElement<ErpInformation> createErpInformation(ErpInformation value) {
        return new JAXBElement<ErpInformation>(_ErpInformation_QNAME, ErpInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData")
    public JAXBElement<MasterData> createMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/STG.DVSE.TMC", name = "GetVersionRequestBody")
    public JAXBElement<GetVersionRequestBody> createGetVersionRequestBody(GetVersionRequestBody value) {
        return new JAXBElement<GetVersionRequestBody>(_GetVersionRequestBody_QNAME, GetVersionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ItemsCollection")
    public JAXBElement<ItemsCollection> createItemsCollection(ItemsCollection value) {
        return new JAXBElement<ItemsCollection>(_ItemsCollection_QNAME, ItemsCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIcon }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfIcon")
    public JAXBElement<ArrayOfIcon> createArrayOfIcon(ArrayOfIcon value) {
        return new JAXBElement<ArrayOfIcon>(_ArrayOfIcon_QNAME, ArrayOfIcon.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationReply")
    public JAXBElement<GetErpInformationReply> createGetErpInformationReply(GetErpInformationReply value) {
        return new JAXBElement<GetErpInformationReply>(_GetErpInformationReply_QNAME, GetErpInformationReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProductGroupTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ProductGroupTmf")
    public JAXBElement<ProductGroupTmf> createProductGroupTmf(ProductGroupTmf value) {
        return new JAXBElement<ProductGroupTmf>(_ProductGroupTmf_QNAME, ProductGroupTmf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountInformationReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountInformationReply")
    public JAXBElement<GetAccountInformationReply> createGetAccountInformationReply(GetAccountInformationReply value) {
        return new JAXBElement<GetAccountInformationReply>(_GetAccountInformationReply_QNAME, GetAccountInformationReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionsRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionsRequestBody")
    public JAXBElement<GetItemsCollectionsRequestBody> createGetItemsCollectionsRequestBody(GetItemsCollectionsRequestBody value) {
        return new JAXBElement<GetItemsCollectionsRequestBody>(_GetItemsCollectionsRequestBody_QNAME, GetItemsCollectionsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Credentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Credentials")
    public JAXBElement<Credentials> createCredentials(Credentials value) {
        return new JAXBElement<Credentials>(_Credentials_QNAME, Credentials.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Memo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memo")
    public JAXBElement<Memo> createMemo(Memo value) {
        return new JAXBElement<Memo>(_Memo_QNAME, Memo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "KeyValueItem")
    public JAXBElement<KeyValueItem> createKeyValueItem(KeyValueItem value) {
        return new JAXBElement<KeyValueItem>(_KeyValueItem_QNAME, KeyValueItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionsResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionsResponseBody")
    public JAXBElement<GetItemsCollectionsResponseBody> createGetItemsCollectionsResponseBody(GetItemsCollectionsResponseBody value) {
        return new JAXBElement<GetItemsCollectionsResponseBody>(_GetItemsCollectionsResponseBody_QNAME, GetItemsCollectionsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderReply")
    public JAXBElement<SendOrderReply> createSendOrderReply(SendOrderReply value) {
        return new JAXBElement<SendOrderReply>(_SendOrderReply_QNAME, SendOrderReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfArticle")
    public JAXBElement<ArrayOfArticle> createArrayOfArticle(ArrayOfArticle value) {
        return new JAXBElement<ArrayOfArticle>(_ArrayOfArticle_QNAME, ArrayOfArticle.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlIndicator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicator")
    public JAXBElement<ControlIndicator> createControlIndicator(ControlIndicator value) {
        return new JAXBElement<ControlIndicator>(_ControlIndicator_QNAME, ControlIndicator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSession")
    public JAXBElement<GetSessionRequestBody> createGetSession(GetSessionRequestBody value) {
        return new JAXBElement<GetSessionRequestBody>(_GetSession_QNAME, GetSessionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfEntityLink")
    public JAXBElement<ArrayOfEntityLink> createArrayOfEntityLink(ArrayOfEntityLink value) {
        return new JAXBElement<ArrayOfEntityLink>(_ArrayOfEntityLink_QNAME, ArrayOfEntityLink.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Vehicle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicle")
    public JAXBElement<Vehicle> createVehicle(Vehicle value) {
        return new JAXBElement<Vehicle>(_Vehicle_QNAME, Vehicle.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfOrder")
    public JAXBElement<ArrayOfOrder> createArrayOfOrder(ArrayOfOrder value) {
        return new JAXBElement<ArrayOfOrder>(_ArrayOfOrder_QNAME, ArrayOfOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DispatchType")
    public JAXBElement<DispatchType> createDispatchType(DispatchType value) {
        return new JAXBElement<DispatchType>(_DispatchType_QNAME, DispatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ServiceConfiguration")
    public JAXBElement<ServiceConfiguration> createServiceConfiguration(ServiceConfiguration value) {
        return new JAXBElement<ServiceConfiguration>(_ServiceConfiguration_QNAME, ServiceConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderPosition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfOrderPosition")
    public JAXBElement<ArrayOfOrderPosition> createArrayOfOrderPosition(ArrayOfOrderPosition value) {
        return new JAXBElement<ArrayOfOrderPosition>(_ArrayOfOrderPosition_QNAME, ArrayOfOrderPosition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRepairTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfRepairTime")
    public JAXBElement<ArrayOfRepairTime> createArrayOfRepairTime(ArrayOfRepairTime value) {
        return new JAXBElement<ArrayOfRepairTime>(_ArrayOfRepairTime_QNAME, ArrayOfRepairTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTour }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfTour")
    public JAXBElement<ArrayOfTour> createArrayOfTour(ArrayOfTour value) {
        return new JAXBElement<ArrayOfTour>(_ArrayOfTour_QNAME, ArrayOfTour.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSessionResponse")
    public JAXBElement<GetSessionResponseBody> createGetSessionResponse(GetSessionResponseBody value) {
        return new JAXBElement<GetSessionResponseBody>(_GetSessionResponse_QNAME, GetSessionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfPrice")
    public JAXBElement<ArrayOfPrice> createArrayOfPrice(ArrayOfPrice value) {
        return new JAXBElement<ArrayOfPrice>(_ArrayOfPrice_QNAME, ArrayOfPrice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidationMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ValidationMessage")
    public JAXBElement<ValidationMessage> createValidationMessage(ValidationMessage value) {
        return new JAXBElement<ValidationMessage>(_ValidationMessage_QNAME, ValidationMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSessionResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DeleteSessionResponseBody")
    public JAXBElement<DeleteSessionResponseBody> createDeleteSessionResponseBody(DeleteSessionResponseBody value) {
        return new JAXBElement<DeleteSessionResponseBody>(_DeleteSessionResponseBody_QNAME, DeleteSessionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVehicle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfVehicle")
    public JAXBElement<ArrayOfVehicle> createArrayOfVehicle(ArrayOfVehicle value) {
        return new JAXBElement<ArrayOfVehicle>(_ArrayOfVehicle_QNAME, ArrayOfVehicle.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderResponse")
    public JAXBElement<SendOrderResponseBody> createSendOrderResponse(SendOrderResponseBody value) {
        return new JAXBElement<SendOrderResponseBody>(_SendOrderResponse_QNAME, SendOrderResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationResponse")
    public JAXBElement<GetErpInformationResponseBody> createGetErpInformationResponse(GetErpInformationResponseBody value) {
        return new JAXBElement<GetErpInformationResponseBody>(_GetErpInformationResponse_QNAME, GetErpInformationResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationResponseBody")
    public JAXBElement<GetErpInformationResponseBody> createGetErpInformationResponseBody(GetErpInformationResponseBody value) {
        return new JAXBElement<GetErpInformationResponseBody>(_GetErpInformationResponseBody_QNAME, GetErpInformationResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAccountData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAccountData")
    public JAXBElement<ArrayOfAccountData> createArrayOfAccountData(ArrayOfAccountData value) {
        return new JAXBElement<ArrayOfAccountData>(_ArrayOfAccountData_QNAME, ArrayOfAccountData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Price }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Price")
    public JAXBElement<Price> createPrice(Price value) {
        return new JAXBElement<Price>(_Price_QNAME, Price.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderPosition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderPosition")
    public JAXBElement<OrderPosition> createOrderPosition(OrderPosition value) {
        return new JAXBElement<OrderPosition>(_OrderPosition_QNAME, OrderPosition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfMemo")
    public JAXBElement<ArrayOfMemo> createArrayOfMemo(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_ArrayOfMemo_QNAME, ArrayOfMemo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfErpInformation")
    public JAXBElement<ArrayOfErpInformation> createArrayOfErpInformation(ArrayOfErpInformation value) {
        return new JAXBElement<ArrayOfErpInformation>(_ArrayOfErpInformation_QNAME, ArrayOfErpInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderRequestBody")
    public JAXBElement<SendOrderRequestBody> createSendOrderRequestBody(SendOrderRequestBody value) {
        return new JAXBElement<SendOrderRequestBody>(_SendOrderRequestBody_QNAME, SendOrderRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItems")
    public JAXBElement<FindItemsRequestBody> createFindItems(FindItemsRequestBody value) {
        return new JAXBElement<FindItemsRequestBody>(_FindItems_QNAME, FindItemsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "BaseRequest")
    public JAXBElement<BaseRequest> createBaseRequest(BaseRequest value) {
        return new JAXBElement<BaseRequest>(_BaseRequest_QNAME, BaseRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Icon }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icon")
    public JAXBElement<Icon> createIcon(Icon value) {
        return new JAXBElement<Icon>(_Icon_QNAME, Icon.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionList")
    public JAXBElement<SelectionList> createSelectionList(SelectionList value) {
        return new JAXBElement<SelectionList>(_SelectionList_QNAME, SelectionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TextBlock }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TextBlock")
    public JAXBElement<TextBlock> createTextBlock(TextBlock value) {
        return new JAXBElement<TextBlock>(_TextBlock_QNAME, TextBlock.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProductGroupTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfProductGroupTmf")
    public JAXBElement<ArrayOfProductGroupTmf> createArrayOfProductGroupTmf(ArrayOfProductGroupTmf value) {
        return new JAXBElement<ArrayOfProductGroupTmf>(_ArrayOfProductGroupTmf_QNAME, ArrayOfProductGroupTmf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SupplierTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SupplierTmf")
    public JAXBElement<SupplierTmf> createSupplierTmf(SupplierTmf value) {
        return new JAXBElement<SupplierTmf>(_SupplierTmf_QNAME, SupplierTmf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotification")
    public JAXBElement<GetNotificationRequestBody> createGetNotification(GetNotificationRequestBody value) {
        return new JAXBElement<GetNotificationRequestBody>(_GetNotification_QNAME, GetNotificationRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrder")
    public JAXBElement<SendOrderRequestBody> createSendOrder(SendOrderRequestBody value) {
        return new JAXBElement<SendOrderRequestBody>(_SendOrder_QNAME, SendOrderRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLinkedItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfLinkedItemsCollection")
    public JAXBElement<ArrayOfLinkedItemsCollection> createArrayOfLinkedItemsCollection(ArrayOfLinkedItemsCollection value) {
        return new JAXBElement<ArrayOfLinkedItemsCollection>(_ArrayOfLinkedItemsCollection_QNAME, ArrayOfLinkedItemsCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Order }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Order")
    public JAXBElement<Order> createOrder(Order value) {
        return new JAXBElement<Order>(_Order_QNAME, Order.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTextBlock }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfTextBlock")
    public JAXBElement<ArrayOfTextBlock> createArrayOfTextBlock(ArrayOfTextBlock value) {
        return new JAXBElement<ArrayOfTextBlock>(_ArrayOfTextBlock_QNAME, ArrayOfTextBlock.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationReply")
    public JAXBElement<GetNotificationReply> createGetNotificationReply(GetNotificationReply value) {
        return new JAXBElement<GetNotificationReply>(_GetNotificationReply_QNAME, GetNotificationReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationRequestBody")
    public JAXBElement<GetErpInformationRequestBody> createGetErpInformationRequestBody(GetErpInformationRequestBody value) {
        return new JAXBElement<GetErpInformationRequestBody>(_GetErpInformationRequestBody_QNAME, GetErpInformationRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSessionReply")
    public JAXBElement<GetSessionReply> createGetSessionReply(GetSessionReply value) {
        return new JAXBElement<GetSessionReply>(_GetSessionReply_QNAME, GetSessionReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderId")
    public JAXBElement<OrderId> createOrderId(OrderId value) {
        return new JAXBElement<OrderId>(_OrderId_QNAME, OrderId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectionListItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionListItem")
    public JAXBElement<SelectionListItem> createSelectionListItem(SelectionListItem value) {
        return new JAXBElement<SelectionListItem>(_SelectionListItem_QNAME, SelectionListItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersionResponse")
    public JAXBElement<GetVersionResponseBody> createGetVersionResponse(GetVersionResponseBody value) {
        return new JAXBElement<GetVersionResponseBody>(_GetVersionResponse_QNAME, GetVersionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfKeyValueItem")
    public JAXBElement<ArrayOfKeyValueItem> createArrayOfKeyValueItem(ArrayOfKeyValueItem value) {
        return new JAXBElement<ArrayOfKeyValueItem>(_ArrayOfKeyValueItem_QNAME, ArrayOfKeyValueItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocument")
    public JAXBElement<SendOrderDocumentRequestBody> createSendOrderDocument(SendOrderDocumentRequestBody value) {
        return new JAXBElement<SendOrderDocumentRequestBody>(_SendOrderDocument_QNAME, SendOrderDocumentRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountDataRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountData")
    public JAXBElement<GetAccountDataRequestBody> createGetAccountData(GetAccountDataRequestBody value) {
        return new JAXBElement<GetAccountDataRequestBody>(_GetAccountData_QNAME, GetAccountDataRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersionResponseBody")
    public JAXBElement<GetVersionResponseBody> createGetVersionResponseBody(GetVersionResponseBody value) {
        return new JAXBElement<GetVersionResponseBody>(_GetVersionResponseBody_QNAME, GetVersionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfSelectionList")
    public JAXBElement<ArrayOfSelectionList> createArrayOfSelectionList(ArrayOfSelectionList value) {
        return new JAXBElement<ArrayOfSelectionList>(_ArrayOfSelectionList_QNAME, ArrayOfSelectionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsRequestBody")
    public JAXBElement<FindItemsRequestBody> createFindItemsRequestBody(FindItemsRequestBody value) {
        return new JAXBElement<FindItemsRequestBody>(_FindItemsRequestBody_QNAME, FindItemsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSessionRequestBody")
    public JAXBElement<GetSessionRequestBody> createGetSessionRequestBody(GetSessionRequestBody value) {
        return new JAXBElement<GetSessionRequestBody>(_GetSessionRequestBody_QNAME, GetSessionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsResponseBody")
    public JAXBElement<FindItemsResponseBody> createFindItemsResponseBody(FindItemsResponseBody value) {
        return new JAXBElement<FindItemsResponseBody>(_FindItemsResponseBody_QNAME, FindItemsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationRequest")
    public JAXBElement<GetNotificationRequest> createGetNotificationRequest(GetNotificationRequest value) {
        return new JAXBElement<GetNotificationRequest>(_GetNotificationRequest_QNAME, GetNotificationRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "User")
    public JAXBElement<User> createUser(User value) {
        return new JAXBElement<User>(_User_QNAME, User.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Warehouse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Warehouse")
    public JAXBElement<Warehouse> createWarehouse(Warehouse value) {
        return new JAXBElement<Warehouse>(_Warehouse_QNAME, Warehouse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfUserDefinedData")
    public JAXBElement<ArrayOfUserDefinedData> createArrayOfUserDefinedData(ArrayOfUserDefinedData value) {
        return new JAXBElement<ArrayOfUserDefinedData>(_ArrayOfUserDefinedData_QNAME, ArrayOfUserDefinedData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionReply")
    public JAXBElement<GetItemsCollectionReply> createGetItemsCollectionReply(GetItemsCollectionReply value) {
        return new JAXBElement<GetItemsCollectionReply>(_GetItemsCollectionReply_QNAME, GetItemsCollectionReply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfOrderId")
    public JAXBElement<ArrayOfOrderId> createArrayOfOrderId(ArrayOfOrderId value) {
        return new JAXBElement<ArrayOfOrderId>(_ArrayOfOrderId_QNAME, ArrayOfOrderId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionsRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollections")
    public JAXBElement<GetItemsCollectionsRequestBody> createGetItemsCollections(GetItemsCollectionsRequestBody value) {
        return new JAXBElement<GetItemsCollectionsRequestBody>(_GetItemsCollections_QNAME, GetItemsCollectionsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationResponseBody")
    public JAXBElement<GetNotificationResponseBody> createGetNotificationResponseBody(GetNotificationResponseBody value) {
        return new JAXBElement<GetNotificationResponseBody>(_GetNotificationResponseBody_QNAME, GetNotificationResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfCustomer")
    public JAXBElement<ArrayOfCustomer> createArrayOfCustomer(ArrayOfCustomer value) {
        return new JAXBElement<ArrayOfCustomer>(_ArrayOfCustomer_QNAME, ArrayOfCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionRequest")
    public JAXBElement<GetItemsCollectionRequest> createGetItemsCollectionRequest(GetItemsCollectionRequest value) {
        return new JAXBElement<GetItemsCollectionRequest>(_GetItemsCollectionRequest_QNAME, GetItemsCollectionRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationResponse")
    public JAXBElement<GetNotificationResponseBody> createGetNotificationResponse(GetNotificationResponseBody value) {
        return new JAXBElement<GetNotificationResponseBody>(_GetNotificationResponse_QNAME, GetNotificationResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection")
    public JAXBElement<OrderCollection> createOrderCollection(OrderCollection value) {
        return new JAXBElement<OrderCollection>(_OrderCollection_QNAME, OrderCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticleTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleTmf")
    public JAXBElement<ArticleTmf> createArticleTmf(ArticleTmf value) {
        return new JAXBElement<ArticleTmf>(_ArticleTmf_QNAME, ArticleTmf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQuantity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfQuantity")
    public JAXBElement<ArrayOfQuantity> createArrayOfQuantity(ArrayOfQuantity value) {
        return new JAXBElement<ArrayOfQuantity>(_ArrayOfQuantity_QNAME, ArrayOfQuantity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSessionRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DeleteSession")
    public JAXBElement<DeleteSessionRequestBody> createDeleteSession(DeleteSessionRequestBody value) {
        return new JAXBElement<DeleteSessionRequestBody>(_DeleteSession_QNAME, DeleteSessionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepairTimesCostRate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "RepairTimesCostRate")
    public JAXBElement<RepairTimesCostRate> createRepairTimesCostRate(RepairTimesCostRate value) {
        return new JAXBElement<RepairTimesCostRate>(_RepairTimesCostRate_QNAME, RepairTimesCostRate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "EntityLink")
    public JAXBElement<EntityLink> createEntityLink(EntityLink value) {
        return new JAXBElement<EntityLink>(_EntityLink_QNAME, EntityLink.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteSessionResponseBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DeleteSessionResponse")
    public JAXBElement<DeleteSessionResponseBody> createDeleteSessionResponse(DeleteSessionResponseBody value) {
        return new JAXBElement<DeleteSessionResponseBody>(_DeleteSessionResponse_QNAME, DeleteSessionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EMasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "EMasterDataType")
    public JAXBElement<EMasterDataType> createEMasterDataType(EMasterDataType value) {
        return new JAXBElement<EMasterDataType>(_EMasterDataType_QNAME, EMasterDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepairTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "RepairTime")
    public JAXBElement<RepairTime> createRepairTime(RepairTime value) {
        return new JAXBElement<RepairTime>(_RepairTime_QNAME, RepairTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfValidationMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfValidationMessage")
    public JAXBElement<ArrayOfValidationMessage> createArrayOfValidationMessage(ArrayOfValidationMessage value) {
        return new JAXBElement<ArrayOfValidationMessage>(_ArrayOfValidationMessage_QNAME, ArrayOfValidationMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserDefinedData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UserDefinedData")
    public JAXBElement<UserDefinedData> createUserDefinedData(UserDefinedData value) {
        return new JAXBElement<UserDefinedData>(_UserDefinedData_QNAME, UserDefinedData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfWarehouse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfWarehouse")
    public JAXBElement<ArrayOfWarehouse> createArrayOfWarehouse(ArrayOfWarehouse value) {
        return new JAXBElement<ArrayOfWarehouse>(_ArrayOfWarehouse_QNAME, ArrayOfWarehouse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Quantity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Quantity")
    public JAXBElement<Quantity> createQuantity(Quantity value) {
        return new JAXBElement<Quantity>(_Quantity_QNAME, Quantity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentRequestBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocumentRequestBody")
    public JAXBElement<SendOrderDocumentRequestBody> createSendOrderDocumentRequestBody(SendOrderDocumentRequestBody value) {
        return new JAXBElement<SendOrderDocumentRequestBody>(_SendOrderDocumentRequestBody_QNAME, SendOrderDocumentRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = GetErpInformationRequest.class)
    public JAXBElement<MasterData> createGetErpInformationRequestMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, GetErpInformationRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpArticleInformation", scope = GetErpInformationRequest.class)
    public JAXBElement<ArrayOfErpInformation> createGetErpInformationRequestErpArticleInformation(ArrayOfErpInformation value) {
        return new JAXBElement<ArrayOfErpInformation>(_GetErpInformationRequestErpArticleInformation_QNAME, ArrayOfErpInformation.class, GetErpInformationRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Salt", scope = Credentials.class)
    public JAXBElement<String> createCredentialsSalt(String value) {
        return new JAXBElement<String>(_CredentialsSalt_QNAME, String.class, Credentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SecurityToken", scope = Credentials.class)
    public JAXBElement<String> createCredentialsSecurityToken(String value) {
        return new JAXBElement<String>(_CredentialsSecurityToken_QNAME, String.class, Credentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SalesAdvisorCredentials", scope = Credentials.class)
    public JAXBElement<User> createCredentialsSalesAdvisorCredentials(User value) {
        return new JAXBElement<User>(_CredentialsSalesAdvisorCredentials_QNAME, User.class, Credentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExternalIdentityProviderId", scope = Credentials.class)
    public JAXBElement<String> createCredentialsExternalIdentityProviderId(String value) {
        return new JAXBElement<String>(_CredentialsExternalIdentityProviderId_QNAME, String.class, Credentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CatalogUserCredentials", scope = Credentials.class)
    public JAXBElement<User> createCredentialsCatalogUserCredentials(User value) {
        return new JAXBElement<User>(_CredentialsCatalogUserCredentials_QNAME, User.class, Credentials.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepairTimesCostRate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TypeOfCostRate", scope = RepairTime.class)
    public JAXBElement<RepairTimesCostRate> createRepairTimeTypeOfCostRate(RepairTimesCostRate value) {
        return new JAXBElement<RepairTimesCostRate>(_RepairTimeTypeOfCostRate_QNAME, RepairTimesCostRate.class, RepairTime.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = RepairTime.class)
    public JAXBElement<String> createRepairTimeDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, RepairTime.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Price }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Price", scope = RepairTime.class)
    public JAXBElement<Price> createRepairTimePrice(Price value) {
        return new JAXBElement<Price>(_Price_QNAME, Price.class, RepairTime.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = RepairTime.class)
    public JAXBElement<String> createRepairTimeId(String value) {
        return new JAXBElement<String>(_RepairTimeId_QNAME, String.class, RepairTime.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = SendOrderRequestBody.class)
    public JAXBElement<SendOrderRequest> createSendOrderRequestBodyRequest(SendOrderRequest value) {
        return new JAXBElement<SendOrderRequest>(_SendOrderRequestBodyRequest_QNAME, SendOrderRequest.class, SendOrderRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = Article.class)
    public JAXBElement<String> createArticleDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, Article.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = Article.class)
    public JAXBElement<String> createArticleId(String value) {
        return new JAXBElement<String>(_RepairTimeId_QNAME, String.class, Article.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleIdErp", scope = Article.class)
    public JAXBElement<String> createArticleArticleIdErp(String value) {
        return new JAXBElement<String>(_ArticleArticleIdErp_QNAME, String.class, Article.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = GetItemsCollectionsRequestBody.class)
    public JAXBElement<GetItemsCollectionRequest> createGetItemsCollectionsRequestBodyRequest(GetItemsCollectionRequest value) {
        return new JAXBElement<GetItemsCollectionRequest>(_SendOrderRequestBodyRequest_QNAME, GetItemsCollectionRequest.class, GetItemsCollectionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntity", scope = ControlIndicator.class)
    public JAXBElement<EntityLink> createControlIndicatorLinkedEntity(EntityLink value) {
        return new JAXBElement<EntityLink>(_ControlIndicatorLinkedEntity_QNAME, EntityLink.class, ControlIndicator.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Parameters", scope = ControlIndicator.class)
    public JAXBElement<ArrayOfKeyValueItem> createControlIndicatorParameters(ArrayOfKeyValueItem value) {
        return new JAXBElement<ArrayOfKeyValueItem>(_ControlIndicatorParameters_QNAME, ArrayOfKeyValueItem.class, ControlIndicator.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection", scope = SendOrderRequest.class)
    public JAXBElement<OrderCollection> createSendOrderRequestOrderCollection(OrderCollection value) {
        return new JAXBElement<OrderCollection>(_OrderCollection_QNAME, OrderCollection.class, SendOrderRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = SendOrderRequest.class)
    public JAXBElement<MasterData> createSendOrderRequestMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, SendOrderRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNotification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = GetNotificationReply.class)
    public JAXBElement<ArrayOfNotification> createGetNotificationReplyItems(ArrayOfNotification value) {
        return new JAXBElement<ArrayOfNotification>(_GetNotificationReplyItems_QNAME, ArrayOfNotification.class, GetNotificationReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationResult", scope = GetErpInformationResponseBody.class)
    public JAXBElement<GetErpInformationReply> createGetErpInformationResponseBodyGetErpInformationResult(GetErpInformationReply value) {
        return new JAXBElement<GetErpInformationReply>(_GetErpInformationResponseBodyGetErpInformationResult_QNAME, GetErpInformationReply.class, GetErpInformationResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = LinkedItemsCollection.class)
    public JAXBElement<String> createLinkedItemsCollectionDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, LinkedItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = LinkedItemsCollection.class)
    public JAXBElement<ArrayOfMemo> createLinkedItemsCollectionMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, LinkedItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItems", scope = LinkedItemsCollection.class)
    public JAXBElement<ArrayOfErpInformation> createLinkedItemsCollectionLinkedItems(ArrayOfErpInformation value) {
        return new JAXBElement<ArrayOfErpInformation>(_LinkedItemsCollectionLinkedItems_QNAME, ArrayOfErpInformation.class, LinkedItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlIndicator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicator", scope = LinkedItemsCollection.class)
    public JAXBElement<ControlIndicator> createLinkedItemsCollectionControlIndicator(ControlIndicator value) {
        return new JAXBElement<ControlIndicator>(_ControlIndicator_QNAME, ControlIndicator.class, LinkedItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AdditionalIdentifier.class)
    public JAXBElement<String> createAdditionalIdentifierDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, AdditionalIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = AdditionalIdentifier.class)
    public JAXBElement<String> createAdditionalIdentifierValue(String value) {
        return new JAXBElement<String>(_AdditionalIdentifierValue_QNAME, String.class, AdditionalIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = GetItemsCollectionReply.class)
    public JAXBElement<ArrayOfItemsCollection> createGetItemsCollectionReplyItems(ArrayOfItemsCollection value) {
        return new JAXBElement<ArrayOfItemsCollection>(_GetNotificationReplyItems_QNAME, ArrayOfItemsCollection.class, GetItemsCollectionReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderResult", scope = SendOrderResponseBody.class)
    public JAXBElement<SendOrderReply> createSendOrderResponseBodySendOrderResult(SendOrderReply value) {
        return new JAXBElement<SendOrderReply>(_SendOrderResponseBodySendOrderResult_QNAME, SendOrderReply.class, SendOrderResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersionResult", scope = GetVersionResponseBody.class)
    public JAXBElement<String> createGetVersionResponseBodyGetVersionResult(String value) {
        return new JAXBElement<String>(_GetVersionResponseBodyGetVersionResult_QNAME, String.class, GetVersionResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = GetAccountDataRequestBody.class)
    public JAXBElement<BaseRequest> createGetAccountDataRequestBodyRequest(BaseRequest value) {
        return new JAXBElement<BaseRequest>(_SendOrderRequestBodyRequest_QNAME, BaseRequest.class, GetAccountDataRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = FindItemsReply.class)
    public JAXBElement<MasterData> createFindItemsReplyMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, FindItemsReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpArticleInformation", scope = FindItemsReply.class)
    public JAXBElement<ArrayOfErpInformation> createFindItemsReplyErpArticleInformation(ArrayOfErpInformation value) {
        return new JAXBElement<ArrayOfErpInformation>(_GetErpInformationRequestErpArticleInformation_QNAME, ArrayOfErpInformation.class, FindItemsReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Item", scope = ErpInformation.class)
    public JAXBElement<EntityLink> createErpInformationItem(EntityLink value) {
        return new JAXBElement<EntityLink>(_ErpInformationItem_QNAME, EntityLink.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Prices", scope = ErpInformation.class)
    public JAXBElement<ArrayOfPrice> createErpInformationPrices(ArrayOfPrice value) {
        return new JAXBElement<ArrayOfPrice>(_ErpInformationPrices_QNAME, ArrayOfPrice.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalDescription", scope = ErpInformation.class)
    public JAXBElement<String> createErpInformationAdditionalDescription(String value) {
        return new JAXBElement<String>(_ErpInformationAdditionalDescription_QNAME, String.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLinkedItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItemsCollections", scope = ErpInformation.class)
    public JAXBElement<ArrayOfLinkedItemsCollection> createErpInformationLinkedItemsCollections(ArrayOfLinkedItemsCollection value) {
        return new JAXBElement<ArrayOfLinkedItemsCollection>(_ErpInformationLinkedItemsCollections_QNAME, ArrayOfLinkedItemsCollection.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicators", scope = ErpInformation.class)
    public JAXBElement<ArrayOfControlIndicator> createErpInformationControlIndicators(ArrayOfControlIndicator value) {
        return new JAXBElement<ArrayOfControlIndicator>(_ErpInformationControlIndicators_QNAME, ArrayOfControlIndicator.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicle", scope = ErpInformation.class)
    public JAXBElement<EntityLink> createErpInformationVehicle(EntityLink value) {
        return new JAXBElement<EntityLink>(_Vehicle_QNAME, EntityLink.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalDescriptionExtended", scope = ErpInformation.class)
    public JAXBElement<String> createErpInformationAdditionalDescriptionExtended(String value) {
        return new JAXBElement<String>(_ErpInformationAdditionalDescriptionExtended_QNAME, String.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = ErpInformation.class)
    public JAXBElement<ArrayOfMemo> createErpInformationMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfWarehouse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Warehouses", scope = ErpInformation.class)
    public JAXBElement<ArrayOfWarehouse> createErpInformationWarehouses(ArrayOfWarehouse value) {
        return new JAXBElement<ArrayOfWarehouse>(_ErpInformationWarehouses_QNAME, ArrayOfWarehouse.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityState", scope = ErpInformation.class)
    public JAXBElement<EntityLink> createErpInformationAvailabilityState(EntityLink value) {
        return new JAXBElement<EntityLink>(_AvailabilityState_QNAME, EntityLink.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = ErpInformation.class)
    public JAXBElement<EntityLink> createErpInformationTour(EntityLink value) {
        return new JAXBElement<EntityLink>(_Tour_QNAME, EntityLink.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StatusInformation", scope = ErpInformation.class)
    public JAXBElement<ArrayOfKeyValueItem> createErpInformationStatusInformation(ArrayOfKeyValueItem value) {
        return new JAXBElement<ArrayOfKeyValueItem>(_ErpInformationStatusInformation_QNAME, ArrayOfKeyValueItem.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SpecialIcons", scope = ErpInformation.class)
    public JAXBElement<ArrayOfEntityLink> createErpInformationSpecialIcons(ArrayOfEntityLink value) {
        return new JAXBElement<ArrayOfEntityLink>(_ErpInformationSpecialIcons_QNAME, ArrayOfEntityLink.class, ErpInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalIdentifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIdentifiers", scope = BaseDto.class)
    public JAXBElement<ArrayOfAdditionalIdentifier> createBaseDtoAdditionalIdentifiers(ArrayOfAdditionalIdentifier value) {
        return new JAXBElement<ArrayOfAdditionalIdentifier>(_BaseDtoAdditionalIdentifiers_QNAME, ArrayOfAdditionalIdentifier.class, BaseDto.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection", scope = SendOrderReply.class)
    public JAXBElement<OrderCollection> createSendOrderReplyOrderCollection(OrderCollection value) {
        return new JAXBElement<OrderCollection>(_OrderCollection_QNAME, OrderCollection.class, SendOrderReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = SendOrderReply.class)
    public JAXBElement<MasterData> createSendOrderReplyMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, SendOrderReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = ItemsCollection.class)
    public JAXBElement<String> createItemsCollectionDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, ItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItems", scope = ItemsCollection.class)
    public JAXBElement<ArrayOfEntityLink> createItemsCollectionLinkedItems(ArrayOfEntityLink value) {
        return new JAXBElement<ArrayOfEntityLink>(_LinkedItemsCollectionLinkedItems_QNAME, ArrayOfEntityLink.class, ItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleIdErp", scope = ItemsCollection.class)
    public JAXBElement<String> createItemsCollectionArticleIdErp(String value) {
        return new JAXBElement<String>(_ArticleArticleIdErp_QNAME, String.class, ItemsCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Item", scope = OrderPosition.class)
    public JAXBElement<EntityLink> createOrderPositionItem(EntityLink value) {
        return new JAXBElement<EntityLink>(_ErpInformationItem_QNAME, EntityLink.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Prices", scope = OrderPosition.class)
    public JAXBElement<ArrayOfPrice> createOrderPositionPrices(ArrayOfPrice value) {
        return new JAXBElement<ArrayOfPrice>(_ErpInformationPrices_QNAME, ArrayOfPrice.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicle", scope = OrderPosition.class)
    public JAXBElement<EntityLink> createOrderPositionVehicle(EntityLink value) {
        return new JAXBElement<EntityLink>(_Vehicle_QNAME, EntityLink.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UserDefinedData", scope = OrderPosition.class)
    public JAXBElement<ArrayOfUserDefinedData> createOrderPositionUserDefinedData(ArrayOfUserDefinedData value) {
        return new JAXBElement<ArrayOfUserDefinedData>(_UserDefinedData_QNAME, ArrayOfUserDefinedData.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StatusInformation", scope = OrderPosition.class)
    public JAXBElement<KeyValueItem> createOrderPositionStatusInformation(KeyValueItem value) {
        return new JAXBElement<KeyValueItem>(_ErpInformationStatusInformation_QNAME, KeyValueItem.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionLists", scope = OrderPosition.class)
    public JAXBElement<ArrayOfSelectionList> createOrderPositionSelectionLists(ArrayOfSelectionList value) {
        return new JAXBElement<ArrayOfSelectionList>(_OrderPositionSelectionLists_QNAME, ArrayOfSelectionList.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = OrderPosition.class)
    public JAXBElement<EntityLink> createOrderPositionTour(EntityLink value) {
        return new JAXBElement<EntityLink>(_Tour_QNAME, EntityLink.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntities", scope = OrderPosition.class)
    public JAXBElement<ArrayOfEntityLink> createOrderPositionLinkedEntities(ArrayOfEntityLink value) {
        return new JAXBElement<ArrayOfEntityLink>(_OrderPositionLinkedEntities_QNAME, ArrayOfEntityLink.class, OrderPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = Price.class)
    public JAXBElement<String> createPriceDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, Price.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = Price.class)
    public JAXBElement<ArrayOfMemo> createPriceMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, Price.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencyCode_Iso_4217", scope = Price.class)
    public JAXBElement<String> createPriceCurrencyCodeIso4217(String value) {
        return new JAXBElement<String>(_PriceCurrencyCodeIso4217_QNAME, String.class, Price.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencySymbol", scope = Price.class)
    public JAXBElement<String> createPriceCurrencySymbol(String value) {
        return new JAXBElement<String>(_PriceCurrencySymbol_QNAME, String.class, Price.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocumentResult", scope = SendOrderDocumentResponseBody.class)
    public JAXBElement<BaseResponse> createSendOrderDocumentResponseBodySendOrderDocumentResult(BaseResponse value) {
        return new JAXBElement<BaseResponse>(_SendOrderDocumentResponseBodySendOrderDocumentResult_QNAME, BaseResponse.class, SendOrderDocumentResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicators", scope = BaseResponse.class)
    public JAXBElement<ArrayOfControlIndicator> createBaseResponseControlIndicators(ArrayOfControlIndicator value) {
        return new JAXBElement<ArrayOfControlIndicator>(_ErpInformationControlIndicators_QNAME, ArrayOfControlIndicator.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErrorCode", scope = BaseResponse.class)
    public JAXBElement<String> createBaseResponseErrorCode(String value) {
        return new JAXBElement<String>(_BaseResponseErrorCode_QNAME, String.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNotification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Notifications", scope = BaseResponse.class)
    public JAXBElement<ArrayOfNotification> createBaseResponseNotifications(ArrayOfNotification value) {
        return new JAXBElement<ArrayOfNotification>(_BaseResponseNotifications_QNAME, ArrayOfNotification.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErrorMessage", scope = BaseResponse.class)
    public JAXBElement<String> createBaseResponseErrorMessage(String value) {
        return new JAXBElement<String>(_BaseResponseErrorMessage_QNAME, String.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfValidationMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ValidationMessages", scope = BaseResponse.class)
    public JAXBElement<ArrayOfValidationMessage> createBaseResponseValidationMessages(ArrayOfValidationMessage value) {
        return new JAXBElement<ArrayOfValidationMessage>(_BaseResponseValidationMessages_QNAME, ArrayOfValidationMessage.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIds", scope = BaseResponse.class)
    public JAXBElement<ArrayOfKeyValueItem> createBaseResponseAdditionalIds(ArrayOfKeyValueItem value) {
        return new JAXBElement<ArrayOfKeyValueItem>(_BaseResponseAdditionalIds_QNAME, ArrayOfKeyValueItem.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Credentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Credentials", scope = BaseResponse.class)
    public JAXBElement<Credentials> createBaseResponseCredentials(Credentials value) {
        return new JAXBElement<Credentials>(_Credentials_QNAME, Credentials.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = Icon.class)
    public JAXBElement<String> createIconDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, Icon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkUrl", scope = Icon.class)
    public JAXBElement<String> createIconLinkUrl(String value) {
        return new JAXBElement<String>(_IconLinkUrl_QNAME, String.class, Icon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Url", scope = Icon.class)
    public JAXBElement<String> createIconUrl(String value) {
        return new JAXBElement<String>(_IconUrl_QNAME, String.class, Icon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TopmotiveId", scope = Icon.class)
    public JAXBElement<String> createIconTopmotiveId(String value) {
        return new JAXBElement<String>(_IconTopmotiveId_QNAME, String.class, Icon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = SelectionListItem.class)
    public JAXBElement<ArrayOfMemo> createSelectionListItemMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, SelectionListItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = SelectionListItem.class)
    public JAXBElement<String> createSelectionListItemText(String value) {
        return new JAXBElement<String>(_SelectionListItemText_QNAME, String.class, SelectionListItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExpectedDelivery", scope = Tour.class)
    public JAXBElement<String> createTourExpectedDelivery(String value) {
        return new JAXBElement<String>(_TourExpectedDelivery_QNAME, String.class, Tour.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = Tour.class)
    public JAXBElement<String> createTourDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, Tour.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = Tour.class)
    public JAXBElement<String> createTourId(String value) {
        return new JAXBElement<String>(_RepairTimeId_QNAME, String.class, Tour.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfItemsCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ItemsCollections", scope = MasterData.class)
    public JAXBElement<ArrayOfItemsCollection> createMasterDataItemsCollections(ArrayOfItemsCollection value) {
        return new JAXBElement<ArrayOfItemsCollection>(_MasterDataItemsCollections_QNAME, ArrayOfItemsCollection.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Addresses", scope = MasterData.class)
    public JAXBElement<ArrayOfAddress> createMasterDataAddresses(ArrayOfAddress value) {
        return new JAXBElement<ArrayOfAddress>(_MasterDataAddresses_QNAME, ArrayOfAddress.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticleTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleTmfs", scope = MasterData.class)
    public JAXBElement<ArrayOfArticleTmf> createMasterDataArticleTmfs(ArrayOfArticleTmf value) {
        return new JAXBElement<ArrayOfArticleTmf>(_MasterDataArticleTmfs_QNAME, ArrayOfArticleTmf.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Customers", scope = MasterData.class)
    public JAXBElement<ArrayOfCustomer> createMasterDataCustomers(ArrayOfCustomer value) {
        return new JAXBElement<ArrayOfCustomer>(_MasterDataCustomers_QNAME, ArrayOfCustomer.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRepairTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "RepairTimes", scope = MasterData.class)
    public JAXBElement<ArrayOfRepairTime> createMasterDataRepairTimes(ArrayOfRepairTime value) {
        return new JAXBElement<ArrayOfRepairTime>(_MasterDataRepairTimes_QNAME, ArrayOfRepairTime.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionListItems", scope = MasterData.class)
    public JAXBElement<ArrayOfSelectionListItem> createMasterDataSelectionListItems(ArrayOfSelectionListItem value) {
        return new JAXBElement<ArrayOfSelectionListItem>(_MasterDataSelectionListItems_QNAME, ArrayOfSelectionListItem.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityStates", scope = MasterData.class)
    public JAXBElement<ArrayOfAvailabilityState> createMasterDataAvailabilityStates(ArrayOfAvailabilityState value) {
        return new JAXBElement<ArrayOfAvailabilityState>(_MasterDataAvailabilityStates_QNAME, ArrayOfAvailabilityState.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDispatchType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DispatchTypes", scope = MasterData.class)
    public JAXBElement<ArrayOfDispatchType> createMasterDataDispatchTypes(ArrayOfDispatchType value) {
        return new JAXBElement<ArrayOfDispatchType>(_MasterDataDispatchTypes_QNAME, ArrayOfDispatchType.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Articles", scope = MasterData.class)
    public JAXBElement<ArrayOfArticle> createMasterDataArticles(ArrayOfArticle value) {
        return new JAXBElement<ArrayOfArticle>(_MasterDataArticles_QNAME, ArrayOfArticle.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIcon }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icons", scope = MasterData.class)
    public JAXBElement<ArrayOfIcon> createMasterDataIcons(ArrayOfIcon value) {
        return new JAXBElement<ArrayOfIcon>(_MasterDataIcons_QNAME, ArrayOfIcon.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTour }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tours", scope = MasterData.class)
    public JAXBElement<ArrayOfTour> createMasterDataTours(ArrayOfTour value) {
        return new JAXBElement<ArrayOfTour>(_MasterDataTours_QNAME, ArrayOfTour.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTextBlock }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TextBlocks", scope = MasterData.class)
    public JAXBElement<ArrayOfTextBlock> createMasterDataTextBlocks(ArrayOfTextBlock value) {
        return new JAXBElement<ArrayOfTextBlock>(_MasterDataTextBlocks_QNAME, ArrayOfTextBlock.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVehicle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicles", scope = MasterData.class)
    public JAXBElement<ArrayOfVehicle> createMasterDataVehicles(ArrayOfVehicle value) {
        return new JAXBElement<ArrayOfVehicle>(_MasterDataVehicles_QNAME, ArrayOfVehicle.class, MasterData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = SelectionList.class)
    public JAXBElement<ArrayOfMemo> createSelectionListMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, SelectionList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = SelectionList.class)
    public JAXBElement<String> createSelectionListId(String value) {
        return new JAXBElement<String>(_RepairTimeId_QNAME, String.class, SelectionList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = SelectionList.class)
    public JAXBElement<String> createSelectionListText(String value) {
        return new JAXBElement<String>(_SelectionListItemText_QNAME, String.class, SelectionList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = SelectionList.class)
    public JAXBElement<ArrayOfEntityLink> createSelectionListItems(ArrayOfEntityLink value) {
        return new JAXBElement<ArrayOfEntityLink>(_GetNotificationReplyItems_QNAME, ArrayOfEntityLink.class, SelectionList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSessionResult", scope = GetSessionResponseBody.class)
    public JAXBElement<GetSessionReply> createGetSessionResponseBodyGetSessionResult(GetSessionReply value) {
        return new JAXBElement<GetSessionReply>(_GetSessionResponseBodyGetSessionResult_QNAME, GetSessionReply.class, GetSessionResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleIdSupplier", scope = ArticleTmf.class)
    public JAXBElement<String> createArticleTmfArticleIdSupplier(String value) {
        return new JAXBElement<String>(_ArticleTmfArticleIdSupplier_QNAME, String.class, ArticleTmf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProductGroupTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ProductGroups", scope = ArticleTmf.class)
    public JAXBElement<ArrayOfProductGroupTmf> createArticleTmfProductGroups(ArrayOfProductGroupTmf value) {
        return new JAXBElement<ArrayOfProductGroupTmf>(_ArticleTmfProductGroups_QNAME, ArrayOfProductGroupTmf.class, ArticleTmf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SupplierTmf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Supplier", scope = ArticleTmf.class)
    public JAXBElement<SupplierTmf> createArticleTmfSupplier(SupplierTmf value) {
        return new JAXBElement<SupplierTmf>(_ArticleTmfSupplier_QNAME, SupplierTmf.class, ArticleTmf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Price }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PricePerUnit", scope = RepairTimesCostRate.class)
    public JAXBElement<Price> createRepairTimesCostRatePricePerUnit(Price value) {
        return new JAXBElement<Price>(_RepairTimesCostRatePricePerUnit_QNAME, Price.class, RepairTimesCostRate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CompanyName", scope = Address.class)
    public JAXBElement<String> createAddressCompanyName(String value) {
        return new JAXBElement<String>(_AddressCompanyName_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PostOfficeBox", scope = Address.class)
    public JAXBElement<String> createAddressPostOfficeBox(String value) {
        return new JAXBElement<String>(_AddressPostOfficeBox_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Type", scope = Address.class)
    public JAXBElement<AddressType> createAddressType(AddressType value) {
        return new JAXBElement<AddressType>(_AddressType_QNAME, AddressType.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AddressDescription", scope = Address.class)
    public JAXBElement<String> createAddressAddressDescription(String value) {
        return new JAXBElement<String>(_AddressAddressDescription_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Phone", scope = Address.class)
    public JAXBElement<String> createAddressPhone(String value) {
        return new JAXBElement<String>(_AddressPhone_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Street", scope = Address.class)
    public JAXBElement<String> createAddressStreet(String value) {
        return new JAXBElement<String>(_AddressStreet_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Country", scope = Address.class)
    public JAXBElement<String> createAddressCountry(String value) {
        return new JAXBElement<String>(_AddressCountry_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = Address.class)
    public JAXBElement<String> createAddressId(String value) {
        return new JAXBElement<String>(_RepairTimeId_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LastName", scope = Address.class)
    public JAXBElement<String> createAddressLastName(String value) {
        return new JAXBElement<String>(_AddressLastName_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "District", scope = Address.class)
    public JAXBElement<String> createAddressDistrict(String value) {
        return new JAXBElement<String>(_AddressDistrict_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Fax", scope = Address.class)
    public JAXBElement<String> createAddressFax(String value) {
        return new JAXBElement<String>(_AddressFax_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalInformation", scope = Address.class)
    public JAXBElement<String> createAddressAdditionalInformation(String value) {
        return new JAXBElement<String>(_AddressAdditionalInformation_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ZIP", scope = Address.class)
    public JAXBElement<String> createAddressZIP(String value) {
        return new JAXBElement<String>(_AddressZIP_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StreetExt", scope = Address.class)
    public JAXBElement<String> createAddressStreetExt(String value) {
        return new JAXBElement<String>(_AddressStreetExt_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Email", scope = Address.class)
    public JAXBElement<String> createAddressEmail(String value) {
        return new JAXBElement<String>(_AddressEmail_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FirstName", scope = Address.class)
    public JAXBElement<String> createAddressFirstName(String value) {
        return new JAXBElement<String>(_AddressFirstName_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Title", scope = Address.class)
    public JAXBElement<String> createAddressTitle(String value) {
        return new JAXBElement<String>(_AddressTitle_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "City", scope = Address.class)
    public JAXBElement<String> createAddressCity(String value) {
        return new JAXBElement<String>(_AddressCity_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Contact", scope = Address.class)
    public JAXBElement<String> createAddressContact(String value) {
        return new JAXBElement<String>(_AddressContact_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MobilePhone", scope = Address.class)
    public JAXBElement<String> createAddressMobilePhone(String value) {
        return new JAXBElement<String>(_AddressMobilePhone_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntity", scope = ValidationMessage.class)
    public JAXBElement<EntityLink> createValidationMessageLinkedEntity(EntityLink value) {
        return new JAXBElement<EntityLink>(_ControlIndicatorLinkedEntity_QNAME, EntityLink.class, ValidationMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Message", scope = ValidationMessage.class)
    public JAXBElement<String> createValidationMessageMessage(String value) {
        return new JAXBElement<String>(_ValidationMessageMessage_QNAME, String.class, ValidationMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencyCode_Iso_4217", scope = DispatchType.class)
    public JAXBElement<String> createDispatchTypeCurrencyCodeIso4217(String value) {
        return new JAXBElement<String>(_PriceCurrencyCodeIso4217_QNAME, String.class, DispatchType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DispatchConditions", scope = DispatchType.class)
    public JAXBElement<String> createDispatchTypeDispatchConditions(String value) {
        return new JAXBElement<String>(_DispatchTypeDispatchConditions_QNAME, String.class, DispatchType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencySymbol", scope = DispatchType.class)
    public JAXBElement<String> createDispatchTypeCurrencySymbol(String value) {
        return new JAXBElement<String>(_PriceCurrencySymbol_QNAME, String.class, DispatchType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExpectedDelivery", scope = Order.class)
    public JAXBElement<String> createOrderExpectedDelivery(String value) {
        return new JAXBElement<String>(_TourExpectedDelivery_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TourInformation", scope = Order.class)
    public JAXBElement<String> createOrderTourInformation(String value) {
        return new JAXBElement<String>(_OrderTourInformation_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UserDefinedData", scope = Order.class)
    public JAXBElement<ArrayOfUserDefinedData> createOrderUserDefinedData(ArrayOfUserDefinedData value) {
        return new JAXBElement<ArrayOfUserDefinedData>(_UserDefinedData_QNAME, ArrayOfUserDefinedData.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StatusInformation", scope = Order.class)
    public JAXBElement<KeyValueItem> createOrderStatusInformation(KeyValueItem value) {
        return new JAXBElement<KeyValueItem>(_ErpInformationStatusInformation_QNAME, KeyValueItem.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "WarehouseInformation", scope = Order.class)
    public JAXBElement<String> createOrderWarehouseInformation(String value) {
        return new JAXBElement<String>(_OrderWarehouseInformation_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderIds", scope = Order.class)
    public JAXBElement<ArrayOfOrderId> createOrderOrderIds(ArrayOfOrderId value) {
        return new JAXBElement<ArrayOfOrderId>(_OrderOrderIds_QNAME, ArrayOfOrderId.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderPosition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = Order.class)
    public JAXBElement<ArrayOfOrderPosition> createOrderItems(ArrayOfOrderPosition value) {
        return new JAXBElement<ArrayOfOrderPosition>(_GetNotificationReplyItems_QNAME, ArrayOfOrderPosition.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionLists", scope = Order.class)
    public JAXBElement<ArrayOfSelectionList> createOrderSelectionLists(ArrayOfSelectionList value) {
        return new JAXBElement<ArrayOfSelectionList>(_OrderPositionSelectionLists_QNAME, ArrayOfSelectionList.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = GetNotificationRequestBody.class)
    public JAXBElement<GetNotificationRequest> createGetNotificationRequestBodyRequest(GetNotificationRequest value) {
        return new JAXBElement<GetNotificationRequest>(_SendOrderRequestBodyRequest_QNAME, GetNotificationRequest.class, GetNotificationRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ServiceConfiguration", scope = GetSessionReply.class)
    public JAXBElement<ServiceConfiguration> createGetSessionReplyServiceConfiguration(ServiceConfiguration value) {
        return new JAXBElement<ServiceConfiguration>(_ServiceConfiguration_QNAME, ServiceConfiguration.class, GetSessionReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SessionId", scope = GetSessionReply.class)
    public JAXBElement<String> createGetSessionReplySessionId(String value) {
        return new JAXBElement<String>(_GetSessionReplySessionId_QNAME, String.class, GetSessionReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ModelGroup", scope = Vehicle.class)
    public JAXBElement<String> createVehicleModelGroup(String value) {
        return new JAXBElement<String>(_VehicleModelGroup_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Type", scope = Vehicle.class)
    public JAXBElement<String> createVehicleType(String value) {
        return new JAXBElement<String>(_AddressType_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TypeCertificateNumber", scope = Vehicle.class)
    public JAXBElement<String> createVehicleTypeCertificateNumber(String value) {
        return new JAXBElement<String>(_VehicleTypeCertificateNumber_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MileageShortIndicator", scope = Vehicle.class)
    public JAXBElement<String> createVehicleMileageShortIndicator(String value) {
        return new JAXBElement<String>(_VehicleMileageShortIndicator_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MotorCode", scope = Vehicle.class)
    public JAXBElement<String> createVehicleMotorCode(String value) {
        return new JAXBElement<String>(_VehicleMotorCode_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Model", scope = Vehicle.class)
    public JAXBElement<String> createVehicleModel(String value) {
        return new JAXBElement<String>(_VehicleModel_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "VehicleOwner", scope = Vehicle.class)
    public JAXBElement<EntityLink> createVehicleVehicleOwner(EntityLink value) {
        return new JAXBElement<EntityLink>(_VehicleVehicleOwner_QNAME, EntityLink.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Make", scope = Vehicle.class)
    public JAXBElement<String> createVehicleMake(String value) {
        return new JAXBElement<String>(_VehicleMake_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PlateId", scope = Vehicle.class)
    public JAXBElement<String> createVehiclePlateId(String value) {
        return new JAXBElement<String>(_VehiclePlateId_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vin", scope = Vehicle.class)
    public JAXBElement<String> createVehicleVin(String value) {
        return new JAXBElement<String>(_VehicleVin_QNAME, String.class, Vehicle.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = GetErpInformationReply.class)
    public JAXBElement<MasterData> createGetErpInformationReplyMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, GetErpInformationReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpArticleInformation", scope = GetErpInformationReply.class)
    public JAXBElement<ArrayOfErpInformation> createGetErpInformationReplyErpArticleInformation(ArrayOfErpInformation value) {
        return new JAXBElement<ArrayOfErpInformation>(_GetErpInformationRequestErpArticleInformation_QNAME, ArrayOfErpInformation.class, GetErpInformationReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAccountData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AccountData", scope = GetAccountInformationReply.class)
    public JAXBElement<ArrayOfAccountData> createGetAccountInformationReplyAccountData(ArrayOfAccountData value) {
        return new JAXBElement<ArrayOfAccountData>(_AccountData_QNAME, ArrayOfAccountData.class, GetAccountInformationReply.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = TextBlock.class)
    public JAXBElement<String> createTextBlockText(String value) {
        return new JAXBElement<String>(_SelectionListItemText_QNAME, String.class, TextBlock.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = SendOrderDocumentRequestBody.class)
    public JAXBElement<SendOrderDocumentRequest> createSendOrderDocumentRequestBodyRequest(SendOrderDocumentRequest value) {
        return new JAXBElement<SendOrderDocumentRequest>(_SendOrderRequestBodyRequest_QNAME, SendOrderDocumentRequest.class, SendOrderDocumentRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationResult", scope = GetNotificationResponseBody.class)
    public JAXBElement<GetNotificationReply> createGetNotificationResponseBodyGetNotificationResult(GetNotificationReply value) {
        return new JAXBElement<GetNotificationReply>(_GetNotificationResponseBodyGetNotificationResult_QNAME, GetNotificationReply.class, GetNotificationResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = DeleteSessionRequestBody.class)
    public JAXBElement<BaseRequest> createDeleteSessionRequestBodyRequest(BaseRequest value) {
        return new JAXBElement<BaseRequest>(_SendOrderRequestBodyRequest_QNAME, BaseRequest.class, DeleteSessionRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = UserDefinedData.class)
    public JAXBElement<String> createUserDefinedDataDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, UserDefinedData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = UserDefinedData.class)
    public JAXBElement<String> createUserDefinedDataValue(String value) {
        return new JAXBElement<String>(_AdditionalIdentifierValue_QNAME, String.class, UserDefinedData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Name", scope = SupplierTmf.class)
    public JAXBElement<String> createSupplierTmfName(String value) {
        return new JAXBElement<String>(_SupplierTmfName_QNAME, String.class, SupplierTmf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Orders", scope = OrderCollection.class)
    public JAXBElement<ArrayOfOrder> createOrderCollectionOrders(ArrayOfOrder value) {
        return new JAXBElement<ArrayOfOrder>(_OrderCollectionOrders_QNAME, ArrayOfOrder.class, OrderCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderIds", scope = OrderCollection.class)
    public JAXBElement<ArrayOfOrderId> createOrderCollectionOrderIds(ArrayOfOrderId value) {
        return new JAXBElement<ArrayOfOrderId>(_OrderOrderIds_QNAME, ArrayOfOrderId.class, OrderCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = OrderCollection.class)
    public JAXBElement<String> createOrderCollectionShortDescription(String value) {
        return new JAXBElement<String>(_OrderCollectionShortDescription_QNAME, String.class, OrderCollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExpectedDeliveryTime", scope = Quantity.class)
    public JAXBElement<String> createQuantityExpectedDeliveryTime(String value) {
        return new JAXBElement<String>(_QuantityExpectedDeliveryTime_QNAME, String.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = Quantity.class)
    public JAXBElement<String> createQuantityDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LotSizes", scope = Quantity.class)
    public JAXBElement<ArrayOfKeyValueItem> createQuantityLotSizes(ArrayOfKeyValueItem value) {
        return new JAXBElement<ArrayOfKeyValueItem>(_QuantityLotSizes_QNAME, ArrayOfKeyValueItem.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PackagingUnit", scope = Quantity.class)
    public JAXBElement<String> createQuantityPackagingUnit(String value) {
        return new JAXBElement<String>(_QuantityPackagingUnit_QNAME, String.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityState", scope = Quantity.class)
    public JAXBElement<EntityLink> createQuantityAvailabilityState(EntityLink value) {
        return new JAXBElement<EntityLink>(_AvailabilityState_QNAME, EntityLink.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "QuantityUnit", scope = Quantity.class)
    public JAXBElement<String> createQuantityQuantityUnit(String value) {
        return new JAXBElement<String>(_QuantityQuantityUnit_QNAME, String.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = Quantity.class)
    public JAXBElement<String> createQuantityTour(String value) {
        return new JAXBElement<String>(_Tour_QNAME, String.class, Quantity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkUrl", scope = Memo.class)
    public JAXBElement<String> createMemoLinkUrl(String value) {
        return new JAXBElement<String>(_IconLinkUrl_QNAME, String.class, Memo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icons", scope = Memo.class)
    public JAXBElement<ArrayOfEntityLink> createMemoIcons(ArrayOfEntityLink value) {
        return new JAXBElement<ArrayOfEntityLink>(_MasterDataIcons_QNAME, ArrayOfEntityLink.class, Memo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Label", scope = Memo.class)
    public JAXBElement<String> createMemoLabel(String value) {
        return new JAXBElement<String>(_MemoLabel_QNAME, String.class, Memo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = Memo.class)
    public JAXBElement<String> createMemoText(String value) {
        return new JAXBElement<String>(_SelectionListItemText_QNAME, String.class, Memo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = OrderId.class)
    public JAXBElement<String> createOrderIdDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, OrderId.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = OrderId.class)
    public JAXBElement<String> createOrderIdValue(String value) {
        return new JAXBElement<String>(_AdditionalIdentifierValue_QNAME, String.class, OrderId.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicators", scope = ServiceConfiguration.class)
    public JAXBElement<ArrayOfControlIndicator> createServiceConfigurationControlIndicators(ArrayOfControlIndicator value) {
        return new JAXBElement<ArrayOfControlIndicator>(_ErpInformationControlIndicators_QNAME, ArrayOfControlIndicator.class, ServiceConfiguration.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SearchTerm", scope = FindItemsRequest.class)
    public JAXBElement<String> createFindItemsRequestSearchTerm(String value) {
        return new JAXBElement<String>(_FindItemsRequestSearchTerm_QNAME, String.class, FindItemsRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionsResult", scope = GetItemsCollectionsResponseBody.class)
    public JAXBElement<GetItemsCollectionReply> createGetItemsCollectionsResponseBodyGetItemsCollectionsResult(GetItemsCollectionReply value) {
        return new JAXBElement<GetItemsCollectionReply>(_GetItemsCollectionsResponseBodyGetItemsCollectionsResult_QNAME, GetItemsCollectionReply.class, GetItemsCollectionsResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = KeyValueItem.class)
    public JAXBElement<String> createKeyValueItemValue(String value) {
        return new JAXBElement<String>(_AdditionalIdentifierValue_QNAME, String.class, KeyValueItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Key", scope = KeyValueItem.class)
    public JAXBElement<String> createKeyValueItemKey(String value) {
        return new JAXBElement<String>(_KeyValueItemKey_QNAME, String.class, KeyValueItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = FindItemsRequestBody.class)
    public JAXBElement<FindItemsRequest> createFindItemsRequestBodyRequest(FindItemsRequest value) {
        return new JAXBElement<FindItemsRequest>(_SendOrderRequestBodyRequest_QNAME, FindItemsRequest.class, FindItemsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AccountData.class)
    public JAXBElement<String> createAccountDataDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, AccountData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = AccountData.class)
    public JAXBElement<ArrayOfMemo> createAccountDataMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, AccountData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = AccountData.class)
    public JAXBElement<String> createAccountDataShortDescription(String value) {
        return new JAXBElement<String>(_OrderCollectionShortDescription_QNAME, String.class, AccountData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = Warehouse.class)
    public JAXBElement<String> createWarehouseDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = Warehouse.class)
    public JAXBElement<ArrayOfMemo> createWarehouseMemos(ArrayOfMemo value) {
        return new JAXBElement<ArrayOfMemo>(_LinkedItemsCollectionMemos_QNAME, ArrayOfMemo.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = Warehouse.class)
    public JAXBElement<String> createWarehouseId(String value) {
        return new JAXBElement<String>(_RepairTimeId_QNAME, String.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQuantity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Quantities", scope = Warehouse.class)
    public JAXBElement<ArrayOfQuantity> createWarehouseQuantities(ArrayOfQuantity value) {
        return new JAXBElement<ArrayOfQuantity>(_WarehouseQuantities_QNAME, ArrayOfQuantity.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Name", scope = Warehouse.class)
    public JAXBElement<String> createWarehouseName(String value) {
        return new JAXBElement<String>(_SupplierTmfName_QNAME, String.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = Warehouse.class)
    public JAXBElement<String> createWarehouseShortDescription(String value) {
        return new JAXBElement<String>(_OrderCollectionShortDescription_QNAME, String.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = Warehouse.class)
    public JAXBElement<EntityLink> createWarehouseTour(EntityLink value) {
        return new JAXBElement<EntityLink>(_Tour_QNAME, EntityLink.class, Warehouse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExternalSessionId", scope = BaseRequest.class)
    public JAXBElement<String> createBaseRequestExternalSessionId(String value) {
        return new JAXBElement<String>(_BaseRequestExternalSessionId_QNAME, String.class, BaseRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SessionId", scope = BaseRequest.class)
    public JAXBElement<String> createBaseRequestSessionId(String value) {
        return new JAXBElement<String>(_GetSessionReplySessionId_QNAME, String.class, BaseRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ClientIp", scope = BaseRequest.class)
    public JAXBElement<String> createBaseRequestClientIp(String value) {
        return new JAXBElement<String>(_BaseRequestClientIp_QNAME, String.class, BaseRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LanguageCodeIso639_1", scope = BaseRequest.class)
    public JAXBElement<String> createBaseRequestLanguageCodeIso6391(String value) {
        return new JAXBElement<String>(_BaseRequestLanguageCodeIso6391_QNAME, String.class, BaseRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIds", scope = BaseRequest.class)
    public JAXBElement<ArrayOfKeyValueItem> createBaseRequestAdditionalIds(ArrayOfKeyValueItem value) {
        return new JAXBElement<ArrayOfKeyValueItem>(_BaseResponseAdditionalIds_QNAME, ArrayOfKeyValueItem.class, BaseRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Credentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Credentials", scope = BaseRequest.class)
    public JAXBElement<Credentials> createBaseRequestCredentials(Credentials value) {
        return new JAXBElement<Credentials>(_Credentials_QNAME, Credentials.class, BaseRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DeleteSessionResult", scope = DeleteSessionResponseBody.class)
    public JAXBElement<BaseResponse> createDeleteSessionResponseBodyDeleteSessionResult(BaseResponse value) {
        return new JAXBElement<BaseResponse>(_DeleteSessionResponseBodyDeleteSessionResult_QNAME, BaseResponse.class, DeleteSessionResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = ProductGroupTmf.class)
    public JAXBElement<String> createProductGroupTmfDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, ProductGroupTmf.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AvailabilityState.class)
    public JAXBElement<String> createAvailabilityStateDescription(String value) {
        return new JAXBElement<String>(_RepairTimeDescription_QNAME, String.class, AvailabilityState.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icon", scope = AvailabilityState.class)
    public JAXBElement<EntityLink> createAvailabilityStateIcon(EntityLink value) {
        return new JAXBElement<EntityLink>(_Icon_QNAME, EntityLink.class, AvailabilityState.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = AvailabilityState.class)
    public JAXBElement<String> createAvailabilityStateShortDescription(String value) {
        return new JAXBElement<String>(_OrderCollectionShortDescription_QNAME, String.class, AvailabilityState.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsResult", scope = FindItemsResponseBody.class)
    public JAXBElement<FindItemsReply> createFindItemsResponseBodyFindItemsResult(FindItemsReply value) {
        return new JAXBElement<FindItemsReply>(_FindItemsResponseBodyFindItemsResult_QNAME, FindItemsReply.class, FindItemsResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Username", scope = User.class)
    public JAXBElement<String> createUserUsername(String value) {
        return new JAXBElement<String>(_UserUsername_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MandatorId", scope = User.class)
    public JAXBElement<String> createUserMandatorId(String value) {
        return new JAXBElement<String>(_UserMandatorId_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerId", scope = User.class)
    public JAXBElement<String> createUserCustomerId(String value) {
        return new JAXBElement<String>(_UserCustomerId_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Password", scope = User.class)
    public JAXBElement<String> createUserPassword(String value) {
        return new JAXBElement<String>(_UserPassword_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = GetSessionRequestBody.class)
    public JAXBElement<BaseRequest> createGetSessionRequestBodyRequest(BaseRequest value) {
        return new JAXBElement<BaseRequest>(_SendOrderRequestBodyRequest_QNAME, BaseRequest.class, GetSessionRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection", scope = SendOrderDocumentRequest.class)
    public JAXBElement<OrderCollection> createSendOrderDocumentRequestOrderCollection(OrderCollection value) {
        return new JAXBElement<OrderCollection>(_OrderCollection_QNAME, OrderCollection.class, SendOrderDocumentRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = SendOrderDocumentRequest.class)
    public JAXBElement<MasterData> createSendOrderDocumentRequestMasterData(MasterData value) {
        return new JAXBElement<MasterData>(_MasterData_QNAME, MasterData.class, SendOrderDocumentRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Document", scope = SendOrderDocumentRequest.class)
    public JAXBElement<String> createSendOrderDocumentRequestDocument(String value) {
        return new JAXBElement<String>(_SendOrderDocumentRequestDocument_QNAME, String.class, SendOrderDocumentRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = GetErpInformationRequestBody.class)
    public JAXBElement<GetErpInformationRequest> createGetErpInformationRequestBodyRequest(GetErpInformationRequest value) {
        return new JAXBElement<GetErpInformationRequest>(_SendOrderRequestBodyRequest_QNAME, GetErpInformationRequest.class, GetErpInformationRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountInformationReply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountDataResult", scope = GetAccountDataResponseBody.class)
    public JAXBElement<GetAccountInformationReply> createGetAccountDataResponseBodyGetAccountDataResult(GetAccountInformationReply value) {
        return new JAXBElement<GetAccountInformationReply>(_GetAccountDataResponseBodyGetAccountDataResult_QNAME, GetAccountInformationReply.class, GetAccountDataResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CompanyName", scope = Customer.class)
    public JAXBElement<String> createCustomerCompanyName(String value) {
        return new JAXBElement<String>(_AddressCompanyName_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Details", scope = Customer.class)
    public JAXBElement<String> createCustomerDetails(String value) {
        return new JAXBElement<String>(_CustomerDetails_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Address", scope = Customer.class)
    public JAXBElement<Address> createCustomerAddress(Address value) {
        return new JAXBElement<Address>(_Address_QNAME, Address.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FirstName", scope = Customer.class)
    public JAXBElement<String> createCustomerFirstName(String value) {
        return new JAXBElement<String>(_AddressFirstName_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Title", scope = Customer.class)
    public JAXBElement<String> createCustomerTitle(String value) {
        return new JAXBElement<String>(_AddressTitle_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerId", scope = Customer.class)
    public JAXBElement<String> createCustomerCustomerId(String value) {
        return new JAXBElement<String>(_UserCustomerId_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Name", scope = Customer.class)
    public JAXBElement<String> createCustomerName(String value) {
        return new JAXBElement<String>(_SupplierTmfName_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = Customer.class)
    public JAXBElement<String> createCustomerShortDescription(String value) {
        return new JAXBElement<String>(_OrderCollectionShortDescription_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLink }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntity", scope = Notification.class)
    public JAXBElement<EntityLink> createNotificationLinkedEntity(EntityLink value) {
        return new JAXBElement<EntityLink>(_ControlIndicatorLinkedEntity_QNAME, EntityLink.class, Notification.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Memo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memo", scope = Notification.class)
    public JAXBElement<Memo> createNotificationMemo(Memo value) {
        return new JAXBElement<Memo>(_Memo_QNAME, Memo.class, Notification.class, value);
    }

}
