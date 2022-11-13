
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.sagag.services.autonet.erp.wsdl.user.GetUserResponseType;
import com.sagag.services.autonet.erp.wsdl.user.GetVersionRequestBodyType;
import com.sagag.services.autonet.erp.wsdl.user.SetSidRequestType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sagag.services.autonet.erp.wsdl.tmconnect package. 
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

    private final static QName _ArrayOfArticleTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfArticleTmf");
    private final static QName _Customer_QNAME = new QName("http://topmotive.eu/TMConnect", "Customer");
    private final static QName _Address_QNAME = new QName("http://topmotive.eu/TMConnect", "Address");
    private final static QName _ArrayOfDispatchType_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfDispatchType");
    private final static QName _FindItemsReply_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsReply");
    private final static QName _GetErpInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformation");
    private final static QName _ArrayOfItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfItemsCollection");
    private final static QName _ArrayOfNotification_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfNotification");
    private final static QName _SendOrderDocumentRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderDocumentRequest");
    private final static QName _LinkedItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedItemsCollection");
    private final static QName _GetErpInformationRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationRequest");
    private final static QName _ArrayOfAddress_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAddress");
    private final static QName _AdditionalIdentifier_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalIdentifier");
    private final static QName _BaseResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "BaseResponse");
    private final static QName _AccountData_QNAME = new QName("http://topmotive.eu/TMConnect", "AccountData");
    private final static QName _SendOrderRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderRequest");
    private final static QName _GetVersion_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersion");
    private final static QName _FindItemsRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "FindItemsRequest");
    private final static QName _AvailabilityState_QNAME = new QName("http://topmotive.eu/TMConnect", "AvailabilityState");
    private final static QName _ArrayOfAvailabilityState_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAvailabilityState");
    private final static QName _ArrayOfAdditionalIdentifier_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAdditionalIdentifier");
    private final static QName _Tour_QNAME = new QName("http://topmotive.eu/TMConnect", "Tour");
    private final static QName _SidRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "SidRequest");
    private final static QName _GetUserRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetUserRequest");
    private final static QName _Article_QNAME = new QName("http://topmotive.eu/TMConnect", "Article");
    private final static QName _BaseDto_QNAME = new QName("http://topmotive.eu/TMConnect", "BaseDto");
    private final static QName _ArrayOfControlIndicator_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfControlIndicator");
    private final static QName _Notification_QNAME = new QName("http://topmotive.eu/TMConnect", "Notification");
    private final static QName _ArrayOfSelectionListItem_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfSelectionListItem");
    private final static QName _ErpInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "ErpInformation");
    private final static QName _MasterData_QNAME = new QName("http://topmotive.eu/TMConnect", "MasterData");
    private final static QName _ItemsCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "ItemsCollection");
    private final static QName _ArrayOfIcon_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfIcon");
    private final static QName _GetErpInformationReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationReply");
    private final static QName _ProductGroupTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ProductGroupTmf");
    private final static QName _GetAccountInformationReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetAccountInformationReply");
    private final static QName _AddressType_QNAME = new QName("http://topmotive.eu/TMConnect", "AddressType");
    private final static QName _Credentials_QNAME = new QName("http://topmotive.eu/TMConnect", "Credentials");
    private final static QName _Memo_QNAME = new QName("http://topmotive.eu/TMConnect", "Memo");
    private final static QName _KeyValueItem_QNAME = new QName("http://topmotive.eu/TMConnect", "KeyValueItem");
    private final static QName _SendOrderReply_QNAME = new QName("http://topmotive.eu/TMConnect", "SendOrderReply");
    private final static QName _ArrayOfArticle_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfArticle");
    private final static QName _ControlIndicator_QNAME = new QName("http://topmotive.eu/TMConnect", "ControlIndicator");
    private final static QName _ArrayOfEntityLink_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfEntityLink");
    private final static QName _Vehicle_QNAME = new QName("http://topmotive.eu/TMConnect", "Vehicle");
    private final static QName _ArrayOfOrder_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfOrder");
    private final static QName _DispatchType_QNAME = new QName("http://topmotive.eu/TMConnect", "DispatchType");
    private final static QName _ServiceConfiguration_QNAME = new QName("http://topmotive.eu/TMConnect", "ServiceConfiguration");
    private final static QName _ArrayOfOrderPosition_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfOrderPosition");
    private final static QName _ArrayOfRepairTime_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfRepairTime");
    private final static QName _ArrayOfTour_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfTour");
    private final static QName _ArrayOfPrice_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfPrice");
    private final static QName _ValidationMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "ValidationMessage");
    private final static QName _ArrayOfVehicle_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfVehicle");
    private final static QName _GetErpInformationResponse_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationResponse");
    private final static QName _GetErpInformationResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationResponseBody");
    private final static QName _ArrayOfAccountData_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfAccountData");
    private final static QName _Price_QNAME = new QName("http://topmotive.eu/TMConnect", "Price");
    private final static QName _OrderPosition_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderPosition");
    private final static QName _ArrayOfMemo_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfMemo");
    private final static QName _ArrayOfErpInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfErpInformation");
    private final static QName _BaseRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "BaseRequest");
    private final static QName _Icon_QNAME = new QName("http://topmotive.eu/TMConnect", "Icon");
    private final static QName _SelectionList_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionList");
    private final static QName _TextBlock_QNAME = new QName("http://topmotive.eu/TMConnect", "TextBlock");
    private final static QName _ArrayOfProductGroupTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfProductGroupTmf");
    private final static QName _SupplierTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "SupplierTmf");
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
    private final static QName _GetVersionResponseBody_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersionResponseBody");
    private final static QName _ArrayOfSelectionList_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfSelectionList");
    private final static QName _GetNotificationRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetNotificationRequest");
    private final static QName _User_QNAME = new QName("http://topmotive.eu/TMConnect", "User");
    private final static QName _Warehouse_QNAME = new QName("http://topmotive.eu/TMConnect", "Warehouse");
    private final static QName _ArrayOfUserDefinedData_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfUserDefinedData");
    private final static QName _GetItemsCollectionReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionReply");
    private final static QName _ArrayOfOrderId_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfOrderId");
    private final static QName _ArrayOfCustomer_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfCustomer");
    private final static QName _GetItemsCollectionRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "GetItemsCollectionRequest");
    private final static QName _OrderCollection_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderCollection");
    private final static QName _ArticleTmf_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleTmf");
    private final static QName _ArrayOfQuantity_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfQuantity");
    private final static QName _RepairTimesCostRate_QNAME = new QName("http://topmotive.eu/TMConnect", "RepairTimesCostRate");
    private final static QName _EntityLink_QNAME = new QName("http://topmotive.eu/TMConnect", "EntityLink");
    private final static QName _EMasterDataType_QNAME = new QName("http://topmotive.eu/TMConnect", "EMasterDataType");
    private final static QName _RepairTime_QNAME = new QName("http://topmotive.eu/TMConnect", "RepairTime");
    private final static QName _GetUserReply_QNAME = new QName("http://topmotive.eu/TMConnect", "GetUserReply");
    private final static QName _ArrayOfValidationMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfValidationMessage");
    private final static QName _UserDefinedData_QNAME = new QName("http://topmotive.eu/TMConnect", "UserDefinedData");
    private final static QName _ArrayOfWarehouse_QNAME = new QName("http://topmotive.eu/TMConnect", "ArrayOfWarehouse");
    private final static QName _Quantity_QNAME = new QName("http://topmotive.eu/TMConnect", "Quantity");
    private final static QName _GetUserResponseElementGetUserResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetUserResult");
    private final static QName _WarehouseTypeDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "Description");
    private final static QName _WarehouseTypeMemos_QNAME = new QName("http://topmotive.eu/TMConnect", "Memos");
    private final static QName _WarehouseTypeId_QNAME = new QName("http://topmotive.eu/TMConnect", "Id");
    private final static QName _WarehouseTypeQuantities_QNAME = new QName("http://topmotive.eu/TMConnect", "Quantities");
    private final static QName _WarehouseTypeName_QNAME = new QName("http://topmotive.eu/TMConnect", "Name");
    private final static QName _WarehouseTypeShortDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "ShortDescription");
    private final static QName _IconTypeLinkUrl_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkUrl");
    private final static QName _IconTypeUrl_QNAME = new QName("http://topmotive.eu/TMConnect", "Url");
    private final static QName _IconTypeTopmotiveId_QNAME = new QName("http://topmotive.eu/TMConnect", "TopmotiveId");
    private final static QName _BaseDtoTypeAdditionalIdentifiers_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalIdentifiers");
    private final static QName _ValidationMessageTypeLinkedEntity_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedEntity");
    private final static QName _ValidationMessageTypeMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "Message");
    private final static QName _GetErpInformationRequestTypeErpArticleInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "ErpArticleInformation");
    private final static QName _ArticleTypeArticleIdErp_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleIdErp");
    private final static QName _UserDefinedDataTypeValue_QNAME = new QName("http://topmotive.eu/TMConnect", "Value");
    private final static QName _GetNotificationReplyTypeItems_QNAME = new QName("http://topmotive.eu/TMConnect", "Items");
    private final static QName _BaseRequestTypeExternalSessionId_QNAME = new QName("http://topmotive.eu/TMConnect", "ExternalSessionId");
    private final static QName _BaseRequestTypeSessionId_QNAME = new QName("http://topmotive.eu/TMConnect", "SessionId");
    private final static QName _BaseRequestTypeClientIp_QNAME = new QName("http://topmotive.eu/TMConnect", "ClientIp");
    private final static QName _BaseRequestTypeLanguageCodeIso6391_QNAME = new QName("http://topmotive.eu/TMConnect", "LanguageCodeIso639_1");
    private final static QName _BaseRequestTypeAdditionalIds_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalIds");
    private final static QName _OrderTypeExpectedDelivery_QNAME = new QName("http://topmotive.eu/TMConnect", "ExpectedDelivery");
    private final static QName _OrderTypeTourInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "TourInformation");
    private final static QName _OrderTypeStatusInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "StatusInformation");
    private final static QName _OrderTypeWarehouseInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "WarehouseInformation");
    private final static QName _OrderTypeOrderIds_QNAME = new QName("http://topmotive.eu/TMConnect", "OrderIds");
    private final static QName _OrderTypeSelectionLists_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionLists");
    private final static QName _SidRequestTypeUID_QNAME = new QName("http://topmotive.eu/TMConnect", "UID");
    private final static QName _SidRequestTypeSID_QNAME = new QName("http://topmotive.eu/TMConnect", "SID");
    private final static QName _GetErpInformationRequestBodyTypeRequest_QNAME = new QName("http://topmotive.eu/TMConnect", "request");
    private final static QName _MasterDataTypeItemsCollections_QNAME = new QName("http://topmotive.eu/TMConnect", "ItemsCollections");
    private final static QName _MasterDataTypeAddresses_QNAME = new QName("http://topmotive.eu/TMConnect", "Addresses");
    private final static QName _MasterDataTypeArticleTmfs_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleTmfs");
    private final static QName _MasterDataTypeCustomers_QNAME = new QName("http://topmotive.eu/TMConnect", "Customers");
    private final static QName _MasterDataTypeRepairTimes_QNAME = new QName("http://topmotive.eu/TMConnect", "RepairTimes");
    private final static QName _MasterDataTypeSelectionListItems_QNAME = new QName("http://topmotive.eu/TMConnect", "SelectionListItems");
    private final static QName _MasterDataTypeAvailabilityStates_QNAME = new QName("http://topmotive.eu/TMConnect", "AvailabilityStates");
    private final static QName _MasterDataTypeDispatchTypes_QNAME = new QName("http://topmotive.eu/TMConnect", "DispatchTypes");
    private final static QName _MasterDataTypeArticles_QNAME = new QName("http://topmotive.eu/TMConnect", "Articles");
    private final static QName _MasterDataTypeIcons_QNAME = new QName("http://topmotive.eu/TMConnect", "Icons");
    private final static QName _MasterDataTypeTours_QNAME = new QName("http://topmotive.eu/TMConnect", "Tours");
    private final static QName _MasterDataTypeTextBlocks_QNAME = new QName("http://topmotive.eu/TMConnect", "TextBlocks");
    private final static QName _MasterDataTypeVehicles_QNAME = new QName("http://topmotive.eu/TMConnect", "Vehicles");
    private final static QName _ErpInformationTypeItem_QNAME = new QName("http://topmotive.eu/TMConnect", "Item");
    private final static QName _ErpInformationTypePrices_QNAME = new QName("http://topmotive.eu/TMConnect", "Prices");
    private final static QName _ErpInformationTypeAdditionalDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalDescription");
    private final static QName _ErpInformationTypeLinkedItemsCollections_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedItemsCollections");
    private final static QName _ErpInformationTypeControlIndicators_QNAME = new QName("http://topmotive.eu/TMConnect", "ControlIndicators");
    private final static QName _ErpInformationTypeAdditionalDescriptionExtended_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalDescriptionExtended");
    private final static QName _ErpInformationTypeWarehouses_QNAME = new QName("http://topmotive.eu/TMConnect", "Warehouses");
    private final static QName _ErpInformationTypeSpecialIcons_QNAME = new QName("http://topmotive.eu/TMConnect", "SpecialIcons");
    private final static QName _SelectionListItemTypeText_QNAME = new QName("http://topmotive.eu/TMConnect", "Text");
    private final static QName _RepairTimeTypeTypeOfCostRate_QNAME = new QName("http://topmotive.eu/TMConnect", "TypeOfCostRate");
    private final static QName _KeyValueItemTypeKey_QNAME = new QName("http://topmotive.eu/TMConnect", "Key");
    private final static QName _GetUserReplyTypeCustomerFirstName_QNAME = new QName("http://topmotive.eu/TMConnect", "CustomerFirstName");
    private final static QName _GetUserReplyTypeStartPage_QNAME = new QName("http://topmotive.eu/TMConnect", "StartPage");
    private final static QName _GetUserReplyTypeHKat_QNAME = new QName("http://topmotive.eu/TMConnect", "HKat");
    private final static QName _GetUserReplyTypePrivileges_QNAME = new QName("http://topmotive.eu/TMConnect", "Privileges");
    private final static QName _GetUserReplyTypeCustomerLastName_QNAME = new QName("http://topmotive.eu/TMConnect", "CustomerLastName");
    private final static QName _GetUserReplyTypeDVSEPassword_QNAME = new QName("http://topmotive.eu/TMConnect", "DVSEPassword");
    private final static QName _GetUserReplyTypeTraderID_QNAME = new QName("http://topmotive.eu/TMConnect", "TraderID");
    private final static QName _GetUserReplyTypeErrorMessage_QNAME = new QName("http://topmotive.eu/TMConnect", "ErrorMessage");
    private final static QName _GetUserReplyTypeCustomerAddress_QNAME = new QName("http://topmotive.eu/TMConnect", "CustomerAddress");
    private final static QName _GetUserReplyTypeDVSEUserName_QNAME = new QName("http://topmotive.eu/TMConnect", "DVSEUserName");
    private final static QName _GetUserReplyTypeErrorOccured_QNAME = new QName("http://topmotive.eu/TMConnect", "ErrorOccured");
    private final static QName _GetUserReplyTypeCustomerID_QNAME = new QName("http://topmotive.eu/TMConnect", "CustomerID");
    private final static QName _GetUserReplyTypeStartPagePosition_QNAME = new QName("http://topmotive.eu/TMConnect", "StartPagePosition");
    private final static QName _DoWorkResponseElementDoWorkResult_QNAME = new QName("http://topmotive.eu/TMConnect", "DoWorkResult");
    private final static QName _CustomerTypeCompanyName_QNAME = new QName("http://topmotive.eu/TMConnect", "CompanyName");
    private final static QName _CustomerTypeDetails_QNAME = new QName("http://topmotive.eu/TMConnect", "Details");
    private final static QName _CustomerTypeFirstName_QNAME = new QName("http://topmotive.eu/TMConnect", "FirstName");
    private final static QName _CustomerTypeTitle_QNAME = new QName("http://topmotive.eu/TMConnect", "Title");
    private final static QName _CustomerTypeCustomerId_QNAME = new QName("http://topmotive.eu/TMConnect", "CustomerId");
    private final static QName _QuantityTypeExpectedDeliveryTime_QNAME = new QName("http://topmotive.eu/TMConnect", "ExpectedDeliveryTime");
    private final static QName _QuantityTypeLotSizes_QNAME = new QName("http://topmotive.eu/TMConnect", "LotSizes");
    private final static QName _QuantityTypePackagingUnit_QNAME = new QName("http://topmotive.eu/TMConnect", "PackagingUnit");
    private final static QName _QuantityTypeQuantityUnit_QNAME = new QName("http://topmotive.eu/TMConnect", "QuantityUnit");
    private final static QName _SetSidElementSidInfo_QNAME = new QName("http://topmotive.eu/TMConnect", "sidInfo");
    private final static QName _AddressTypePostOfficeBox_QNAME = new QName("http://topmotive.eu/TMConnect", "PostOfficeBox");
    private final static QName _AddressTypeType_QNAME = new QName("http://topmotive.eu/TMConnect", "Type");
    private final static QName _AddressTypeAddressDescription_QNAME = new QName("http://topmotive.eu/TMConnect", "AddressDescription");
    private final static QName _AddressTypePhone_QNAME = new QName("http://topmotive.eu/TMConnect", "Phone");
    private final static QName _AddressTypeStreet_QNAME = new QName("http://topmotive.eu/TMConnect", "Street");
    private final static QName _AddressTypeCountry_QNAME = new QName("http://topmotive.eu/TMConnect", "Country");
    private final static QName _AddressTypeLastName_QNAME = new QName("http://topmotive.eu/TMConnect", "LastName");
    private final static QName _AddressTypeDistrict_QNAME = new QName("http://topmotive.eu/TMConnect", "District");
    private final static QName _AddressTypeFax_QNAME = new QName("http://topmotive.eu/TMConnect", "Fax");
    private final static QName _AddressTypeAdditionalInformation_QNAME = new QName("http://topmotive.eu/TMConnect", "AdditionalInformation");
    private final static QName _AddressTypeZIP_QNAME = new QName("http://topmotive.eu/TMConnect", "ZIP");
    private final static QName _AddressTypeStreetExt_QNAME = new QName("http://topmotive.eu/TMConnect", "StreetExt");
    private final static QName _AddressTypeEmail_QNAME = new QName("http://topmotive.eu/TMConnect", "Email");
    private final static QName _AddressTypeCity_QNAME = new QName("http://topmotive.eu/TMConnect", "City");
    private final static QName _AddressTypeContact_QNAME = new QName("http://topmotive.eu/TMConnect", "Contact");
    private final static QName _AddressTypeMobilePhone_QNAME = new QName("http://topmotive.eu/TMConnect", "MobilePhone");
    private final static QName _ArticleTmfTypeArticleIdSupplier_QNAME = new QName("http://topmotive.eu/TMConnect", "ArticleIdSupplier");
    private final static QName _ArticleTmfTypeProductGroups_QNAME = new QName("http://topmotive.eu/TMConnect", "ProductGroups");
    private final static QName _ArticleTmfTypeSupplier_QNAME = new QName("http://topmotive.eu/TMConnect", "Supplier");
    private final static QName _OrderPositionTypeLinkedEntities_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedEntities");
    private final static QName _LinkedItemsCollectionTypeLinkedItems_QNAME = new QName("http://topmotive.eu/TMConnect", "LinkedItems");
    private final static QName _GetUserRequestTypeANUserName_QNAME = new QName("http://topmotive.eu/TMConnect", "ANUserName");
    private final static QName _GetUserRequestTypeANPassword_QNAME = new QName("http://topmotive.eu/TMConnect", "ANPassword");
    private final static QName _ControlIndicatorTypeParameters_QNAME = new QName("http://topmotive.eu/TMConnect", "Parameters");
    private final static QName _UserTypeUsername_QNAME = new QName("http://topmotive.eu/TMConnect", "Username");
    private final static QName _UserTypeMandatorId_QNAME = new QName("http://topmotive.eu/TMConnect", "MandatorId");
    private final static QName _UserTypePassword_QNAME = new QName("http://topmotive.eu/TMConnect", "Password");
    private final static QName _CredentialsTypeSalt_QNAME = new QName("http://topmotive.eu/TMConnect", "Salt");
    private final static QName _CredentialsTypeSecurityToken_QNAME = new QName("http://topmotive.eu/TMConnect", "SecurityToken");
    private final static QName _CredentialsTypeSalesAdvisorCredentials_QNAME = new QName("http://topmotive.eu/TMConnect", "SalesAdvisorCredentials");
    private final static QName _CredentialsTypeExternalIdentityProviderId_QNAME = new QName("http://topmotive.eu/TMConnect", "ExternalIdentityProviderId");
    private final static QName _CredentialsTypeCatalogUserCredentials_QNAME = new QName("http://topmotive.eu/TMConnect", "CatalogUserCredentials");
    private final static QName _VehicleTypeModelGroup_QNAME = new QName("http://topmotive.eu/TMConnect", "ModelGroup");
    private final static QName _VehicleTypeTypeCertificateNumber_QNAME = new QName("http://topmotive.eu/TMConnect", "TypeCertificateNumber");
    private final static QName _VehicleTypeMileageShortIndicator_QNAME = new QName("http://topmotive.eu/TMConnect", "MileageShortIndicator");
    private final static QName _VehicleTypeMotorCode_QNAME = new QName("http://topmotive.eu/TMConnect", "MotorCode");
    private final static QName _VehicleTypeModel_QNAME = new QName("http://topmotive.eu/TMConnect", "Model");
    private final static QName _VehicleTypeVehicleOwner_QNAME = new QName("http://topmotive.eu/TMConnect", "VehicleOwner");
    private final static QName _VehicleTypeMake_QNAME = new QName("http://topmotive.eu/TMConnect", "Make");
    private final static QName _VehicleTypePlateId_QNAME = new QName("http://topmotive.eu/TMConnect", "PlateId");
    private final static QName _VehicleTypeVin_QNAME = new QName("http://topmotive.eu/TMConnect", "Vin");
    private final static QName _OrderCollectionTypeOrders_QNAME = new QName("http://topmotive.eu/TMConnect", "Orders");
    private final static QName _DispatchTypeTypeCurrencyCodeIso4217_QNAME = new QName("http://topmotive.eu/TMConnect", "CurrencyCode_Iso_4217");
    private final static QName _DispatchTypeTypeDispatchConditions_QNAME = new QName("http://topmotive.eu/TMConnect", "DispatchConditions");
    private final static QName _DispatchTypeTypeCurrencySymbol_QNAME = new QName("http://topmotive.eu/TMConnect", "CurrencySymbol");
    private final static QName _GetErpInformationResponseBodyTypeGetErpInformationResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetErpInformationResult");
    private final static QName _SendOrderDocumentRequestTypeDocument_QNAME = new QName("http://topmotive.eu/TMConnect", "Document");
    private final static QName _MemoTypeLabel_QNAME = new QName("http://topmotive.eu/TMConnect", "Label");
    private final static QName _FindItemsRequestTypeSearchTerm_QNAME = new QName("http://topmotive.eu/TMConnect", "SearchTerm");
    private final static QName _RepairTimesCostRateTypePricePerUnit_QNAME = new QName("http://topmotive.eu/TMConnect", "PricePerUnit");
    private final static QName _BaseResponseTypeErrorCode_QNAME = new QName("http://topmotive.eu/TMConnect", "ErrorCode");
    private final static QName _BaseResponseTypeNotifications_QNAME = new QName("http://topmotive.eu/TMConnect", "Notifications");
    private final static QName _BaseResponseTypeValidationMessages_QNAME = new QName("http://topmotive.eu/TMConnect", "ValidationMessages");
    private final static QName _GetVersionResponseBodyTypeGetVersionResult_QNAME = new QName("http://topmotive.eu/TMConnect", "GetVersionResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sagag.services.autonet.erp.wsdl.tmconnect
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link ArrayOfDispatchTypeType }
     * 
     */
    public ArrayOfDispatchTypeType createArrayOfDispatchTypeType() {
        return new ArrayOfDispatchTypeType();
    }

    /**
     * Create an instance of {@link CustomerType }
     * 
     */
    public CustomerType createCustomerType() {
        return new CustomerType();
    }

    /**
     * Create an instance of {@link FindItemsReplyType }
     * 
     */
    public FindItemsReplyType createFindItemsReplyType() {
        return new FindItemsReplyType();
    }

    /**
     * Create an instance of {@link DoWorkElement }
     * 
     */
    public DoWorkElement createDoWorkElement() {
        return new DoWorkElement();
    }

    /**
     * Create an instance of {@link ArrayOfArticleTmfType }
     * 
     */
    public ArrayOfArticleTmfType createArrayOfArticleTmfType() {
        return new ArrayOfArticleTmfType();
    }

    /**
     * Create an instance of {@link SendOrderDocumentRequestType }
     * 
     */
    public SendOrderDocumentRequestType createSendOrderDocumentRequestType() {
        return new SendOrderDocumentRequestType();
    }

    /**
     * Create an instance of {@link SetSidResponseElement }
     * 
     */
    public SetSidResponseElement createSetSidResponseElement() {
        return new SetSidResponseElement();
    }

    /**
     * Create an instance of {@link LinkedItemsCollectionType }
     * 
     */
    public LinkedItemsCollectionType createLinkedItemsCollectionType() {
        return new LinkedItemsCollectionType();
    }

    /**
     * Create an instance of {@link GetErpInformationRequestType }
     * 
     */
    public GetErpInformationRequestType createGetErpInformationRequestType() {
        return new GetErpInformationRequestType();
    }

    /**
     * Create an instance of {@link ArrayOfAddressType }
     * 
     */
    public ArrayOfAddressType createArrayOfAddressType() {
        return new ArrayOfAddressType();
    }

    /**
     * Create an instance of {@link GetErpInformationRequestBodyType }
     * 
     */
    public GetErpInformationRequestBodyType createGetErpInformationRequestBodyType() {
        return new GetErpInformationRequestBodyType();
    }

    /**
     * Create an instance of {@link ArrayOfItemsCollectionType }
     * 
     */
    public ArrayOfItemsCollectionType createArrayOfItemsCollectionType() {
        return new ArrayOfItemsCollectionType();
    }

    /**
     * Create an instance of {@link ArrayOfNotificationType }
     * 
     */
    public ArrayOfNotificationType createArrayOfNotificationType() {
        return new ArrayOfNotificationType();
    }

    /**
     * Create an instance of {@link GetUserRequestType }
     * 
     */
    public GetUserRequestType createGetUserRequestType() {
        return new GetUserRequestType();
    }

    /**
     * Create an instance of {@link ArticleType }
     * 
     */
    public ArticleType createArticleType() {
        return new ArticleType();
    }

    /**
     * Create an instance of {@link BaseDtoType }
     * 
     */
    public BaseDtoType createBaseDtoType() {
        return new BaseDtoType();
    }

    /**
     * Create an instance of {@link SetSidElement }
     * 
     */
    public SetSidElement createSetSidElement() {
        return new SetSidElement();
    }

    /**
     * Create an instance of {@link ArrayOfControlIndicatorType }
     * 
     */
    public ArrayOfControlIndicatorType createArrayOfControlIndicatorType() {
        return new ArrayOfControlIndicatorType();
    }

    /**
     * Create an instance of {@link NotificationType }
     * 
     */
    public NotificationType createNotificationType() {
        return new NotificationType();
    }

    /**
     * Create an instance of {@link ArrayOfSelectionListItemType }
     * 
     */
    public ArrayOfSelectionListItemType createArrayOfSelectionListItemType() {
        return new ArrayOfSelectionListItemType();
    }

    /**
     * Create an instance of {@link AdditionalIdentifierType }
     * 
     */
    public AdditionalIdentifierType createAdditionalIdentifierType() {
        return new AdditionalIdentifierType();
    }

    /**
     * Create an instance of {@link BaseResponseType }
     * 
     */
    public BaseResponseType createBaseResponseType() {
        return new BaseResponseType();
    }

    /**
     * Create an instance of {@link AccountDataType }
     * 
     */
    public AccountDataType createAccountDataType() {
        return new AccountDataType();
    }

    /**
     * Create an instance of {@link SendOrderRequestType }
     * 
     */
    public SendOrderRequestType createSendOrderRequestType() {
        return new SendOrderRequestType();
    }

    /**
     * Create an instance of {@link FindItemsRequestType }
     * 
     */
    public FindItemsRequestType createFindItemsRequestType() {
        return new FindItemsRequestType();
    }

    /**
     * Create an instance of {@link AvailabilityStateType }
     * 
     */
    public AvailabilityStateType createAvailabilityStateType() {
        return new AvailabilityStateType();
    }

    /**
     * Create an instance of {@link ArrayOfAvailabilityStateType }
     * 
     */
    public ArrayOfAvailabilityStateType createArrayOfAvailabilityStateType() {
        return new ArrayOfAvailabilityStateType();
    }

    /**
     * Create an instance of {@link ArrayOfAdditionalIdentifierType }
     * 
     */
    public ArrayOfAdditionalIdentifierType createArrayOfAdditionalIdentifierType() {
        return new ArrayOfAdditionalIdentifierType();
    }

    /**
     * Create an instance of {@link TourType }
     * 
     */
    public TourType createTourType() {
        return new TourType();
    }

    /**
     * Create an instance of {@link SidRequestType }
     * 
     */
    public SidRequestType createSidRequestType() {
        return new SidRequestType();
    }

    /**
     * Create an instance of {@link KeyValueItemType }
     * 
     */
    public KeyValueItemType createKeyValueItemType() {
        return new KeyValueItemType();
    }

    /**
     * Create an instance of {@link SendOrderReplyType }
     * 
     */
    public SendOrderReplyType createSendOrderReplyType() {
        return new SendOrderReplyType();
    }

    /**
     * Create an instance of {@link ArrayOfArticleType }
     * 
     */
    public ArrayOfArticleType createArrayOfArticleType() {
        return new ArrayOfArticleType();
    }

    /**
     * Create an instance of {@link ControlIndicatorType }
     * 
     */
    public ControlIndicatorType createControlIndicatorType() {
        return new ControlIndicatorType();
    }

    /**
     * Create an instance of {@link ErpInformationType }
     * 
     */
    public ErpInformationType createErpInformationType() {
        return new ErpInformationType();
    }

    /**
     * Create an instance of {@link MasterDataType }
     * 
     */
    public MasterDataType createMasterDataType() {
        return new MasterDataType();
    }

    /**
     * Create an instance of {@link ItemsCollectionType }
     * 
     */
    public ItemsCollectionType createItemsCollectionType() {
        return new ItemsCollectionType();
    }

    /**
     * Create an instance of {@link ArrayOfIconType }
     * 
     */
    public ArrayOfIconType createArrayOfIconType() {
        return new ArrayOfIconType();
    }

    /**
     * Create an instance of {@link GetErpInformationReplyType }
     * 
     */
    public GetErpInformationReplyType createGetErpInformationReplyType() {
        return new GetErpInformationReplyType();
    }

    /**
     * Create an instance of {@link AddressTypeType }
     * 
     */
    public AddressTypeType createAddressTypeType() {
        return new AddressTypeType();
    }

    /**
     * Create an instance of {@link ProductGroupTmfType }
     * 
     */
    public ProductGroupTmfType createProductGroupTmfType() {
        return new ProductGroupTmfType();
    }

    /**
     * Create an instance of {@link GetAccountInformationReplyType }
     * 
     */
    public GetAccountInformationReplyType createGetAccountInformationReplyType() {
        return new GetAccountInformationReplyType();
    }

    /**
     * Create an instance of {@link CredentialsType }
     * 
     */
    public CredentialsType createCredentialsType() {
        return new CredentialsType();
    }

    /**
     * Create an instance of {@link MemoType }
     * 
     */
    public MemoType createMemoType() {
        return new MemoType();
    }

    /**
     * Create an instance of {@link ArrayOfPriceType }
     * 
     */
    public ArrayOfPriceType createArrayOfPriceType() {
        return new ArrayOfPriceType();
    }

    /**
     * Create an instance of {@link ValidationMessageType }
     * 
     */
    public ValidationMessageType createValidationMessageType() {
        return new ValidationMessageType();
    }

    /**
     * Create an instance of {@link ArrayOfVehicleType }
     * 
     */
    public ArrayOfVehicleType createArrayOfVehicleType() {
        return new ArrayOfVehicleType();
    }

    /**
     * Create an instance of {@link GetErpInformationResponseBodyType }
     * 
     */
    public GetErpInformationResponseBodyType createGetErpInformationResponseBodyType() {
        return new GetErpInformationResponseBodyType();
    }

    /**
     * Create an instance of {@link ArrayOfEntityLinkType }
     * 
     */
    public ArrayOfEntityLinkType createArrayOfEntityLinkType() {
        return new ArrayOfEntityLinkType();
    }

    /**
     * Create an instance of {@link VehicleType }
     * 
     */
    public VehicleType createVehicleType() {
        return new VehicleType();
    }

    /**
     * Create an instance of {@link ArrayOfOrderType }
     * 
     */
    public ArrayOfOrderType createArrayOfOrderType() {
        return new ArrayOfOrderType();
    }

    /**
     * Create an instance of {@link ServiceConfigurationType }
     * 
     */
    public ServiceConfigurationType createServiceConfigurationType() {
        return new ServiceConfigurationType();
    }

    /**
     * Create an instance of {@link DispatchTypeType }
     * 
     */
    public DispatchTypeType createDispatchTypeType() {
        return new DispatchTypeType();
    }

    /**
     * Create an instance of {@link ArrayOfOrderPositionType }
     * 
     */
    public ArrayOfOrderPositionType createArrayOfOrderPositionType() {
        return new ArrayOfOrderPositionType();
    }

    /**
     * Create an instance of {@link DoWorkResponseElement }
     * 
     */
    public DoWorkResponseElement createDoWorkResponseElement() {
        return new DoWorkResponseElement();
    }

    /**
     * Create an instance of {@link ArrayOfRepairTimeType }
     * 
     */
    public ArrayOfRepairTimeType createArrayOfRepairTimeType() {
        return new ArrayOfRepairTimeType();
    }

    /**
     * Create an instance of {@link ArrayOfTourType }
     * 
     */
    public ArrayOfTourType createArrayOfTourType() {
        return new ArrayOfTourType();
    }

    /**
     * Create an instance of {@link GetUserElement }
     * 
     */
    public GetUserElement createGetUserElement() {
        return new GetUserElement();
    }

    /**
     * Create an instance of {@link ArrayOfLinkedItemsCollectionType }
     * 
     */
    public ArrayOfLinkedItemsCollectionType createArrayOfLinkedItemsCollectionType() {
        return new ArrayOfLinkedItemsCollectionType();
    }

    /**
     * Create an instance of {@link OrderType }
     * 
     */
    public OrderType createOrderType() {
        return new OrderType();
    }

    /**
     * Create an instance of {@link ArrayOfTextBlockType }
     * 
     */
    public ArrayOfTextBlockType createArrayOfTextBlockType() {
        return new ArrayOfTextBlockType();
    }

    /**
     * Create an instance of {@link GetNotificationReplyType }
     * 
     */
    public GetNotificationReplyType createGetNotificationReplyType() {
        return new GetNotificationReplyType();
    }

    /**
     * Create an instance of {@link GetSessionReplyType }
     * 
     */
    public GetSessionReplyType createGetSessionReplyType() {
        return new GetSessionReplyType();
    }

    /**
     * Create an instance of {@link OrderIdType }
     * 
     */
    public OrderIdType createOrderIdType() {
        return new OrderIdType();
    }

    /**
     * Create an instance of {@link GetVersionResponseBodyType }
     * 
     */
    public GetVersionResponseBodyType createGetVersionResponseBodyType() {
        return new GetVersionResponseBodyType();
    }

    /**
     * Create an instance of {@link SelectionListItemType }
     * 
     */
    public SelectionListItemType createSelectionListItemType() {
        return new SelectionListItemType();
    }

    /**
     * Create an instance of {@link ArrayOfAccountDataType }
     * 
     */
    public ArrayOfAccountDataType createArrayOfAccountDataType() {
        return new ArrayOfAccountDataType();
    }

    /**
     * Create an instance of {@link PriceType }
     * 
     */
    public PriceType createPriceType() {
        return new PriceType();
    }

    /**
     * Create an instance of {@link OrderPositionType }
     * 
     */
    public OrderPositionType createOrderPositionType() {
        return new OrderPositionType();
    }

    /**
     * Create an instance of {@link BaseRequestType }
     * 
     */
    public BaseRequestType createBaseRequestType() {
        return new BaseRequestType();
    }

    /**
     * Create an instance of {@link ArrayOfMemoType }
     * 
     */
    public ArrayOfMemoType createArrayOfMemoType() {
        return new ArrayOfMemoType();
    }

    /**
     * Create an instance of {@link ArrayOfErpInformationType }
     * 
     */
    public ArrayOfErpInformationType createArrayOfErpInformationType() {
        return new ArrayOfErpInformationType();
    }

    /**
     * Create an instance of {@link SelectionListType }
     * 
     */
    public SelectionListType createSelectionListType() {
        return new SelectionListType();
    }

    /**
     * Create an instance of {@link IconType }
     * 
     */
    public IconType createIconType() {
        return new IconType();
    }

    /**
     * Create an instance of {@link SupplierTmfType }
     * 
     */
    public SupplierTmfType createSupplierTmfType() {
        return new SupplierTmfType();
    }

    /**
     * Create an instance of {@link TextBlockType }
     * 
     */
    public TextBlockType createTextBlockType() {
        return new TextBlockType();
    }

    /**
     * Create an instance of {@link ArrayOfProductGroupTmfType }
     * 
     */
    public ArrayOfProductGroupTmfType createArrayOfProductGroupTmfType() {
        return new ArrayOfProductGroupTmfType();
    }

    /**
     * Create an instance of {@link WarehouseType }
     * 
     */
    public WarehouseType createWarehouseType() {
        return new WarehouseType();
    }

    /**
     * Create an instance of {@link ArrayOfUserDefinedDataType }
     * 
     */
    public ArrayOfUserDefinedDataType createArrayOfUserDefinedDataType() {
        return new ArrayOfUserDefinedDataType();
    }

    /**
     * Create an instance of {@link GetItemsCollectionReplyType }
     * 
     */
    public GetItemsCollectionReplyType createGetItemsCollectionReplyType() {
        return new GetItemsCollectionReplyType();
    }

    /**
     * Create an instance of {@link UserType }
     * 
     */
    public UserType createUserType() {
        return new UserType();
    }

    /**
     * Create an instance of {@link ArrayOfOrderIdType }
     * 
     */
    public ArrayOfOrderIdType createArrayOfOrderIdType() {
        return new ArrayOfOrderIdType();
    }

    /**
     * Create an instance of {@link ArrayOfCustomerType }
     * 
     */
    public ArrayOfCustomerType createArrayOfCustomerType() {
        return new ArrayOfCustomerType();
    }

    /**
     * Create an instance of {@link GetItemsCollectionRequestType }
     * 
     */
    public GetItemsCollectionRequestType createGetItemsCollectionRequestType() {
        return new GetItemsCollectionRequestType();
    }

    /**
     * Create an instance of {@link ArrayOfKeyValueItemType }
     * 
     */
    public ArrayOfKeyValueItemType createArrayOfKeyValueItemType() {
        return new ArrayOfKeyValueItemType();
    }

    /**
     * Create an instance of {@link ArrayOfSelectionListType }
     * 
     */
    public ArrayOfSelectionListType createArrayOfSelectionListType() {
        return new ArrayOfSelectionListType();
    }

    /**
     * Create an instance of {@link GetNotificationRequestType }
     * 
     */
    public GetNotificationRequestType createGetNotificationRequestType() {
        return new GetNotificationRequestType();
    }

    /**
     * Create an instance of {@link ArrayOfValidationMessageType }
     * 
     */
    public ArrayOfValidationMessageType createArrayOfValidationMessageType() {
        return new ArrayOfValidationMessageType();
    }

    /**
     * Create an instance of {@link UserDefinedDataType }
     * 
     */
    public UserDefinedDataType createUserDefinedDataType() {
        return new UserDefinedDataType();
    }

    /**
     * Create an instance of {@link ArrayOfWarehouseType }
     * 
     */
    public ArrayOfWarehouseType createArrayOfWarehouseType() {
        return new ArrayOfWarehouseType();
    }

    /**
     * Create an instance of {@link QuantityType }
     * 
     */
    public QuantityType createQuantityType() {
        return new QuantityType();
    }

    /**
     * Create an instance of {@link ArrayOfQuantityType }
     * 
     */
    public ArrayOfQuantityType createArrayOfQuantityType() {
        return new ArrayOfQuantityType();
    }

    /**
     * Create an instance of {@link OrderCollectionType }
     * 
     */
    public OrderCollectionType createOrderCollectionType() {
        return new OrderCollectionType();
    }

    /**
     * Create an instance of {@link ArticleTmfType }
     * 
     */
    public ArticleTmfType createArticleTmfType() {
        return new ArticleTmfType();
    }

    /**
     * Create an instance of {@link RepairTimesCostRateType }
     * 
     */
    public RepairTimesCostRateType createRepairTimesCostRateType() {
        return new RepairTimesCostRateType();
    }

    /**
     * Create an instance of {@link EntityLinkType }
     * 
     */
    public EntityLinkType createEntityLinkType() {
        return new EntityLinkType();
    }

    /**
     * Create an instance of {@link GetUserResponseElement }
     * 
     */
    public GetUserResponseElement createGetUserResponseElement() {
        return new GetUserResponseElement();
    }

    /**
     * Create an instance of {@link RepairTimeType }
     * 
     */
    public RepairTimeType createRepairTimeType() {
        return new RepairTimeType();
    }

    /**
     * Create an instance of {@link GetUserReplyType }
     * 
     */
    public GetUserReplyType createGetUserReplyType() {
        return new GetUserReplyType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticleTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfArticleTmf")
    public JAXBElement<ArrayOfArticleTmfType> createArrayOfArticleTmf(ArrayOfArticleTmfType value) {
        return new JAXBElement<ArrayOfArticleTmfType>(_ArrayOfArticleTmf_QNAME, ArrayOfArticleTmfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Customer")
    public JAXBElement<CustomerType> createCustomer(CustomerType value) {
        return new JAXBElement<CustomerType>(_Customer_QNAME, CustomerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Address")
    public JAXBElement<AddressType> createAddress(AddressType value) {
        return new JAXBElement<AddressType>(_Address_QNAME, AddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDispatchTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfDispatchType")
    public JAXBElement<ArrayOfDispatchTypeType> createArrayOfDispatchType(ArrayOfDispatchTypeType value) {
        return new JAXBElement<ArrayOfDispatchTypeType>(_ArrayOfDispatchType_QNAME, ArrayOfDispatchTypeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsReply")
    public JAXBElement<FindItemsReplyType> createFindItemsReply(FindItemsReplyType value) {
        return new JAXBElement<FindItemsReplyType>(_FindItemsReply_QNAME, FindItemsReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequestBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformation")
    public JAXBElement<GetErpInformationRequestBodyType> createGetErpInformation(GetErpInformationRequestBodyType value) {
        return new JAXBElement<GetErpInformationRequestBodyType>(_GetErpInformation_QNAME, GetErpInformationRequestBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfItemsCollection")
    public JAXBElement<ArrayOfItemsCollectionType> createArrayOfItemsCollection(ArrayOfItemsCollectionType value) {
        return new JAXBElement<ArrayOfItemsCollectionType>(_ArrayOfItemsCollection_QNAME, ArrayOfItemsCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNotificationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfNotification")
    public JAXBElement<ArrayOfNotificationType> createArrayOfNotification(ArrayOfNotificationType value) {
        return new JAXBElement<ArrayOfNotificationType>(_ArrayOfNotification_QNAME, ArrayOfNotificationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderDocumentRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderDocumentRequest")
    public JAXBElement<SendOrderDocumentRequestType> createSendOrderDocumentRequest(SendOrderDocumentRequestType value) {
        return new JAXBElement<SendOrderDocumentRequestType>(_SendOrderDocumentRequest_QNAME, SendOrderDocumentRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LinkedItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItemsCollection")
    public JAXBElement<LinkedItemsCollectionType> createLinkedItemsCollection(LinkedItemsCollectionType value) {
        return new JAXBElement<LinkedItemsCollectionType>(_LinkedItemsCollection_QNAME, LinkedItemsCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationRequest")
    public JAXBElement<GetErpInformationRequestType> createGetErpInformationRequest(GetErpInformationRequestType value) {
        return new JAXBElement<GetErpInformationRequestType>(_GetErpInformationRequest_QNAME, GetErpInformationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAddress")
    public JAXBElement<ArrayOfAddressType> createArrayOfAddress(ArrayOfAddressType value) {
        return new JAXBElement<ArrayOfAddressType>(_ArrayOfAddress_QNAME, ArrayOfAddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIdentifier")
    public JAXBElement<AdditionalIdentifierType> createAdditionalIdentifier(AdditionalIdentifierType value) {
        return new JAXBElement<AdditionalIdentifierType>(_AdditionalIdentifier_QNAME, AdditionalIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "BaseResponse")
    public JAXBElement<BaseResponseType> createBaseResponse(BaseResponseType value) {
        return new JAXBElement<BaseResponseType>(_BaseResponse_QNAME, BaseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AccountData")
    public JAXBElement<AccountDataType> createAccountData(AccountDataType value) {
        return new JAXBElement<AccountDataType>(_AccountData_QNAME, AccountDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderRequest")
    public JAXBElement<SendOrderRequestType> createSendOrderRequest(SendOrderRequestType value) {
        return new JAXBElement<SendOrderRequestType>(_SendOrderRequest_QNAME, SendOrderRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersion")
    public JAXBElement<GetVersionRequestBodyType> createGetVersion(GetVersionRequestBodyType value) {
        return new JAXBElement<GetVersionRequestBodyType>(_GetVersion_QNAME, GetVersionRequestBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindItemsRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FindItemsRequest")
    public JAXBElement<FindItemsRequestType> createFindItemsRequest(FindItemsRequestType value) {
        return new JAXBElement<FindItemsRequestType>(_FindItemsRequest_QNAME, FindItemsRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AvailabilityStateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityState")
    public JAXBElement<AvailabilityStateType> createAvailabilityState(AvailabilityStateType value) {
        return new JAXBElement<AvailabilityStateType>(_AvailabilityState_QNAME, AvailabilityStateType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityStateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAvailabilityState")
    public JAXBElement<ArrayOfAvailabilityStateType> createArrayOfAvailabilityState(ArrayOfAvailabilityStateType value) {
        return new JAXBElement<ArrayOfAvailabilityStateType>(_ArrayOfAvailabilityState_QNAME, ArrayOfAvailabilityStateType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAdditionalIdentifier")
    public JAXBElement<ArrayOfAdditionalIdentifierType> createArrayOfAdditionalIdentifier(ArrayOfAdditionalIdentifierType value) {
        return new JAXBElement<ArrayOfAdditionalIdentifierType>(_ArrayOfAdditionalIdentifier_QNAME, ArrayOfAdditionalIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TourType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour")
    public JAXBElement<TourType> createTour(TourType value) {
        return new JAXBElement<TourType>(_Tour_QNAME, TourType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SidRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SidRequest")
    public JAXBElement<SidRequestType> createSidRequest(SidRequestType value) {
        return new JAXBElement<SidRequestType>(_SidRequest_QNAME, SidRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetUserRequest")
    public JAXBElement<GetUserRequestType> createGetUserRequest(GetUserRequestType value) {
        return new JAXBElement<GetUserRequestType>(_GetUserRequest_QNAME, GetUserRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Article")
    public JAXBElement<ArticleType> createArticle(ArticleType value) {
        return new JAXBElement<ArticleType>(_Article_QNAME, ArticleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseDtoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "BaseDto")
    public JAXBElement<BaseDtoType> createBaseDto(BaseDtoType value) {
        return new JAXBElement<BaseDtoType>(_BaseDto_QNAME, BaseDtoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfControlIndicator")
    public JAXBElement<ArrayOfControlIndicatorType> createArrayOfControlIndicator(ArrayOfControlIndicatorType value) {
        return new JAXBElement<ArrayOfControlIndicatorType>(_ArrayOfControlIndicator_QNAME, ArrayOfControlIndicatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotificationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Notification")
    public JAXBElement<NotificationType> createNotification(NotificationType value) {
        return new JAXBElement<NotificationType>(_Notification_QNAME, NotificationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfSelectionListItem")
    public JAXBElement<ArrayOfSelectionListItemType> createArrayOfSelectionListItem(ArrayOfSelectionListItemType value) {
        return new JAXBElement<ArrayOfSelectionListItemType>(_ArrayOfSelectionListItem_QNAME, ArrayOfSelectionListItemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErpInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpInformation")
    public JAXBElement<ErpInformationType> createErpInformation(ErpInformationType value) {
        return new JAXBElement<ErpInformationType>(_ErpInformation_QNAME, ErpInformationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData")
    public JAXBElement<MasterDataType> createMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ItemsCollection")
    public JAXBElement<ItemsCollectionType> createItemsCollection(ItemsCollectionType value) {
        return new JAXBElement<ItemsCollectionType>(_ItemsCollection_QNAME, ItemsCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIconType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfIcon")
    public JAXBElement<ArrayOfIconType> createArrayOfIcon(ArrayOfIconType value) {
        return new JAXBElement<ArrayOfIconType>(_ArrayOfIcon_QNAME, ArrayOfIconType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationReply")
    public JAXBElement<GetErpInformationReplyType> createGetErpInformationReply(GetErpInformationReplyType value) {
        return new JAXBElement<GetErpInformationReplyType>(_GetErpInformationReply_QNAME, GetErpInformationReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProductGroupTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ProductGroupTmf")
    public JAXBElement<ProductGroupTmfType> createProductGroupTmf(ProductGroupTmfType value) {
        return new JAXBElement<ProductGroupTmfType>(_ProductGroupTmf_QNAME, ProductGroupTmfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountInformationReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetAccountInformationReply")
    public JAXBElement<GetAccountInformationReplyType> createGetAccountInformationReply(GetAccountInformationReplyType value) {
        return new JAXBElement<GetAccountInformationReplyType>(_GetAccountInformationReply_QNAME, GetAccountInformationReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AddressType")
    public JAXBElement<AddressTypeType> createAddressType(AddressTypeType value) {
        return new JAXBElement<AddressTypeType>(_AddressType_QNAME, AddressTypeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CredentialsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Credentials")
    public JAXBElement<CredentialsType> createCredentials(CredentialsType value) {
        return new JAXBElement<CredentialsType>(_Credentials_QNAME, CredentialsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memo")
    public JAXBElement<MemoType> createMemo(MemoType value) {
        return new JAXBElement<MemoType>(_Memo_QNAME, MemoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "KeyValueItem")
    public JAXBElement<KeyValueItemType> createKeyValueItem(KeyValueItemType value) {
        return new JAXBElement<KeyValueItemType>(_KeyValueItem_QNAME, KeyValueItemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOrderReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SendOrderReply")
    public JAXBElement<SendOrderReplyType> createSendOrderReply(SendOrderReplyType value) {
        return new JAXBElement<SendOrderReplyType>(_SendOrderReply_QNAME, SendOrderReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfArticle")
    public JAXBElement<ArrayOfArticleType> createArrayOfArticle(ArrayOfArticleType value) {
        return new JAXBElement<ArrayOfArticleType>(_ArrayOfArticle_QNAME, ArrayOfArticleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlIndicatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicator")
    public JAXBElement<ControlIndicatorType> createControlIndicator(ControlIndicatorType value) {
        return new JAXBElement<ControlIndicatorType>(_ControlIndicator_QNAME, ControlIndicatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfEntityLink")
    public JAXBElement<ArrayOfEntityLinkType> createArrayOfEntityLink(ArrayOfEntityLinkType value) {
        return new JAXBElement<ArrayOfEntityLinkType>(_ArrayOfEntityLink_QNAME, ArrayOfEntityLinkType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VehicleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicle")
    public JAXBElement<VehicleType> createVehicle(VehicleType value) {
        return new JAXBElement<VehicleType>(_Vehicle_QNAME, VehicleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfOrder")
    public JAXBElement<ArrayOfOrderType> createArrayOfOrder(ArrayOfOrderType value) {
        return new JAXBElement<ArrayOfOrderType>(_ArrayOfOrder_QNAME, ArrayOfOrderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DispatchType")
    public JAXBElement<DispatchTypeType> createDispatchType(DispatchTypeType value) {
        return new JAXBElement<DispatchTypeType>(_DispatchType_QNAME, DispatchTypeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceConfigurationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ServiceConfiguration")
    public JAXBElement<ServiceConfigurationType> createServiceConfiguration(ServiceConfigurationType value) {
        return new JAXBElement<ServiceConfigurationType>(_ServiceConfiguration_QNAME, ServiceConfigurationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderPositionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfOrderPosition")
    public JAXBElement<ArrayOfOrderPositionType> createArrayOfOrderPosition(ArrayOfOrderPositionType value) {
        return new JAXBElement<ArrayOfOrderPositionType>(_ArrayOfOrderPosition_QNAME, ArrayOfOrderPositionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRepairTimeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfRepairTime")
    public JAXBElement<ArrayOfRepairTimeType> createArrayOfRepairTime(ArrayOfRepairTimeType value) {
        return new JAXBElement<ArrayOfRepairTimeType>(_ArrayOfRepairTime_QNAME, ArrayOfRepairTimeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTourType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfTour")
    public JAXBElement<ArrayOfTourType> createArrayOfTour(ArrayOfTourType value) {
        return new JAXBElement<ArrayOfTourType>(_ArrayOfTour_QNAME, ArrayOfTourType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPriceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfPrice")
    public JAXBElement<ArrayOfPriceType> createArrayOfPrice(ArrayOfPriceType value) {
        return new JAXBElement<ArrayOfPriceType>(_ArrayOfPrice_QNAME, ArrayOfPriceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidationMessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ValidationMessage")
    public JAXBElement<ValidationMessageType> createValidationMessage(ValidationMessageType value) {
        return new JAXBElement<ValidationMessageType>(_ValidationMessage_QNAME, ValidationMessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVehicleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfVehicle")
    public JAXBElement<ArrayOfVehicleType> createArrayOfVehicle(ArrayOfVehicleType value) {
        return new JAXBElement<ArrayOfVehicleType>(_ArrayOfVehicle_QNAME, ArrayOfVehicleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationResponseBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationResponse")
    public JAXBElement<GetErpInformationResponseBodyType> createGetErpInformationResponse(GetErpInformationResponseBodyType value) {
        return new JAXBElement<GetErpInformationResponseBodyType>(_GetErpInformationResponse_QNAME, GetErpInformationResponseBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationResponseBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationResponseBody")
    public JAXBElement<GetErpInformationResponseBodyType> createGetErpInformationResponseBody(GetErpInformationResponseBodyType value) {
        return new JAXBElement<GetErpInformationResponseBodyType>(_GetErpInformationResponseBody_QNAME, GetErpInformationResponseBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAccountDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfAccountData")
    public JAXBElement<ArrayOfAccountDataType> createArrayOfAccountData(ArrayOfAccountDataType value) {
        return new JAXBElement<ArrayOfAccountDataType>(_ArrayOfAccountData_QNAME, ArrayOfAccountDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PriceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Price")
    public JAXBElement<PriceType> createPrice(PriceType value) {
        return new JAXBElement<PriceType>(_Price_QNAME, PriceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderPositionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderPosition")
    public JAXBElement<OrderPositionType> createOrderPosition(OrderPositionType value) {
        return new JAXBElement<OrderPositionType>(_OrderPosition_QNAME, OrderPositionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfMemo")
    public JAXBElement<ArrayOfMemoType> createArrayOfMemo(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_ArrayOfMemo_QNAME, ArrayOfMemoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfErpInformation")
    public JAXBElement<ArrayOfErpInformationType> createArrayOfErpInformation(ArrayOfErpInformationType value) {
        return new JAXBElement<ArrayOfErpInformationType>(_ArrayOfErpInformation_QNAME, ArrayOfErpInformationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "BaseRequest")
    public JAXBElement<BaseRequestType> createBaseRequest(BaseRequestType value) {
        return new JAXBElement<BaseRequestType>(_BaseRequest_QNAME, BaseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IconType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icon")
    public JAXBElement<IconType> createIcon(IconType value) {
        return new JAXBElement<IconType>(_Icon_QNAME, IconType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectionListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionList")
    public JAXBElement<SelectionListType> createSelectionList(SelectionListType value) {
        return new JAXBElement<SelectionListType>(_SelectionList_QNAME, SelectionListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TextBlockType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TextBlock")
    public JAXBElement<TextBlockType> createTextBlock(TextBlockType value) {
        return new JAXBElement<TextBlockType>(_TextBlock_QNAME, TextBlockType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProductGroupTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfProductGroupTmf")
    public JAXBElement<ArrayOfProductGroupTmfType> createArrayOfProductGroupTmf(ArrayOfProductGroupTmfType value) {
        return new JAXBElement<ArrayOfProductGroupTmfType>(_ArrayOfProductGroupTmf_QNAME, ArrayOfProductGroupTmfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SupplierTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SupplierTmf")
    public JAXBElement<SupplierTmfType> createSupplierTmf(SupplierTmfType value) {
        return new JAXBElement<SupplierTmfType>(_SupplierTmf_QNAME, SupplierTmfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLinkedItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfLinkedItemsCollection")
    public JAXBElement<ArrayOfLinkedItemsCollectionType> createArrayOfLinkedItemsCollection(ArrayOfLinkedItemsCollectionType value) {
        return new JAXBElement<ArrayOfLinkedItemsCollectionType>(_ArrayOfLinkedItemsCollection_QNAME, ArrayOfLinkedItemsCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Order")
    public JAXBElement<OrderType> createOrder(OrderType value) {
        return new JAXBElement<OrderType>(_Order_QNAME, OrderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTextBlockType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfTextBlock")
    public JAXBElement<ArrayOfTextBlockType> createArrayOfTextBlock(ArrayOfTextBlockType value) {
        return new JAXBElement<ArrayOfTextBlockType>(_ArrayOfTextBlock_QNAME, ArrayOfTextBlockType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationReply")
    public JAXBElement<GetNotificationReplyType> createGetNotificationReply(GetNotificationReplyType value) {
        return new JAXBElement<GetNotificationReplyType>(_GetNotificationReply_QNAME, GetNotificationReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequestBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationRequestBody")
    public JAXBElement<GetErpInformationRequestBodyType> createGetErpInformationRequestBody(GetErpInformationRequestBodyType value) {
        return new JAXBElement<GetErpInformationRequestBodyType>(_GetErpInformationRequestBody_QNAME, GetErpInformationRequestBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetSessionReply")
    public JAXBElement<GetSessionReplyType> createGetSessionReply(GetSessionReplyType value) {
        return new JAXBElement<GetSessionReplyType>(_GetSessionReply_QNAME, GetSessionReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderId")
    public JAXBElement<OrderIdType> createOrderId(OrderIdType value) {
        return new JAXBElement<OrderIdType>(_OrderId_QNAME, OrderIdType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectionListItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionListItem")
    public JAXBElement<SelectionListItemType> createSelectionListItem(SelectionListItemType value) {
        return new JAXBElement<SelectionListItemType>(_SelectionListItem_QNAME, SelectionListItemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersionResponse")
    public JAXBElement<GetVersionResponseBodyType> createGetVersionResponse(GetVersionResponseBodyType value) {
        return new JAXBElement<GetVersionResponseBodyType>(_GetVersionResponse_QNAME, GetVersionResponseBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfKeyValueItem")
    public JAXBElement<ArrayOfKeyValueItemType> createArrayOfKeyValueItem(ArrayOfKeyValueItemType value) {
        return new JAXBElement<ArrayOfKeyValueItemType>(_ArrayOfKeyValueItem_QNAME, ArrayOfKeyValueItemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersionResponseBody")
    public JAXBElement<GetVersionResponseBodyType> createGetVersionResponseBody(GetVersionResponseBodyType value) {
        return new JAXBElement<GetVersionResponseBodyType>(_GetVersionResponseBody_QNAME, GetVersionResponseBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfSelectionList")
    public JAXBElement<ArrayOfSelectionListType> createArrayOfSelectionList(ArrayOfSelectionListType value) {
        return new JAXBElement<ArrayOfSelectionListType>(_ArrayOfSelectionList_QNAME, ArrayOfSelectionListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotificationRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetNotificationRequest")
    public JAXBElement<GetNotificationRequestType> createGetNotificationRequest(GetNotificationRequestType value) {
        return new JAXBElement<GetNotificationRequestType>(_GetNotificationRequest_QNAME, GetNotificationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "User")
    public JAXBElement<UserType> createUser(UserType value) {
        return new JAXBElement<UserType>(_User_QNAME, UserType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WarehouseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Warehouse")
    public JAXBElement<WarehouseType> createWarehouse(WarehouseType value) {
        return new JAXBElement<WarehouseType>(_Warehouse_QNAME, WarehouseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfUserDefinedData")
    public JAXBElement<ArrayOfUserDefinedDataType> createArrayOfUserDefinedData(ArrayOfUserDefinedDataType value) {
        return new JAXBElement<ArrayOfUserDefinedDataType>(_ArrayOfUserDefinedData_QNAME, ArrayOfUserDefinedDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionReply")
    public JAXBElement<GetItemsCollectionReplyType> createGetItemsCollectionReply(GetItemsCollectionReplyType value) {
        return new JAXBElement<GetItemsCollectionReplyType>(_GetItemsCollectionReply_QNAME, GetItemsCollectionReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfOrderId")
    public JAXBElement<ArrayOfOrderIdType> createArrayOfOrderId(ArrayOfOrderIdType value) {
        return new JAXBElement<ArrayOfOrderIdType>(_ArrayOfOrderId_QNAME, ArrayOfOrderIdType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomerType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfCustomer")
    public JAXBElement<ArrayOfCustomerType> createArrayOfCustomer(ArrayOfCustomerType value) {
        return new JAXBElement<ArrayOfCustomerType>(_ArrayOfCustomer_QNAME, ArrayOfCustomerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsCollectionRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetItemsCollectionRequest")
    public JAXBElement<GetItemsCollectionRequestType> createGetItemsCollectionRequest(GetItemsCollectionRequestType value) {
        return new JAXBElement<GetItemsCollectionRequestType>(_GetItemsCollectionRequest_QNAME, GetItemsCollectionRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection")
    public JAXBElement<OrderCollectionType> createOrderCollection(OrderCollectionType value) {
        return new JAXBElement<OrderCollectionType>(_OrderCollection_QNAME, OrderCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticleTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleTmf")
    public JAXBElement<ArticleTmfType> createArticleTmf(ArticleTmfType value) {
        return new JAXBElement<ArticleTmfType>(_ArticleTmf_QNAME, ArticleTmfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQuantityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfQuantity")
    public JAXBElement<ArrayOfQuantityType> createArrayOfQuantity(ArrayOfQuantityType value) {
        return new JAXBElement<ArrayOfQuantityType>(_ArrayOfQuantity_QNAME, ArrayOfQuantityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepairTimesCostRateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "RepairTimesCostRate")
    public JAXBElement<RepairTimesCostRateType> createRepairTimesCostRate(RepairTimesCostRateType value) {
        return new JAXBElement<RepairTimesCostRateType>(_RepairTimesCostRate_QNAME, RepairTimesCostRateType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "EntityLink")
    public JAXBElement<EntityLinkType> createEntityLink(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_EntityLink_QNAME, EntityLinkType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EMasterDataTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "EMasterDataType")
    public JAXBElement<EMasterDataTypeType> createEMasterDataType(EMasterDataTypeType value) {
        return new JAXBElement<EMasterDataTypeType>(_EMasterDataType_QNAME, EMasterDataTypeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepairTimeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "RepairTime")
    public JAXBElement<RepairTimeType> createRepairTime(RepairTimeType value) {
        return new JAXBElement<RepairTimeType>(_RepairTime_QNAME, RepairTimeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetUserReply")
    public JAXBElement<GetUserReplyType> createGetUserReply(GetUserReplyType value) {
        return new JAXBElement<GetUserReplyType>(_GetUserReply_QNAME, GetUserReplyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfValidationMessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfValidationMessage")
    public JAXBElement<ArrayOfValidationMessageType> createArrayOfValidationMessage(ArrayOfValidationMessageType value) {
        return new JAXBElement<ArrayOfValidationMessageType>(_ArrayOfValidationMessage_QNAME, ArrayOfValidationMessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserDefinedDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UserDefinedData")
    public JAXBElement<UserDefinedDataType> createUserDefinedData(UserDefinedDataType value) {
        return new JAXBElement<UserDefinedDataType>(_UserDefinedData_QNAME, UserDefinedDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfWarehouseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArrayOfWarehouse")
    public JAXBElement<ArrayOfWarehouseType> createArrayOfWarehouse(ArrayOfWarehouseType value) {
        return new JAXBElement<ArrayOfWarehouseType>(_ArrayOfWarehouse_QNAME, ArrayOfWarehouseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuantityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Quantity")
    public JAXBElement<QuantityType> createQuantity(QuantityType value) {
        return new JAXBElement<QuantityType>(_Quantity_QNAME, QuantityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetUserResult", scope = GetUserResponseElement.class)
    public JAXBElement<GetUserResponseType> createGetUserResponseElementGetUserResult(GetUserResponseType value) {
        return new JAXBElement<GetUserResponseType>(_GetUserResponseElementGetUserResult_QNAME, GetUserResponseType.class, GetUserResponseElement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = WarehouseType.class)
    public JAXBElement<String> createWarehouseTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = WarehouseType.class)
    public JAXBElement<ArrayOfMemoType> createWarehouseTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = WarehouseType.class)
    public JAXBElement<String> createWarehouseTypeId(String value) {
        return new JAXBElement<String>(_WarehouseTypeId_QNAME, String.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQuantityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Quantities", scope = WarehouseType.class)
    public JAXBElement<ArrayOfQuantityType> createWarehouseTypeQuantities(ArrayOfQuantityType value) {
        return new JAXBElement<ArrayOfQuantityType>(_WarehouseTypeQuantities_QNAME, ArrayOfQuantityType.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Name", scope = WarehouseType.class)
    public JAXBElement<String> createWarehouseTypeName(String value) {
        return new JAXBElement<String>(_WarehouseTypeName_QNAME, String.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = WarehouseType.class)
    public JAXBElement<String> createWarehouseTypeShortDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeShortDescription_QNAME, String.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = WarehouseType.class)
    public JAXBElement<EntityLinkType> createWarehouseTypeTour(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_Tour_QNAME, EntityLinkType.class, WarehouseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = IconType.class)
    public JAXBElement<String> createIconTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, IconType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkUrl", scope = IconType.class)
    public JAXBElement<String> createIconTypeLinkUrl(String value) {
        return new JAXBElement<String>(_IconTypeLinkUrl_QNAME, String.class, IconType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Url", scope = IconType.class)
    public JAXBElement<String> createIconTypeUrl(String value) {
        return new JAXBElement<String>(_IconTypeUrl_QNAME, String.class, IconType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TopmotiveId", scope = IconType.class)
    public JAXBElement<String> createIconTypeTopmotiveId(String value) {
        return new JAXBElement<String>(_IconTypeTopmotiveId_QNAME, String.class, IconType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIdentifiers", scope = BaseDtoType.class)
    public JAXBElement<ArrayOfAdditionalIdentifierType> createBaseDtoTypeAdditionalIdentifiers(ArrayOfAdditionalIdentifierType value) {
        return new JAXBElement<ArrayOfAdditionalIdentifierType>(_BaseDtoTypeAdditionalIdentifiers_QNAME, ArrayOfAdditionalIdentifierType.class, BaseDtoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntity", scope = ValidationMessageType.class)
    public JAXBElement<EntityLinkType> createValidationMessageTypeLinkedEntity(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_ValidationMessageTypeLinkedEntity_QNAME, EntityLinkType.class, ValidationMessageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Message", scope = ValidationMessageType.class)
    public JAXBElement<String> createValidationMessageTypeMessage(String value) {
        return new JAXBElement<String>(_ValidationMessageTypeMessage_QNAME, String.class, ValidationMessageType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = GetErpInformationRequestType.class)
    public JAXBElement<MasterDataType> createGetErpInformationRequestTypeMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, GetErpInformationRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpArticleInformation", scope = GetErpInformationRequestType.class)
    public JAXBElement<ArrayOfErpInformationType> createGetErpInformationRequestTypeErpArticleInformation(ArrayOfErpInformationType value) {
        return new JAXBElement<ArrayOfErpInformationType>(_GetErpInformationRequestTypeErpArticleInformation_QNAME, ArrayOfErpInformationType.class, GetErpInformationRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = ArticleType.class)
    public JAXBElement<String> createArticleTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, ArticleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = ArticleType.class)
    public JAXBElement<String> createArticleTypeId(String value) {
        return new JAXBElement<String>(_WarehouseTypeId_QNAME, String.class, ArticleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleIdErp", scope = ArticleType.class)
    public JAXBElement<String> createArticleTypeArticleIdErp(String value) {
        return new JAXBElement<String>(_ArticleTypeArticleIdErp_QNAME, String.class, ArticleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = UserDefinedDataType.class)
    public JAXBElement<String> createUserDefinedDataTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, UserDefinedDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = UserDefinedDataType.class)
    public JAXBElement<String> createUserDefinedDataTypeValue(String value) {
        return new JAXBElement<String>(_UserDefinedDataTypeValue_QNAME, String.class, UserDefinedDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNotificationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = GetNotificationReplyType.class)
    public JAXBElement<ArrayOfNotificationType> createGetNotificationReplyTypeItems(ArrayOfNotificationType value) {
        return new JAXBElement<ArrayOfNotificationType>(_GetNotificationReplyTypeItems_QNAME, ArrayOfNotificationType.class, GetNotificationReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExternalSessionId", scope = BaseRequestType.class)
    public JAXBElement<String> createBaseRequestTypeExternalSessionId(String value) {
        return new JAXBElement<String>(_BaseRequestTypeExternalSessionId_QNAME, String.class, BaseRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SessionId", scope = BaseRequestType.class)
    public JAXBElement<String> createBaseRequestTypeSessionId(String value) {
        return new JAXBElement<String>(_BaseRequestTypeSessionId_QNAME, String.class, BaseRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ClientIp", scope = BaseRequestType.class)
    public JAXBElement<String> createBaseRequestTypeClientIp(String value) {
        return new JAXBElement<String>(_BaseRequestTypeClientIp_QNAME, String.class, BaseRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LanguageCodeIso639_1", scope = BaseRequestType.class)
    public JAXBElement<String> createBaseRequestTypeLanguageCodeIso6391(String value) {
        return new JAXBElement<String>(_BaseRequestTypeLanguageCodeIso6391_QNAME, String.class, BaseRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIds", scope = BaseRequestType.class)
    public JAXBElement<ArrayOfKeyValueItemType> createBaseRequestTypeAdditionalIds(ArrayOfKeyValueItemType value) {
        return new JAXBElement<ArrayOfKeyValueItemType>(_BaseRequestTypeAdditionalIds_QNAME, ArrayOfKeyValueItemType.class, BaseRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CredentialsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Credentials", scope = BaseRequestType.class)
    public JAXBElement<CredentialsType> createBaseRequestTypeCredentials(CredentialsType value) {
        return new JAXBElement<CredentialsType>(_Credentials_QNAME, CredentialsType.class, BaseRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExpectedDelivery", scope = OrderType.class)
    public JAXBElement<String> createOrderTypeExpectedDelivery(String value) {
        return new JAXBElement<String>(_OrderTypeExpectedDelivery_QNAME, String.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TourInformation", scope = OrderType.class)
    public JAXBElement<String> createOrderTypeTourInformation(String value) {
        return new JAXBElement<String>(_OrderTypeTourInformation_QNAME, String.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UserDefinedData", scope = OrderType.class)
    public JAXBElement<ArrayOfUserDefinedDataType> createOrderTypeUserDefinedData(ArrayOfUserDefinedDataType value) {
        return new JAXBElement<ArrayOfUserDefinedDataType>(_UserDefinedData_QNAME, ArrayOfUserDefinedDataType.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StatusInformation", scope = OrderType.class)
    public JAXBElement<KeyValueItemType> createOrderTypeStatusInformation(KeyValueItemType value) {
        return new JAXBElement<KeyValueItemType>(_OrderTypeStatusInformation_QNAME, KeyValueItemType.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "WarehouseInformation", scope = OrderType.class)
    public JAXBElement<String> createOrderTypeWarehouseInformation(String value) {
        return new JAXBElement<String>(_OrderTypeWarehouseInformation_QNAME, String.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderIds", scope = OrderType.class)
    public JAXBElement<ArrayOfOrderIdType> createOrderTypeOrderIds(ArrayOfOrderIdType value) {
        return new JAXBElement<ArrayOfOrderIdType>(_OrderTypeOrderIds_QNAME, ArrayOfOrderIdType.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderPositionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = OrderType.class)
    public JAXBElement<ArrayOfOrderPositionType> createOrderTypeItems(ArrayOfOrderPositionType value) {
        return new JAXBElement<ArrayOfOrderPositionType>(_GetNotificationReplyTypeItems_QNAME, ArrayOfOrderPositionType.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionLists", scope = OrderType.class)
    public JAXBElement<ArrayOfSelectionListType> createOrderTypeSelectionLists(ArrayOfSelectionListType value) {
        return new JAXBElement<ArrayOfSelectionListType>(_OrderTypeSelectionLists_QNAME, ArrayOfSelectionListType.class, OrderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UID", scope = SidRequestType.class)
    public JAXBElement<String> createSidRequestTypeUID(String value) {
        return new JAXBElement<String>(_SidRequestTypeUID_QNAME, String.class, SidRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SID", scope = SidRequestType.class)
    public JAXBElement<String> createSidRequestTypeSID(String value) {
        return new JAXBElement<String>(_SidRequestTypeSID_QNAME, String.class, SidRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = GetItemsCollectionReplyType.class)
    public JAXBElement<ArrayOfItemsCollectionType> createGetItemsCollectionReplyTypeItems(ArrayOfItemsCollectionType value) {
        return new JAXBElement<ArrayOfItemsCollectionType>(_GetNotificationReplyTypeItems_QNAME, ArrayOfItemsCollectionType.class, GetItemsCollectionReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Name", scope = SupplierTmfType.class)
    public JAXBElement<String> createSupplierTmfTypeName(String value) {
        return new JAXBElement<String>(_WarehouseTypeName_QNAME, String.class, SupplierTmfType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "request", scope = GetErpInformationRequestBodyType.class)
    public JAXBElement<GetErpInformationRequestType> createGetErpInformationRequestBodyTypeRequest(GetErpInformationRequestType value) {
        return new JAXBElement<GetErpInformationRequestType>(_GetErpInformationRequestBodyTypeRequest_QNAME, GetErpInformationRequestType.class, GetErpInformationRequestBodyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ItemsCollections", scope = MasterDataType.class)
    public JAXBElement<ArrayOfItemsCollectionType> createMasterDataTypeItemsCollections(ArrayOfItemsCollectionType value) {
        return new JAXBElement<ArrayOfItemsCollectionType>(_MasterDataTypeItemsCollections_QNAME, ArrayOfItemsCollectionType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Addresses", scope = MasterDataType.class)
    public JAXBElement<ArrayOfAddressType> createMasterDataTypeAddresses(ArrayOfAddressType value) {
        return new JAXBElement<ArrayOfAddressType>(_MasterDataTypeAddresses_QNAME, ArrayOfAddressType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticleTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleTmfs", scope = MasterDataType.class)
    public JAXBElement<ArrayOfArticleTmfType> createMasterDataTypeArticleTmfs(ArrayOfArticleTmfType value) {
        return new JAXBElement<ArrayOfArticleTmfType>(_MasterDataTypeArticleTmfs_QNAME, ArrayOfArticleTmfType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomerType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Customers", scope = MasterDataType.class)
    public JAXBElement<ArrayOfCustomerType> createMasterDataTypeCustomers(ArrayOfCustomerType value) {
        return new JAXBElement<ArrayOfCustomerType>(_MasterDataTypeCustomers_QNAME, ArrayOfCustomerType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRepairTimeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "RepairTimes", scope = MasterDataType.class)
    public JAXBElement<ArrayOfRepairTimeType> createMasterDataTypeRepairTimes(ArrayOfRepairTimeType value) {
        return new JAXBElement<ArrayOfRepairTimeType>(_MasterDataTypeRepairTimes_QNAME, ArrayOfRepairTimeType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionListItems", scope = MasterDataType.class)
    public JAXBElement<ArrayOfSelectionListItemType> createMasterDataTypeSelectionListItems(ArrayOfSelectionListItemType value) {
        return new JAXBElement<ArrayOfSelectionListItemType>(_MasterDataTypeSelectionListItems_QNAME, ArrayOfSelectionListItemType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAvailabilityStateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityStates", scope = MasterDataType.class)
    public JAXBElement<ArrayOfAvailabilityStateType> createMasterDataTypeAvailabilityStates(ArrayOfAvailabilityStateType value) {
        return new JAXBElement<ArrayOfAvailabilityStateType>(_MasterDataTypeAvailabilityStates_QNAME, ArrayOfAvailabilityStateType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDispatchTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DispatchTypes", scope = MasterDataType.class)
    public JAXBElement<ArrayOfDispatchTypeType> createMasterDataTypeDispatchTypes(ArrayOfDispatchTypeType value) {
        return new JAXBElement<ArrayOfDispatchTypeType>(_MasterDataTypeDispatchTypes_QNAME, ArrayOfDispatchTypeType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Articles", scope = MasterDataType.class)
    public JAXBElement<ArrayOfArticleType> createMasterDataTypeArticles(ArrayOfArticleType value) {
        return new JAXBElement<ArrayOfArticleType>(_MasterDataTypeArticles_QNAME, ArrayOfArticleType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIconType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icons", scope = MasterDataType.class)
    public JAXBElement<ArrayOfIconType> createMasterDataTypeIcons(ArrayOfIconType value) {
        return new JAXBElement<ArrayOfIconType>(_MasterDataTypeIcons_QNAME, ArrayOfIconType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTourType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tours", scope = MasterDataType.class)
    public JAXBElement<ArrayOfTourType> createMasterDataTypeTours(ArrayOfTourType value) {
        return new JAXBElement<ArrayOfTourType>(_MasterDataTypeTours_QNAME, ArrayOfTourType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTextBlockType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TextBlocks", scope = MasterDataType.class)
    public JAXBElement<ArrayOfTextBlockType> createMasterDataTypeTextBlocks(ArrayOfTextBlockType value) {
        return new JAXBElement<ArrayOfTextBlockType>(_MasterDataTypeTextBlocks_QNAME, ArrayOfTextBlockType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVehicleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicles", scope = MasterDataType.class)
    public JAXBElement<ArrayOfVehicleType> createMasterDataTypeVehicles(ArrayOfVehicleType value) {
        return new JAXBElement<ArrayOfVehicleType>(_MasterDataTypeVehicles_QNAME, ArrayOfVehicleType.class, MasterDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Item", scope = ErpInformationType.class)
    public JAXBElement<EntityLinkType> createErpInformationTypeItem(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_ErpInformationTypeItem_QNAME, EntityLinkType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPriceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Prices", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfPriceType> createErpInformationTypePrices(ArrayOfPriceType value) {
        return new JAXBElement<ArrayOfPriceType>(_ErpInformationTypePrices_QNAME, ArrayOfPriceType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalDescription", scope = ErpInformationType.class)
    public JAXBElement<String> createErpInformationTypeAdditionalDescription(String value) {
        return new JAXBElement<String>(_ErpInformationTypeAdditionalDescription_QNAME, String.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLinkedItemsCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItemsCollections", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfLinkedItemsCollectionType> createErpInformationTypeLinkedItemsCollections(ArrayOfLinkedItemsCollectionType value) {
        return new JAXBElement<ArrayOfLinkedItemsCollectionType>(_ErpInformationTypeLinkedItemsCollections_QNAME, ArrayOfLinkedItemsCollectionType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicators", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfControlIndicatorType> createErpInformationTypeControlIndicators(ArrayOfControlIndicatorType value) {
        return new JAXBElement<ArrayOfControlIndicatorType>(_ErpInformationTypeControlIndicators_QNAME, ArrayOfControlIndicatorType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicle", scope = ErpInformationType.class)
    public JAXBElement<EntityLinkType> createErpInformationTypeVehicle(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_Vehicle_QNAME, EntityLinkType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalDescriptionExtended", scope = ErpInformationType.class)
    public JAXBElement<String> createErpInformationTypeAdditionalDescriptionExtended(String value) {
        return new JAXBElement<String>(_ErpInformationTypeAdditionalDescriptionExtended_QNAME, String.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfMemoType> createErpInformationTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfWarehouseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Warehouses", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfWarehouseType> createErpInformationTypeWarehouses(ArrayOfWarehouseType value) {
        return new JAXBElement<ArrayOfWarehouseType>(_ErpInformationTypeWarehouses_QNAME, ArrayOfWarehouseType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityState", scope = ErpInformationType.class)
    public JAXBElement<EntityLinkType> createErpInformationTypeAvailabilityState(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_AvailabilityState_QNAME, EntityLinkType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = ErpInformationType.class)
    public JAXBElement<EntityLinkType> createErpInformationTypeTour(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_Tour_QNAME, EntityLinkType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StatusInformation", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfKeyValueItemType> createErpInformationTypeStatusInformation(ArrayOfKeyValueItemType value) {
        return new JAXBElement<ArrayOfKeyValueItemType>(_OrderTypeStatusInformation_QNAME, ArrayOfKeyValueItemType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SpecialIcons", scope = ErpInformationType.class)
    public JAXBElement<ArrayOfEntityLinkType> createErpInformationTypeSpecialIcons(ArrayOfEntityLinkType value) {
        return new JAXBElement<ArrayOfEntityLinkType>(_ErpInformationTypeSpecialIcons_QNAME, ArrayOfEntityLinkType.class, ErpInformationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = SelectionListItemType.class)
    public JAXBElement<ArrayOfMemoType> createSelectionListItemTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, SelectionListItemType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = SelectionListItemType.class)
    public JAXBElement<String> createSelectionListItemTypeText(String value) {
        return new JAXBElement<String>(_SelectionListItemTypeText_QNAME, String.class, SelectionListItemType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AvailabilityStateType.class)
    public JAXBElement<String> createAvailabilityStateTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, AvailabilityStateType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icon", scope = AvailabilityStateType.class)
    public JAXBElement<EntityLinkType> createAvailabilityStateTypeIcon(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_Icon_QNAME, EntityLinkType.class, AvailabilityStateType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = AvailabilityStateType.class)
    public JAXBElement<String> createAvailabilityStateTypeShortDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeShortDescription_QNAME, String.class, AvailabilityStateType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepairTimesCostRateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TypeOfCostRate", scope = RepairTimeType.class)
    public JAXBElement<RepairTimesCostRateType> createRepairTimeTypeTypeOfCostRate(RepairTimesCostRateType value) {
        return new JAXBElement<RepairTimesCostRateType>(_RepairTimeTypeTypeOfCostRate_QNAME, RepairTimesCostRateType.class, RepairTimeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = RepairTimeType.class)
    public JAXBElement<String> createRepairTimeTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, RepairTimeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PriceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Price", scope = RepairTimeType.class)
    public JAXBElement<PriceType> createRepairTimeTypePrice(PriceType value) {
        return new JAXBElement<PriceType>(_Price_QNAME, PriceType.class, RepairTimeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = RepairTimeType.class)
    public JAXBElement<String> createRepairTimeTypeId(String value) {
        return new JAXBElement<String>(_WarehouseTypeId_QNAME, String.class, RepairTimeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = KeyValueItemType.class)
    public JAXBElement<String> createKeyValueItemTypeValue(String value) {
        return new JAXBElement<String>(_UserDefinedDataTypeValue_QNAME, String.class, KeyValueItemType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Key", scope = KeyValueItemType.class)
    public JAXBElement<String> createKeyValueItemTypeKey(String value) {
        return new JAXBElement<String>(_KeyValueItemTypeKey_QNAME, String.class, KeyValueItemType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = OrderIdType.class)
    public JAXBElement<String> createOrderIdTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, OrderIdType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = OrderIdType.class)
    public JAXBElement<String> createOrderIdTypeValue(String value) {
        return new JAXBElement<String>(_UserDefinedDataTypeValue_QNAME, String.class, OrderIdType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = FindItemsReplyType.class)
    public JAXBElement<MasterDataType> createFindItemsReplyTypeMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, FindItemsReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpArticleInformation", scope = FindItemsReplyType.class)
    public JAXBElement<ArrayOfErpInformationType> createFindItemsReplyTypeErpArticleInformation(ArrayOfErpInformationType value) {
        return new JAXBElement<ArrayOfErpInformationType>(_GetErpInformationRequestTypeErpArticleInformation_QNAME, ArrayOfErpInformationType.class, FindItemsReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UID", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeUID(String value) {
        return new JAXBElement<String>(_SidRequestTypeUID_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerFirstName", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeCustomerFirstName(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeCustomerFirstName_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StartPage", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeStartPage(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeStartPage_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "HKat", scope = GetUserReplyType.class)
    public JAXBElement<Integer> createGetUserReplyTypeHKat(Integer value) {
        return new JAXBElement<Integer>(_GetUserReplyTypeHKat_QNAME, Integer.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Privileges", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypePrivileges(String value) {
        return new JAXBElement<String>(_GetUserReplyTypePrivileges_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerLastName", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeCustomerLastName(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeCustomerLastName_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DVSEPassword", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeDVSEPassword(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeDVSEPassword_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TraderID", scope = GetUserReplyType.class)
    public JAXBElement<Integer> createGetUserReplyTypeTraderID(Integer value) {
        return new JAXBElement<Integer>(_GetUserReplyTypeTraderID_QNAME, Integer.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErrorMessage", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeErrorMessage(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeErrorMessage_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerAddress", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeCustomerAddress(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeCustomerAddress_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DVSEUserName", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeDVSEUserName(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeDVSEUserName_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErrorOccured", scope = GetUserReplyType.class)
    public JAXBElement<Short> createGetUserReplyTypeErrorOccured(Short value) {
        return new JAXBElement<Short>(_GetUserReplyTypeErrorOccured_QNAME, Short.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerID", scope = GetUserReplyType.class)
    public JAXBElement<String> createGetUserReplyTypeCustomerID(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeCustomerID_QNAME, String.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StartPagePosition", scope = GetUserReplyType.class)
    public JAXBElement<Integer> createGetUserReplyTypeStartPagePosition(Integer value) {
        return new JAXBElement<Integer>(_GetUserReplyTypeStartPagePosition_QNAME, Integer.class, GetUserReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DoWorkResult", scope = DoWorkResponseElement.class)
    public JAXBElement<String> createDoWorkResponseElementDoWorkResult(String value) {
        return new JAXBElement<String>(_DoWorkResponseElementDoWorkResult_QNAME, String.class, DoWorkResponseElement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CompanyName", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeCompanyName(String value) {
        return new JAXBElement<String>(_CustomerTypeCompanyName_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Details", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeDetails(String value) {
        return new JAXBElement<String>(_CustomerTypeDetails_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Address", scope = CustomerType.class)
    public JAXBElement<AddressType> createCustomerTypeAddress(AddressType value) {
        return new JAXBElement<AddressType>(_Address_QNAME, AddressType.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FirstName", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeFirstName(String value) {
        return new JAXBElement<String>(_CustomerTypeFirstName_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Title", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeTitle(String value) {
        return new JAXBElement<String>(_CustomerTypeTitle_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerId", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeCustomerId(String value) {
        return new JAXBElement<String>(_CustomerTypeCustomerId_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Name", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeName(String value) {
        return new JAXBElement<String>(_WarehouseTypeName_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = CustomerType.class)
    public JAXBElement<String> createCustomerTypeShortDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeShortDescription_QNAME, String.class, CustomerType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExpectedDeliveryTime", scope = QuantityType.class)
    public JAXBElement<String> createQuantityTypeExpectedDeliveryTime(String value) {
        return new JAXBElement<String>(_QuantityTypeExpectedDeliveryTime_QNAME, String.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = QuantityType.class)
    public JAXBElement<String> createQuantityTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LotSizes", scope = QuantityType.class)
    public JAXBElement<ArrayOfKeyValueItemType> createQuantityTypeLotSizes(ArrayOfKeyValueItemType value) {
        return new JAXBElement<ArrayOfKeyValueItemType>(_QuantityTypeLotSizes_QNAME, ArrayOfKeyValueItemType.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PackagingUnit", scope = QuantityType.class)
    public JAXBElement<String> createQuantityTypePackagingUnit(String value) {
        return new JAXBElement<String>(_QuantityTypePackagingUnit_QNAME, String.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AvailabilityState", scope = QuantityType.class)
    public JAXBElement<EntityLinkType> createQuantityTypeAvailabilityState(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_AvailabilityState_QNAME, EntityLinkType.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "QuantityUnit", scope = QuantityType.class)
    public JAXBElement<String> createQuantityTypeQuantityUnit(String value) {
        return new JAXBElement<String>(_QuantityTypeQuantityUnit_QNAME, String.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = QuantityType.class)
    public JAXBElement<String> createQuantityTypeTour(String value) {
        return new JAXBElement<String>(_Tour_QNAME, String.class, QuantityType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSidRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "sidInfo", scope = SetSidElement.class)
    public JAXBElement<SetSidRequestType> createSetSidElementSidInfo(SetSidRequestType value) {
        return new JAXBElement<SetSidRequestType>(_SetSidElementSidInfo_QNAME, SetSidRequestType.class, SetSidElement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CompanyName", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeCompanyName(String value) {
        return new JAXBElement<String>(_CustomerTypeCompanyName_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PostOfficeBox", scope = AddressType.class)
    public JAXBElement<String> createAddressTypePostOfficeBox(String value) {
        return new JAXBElement<String>(_AddressTypePostOfficeBox_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressTypeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Type", scope = AddressType.class)
    public JAXBElement<AddressTypeType> createAddressTypeType(AddressTypeType value) {
        return new JAXBElement<AddressTypeType>(_AddressTypeType_QNAME, AddressTypeType.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AddressDescription", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeAddressDescription(String value) {
        return new JAXBElement<String>(_AddressTypeAddressDescription_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Phone", scope = AddressType.class)
    public JAXBElement<String> createAddressTypePhone(String value) {
        return new JAXBElement<String>(_AddressTypePhone_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Street", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeStreet(String value) {
        return new JAXBElement<String>(_AddressTypeStreet_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Country", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeCountry(String value) {
        return new JAXBElement<String>(_AddressTypeCountry_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeId(String value) {
        return new JAXBElement<String>(_WarehouseTypeId_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LastName", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeLastName(String value) {
        return new JAXBElement<String>(_AddressTypeLastName_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "District", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeDistrict(String value) {
        return new JAXBElement<String>(_AddressTypeDistrict_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Fax", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeFax(String value) {
        return new JAXBElement<String>(_AddressTypeFax_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalInformation", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeAdditionalInformation(String value) {
        return new JAXBElement<String>(_AddressTypeAdditionalInformation_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ZIP", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeZIP(String value) {
        return new JAXBElement<String>(_AddressTypeZIP_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StreetExt", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeStreetExt(String value) {
        return new JAXBElement<String>(_AddressTypeStreetExt_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Email", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeEmail(String value) {
        return new JAXBElement<String>(_AddressTypeEmail_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "FirstName", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeFirstName(String value) {
        return new JAXBElement<String>(_CustomerTypeFirstName_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Title", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeTitle(String value) {
        return new JAXBElement<String>(_CustomerTypeTitle_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "City", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeCity(String value) {
        return new JAXBElement<String>(_AddressTypeCity_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Contact", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeContact(String value) {
        return new JAXBElement<String>(_AddressTypeContact_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MobilePhone", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeMobilePhone(String value) {
        return new JAXBElement<String>(_AddressTypeMobilePhone_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleIdSupplier", scope = ArticleTmfType.class)
    public JAXBElement<String> createArticleTmfTypeArticleIdSupplier(String value) {
        return new JAXBElement<String>(_ArticleTmfTypeArticleIdSupplier_QNAME, String.class, ArticleTmfType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProductGroupTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ProductGroups", scope = ArticleTmfType.class)
    public JAXBElement<ArrayOfProductGroupTmfType> createArticleTmfTypeProductGroups(ArrayOfProductGroupTmfType value) {
        return new JAXBElement<ArrayOfProductGroupTmfType>(_ArticleTmfTypeProductGroups_QNAME, ArrayOfProductGroupTmfType.class, ArticleTmfType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SupplierTmfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Supplier", scope = ArticleTmfType.class)
    public JAXBElement<SupplierTmfType> createArticleTmfTypeSupplier(SupplierTmfType value) {
        return new JAXBElement<SupplierTmfType>(_ArticleTmfTypeSupplier_QNAME, SupplierTmfType.class, ArticleTmfType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicators", scope = ServiceConfigurationType.class)
    public JAXBElement<ArrayOfControlIndicatorType> createServiceConfigurationTypeControlIndicators(ArrayOfControlIndicatorType value) {
        return new JAXBElement<ArrayOfControlIndicatorType>(_ErpInformationTypeControlIndicators_QNAME, ArrayOfControlIndicatorType.class, ServiceConfigurationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Item", scope = OrderPositionType.class)
    public JAXBElement<EntityLinkType> createOrderPositionTypeItem(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_ErpInformationTypeItem_QNAME, EntityLinkType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPriceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Prices", scope = OrderPositionType.class)
    public JAXBElement<ArrayOfPriceType> createOrderPositionTypePrices(ArrayOfPriceType value) {
        return new JAXBElement<ArrayOfPriceType>(_ErpInformationTypePrices_QNAME, ArrayOfPriceType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vehicle", scope = OrderPositionType.class)
    public JAXBElement<EntityLinkType> createOrderPositionTypeVehicle(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_Vehicle_QNAME, EntityLinkType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUserDefinedDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "UserDefinedData", scope = OrderPositionType.class)
    public JAXBElement<ArrayOfUserDefinedDataType> createOrderPositionTypeUserDefinedData(ArrayOfUserDefinedDataType value) {
        return new JAXBElement<ArrayOfUserDefinedDataType>(_UserDefinedData_QNAME, ArrayOfUserDefinedDataType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "StatusInformation", scope = OrderPositionType.class)
    public JAXBElement<KeyValueItemType> createOrderPositionTypeStatusInformation(KeyValueItemType value) {
        return new JAXBElement<KeyValueItemType>(_OrderTypeStatusInformation_QNAME, KeyValueItemType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSelectionListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SelectionLists", scope = OrderPositionType.class)
    public JAXBElement<ArrayOfSelectionListType> createOrderPositionTypeSelectionLists(ArrayOfSelectionListType value) {
        return new JAXBElement<ArrayOfSelectionListType>(_OrderTypeSelectionLists_QNAME, ArrayOfSelectionListType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Tour", scope = OrderPositionType.class)
    public JAXBElement<EntityLinkType> createOrderPositionTypeTour(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_Tour_QNAME, EntityLinkType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntities", scope = OrderPositionType.class)
    public JAXBElement<ArrayOfEntityLinkType> createOrderPositionTypeLinkedEntities(ArrayOfEntityLinkType value) {
        return new JAXBElement<ArrayOfEntityLinkType>(_OrderPositionTypeLinkedEntities_QNAME, ArrayOfEntityLinkType.class, OrderPositionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceConfigurationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ServiceConfiguration", scope = GetSessionReplyType.class)
    public JAXBElement<ServiceConfigurationType> createGetSessionReplyTypeServiceConfiguration(ServiceConfigurationType value) {
        return new JAXBElement<ServiceConfigurationType>(_ServiceConfiguration_QNAME, ServiceConfigurationType.class, GetSessionReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SessionId", scope = GetSessionReplyType.class)
    public JAXBElement<String> createGetSessionReplyTypeSessionId(String value) {
        return new JAXBElement<String>(_BaseRequestTypeSessionId_QNAME, String.class, GetSessionReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = LinkedItemsCollectionType.class)
    public JAXBElement<String> createLinkedItemsCollectionTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, LinkedItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = LinkedItemsCollectionType.class)
    public JAXBElement<ArrayOfMemoType> createLinkedItemsCollectionTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, LinkedItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItems", scope = LinkedItemsCollectionType.class)
    public JAXBElement<ArrayOfErpInformationType> createLinkedItemsCollectionTypeLinkedItems(ArrayOfErpInformationType value) {
        return new JAXBElement<ArrayOfErpInformationType>(_LinkedItemsCollectionTypeLinkedItems_QNAME, ArrayOfErpInformationType.class, LinkedItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlIndicatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicator", scope = LinkedItemsCollectionType.class)
    public JAXBElement<ControlIndicatorType> createLinkedItemsCollectionTypeControlIndicator(ControlIndicatorType value) {
        return new JAXBElement<ControlIndicatorType>(_ControlIndicator_QNAME, ControlIndicatorType.class, LinkedItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ANUserName", scope = GetUserRequestType.class)
    public JAXBElement<String> createGetUserRequestTypeANUserName(String value) {
        return new JAXBElement<String>(_GetUserRequestTypeANUserName_QNAME, String.class, GetUserRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ANPassword", scope = GetUserRequestType.class)
    public JAXBElement<String> createGetUserRequestTypeANPassword(String value) {
        return new JAXBElement<String>(_GetUserRequestTypeANPassword_QNAME, String.class, GetUserRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntity", scope = ControlIndicatorType.class)
    public JAXBElement<EntityLinkType> createControlIndicatorTypeLinkedEntity(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_ValidationMessageTypeLinkedEntity_QNAME, EntityLinkType.class, ControlIndicatorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Parameters", scope = ControlIndicatorType.class)
    public JAXBElement<ArrayOfKeyValueItemType> createControlIndicatorTypeParameters(ArrayOfKeyValueItemType value) {
        return new JAXBElement<ArrayOfKeyValueItemType>(_ControlIndicatorTypeParameters_QNAME, ArrayOfKeyValueItemType.class, ControlIndicatorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Username", scope = UserType.class)
    public JAXBElement<String> createUserTypeUsername(String value) {
        return new JAXBElement<String>(_UserTypeUsername_QNAME, String.class, UserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MandatorId", scope = UserType.class)
    public JAXBElement<String> createUserTypeMandatorId(String value) {
        return new JAXBElement<String>(_UserTypeMandatorId_QNAME, String.class, UserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CustomerId", scope = UserType.class)
    public JAXBElement<String> createUserTypeCustomerId(String value) {
        return new JAXBElement<String>(_CustomerTypeCustomerId_QNAME, String.class, UserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Password", scope = UserType.class)
    public JAXBElement<String> createUserTypePassword(String value) {
        return new JAXBElement<String>(_UserTypePassword_QNAME, String.class, UserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = TextBlockType.class)
    public JAXBElement<String> createTextBlockTypeText(String value) {
        return new JAXBElement<String>(_SelectionListItemTypeText_QNAME, String.class, TextBlockType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection", scope = SendOrderRequestType.class)
    public JAXBElement<OrderCollectionType> createSendOrderRequestTypeOrderCollection(OrderCollectionType value) {
        return new JAXBElement<OrderCollectionType>(_OrderCollection_QNAME, OrderCollectionType.class, SendOrderRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = SendOrderRequestType.class)
    public JAXBElement<MasterDataType> createSendOrderRequestTypeMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, SendOrderRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AddressTypeType.class)
    public JAXBElement<String> createAddressTypeTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, AddressTypeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AdditionalIdentifierType.class)
    public JAXBElement<String> createAdditionalIdentifierTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, AdditionalIdentifierType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Value", scope = AdditionalIdentifierType.class)
    public JAXBElement<String> createAdditionalIdentifierTypeValue(String value) {
        return new JAXBElement<String>(_UserDefinedDataTypeValue_QNAME, String.class, AdditionalIdentifierType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Salt", scope = CredentialsType.class)
    public JAXBElement<String> createCredentialsTypeSalt(String value) {
        return new JAXBElement<String>(_CredentialsTypeSalt_QNAME, String.class, CredentialsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SecurityToken", scope = CredentialsType.class)
    public JAXBElement<String> createCredentialsTypeSecurityToken(String value) {
        return new JAXBElement<String>(_CredentialsTypeSecurityToken_QNAME, String.class, CredentialsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SalesAdvisorCredentials", scope = CredentialsType.class)
    public JAXBElement<UserType> createCredentialsTypeSalesAdvisorCredentials(UserType value) {
        return new JAXBElement<UserType>(_CredentialsTypeSalesAdvisorCredentials_QNAME, UserType.class, CredentialsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExternalIdentityProviderId", scope = CredentialsType.class)
    public JAXBElement<String> createCredentialsTypeExternalIdentityProviderId(String value) {
        return new JAXBElement<String>(_CredentialsTypeExternalIdentityProviderId_QNAME, String.class, CredentialsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CatalogUserCredentials", scope = CredentialsType.class)
    public JAXBElement<UserType> createCredentialsTypeCatalogUserCredentials(UserType value) {
        return new JAXBElement<UserType>(_CredentialsTypeCatalogUserCredentials_QNAME, UserType.class, CredentialsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = SelectionListType.class)
    public JAXBElement<ArrayOfMemoType> createSelectionListTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, SelectionListType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = SelectionListType.class)
    public JAXBElement<String> createSelectionListTypeId(String value) {
        return new JAXBElement<String>(_WarehouseTypeId_QNAME, String.class, SelectionListType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = SelectionListType.class)
    public JAXBElement<String> createSelectionListTypeText(String value) {
        return new JAXBElement<String>(_SelectionListItemTypeText_QNAME, String.class, SelectionListType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Items", scope = SelectionListType.class)
    public JAXBElement<ArrayOfEntityLinkType> createSelectionListTypeItems(ArrayOfEntityLinkType value) {
        return new JAXBElement<ArrayOfEntityLinkType>(_GetNotificationReplyTypeItems_QNAME, ArrayOfEntityLinkType.class, SelectionListType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ANUserName", scope = GetUserElement.class)
    public JAXBElement<String> createGetUserElementANUserName(String value) {
        return new JAXBElement<String>(_GetUserRequestTypeANUserName_QNAME, String.class, GetUserElement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ANPassword", scope = GetUserElement.class)
    public JAXBElement<String> createGetUserElementANPassword(String value) {
        return new JAXBElement<String>(_GetUserRequestTypeANPassword_QNAME, String.class, GetUserElement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ModelGroup", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeModelGroup(String value) {
        return new JAXBElement<String>(_VehicleTypeModelGroup_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Type", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeType(String value) {
        return new JAXBElement<String>(_AddressTypeType_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "TypeCertificateNumber", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeTypeCertificateNumber(String value) {
        return new JAXBElement<String>(_VehicleTypeTypeCertificateNumber_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MileageShortIndicator", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeMileageShortIndicator(String value) {
        return new JAXBElement<String>(_VehicleTypeMileageShortIndicator_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MotorCode", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeMotorCode(String value) {
        return new JAXBElement<String>(_VehicleTypeMotorCode_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Model", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeModel(String value) {
        return new JAXBElement<String>(_VehicleTypeModel_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "VehicleOwner", scope = VehicleType.class)
    public JAXBElement<EntityLinkType> createVehicleTypeVehicleOwner(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_VehicleTypeVehicleOwner_QNAME, EntityLinkType.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Make", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeMake(String value) {
        return new JAXBElement<String>(_VehicleTypeMake_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PlateId", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypePlateId(String value) {
        return new JAXBElement<String>(_VehicleTypePlateId_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Vin", scope = VehicleType.class)
    public JAXBElement<String> createVehicleTypeVin(String value) {
        return new JAXBElement<String>(_VehicleTypeVin_QNAME, String.class, VehicleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedEntity", scope = NotificationType.class)
    public JAXBElement<EntityLinkType> createNotificationTypeLinkedEntity(EntityLinkType value) {
        return new JAXBElement<EntityLinkType>(_ValidationMessageTypeLinkedEntity_QNAME, EntityLinkType.class, NotificationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memo", scope = NotificationType.class)
    public JAXBElement<MemoType> createNotificationTypeMemo(MemoType value) {
        return new JAXBElement<MemoType>(_Memo_QNAME, MemoType.class, NotificationType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = AccountDataType.class)
    public JAXBElement<String> createAccountDataTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, AccountDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = AccountDataType.class)
    public JAXBElement<ArrayOfMemoType> createAccountDataTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, AccountDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = AccountDataType.class)
    public JAXBElement<String> createAccountDataTypeShortDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeShortDescription_QNAME, String.class, AccountDataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = ProductGroupTmfType.class)
    public JAXBElement<String> createProductGroupTmfTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, ProductGroupTmfType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Orders", scope = OrderCollectionType.class)
    public JAXBElement<ArrayOfOrderType> createOrderCollectionTypeOrders(ArrayOfOrderType value) {
        return new JAXBElement<ArrayOfOrderType>(_OrderCollectionTypeOrders_QNAME, ArrayOfOrderType.class, OrderCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderIds", scope = OrderCollectionType.class)
    public JAXBElement<ArrayOfOrderIdType> createOrderCollectionTypeOrderIds(ArrayOfOrderIdType value) {
        return new JAXBElement<ArrayOfOrderIdType>(_OrderTypeOrderIds_QNAME, ArrayOfOrderIdType.class, OrderCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ShortDescription", scope = OrderCollectionType.class)
    public JAXBElement<String> createOrderCollectionTypeShortDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeShortDescription_QNAME, String.class, OrderCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencyCode_Iso_4217", scope = DispatchTypeType.class)
    public JAXBElement<String> createDispatchTypeTypeCurrencyCodeIso4217(String value) {
        return new JAXBElement<String>(_DispatchTypeTypeCurrencyCodeIso4217_QNAME, String.class, DispatchTypeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "DispatchConditions", scope = DispatchTypeType.class)
    public JAXBElement<String> createDispatchTypeTypeDispatchConditions(String value) {
        return new JAXBElement<String>(_DispatchTypeTypeDispatchConditions_QNAME, String.class, DispatchTypeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencySymbol", scope = DispatchTypeType.class)
    public JAXBElement<String> createDispatchTypeTypeCurrencySymbol(String value) {
        return new JAXBElement<String>(_DispatchTypeTypeCurrencySymbol_QNAME, String.class, DispatchTypeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = PriceType.class)
    public JAXBElement<String> createPriceTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, PriceType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMemoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Memos", scope = PriceType.class)
    public JAXBElement<ArrayOfMemoType> createPriceTypeMemos(ArrayOfMemoType value) {
        return new JAXBElement<ArrayOfMemoType>(_WarehouseTypeMemos_QNAME, ArrayOfMemoType.class, PriceType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencyCode_Iso_4217", scope = PriceType.class)
    public JAXBElement<String> createPriceTypeCurrencyCodeIso4217(String value) {
        return new JAXBElement<String>(_DispatchTypeTypeCurrencyCodeIso4217_QNAME, String.class, PriceType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "CurrencySymbol", scope = PriceType.class)
    public JAXBElement<String> createPriceTypeCurrencySymbol(String value) {
        return new JAXBElement<String>(_DispatchTypeTypeCurrencySymbol_QNAME, String.class, PriceType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetErpInformationReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetErpInformationResult", scope = GetErpInformationResponseBodyType.class)
    public JAXBElement<GetErpInformationReplyType> createGetErpInformationResponseBodyTypeGetErpInformationResult(GetErpInformationReplyType value) {
        return new JAXBElement<GetErpInformationReplyType>(_GetErpInformationResponseBodyTypeGetErpInformationResult_QNAME, GetErpInformationReplyType.class, GetErpInformationResponseBodyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection", scope = SendOrderDocumentRequestType.class)
    public JAXBElement<OrderCollectionType> createSendOrderDocumentRequestTypeOrderCollection(OrderCollectionType value) {
        return new JAXBElement<OrderCollectionType>(_OrderCollection_QNAME, OrderCollectionType.class, SendOrderDocumentRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = SendOrderDocumentRequestType.class)
    public JAXBElement<MasterDataType> createSendOrderDocumentRequestTypeMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, SendOrderDocumentRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Document", scope = SendOrderDocumentRequestType.class)
    public JAXBElement<String> createSendOrderDocumentRequestTypeDocument(String value) {
        return new JAXBElement<String>(_SendOrderDocumentRequestTypeDocument_QNAME, String.class, SendOrderDocumentRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkUrl", scope = MemoType.class)
    public JAXBElement<String> createMemoTypeLinkUrl(String value) {
        return new JAXBElement<String>(_IconTypeLinkUrl_QNAME, String.class, MemoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Icons", scope = MemoType.class)
    public JAXBElement<ArrayOfEntityLinkType> createMemoTypeIcons(ArrayOfEntityLinkType value) {
        return new JAXBElement<ArrayOfEntityLinkType>(_MasterDataTypeIcons_QNAME, ArrayOfEntityLinkType.class, MemoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Label", scope = MemoType.class)
    public JAXBElement<String> createMemoTypeLabel(String value) {
        return new JAXBElement<String>(_MemoTypeLabel_QNAME, String.class, MemoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Text", scope = MemoType.class)
    public JAXBElement<String> createMemoTypeText(String value) {
        return new JAXBElement<String>(_SelectionListItemTypeText_QNAME, String.class, MemoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "SearchTerm", scope = FindItemsRequestType.class)
    public JAXBElement<String> createFindItemsRequestTypeSearchTerm(String value) {
        return new JAXBElement<String>(_FindItemsRequestTypeSearchTerm_QNAME, String.class, FindItemsRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PriceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "PricePerUnit", scope = RepairTimesCostRateType.class)
    public JAXBElement<PriceType> createRepairTimesCostRateTypePricePerUnit(PriceType value) {
        return new JAXBElement<PriceType>(_RepairTimesCostRateTypePricePerUnit_QNAME, PriceType.class, RepairTimesCostRateType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = GetErpInformationReplyType.class)
    public JAXBElement<MasterDataType> createGetErpInformationReplyTypeMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, GetErpInformationReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErpArticleInformation", scope = GetErpInformationReplyType.class)
    public JAXBElement<ArrayOfErpInformationType> createGetErpInformationReplyTypeErpArticleInformation(ArrayOfErpInformationType value) {
        return new JAXBElement<ArrayOfErpInformationType>(_GetErpInformationRequestTypeErpArticleInformation_QNAME, ArrayOfErpInformationType.class, GetErpInformationReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAccountDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AccountData", scope = GetAccountInformationReplyType.class)
    public JAXBElement<ArrayOfAccountDataType> createGetAccountInformationReplyTypeAccountData(ArrayOfAccountDataType value) {
        return new JAXBElement<ArrayOfAccountDataType>(_AccountData_QNAME, ArrayOfAccountDataType.class, GetAccountInformationReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfControlIndicatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ControlIndicators", scope = BaseResponseType.class)
    public JAXBElement<ArrayOfControlIndicatorType> createBaseResponseTypeControlIndicators(ArrayOfControlIndicatorType value) {
        return new JAXBElement<ArrayOfControlIndicatorType>(_ErpInformationTypeControlIndicators_QNAME, ArrayOfControlIndicatorType.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErrorCode", scope = BaseResponseType.class)
    public JAXBElement<String> createBaseResponseTypeErrorCode(String value) {
        return new JAXBElement<String>(_BaseResponseTypeErrorCode_QNAME, String.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNotificationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Notifications", scope = BaseResponseType.class)
    public JAXBElement<ArrayOfNotificationType> createBaseResponseTypeNotifications(ArrayOfNotificationType value) {
        return new JAXBElement<ArrayOfNotificationType>(_BaseResponseTypeNotifications_QNAME, ArrayOfNotificationType.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ErrorMessage", scope = BaseResponseType.class)
    public JAXBElement<String> createBaseResponseTypeErrorMessage(String value) {
        return new JAXBElement<String>(_GetUserReplyTypeErrorMessage_QNAME, String.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfValidationMessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ValidationMessages", scope = BaseResponseType.class)
    public JAXBElement<ArrayOfValidationMessageType> createBaseResponseTypeValidationMessages(ArrayOfValidationMessageType value) {
        return new JAXBElement<ArrayOfValidationMessageType>(_BaseResponseTypeValidationMessages_QNAME, ArrayOfValidationMessageType.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueItemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "AdditionalIds", scope = BaseResponseType.class)
    public JAXBElement<ArrayOfKeyValueItemType> createBaseResponseTypeAdditionalIds(ArrayOfKeyValueItemType value) {
        return new JAXBElement<ArrayOfKeyValueItemType>(_BaseRequestTypeAdditionalIds_QNAME, ArrayOfKeyValueItemType.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CredentialsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Credentials", scope = BaseResponseType.class)
    public JAXBElement<CredentialsType> createBaseResponseTypeCredentials(CredentialsType value) {
        return new JAXBElement<CredentialsType>(_Credentials_QNAME, CredentialsType.class, BaseResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = ItemsCollectionType.class)
    public JAXBElement<String> createItemsCollectionTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, ItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEntityLinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "LinkedItems", scope = ItemsCollectionType.class)
    public JAXBElement<ArrayOfEntityLinkType> createItemsCollectionTypeLinkedItems(ArrayOfEntityLinkType value) {
        return new JAXBElement<ArrayOfEntityLinkType>(_LinkedItemsCollectionTypeLinkedItems_QNAME, ArrayOfEntityLinkType.class, ItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ArticleIdErp", scope = ItemsCollectionType.class)
    public JAXBElement<String> createItemsCollectionTypeArticleIdErp(String value) {
        return new JAXBElement<String>(_ArticleTypeArticleIdErp_QNAME, String.class, ItemsCollectionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "GetVersionResult", scope = GetVersionResponseBodyType.class)
    public JAXBElement<String> createGetVersionResponseBodyTypeGetVersionResult(String value) {
        return new JAXBElement<String>(_GetVersionResponseBodyTypeGetVersionResult_QNAME, String.class, GetVersionResponseBodyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderCollectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "OrderCollection", scope = SendOrderReplyType.class)
    public JAXBElement<OrderCollectionType> createSendOrderReplyTypeOrderCollection(OrderCollectionType value) {
        return new JAXBElement<OrderCollectionType>(_OrderCollection_QNAME, OrderCollectionType.class, SendOrderReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasterDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "MasterData", scope = SendOrderReplyType.class)
    public JAXBElement<MasterDataType> createSendOrderReplyTypeMasterData(MasterDataType value) {
        return new JAXBElement<MasterDataType>(_MasterData_QNAME, MasterDataType.class, SendOrderReplyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "ExpectedDelivery", scope = TourType.class)
    public JAXBElement<String> createTourTypeExpectedDelivery(String value) {
        return new JAXBElement<String>(_OrderTypeExpectedDelivery_QNAME, String.class, TourType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Description", scope = TourType.class)
    public JAXBElement<String> createTourTypeDescription(String value) {
        return new JAXBElement<String>(_WarehouseTypeDescription_QNAME, String.class, TourType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://topmotive.eu/TMConnect", name = "Id", scope = TourType.class)
    public JAXBElement<String> createTourTypeId(String value) {
        return new JAXBElement<String>(_WarehouseTypeId_QNAME, String.class, TourType.class, value);
    }

}
