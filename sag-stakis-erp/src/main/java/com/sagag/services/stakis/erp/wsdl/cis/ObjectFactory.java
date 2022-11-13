
package com.sagag.services.stakis.erp.wsdl.cis;

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
 * generated in the com.sagag.services.stakis.erp.wsdl.cis package.
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

    private final static QName _GetVoucherTypesResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherTypesResponseBody");
    private final static QName _OutSession_QNAME = new QName("DVSE.WebApp.CISService", "OutSession");
    private final static QName _GetInvoices_QNAME = new QName("DVSE.WebApp.CISService", "GetInvoices");
    private final static QName _OutCustomer_QNAME = new QName("DVSE.WebApp.CISService", "OutCustomer");
    private final static QName _Account_QNAME = new QName("DVSE.WebApp.CISService", "Account");
    private final static QName _SaleType_QNAME = new QName("DVSE.WebApp.CISService", "SaleType");
    private final static QName _ReturnItemsRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "ReturnItemsRequestBody");
    private final static QName _CustomerState_QNAME = new QName("DVSE.WebApp.CISService", "CustomerState");
    private final static QName _Invoice_QNAME = new QName("DVSE.WebApp.CISService", "Invoice");
    private final static QName _ReturnItem_QNAME = new QName("DVSE.WebApp.CISService", "ReturnItem");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Customer_QNAME = new QName("DVSE.WebApp.CISService", "Customer");
    private final static QName _Address_QNAME = new QName("DVSE.WebApp.CISService", "Address");
    private final static QName _ArrayOfDispatchType_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfDispatchType");
    private final static QName _OutSales_QNAME = new QName("DVSE.WebApp.CISService", "OutSales");
    private final static QName _ArrayOfSaleSubType_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfSaleSubType");
    private final static QName _GetQueryTypesVoucherResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesVoucherResponseBody");
    private final static QName _ArrayOfInvoice_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfInvoice");
    private final static QName _GetSalesResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesResponseBody");
    private final static QName _GetReturnItemOptionsResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetReturnItemOptionsResponse");
    private final static QName _SalesOutletList_QNAME = new QName("DVSE.WebApp.CISService", "SalesOutletList");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _ArrayOfAddress_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfAddress");
    private final static QName _OutQueryTypes_QNAME = new QName("DVSE.WebApp.CISService", "OutQueryTypes");
    private final static QName _OutVoucherDetails_QNAME = new QName("DVSE.WebApp.CISService", "OutVoucherDetails");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _ReturnReason_QNAME = new QName("DVSE.WebApp.CISService", "ReturnReason");
    private final static QName _GetEnabledFunctionsResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetEnabledFunctionsResponseBody");
    private final static QName _OutVouchers_QNAME = new QName("DVSE.WebApp.CISService", "OutVouchers");
    private final static QName _GetCustomerResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomerResponse");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _ArrayOfTax_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfTax");
    private final static QName _GetInvoicesRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetInvoicesRequestBody");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _VoucherSummaryItem_QNAME = new QName("DVSE.WebApp.CISService", "VoucherSummaryItem");
    private final static QName _Note_QNAME = new QName("DVSE.WebApp.CISService", "Note");
    private final static QName _GetCustomersResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomersResponse");
    private final static QName _GetCustomerResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomerResponseBody");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _ArrayOfSalesOutlet_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfSalesOutlet");
    private final static QName _GetSalesRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesRequestBody");
    private final static QName _CustomerItem_QNAME = new QName("DVSE.WebApp.CISService", "CustomerItem");
    private final static QName _ResetVoucherItemsCanReturnProperty_QNAME = new QName("DVSE.WebApp.CISService", "ResetVoucherItemsCanReturnProperty");
    private final static QName _Sale_QNAME = new QName("DVSE.WebApp.CISService", "Sale");
    private final static QName _GetCustomerRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomerRequestBody");
    private final static QName _ResetVoucherItemsCanReturnPropertyResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "ResetVoucherItemsCanReturnPropertyResponseBody");
    private final static QName _GetSessionResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetSessionResponseBody");
    private final static QName _ArrayOfReturnItem_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfReturnItem");
    private final static QName _OutReturnItemOptions_QNAME = new QName("DVSE.WebApp.CISService", "OutReturnItemOptions");
    private final static QName _GetCustomersResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomersResponseBody");
    private final static QName _ArrayOfSaleType_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfSaleType");
    private final static QName _GetCustomersRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomersRequestBody");
    private final static QName _GetSalesInformationRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesInformationRequestBody");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _GetConnectedVouchersResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetConnectedVouchersResponse");
    private final static QName _BaseResponse_QNAME = new QName("DVSE.WebApp.CISService", "BaseResponse");
    private final static QName _GetCustomer_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomer");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _GetVersion_QNAME = new QName("DVSE.WebApp.CISService", "GetVersion");
    private final static QName _ElectronicAddress_QNAME = new QName("DVSE.WebApp.CISService", "ElectronicAddress");
    private final static QName _GetVoucherDetailsRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherDetailsRequestBody");
    private final static QName _MonetaryValue_QNAME = new QName("DVSE.WebApp.CISService", "MonetaryValue");
    private final static QName _ErpCredential_QNAME = new QName("DVSE.WebApp.CISService", "ErpCredential");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _ArrayOfVoucherSummaryItem_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfVoucherSummaryItem");
    private final static QName _GetSession_QNAME = new QName("DVSE.WebApp.CISService", "GetSession");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Installment_QNAME = new QName("DVSE.WebApp.CISService", "Installment");
    private final static QName _GetVouchersResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetVouchersResponse");
    private final static QName _ArrayOfInstallment_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfInstallment");
    private final static QName _ArrayOfState_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfState");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _GetEnabledFunctionsRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetEnabledFunctionsRequestBody");
    private final static QName _ArrayOfInt_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfInt");
    private final static QName _GetVoucherTypesResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherTypesResponse");
    private final static QName _GetQueryTypesCustomerRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesCustomerRequestBody");
    private final static QName _GetInvoicesResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetInvoicesResponseBody");
    private final static QName _ArrayOfConceptCustomer_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfConceptCustomer");
    private final static QName _GetQueryTypesCustomer_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesCustomer");
    private final static QName _SalesOutlet_QNAME = new QName("DVSE.WebApp.CISService", "SalesOutlet");
    private final static QName _GetVoucherTypes_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherTypes");
    private final static QName _GetVoucherDetailsResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherDetailsResponse");
    private final static QName _Tax_QNAME = new QName("DVSE.WebApp.CISService", "Tax");
    private final static QName _GetSales_QNAME = new QName("DVSE.WebApp.CISService", "GetSales");
    private final static QName _CustomerDetails_QNAME = new QName("DVSE.WebApp.CISService", "CustomerDetails");
    private final static QName _GetConnectedVouchersRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetConnectedVouchersRequestBody");
    private final static QName _ArrayOfPrice_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfPrice");
    private final static QName _GetReturnItemOptionsResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetReturnItemOptionsResponseBody");
    private final static QName _BaseCustomer_QNAME = new QName("DVSE.WebApp.CISService", "BaseCustomer");
    private final static QName _GetSalesInformationResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesInformationResponseBody");
    private final static QName _ReturnItemsResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "ReturnItemsResponseBody");
    private final static QName _GetReturnItemOptions_QNAME = new QName("DVSE.WebApp.CISService", "GetReturnItemOptions");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _GetSessionResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetSessionResponse");
    private final static QName _VoucherItem_QNAME = new QName("DVSE.WebApp.CISService", "VoucherItem");
    private final static QName _GetVouchersResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVouchersResponseBody");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _GetQueryTypesVoucherResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesVoucherResponse");
    private final static QName _DispatchType_QNAME = new QName("DVSE.WebApp.CISService", "DispatchType");
    private final static QName _ArrayOfMonetaryValue_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfMonetaryValue");
    private final static QName _GetQueryTypesVoucher_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesVoucher");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _ArrayOfReturnReason_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfReturnReason");
    private final static QName _GetVersionResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetVersionResponse");
    private final static QName _ArrayOfSale_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfSale");
    private final static QName _ArrayOfQueryType_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfQueryType");
    private final static QName _OutSalesInformation_QNAME = new QName("DVSE.WebApp.CISService", "OutSalesInformation");
    private final static QName _OutVoucherTypes_QNAME = new QName("DVSE.WebApp.CISService", "OutVoucherTypes");
    private final static QName _SaleSubType_QNAME = new QName("DVSE.WebApp.CISService", "SaleSubType");
    private final static QName _GetReturnItemOptionsRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetReturnItemOptionsRequestBody");
    private final static QName _QueryType_QNAME = new QName("DVSE.WebApp.CISService", "QueryType");
    private final static QName _VoucherType_QNAME = new QName("DVSE.WebApp.CISService", "VoucherType");
    private final static QName _GetVoucherDetails_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherDetails");
    private final static QName _ResetVoucherItemsCanReturnPropertyRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "ResetVoucherItemsCanReturnPropertyRequestBody");
    private final static QName _Price_QNAME = new QName("DVSE.WebApp.CISService", "Price");
    private final static QName _ConceptCustomer_QNAME = new QName("DVSE.WebApp.CISService", "ConceptCustomer");
    private final static QName _State_QNAME = new QName("DVSE.WebApp.CISService", "State");
    private final static QName _ArrayOfElectronicAddress_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfElectronicAddress");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _ArrayOfCustomer_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfCustomer");
    private final static QName _ReturnItems_QNAME = new QName("DVSE.WebApp.CISService", "ReturnItems");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _GetConnectedVouchersResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetConnectedVouchersResponseBody");
    private final static QName _GetEnabledFunctions_QNAME = new QName("DVSE.WebApp.CISService", "GetEnabledFunctions");
    private final static QName _ArrayOfCustomerItem_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfCustomerItem");
    private final static QName _GetVoucherTypesRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherTypesRequestBody");
    private final static QName _GetSalesInformationResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesInformationResponse");
    private final static QName _ReturnItemsResponse_QNAME = new QName("DVSE.WebApp.CISService", "ReturnItemsResponse");
    private final static QName _ArrayOfVoucher_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfVoucher");
    private final static QName _GetSalesResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesResponse");
    private final static QName _OutInvoices_QNAME = new QName("DVSE.WebApp.CISService", "OutInvoices");
    private final static QName _OutCustomers_QNAME = new QName("DVSE.WebApp.CISService", "OutCustomers");
    private final static QName _GetVersionResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVersionResponseBody");
    private final static QName _ResetVoucherItemsCanReturnPropertyResponse_QNAME = new QName("DVSE.WebApp.CISService", "ResetVoucherItemsCanReturnPropertyResponse");
    private final static QName _GetVoucherDetailsResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherDetailsResponseBody");
    private final static QName _GetSessionRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetSessionRequestBody");
    private final static QName _BillingType_QNAME = new QName("DVSE.WebApp.CISService", "BillingType");
    private final static QName _GetQueryTypesCustomerResponseBody_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesCustomerResponseBody");
    private final static QName _ArrayOfVoucherType_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfVoucherType");
    private final static QName _GetSalesInformation_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesInformation");
    private final static QName _GetQueryTypesCustomerResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesCustomerResponse");
    private final static QName _GetInvoicesResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetInvoicesResponse");
    private final static QName _GetQueryTypesVoucherRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesVoucherRequestBody");
    private final static QName _GetEnabledFunctionsResponse_QNAME = new QName("DVSE.WebApp.CISService", "GetEnabledFunctionsResponse");
    private final static QName _ArrayOfErpCredential_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfErpCredential");
    private final static QName _Contact_QNAME = new QName("DVSE.WebApp.CISService", "Contact");
    private final static QName _DispatchTypeList_QNAME = new QName("DVSE.WebApp.CISService", "DispatchTypeList");
    private final static QName _Voucher_QNAME = new QName("DVSE.WebApp.CISService", "Voucher");
    private final static QName _GetConnectedVouchers_QNAME = new QName("DVSE.WebApp.CISService", "GetConnectedVouchers");
    private final static QName _ArrayOfContact_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfContact");
    private final static QName _GetCustomers_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomers");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _GetVersionRequestBody_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "GetVersionRequestBody");
    private final static QName _GetVouchersRequestBody_QNAME = new QName("DVSE.WebApp.CISService", "GetVouchersRequestBody");
    private final static QName _ArrayOfVoucherItem_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfVoucherItem");
    private final static QName _Filter_QNAME = new QName("DVSE.WebApp.CISService", "filter");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _ArrayOfString_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfString");
    private final static QName _ArrayOfNote_QNAME = new QName("DVSE.WebApp.CISService", "ArrayOfNote");
    private final static QName _GetVouchers_QNAME = new QName("DVSE.WebApp.CISService", "GetVouchers");
    private final static QName _StateDescription_QNAME = new QName("DVSE.WebApp.CISService", "Description");
    private final static QName _GetQueryTypesVoucherResponseBodyGetQueryTypesVoucherResult_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesVoucherResult");
    private final static QName _DispatchTypeListDefault_QNAME = new QName("DVSE.WebApp.CISService", "Default");
    private final static QName _DispatchTypeListDispatchTypes_QNAME = new QName("DVSE.WebApp.CISService", "DispatchTypes");
    private final static QName _GetEnabledFunctionsRequestBodyCustomerId_QNAME = new QName("DVSE.WebApp.CISService", "customerId");
    private final static QName _GetEnabledFunctionsRequestBodySessionId_QNAME = new QName("DVSE.WebApp.CISService", "sessionId");
    private final static QName _GetEnabledFunctionsRequestBodyLanguage_QNAME = new QName("DVSE.WebApp.CISService", "language");
    private final static QName _GetSalesRequestBodyDateTo_QNAME = new QName("DVSE.WebApp.CISService", "dateTo");
    private final static QName _GetSalesRequestBodyDateFrom_QNAME = new QName("DVSE.WebApp.CISService", "dateFrom");
    private final static QName _VoucherItemReturnState_QNAME = new QName("DVSE.WebApp.CISService", "ReturnState");
    private final static QName _VoucherItemOrderNo_QNAME = new QName("DVSE.WebApp.CISService", "OrderNo");
    private final static QName _VoucherItemInformation_QNAME = new QName("DVSE.WebApp.CISService", "Information");
    private final static QName _VoucherItemArticleDescription_QNAME = new QName("DVSE.WebApp.CISService", "ArticleDescription");
    private final static QName _VoucherItemWholesalerArticleNumber_QNAME = new QName("DVSE.WebApp.CISService", "WholesalerArticleNumber");
    private final static QName _VoucherItemGenericArticleId_QNAME = new QName("DVSE.WebApp.CISService", "GenericArticleId");
    private final static QName _VoucherItemVehicleInfo_QNAME = new QName("DVSE.WebApp.CISService", "VehicleInfo");
    private final static QName _VoucherItemShippingMode_QNAME = new QName("DVSE.WebApp.CISService", "ShippingMode");
    private final static QName _VoucherItemVoucherId_QNAME = new QName("DVSE.WebApp.CISService", "VoucherId");
    private final static QName _VoucherItemId_QNAME = new QName("DVSE.WebApp.CISService", "Id");
    private final static QName _VoucherItemSupplierArticleNumber_QNAME = new QName("DVSE.WebApp.CISService", "SupplierArticleNumber");
    private final static QName _VoucherItemPlateId_QNAME = new QName("DVSE.WebApp.CISService", "PlateId");
    private final static QName _VoucherItemQuantityUnit_QNAME = new QName("DVSE.WebApp.CISService", "QuantityUnit");
    private final static QName _VoucherItemReturnInfo_QNAME = new QName("DVSE.WebApp.CISService", "ReturnInfo");
    private final static QName _VoucherItemSupplierId_QNAME = new QName("DVSE.WebApp.CISService", "SupplierId");
    private final static QName _VoucherItemPrices_QNAME = new QName("DVSE.WebApp.CISService", "Prices");
    private final static QName _VoucherItemGenericArticle_QNAME = new QName("DVSE.WebApp.CISService", "GenericArticle");
    private final static QName _VoucherItemCurrencyCode_QNAME = new QName("DVSE.WebApp.CISService", "CurrencyCode");
    private final static QName _VoucherItemSupplierName_QNAME = new QName("DVSE.WebApp.CISService", "SupplierName");
    private final static QName _VoucherItemTaxes_QNAME = new QName("DVSE.WebApp.CISService", "Taxes");
    private final static QName _GetVoucherDetailsResponseBodyGetVoucherDetailsResult_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherDetailsResult");
    private final static QName _ErpCredentialUrl_QNAME = new QName("DVSE.WebApp.CISService", "Url");
    private final static QName _ErpCredentialErpCustomerId_QNAME = new QName("DVSE.WebApp.CISService", "ErpCustomerId");
    private final static QName _ErpCredentialErpPassword_QNAME = new QName("DVSE.WebApp.CISService", "ErpPassword");
    private final static QName _ErpCredentialErpCustomerId3_QNAME = new QName("DVSE.WebApp.CISService", "ErpCustomerId3");
    private final static QName _ErpCredentialErpUsername_QNAME = new QName("DVSE.WebApp.CISService", "ErpUsername");
    private final static QName _ErpCredentialErpCustomerId2_QNAME = new QName("DVSE.WebApp.CISService", "ErpCustomerId2");
    private final static QName _GetInvoicesRequestBodyFilter_QNAME = new QName("DVSE.WebApp.CISService", "filter");
    private final static QName _GetCustomersRequestBodyQuery_QNAME = new QName("DVSE.WebApp.CISService", "query");
    private final static QName _GetReturnItemOptionsResponseBodyGetReturnItemOptionsResult_QNAME = new QName("DVSE.WebApp.CISService", "GetReturnItemOptionsResult");
    private final static QName _CustomerDetailsNotes_QNAME = new QName("DVSE.WebApp.CISService", "Notes");
    private final static QName _CustomerDetailsCustomerItems_QNAME = new QName("DVSE.WebApp.CISService", "CustomerItems");
    private final static QName _CustomerDetailsTourInfo_QNAME = new QName("DVSE.WebApp.CISService", "TourInfo");
    private final static QName _CustomerDetailsErpCredentials_QNAME = new QName("DVSE.WebApp.CISService", "ErpCredentials");
    private final static QName _CustomerDetailsContacts_QNAME = new QName("DVSE.WebApp.CISService", "Contacts");
    private final static QName _CustomerDetailsCustomerGroup_QNAME = new QName("DVSE.WebApp.CISService", "CustomerGroup");
    private final static QName _CustomerDetailsCooperationGroup_QNAME = new QName("DVSE.WebApp.CISService", "CooperationGroup");
    private final static QName _SalesOutletListSalesOutlets_QNAME = new QName("DVSE.WebApp.CISService", "SalesOutlets");
    private final static QName _GetSessionResponseBodyGetSessionResult_QNAME = new QName("DVSE.WebApp.CISService", "GetSessionResult");
    private final static QName _OutReturnItemOptionsReturnReasons_QNAME = new QName("DVSE.WebApp.CISService", "ReturnReasons");
    private final static QName _OutReturnItemOptionsReturnStates_QNAME = new QName("DVSE.WebApp.CISService", "ReturnStates");
    private final static QName _GetVoucherDetailsRequestBodyVoucherId_QNAME = new QName("DVSE.WebApp.CISService", "voucherId");
    private final static QName _BaseResponseErrorMessage_QNAME = new QName("DVSE.WebApp.CISService", "ErrorMessage");
    private final static QName _GetCustomersResponseBodyGetCustomersResult_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomersResult");
    private final static QName _OutVoucherDetailsVoucherItems_QNAME = new QName("DVSE.WebApp.CISService", "VoucherItems");
    private final static QName _GetSalesResponseBodyGetSalesResult_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesResult");
    private final static QName _SaleTypeSaleSubTypes_QNAME = new QName("DVSE.WebApp.CISService", "SaleSubTypes");
    private final static QName _GetCustomerResponseBodyGetCustomerResult_QNAME = new QName("DVSE.WebApp.CISService", "GetCustomerResult");
    private final static QName _ConceptCustomerImageUrl_QNAME = new QName("DVSE.WebApp.CISService", "ImageUrl");
    private final static QName _ConceptCustomerText_QNAME = new QName("DVSE.WebApp.CISService", "Text");
    private final static QName _ConceptCustomerUrlText_QNAME = new QName("DVSE.WebApp.CISService", "UrlText");
    private final static QName _ConceptCustomerIconUrl_QNAME = new QName("DVSE.WebApp.CISService", "IconUrl");
    private final static QName _OutSalesSales_QNAME = new QName("DVSE.WebApp.CISService", "Sales");
    private final static QName _ReturnItemsResponseBodyReturnItemsResult_QNAME = new QName("DVSE.WebApp.CISService", "ReturnItemsResult");
    private final static QName _OutQueryTypesQueryTypes_QNAME = new QName("DVSE.WebApp.CISService", "QueryTypes");
    private final static QName _OutCustomersCustomers_QNAME = new QName("DVSE.WebApp.CISService", "Customers");
    private final static QName _GetSalesInformationResponseBodyGetSalesInformationResult_QNAME = new QName("DVSE.WebApp.CISService", "GetSalesInformationResult");
    private final static QName _ResetVoucherItemsCanReturnPropertyResponseBodyResetVoucherItemsCanReturnPropertyResult_QNAME = new QName("DVSE.WebApp.CISService", "ResetVoucherItemsCanReturnPropertyResult");
    private final static QName _OutSessionSessionId_QNAME = new QName("DVSE.WebApp.CISService", "SessionId");
    private final static QName _ReturnItemsRequestBodyReturnableItems_QNAME = new QName("DVSE.WebApp.CISService", "returnableItems");
    private final static QName _ElectronicAddressValue_QNAME = new QName("DVSE.WebApp.CISService", "Value");
    private final static QName _OutVouchersVouchers_QNAME = new QName("DVSE.WebApp.CISService", "Vouchers");
    private final static QName _GetEnabledFunctionsResponseBodyGetEnabledFunctionsResult_QNAME = new QName("DVSE.WebApp.CISService", "GetEnabledFunctionsResult");
    private final static QName _VoucherSalespoint_QNAME = new QName("DVSE.WebApp.CISService", "Salespoint");
    private final static QName _VoucherDeliveryAddress_QNAME = new QName("DVSE.WebApp.CISService", "DeliveryAddress");
    private final static QName _VoucherPaymentInformation_QNAME = new QName("DVSE.WebApp.CISService", "PaymentInformation");
    private final static QName _VoucherVoucherDescription_QNAME = new QName("DVSE.WebApp.CISService", "VoucherDescription");
    private final static QName _VoucherDeliveryType_QNAME = new QName("DVSE.WebApp.CISService", "DeliveryType");
    private final static QName _VoucherTour_QNAME = new QName("DVSE.WebApp.CISService", "Tour");
    private final static QName _VoucherInvoiceAddress_QNAME = new QName("DVSE.WebApp.CISService", "InvoiceAddress");
    private final static QName _VoucherVoucherSummaryItems_QNAME = new QName("DVSE.WebApp.CISService", "VoucherSummaryItems");
    private final static QName _AddressStreet_QNAME = new QName("DVSE.WebApp.CISService", "Street");
    private final static QName _AddressCountry_QNAME = new QName("DVSE.WebApp.CISService", "Country");
    private final static QName _AddressCity_QNAME = new QName("DVSE.WebApp.CISService", "City");
    private final static QName _AddressDistrict_QNAME = new QName("DVSE.WebApp.CISService", "District");
    private final static QName _AddressAlternativeRecipient_QNAME = new QName("DVSE.WebApp.CISService", "AlternativeRecipient");
    private final static QName _AddressZIP_QNAME = new QName("DVSE.WebApp.CISService", "ZIP");
    private final static QName _AddressStreetExt_QNAME = new QName("DVSE.WebApp.CISService", "StreetExt");
    private final static QName _AddressPostOfficeBox_QNAME = new QName("DVSE.WebApp.CISService", "PostOfficeBox");
    private final static QName _AddressPhone_QNAME = new QName("DVSE.WebApp.CISService", "Phone");
    private final static QName _GetVoucherTypesResponseBodyGetVoucherTypesResult_QNAME = new QName("DVSE.WebApp.CISService", "GetVoucherTypesResult");
    private final static QName _GetConnectedVouchersResponseBodyGetConnectedVouchersResult_QNAME = new QName("DVSE.WebApp.CISService", "GetConnectedVouchersResult");
    private final static QName _GetQueryTypesCustomerResponseBodyGetQueryTypesCustomerResult_QNAME = new QName("DVSE.WebApp.CISService", "GetQueryTypesCustomerResult");
    private final static QName _InvoicePaymentType_QNAME = new QName("DVSE.WebApp.CISService", "PaymentType");
    private final static QName _InvoiceInstallments_QNAME = new QName("DVSE.WebApp.CISService", "Installments");
    private final static QName _OutInvoicesInvoices_QNAME = new QName("DVSE.WebApp.CISService", "Invoices");
    private final static QName _OutInvoicesStates_QNAME = new QName("DVSE.WebApp.CISService", "States");
    private final static QName _ContactLastName_QNAME = new QName("DVSE.WebApp.CISService", "LastName");
    private final static QName _ContactContactDescription_QNAME = new QName("DVSE.WebApp.CISService", "ContactDescription");
    private final static QName _ContactJobDescription_QNAME = new QName("DVSE.WebApp.CISService", "JobDescription");
    private final static QName _ContactFirstName_QNAME = new QName("DVSE.WebApp.CISService", "FirstName");
    private final static QName _ContactElectronicAddresses_QNAME = new QName("DVSE.WebApp.CISService", "ElectronicAddresses");
    private final static QName _GetSessionRequestBodyUsername_QNAME = new QName("DVSE.WebApp.CISService", "username");
    private final static QName _GetSessionRequestBodyPassword_QNAME = new QName("DVSE.WebApp.CISService", "password");
    private final static QName _AccountUsedCreditLimit_QNAME = new QName("DVSE.WebApp.CISService", "UsedCreditLimit");
    private final static QName _AccountMonetaryValueList_QNAME = new QName("DVSE.WebApp.CISService", "MonetaryValueList");
    private final static QName _AccountCreditLimit_QNAME = new QName("DVSE.WebApp.CISService", "CreditLimit");
    private final static QName _AccountBalanceDue_QNAME = new QName("DVSE.WebApp.CISService", "BalanceDue");
    private final static QName _FilterQueryValue_QNAME = new QName("DVSE.WebApp.CISService", "QueryValue");
    private final static QName _FilterDateTo_QNAME = new QName("DVSE.WebApp.CISService", "DateTo");
    private final static QName _FilterQuery_QNAME = new QName("DVSE.WebApp.CISService", "Query");
    private final static QName _FilterDateFrom_QNAME = new QName("DVSE.WebApp.CISService", "DateFrom");
    private final static QName _ReturnItemVoucherItemId_QNAME = new QName("DVSE.WebApp.CISService", "VoucherItemId");
    private final static QName _GetConnectedVouchersRequestBodyParentId_QNAME = new QName("DVSE.WebApp.CISService", "parentId");
    private final static QName _GetVersionResponseBodyGetVersionResult_QNAME = new QName("DVSE.WebApp.CISService", "GetVersionResult");
    private final static QName _BaseCustomerCompanyName_QNAME = new QName("DVSE.WebApp.CISService", "CompanyName");
    private final static QName _BaseCustomerAddresses_QNAME = new QName("DVSE.WebApp.CISService", "Addresses");
    private final static QName _CustomerItemValues_QNAME = new QName("DVSE.WebApp.CISService", "Values");
    private final static QName _GetInvoicesResponseBodyGetInvoicesResult_QNAME = new QName("DVSE.WebApp.CISService", "GetInvoicesResult");
    private final static QName _GetVouchersResponseBodyGetVouchersResult_QNAME = new QName("DVSE.WebApp.CISService", "GetVouchersResult");
    private final static QName _OutSalesInformationDisplayModes_QNAME = new QName("DVSE.WebApp.CISService", "DisplayModes");
    private final static QName _OutSalesInformationIntervals_QNAME = new QName("DVSE.WebApp.CISService", "Intervals");
    private final static QName _OutSalesInformationSaleTypes_QNAME = new QName("DVSE.WebApp.CISService", "SaleTypes");
    private final static QName _OutVoucherTypesVoucherTypes_QNAME = new QName("DVSE.WebApp.CISService", "VoucherTypes");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sagag.services.stakis.erp.wsdl.cis
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Account }
     *
     */
    public Account createAccount() {
        return new Account();
    }

    /**
     * Create an instance of {@link SaleType }
     *
     */
    public SaleType createSaleType() {
        return new SaleType();
    }

    /**
     * Create an instance of {@link ReturnItemsRequestBody }
     *
     */
    public ReturnItemsRequestBody createReturnItemsRequestBody() {
        return new ReturnItemsRequestBody();
    }

    /**
     * Create an instance of {@link CustomerState }
     *
     */
    public CustomerState createCustomerState() {
        return new CustomerState();
    }

    /**
     * Create an instance of {@link Invoice }
     *
     */
    public Invoice createInvoice() {
        return new Invoice();
    }

    /**
     * Create an instance of {@link ReturnItem }
     *
     */
    public ReturnItem createReturnItem() {
        return new ReturnItem();
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
     * Create an instance of {@link GetVoucherTypesResponseBody }
     *
     */
    public GetVoucherTypesResponseBody createGetVoucherTypesResponseBody() {
        return new GetVoucherTypesResponseBody();
    }

    /**
     * Create an instance of {@link OutSession }
     *
     */
    public OutSession createOutSession() {
        return new OutSession();
    }

    /**
     * Create an instance of {@link GetInvoicesRequestBody }
     *
     */
    public GetInvoicesRequestBody createGetInvoicesRequestBody() {
        return new GetInvoicesRequestBody();
    }

    /**
     * Create an instance of {@link OutCustomer }
     *
     */
    public OutCustomer createOutCustomer() {
        return new OutCustomer();
    }

    /**
     * Create an instance of {@link GetReturnItemOptionsResponseBody }
     *
     */
    public GetReturnItemOptionsResponseBody createGetReturnItemOptionsResponseBody() {
        return new GetReturnItemOptionsResponseBody();
    }

    /**
     * Create an instance of {@link SalesOutletList }
     *
     */
    public SalesOutletList createSalesOutletList() {
        return new SalesOutletList();
    }

    /**
     * Create an instance of {@link ArrayOfSaleSubType }
     *
     */
    public ArrayOfSaleSubType createArrayOfSaleSubType() {
        return new ArrayOfSaleSubType();
    }

    /**
     * Create an instance of {@link OutSales }
     *
     */
    public OutSales createOutSales() {
        return new OutSales();
    }

    /**
     * Create an instance of {@link ArrayOfInvoice }
     *
     */
    public ArrayOfInvoice createArrayOfInvoice() {
        return new ArrayOfInvoice();
    }

    /**
     * Create an instance of {@link GetQueryTypesVoucherResponseBody }
     *
     */
    public GetQueryTypesVoucherResponseBody createGetQueryTypesVoucherResponseBody() {
        return new GetQueryTypesVoucherResponseBody();
    }

    /**
     * Create an instance of {@link GetSalesResponseBody }
     *
     */
    public GetSalesResponseBody createGetSalesResponseBody() {
        return new GetSalesResponseBody();
    }

    /**
     * Create an instance of {@link OutVoucherDetails }
     *
     */
    public OutVoucherDetails createOutVoucherDetails() {
        return new OutVoucherDetails();
    }

    /**
     * Create an instance of {@link OutQueryTypes }
     *
     */
    public OutQueryTypes createOutQueryTypes() {
        return new OutQueryTypes();
    }

    /**
     * Create an instance of {@link ArrayOfAddress }
     *
     */
    public ArrayOfAddress createArrayOfAddress() {
        return new ArrayOfAddress();
    }

    /**
     * Create an instance of {@link VoucherSummaryItem }
     *
     */
    public VoucherSummaryItem createVoucherSummaryItem() {
        return new VoucherSummaryItem();
    }

    /**
     * Create an instance of {@link GetCustomersResponseBody }
     *
     */
    public GetCustomersResponseBody createGetCustomersResponseBody() {
        return new GetCustomersResponseBody();
    }

    /**
     * Create an instance of {@link Note }
     *
     */
    public Note createNote() {
        return new Note();
    }

    /**
     * Create an instance of {@link ReturnReason }
     *
     */
    public ReturnReason createReturnReason() {
        return new ReturnReason();
    }

    /**
     * Create an instance of {@link GetEnabledFunctionsResponseBody }
     *
     */
    public GetEnabledFunctionsResponseBody createGetEnabledFunctionsResponseBody() {
        return new GetEnabledFunctionsResponseBody();
    }

    /**
     * Create an instance of {@link OutVouchers }
     *
     */
    public OutVouchers createOutVouchers() {
        return new OutVouchers();
    }

    /**
     * Create an instance of {@link GetCustomerResponseBody }
     *
     */
    public GetCustomerResponseBody createGetCustomerResponseBody() {
        return new GetCustomerResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfTax }
     *
     */
    public ArrayOfTax createArrayOfTax() {
        return new ArrayOfTax();
    }

    /**
     * Create an instance of {@link Sale }
     *
     */
    public Sale createSale() {
        return new Sale();
    }

    /**
     * Create an instance of {@link GetCustomerRequestBody }
     *
     */
    public GetCustomerRequestBody createGetCustomerRequestBody() {
        return new GetCustomerRequestBody();
    }

    /**
     * Create an instance of {@link CustomerItem }
     *
     */
    public CustomerItem createCustomerItem() {
        return new CustomerItem();
    }

    /**
     * Create an instance of {@link ResetVoucherItemsCanReturnPropertyRequestBody }
     *
     */
    public ResetVoucherItemsCanReturnPropertyRequestBody createResetVoucherItemsCanReturnPropertyRequestBody() {
        return new ResetVoucherItemsCanReturnPropertyRequestBody();
    }

    /**
     * Create an instance of {@link ResetVoucherItemsCanReturnPropertyResponseBody }
     *
     */
    public ResetVoucherItemsCanReturnPropertyResponseBody createResetVoucherItemsCanReturnPropertyResponseBody() {
        return new ResetVoucherItemsCanReturnPropertyResponseBody();
    }

    /**
     * Create an instance of {@link GetSessionResponseBody }
     *
     */
    public GetSessionResponseBody createGetSessionResponseBody() {
        return new GetSessionResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfReturnItem }
     *
     */
    public ArrayOfReturnItem createArrayOfReturnItem() {
        return new ArrayOfReturnItem();
    }

    /**
     * Create an instance of {@link ArrayOfSalesOutlet }
     *
     */
    public ArrayOfSalesOutlet createArrayOfSalesOutlet() {
        return new ArrayOfSalesOutlet();
    }

    /**
     * Create an instance of {@link GetSalesRequestBody }
     *
     */
    public GetSalesRequestBody createGetSalesRequestBody() {
        return new GetSalesRequestBody();
    }

    /**
     * Create an instance of {@link BaseResponse }
     *
     */
    public BaseResponse createBaseResponse() {
        return new BaseResponse();
    }

    /**
     * Create an instance of {@link GetVersionRequestBody }
     *
     */
    public GetVersionRequestBody createGetVersionRequestBody() {
        return new GetVersionRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfSaleType }
     *
     */
    public ArrayOfSaleType createArrayOfSaleType() {
        return new ArrayOfSaleType();
    }

    /**
     * Create an instance of {@link OutReturnItemOptions }
     *
     */
    public OutReturnItemOptions createOutReturnItemOptions() {
        return new OutReturnItemOptions();
    }

    /**
     * Create an instance of {@link GetCustomersRequestBody }
     *
     */
    public GetCustomersRequestBody createGetCustomersRequestBody() {
        return new GetCustomersRequestBody();
    }

    /**
     * Create an instance of {@link GetConnectedVouchersResponseBody }
     *
     */
    public GetConnectedVouchersResponseBody createGetConnectedVouchersResponseBody() {
        return new GetConnectedVouchersResponseBody();
    }

    /**
     * Create an instance of {@link GetSalesInformationRequestBody }
     *
     */
    public GetSalesInformationRequestBody createGetSalesInformationRequestBody() {
        return new GetSalesInformationRequestBody();
    }

    /**
     * Create an instance of {@link Installment }
     *
     */
    public Installment createInstallment() {
        return new Installment();
    }

    /**
     * Create an instance of {@link GetVouchersResponseBody }
     *
     */
    public GetVouchersResponseBody createGetVouchersResponseBody() {
        return new GetVouchersResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfInstallment }
     *
     */
    public ArrayOfInstallment createArrayOfInstallment() {
        return new ArrayOfInstallment();
    }

    /**
     * Create an instance of {@link ArrayOfState }
     *
     */
    public ArrayOfState createArrayOfState() {
        return new ArrayOfState();
    }

    /**
     * Create an instance of {@link MonetaryValue }
     *
     */
    public MonetaryValue createMonetaryValue() {
        return new MonetaryValue();
    }

    /**
     * Create an instance of {@link ElectronicAddress }
     *
     */
    public ElectronicAddress createElectronicAddress() {
        return new ElectronicAddress();
    }

    /**
     * Create an instance of {@link GetVoucherDetailsRequestBody }
     *
     */
    public GetVoucherDetailsRequestBody createGetVoucherDetailsRequestBody() {
        return new GetVoucherDetailsRequestBody();
    }

    /**
     * Create an instance of {@link ErpCredential }
     *
     */
    public ErpCredential createErpCredential() {
        return new ErpCredential();
    }

    /**
     * Create an instance of {@link ArrayOfVoucherSummaryItem }
     *
     */
    public ArrayOfVoucherSummaryItem createArrayOfVoucherSummaryItem() {
        return new ArrayOfVoucherSummaryItem();
    }

    /**
     * Create an instance of {@link GetSessionRequestBody }
     *
     */
    public GetSessionRequestBody createGetSessionRequestBody() {
        return new GetSessionRequestBody();
    }

    /**
     * Create an instance of {@link GetQueryTypesCustomerRequestBody }
     *
     */
    public GetQueryTypesCustomerRequestBody createGetQueryTypesCustomerRequestBody() {
        return new GetQueryTypesCustomerRequestBody();
    }

    /**
     * Create an instance of {@link GetInvoicesResponseBody }
     *
     */
    public GetInvoicesResponseBody createGetInvoicesResponseBody() {
        return new GetInvoicesResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfConceptCustomer }
     *
     */
    public ArrayOfConceptCustomer createArrayOfConceptCustomer() {
        return new ArrayOfConceptCustomer();
    }

    /**
     * Create an instance of {@link GetEnabledFunctionsRequestBody }
     *
     */
    public GetEnabledFunctionsRequestBody createGetEnabledFunctionsRequestBody() {
        return new GetEnabledFunctionsRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfInt }
     *
     */
    public ArrayOfInt createArrayOfInt() {
        return new ArrayOfInt();
    }

    /**
     * Create an instance of {@link BaseCustomer }
     *
     */
    public BaseCustomer createBaseCustomer() {
        return new BaseCustomer();
    }

    /**
     * Create an instance of {@link ArrayOfPrice }
     *
     */
    public ArrayOfPrice createArrayOfPrice() {
        return new ArrayOfPrice();
    }

    /**
     * Create an instance of {@link GetSalesInformationResponseBody }
     *
     */
    public GetSalesInformationResponseBody createGetSalesInformationResponseBody() {
        return new GetSalesInformationResponseBody();
    }

    /**
     * Create an instance of {@link SalesOutlet }
     *
     */
    public SalesOutlet createSalesOutlet() {
        return new SalesOutlet();
    }

    /**
     * Create an instance of {@link GetVoucherTypesRequestBody }
     *
     */
    public GetVoucherTypesRequestBody createGetVoucherTypesRequestBody() {
        return new GetVoucherTypesRequestBody();
    }

    /**
     * Create an instance of {@link GetVoucherDetailsResponseBody }
     *
     */
    public GetVoucherDetailsResponseBody createGetVoucherDetailsResponseBody() {
        return new GetVoucherDetailsResponseBody();
    }

    /**
     * Create an instance of {@link Tax }
     *
     */
    public Tax createTax() {
        return new Tax();
    }

    /**
     * Create an instance of {@link CustomerDetails }
     *
     */
    public CustomerDetails createCustomerDetails() {
        return new CustomerDetails();
    }

    /**
     * Create an instance of {@link GetConnectedVouchersRequestBody }
     *
     */
    public GetConnectedVouchersRequestBody createGetConnectedVouchersRequestBody() {
        return new GetConnectedVouchersRequestBody();
    }

    /**
     * Create an instance of {@link VoucherItem }
     *
     */
    public VoucherItem createVoucherItem() {
        return new VoucherItem();
    }

    /**
     * Create an instance of {@link DispatchType }
     *
     */
    public DispatchType createDispatchType() {
        return new DispatchType();
    }

    /**
     * Create an instance of {@link ReturnItemsResponseBody }
     *
     */
    public ReturnItemsResponseBody createReturnItemsResponseBody() {
        return new ReturnItemsResponseBody();
    }

    /**
     * Create an instance of {@link GetReturnItemOptionsRequestBody }
     *
     */
    public GetReturnItemOptionsRequestBody createGetReturnItemOptionsRequestBody() {
        return new GetReturnItemOptionsRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfSale }
     *
     */
    public ArrayOfSale createArrayOfSale() {
        return new ArrayOfSale();
    }

    /**
     * Create an instance of {@link ArrayOfQueryType }
     *
     */
    public ArrayOfQueryType createArrayOfQueryType() {
        return new ArrayOfQueryType();
    }

    /**
     * Create an instance of {@link ArrayOfMonetaryValue }
     *
     */
    public ArrayOfMonetaryValue createArrayOfMonetaryValue() {
        return new ArrayOfMonetaryValue();
    }

    /**
     * Create an instance of {@link GetQueryTypesVoucherRequestBody }
     *
     */
    public GetQueryTypesVoucherRequestBody createGetQueryTypesVoucherRequestBody() {
        return new GetQueryTypesVoucherRequestBody();
    }

    /**
     * Create an instance of {@link GetVersionResponseBody }
     *
     */
    public GetVersionResponseBody createGetVersionResponseBody() {
        return new GetVersionResponseBody();
    }

    /**
     * Create an instance of {@link ArrayOfReturnReason }
     *
     */
    public ArrayOfReturnReason createArrayOfReturnReason() {
        return new ArrayOfReturnReason();
    }

    /**
     * Create an instance of {@link Price }
     *
     */
    public Price createPrice() {
        return new Price();
    }

    /**
     * Create an instance of {@link State }
     *
     */
    public State createState() {
        return new State();
    }

    /**
     * Create an instance of {@link ConceptCustomer }
     *
     */
    public ConceptCustomer createConceptCustomer() {
        return new ConceptCustomer();
    }

    /**
     * Create an instance of {@link OutVoucherTypes }
     *
     */
    public OutVoucherTypes createOutVoucherTypes() {
        return new OutVoucherTypes();
    }

    /**
     * Create an instance of {@link OutSalesInformation }
     *
     */
    public OutSalesInformation createOutSalesInformation() {
        return new OutSalesInformation();
    }

    /**
     * Create an instance of {@link SaleSubType }
     *
     */
    public SaleSubType createSaleSubType() {
        return new SaleSubType();
    }

    /**
     * Create an instance of {@link QueryType }
     *
     */
    public QueryType createQueryType() {
        return new QueryType();
    }

    /**
     * Create an instance of {@link VoucherType }
     *
     */
    public VoucherType createVoucherType() {
        return new VoucherType();
    }

    /**
     * Create an instance of {@link ArrayOfVoucher }
     *
     */
    public ArrayOfVoucher createArrayOfVoucher() {
        return new ArrayOfVoucher();
    }

    /**
     * Create an instance of {@link OutInvoices }
     *
     */
    public OutInvoices createOutInvoices() {
        return new OutInvoices();
    }

    /**
     * Create an instance of {@link ArrayOfElectronicAddress }
     *
     */
    public ArrayOfElectronicAddress createArrayOfElectronicAddress() {
        return new ArrayOfElectronicAddress();
    }

    /**
     * Create an instance of {@link ArrayOfCustomer }
     *
     */
    public ArrayOfCustomer createArrayOfCustomer() {
        return new ArrayOfCustomer();
    }

    /**
     * Create an instance of {@link ArrayOfCustomerItem }
     *
     */
    public ArrayOfCustomerItem createArrayOfCustomerItem() {
        return new ArrayOfCustomerItem();
    }

    /**
     * Create an instance of {@link ArrayOfVoucherType }
     *
     */
    public ArrayOfVoucherType createArrayOfVoucherType() {
        return new ArrayOfVoucherType();
    }

    /**
     * Create an instance of {@link GetQueryTypesCustomerResponseBody }
     *
     */
    public GetQueryTypesCustomerResponseBody createGetQueryTypesCustomerResponseBody() {
        return new GetQueryTypesCustomerResponseBody();
    }

    /**
     * Create an instance of {@link OutCustomers }
     *
     */
    public OutCustomers createOutCustomers() {
        return new OutCustomers();
    }

    /**
     * Create an instance of {@link BillingType }
     *
     */
    public BillingType createBillingType() {
        return new BillingType();
    }

    /**
     * Create an instance of {@link ArrayOfErpCredential }
     *
     */
    public ArrayOfErpCredential createArrayOfErpCredential() {
        return new ArrayOfErpCredential();
    }

    /**
     * Create an instance of {@link Contact }
     *
     */
    public Contact createContact() {
        return new Contact();
    }

    /**
     * Create an instance of {@link DispatchTypeList }
     *
     */
    public DispatchTypeList createDispatchTypeList() {
        return new DispatchTypeList();
    }

    /**
     * Create an instance of {@link Voucher }
     *
     */
    public Voucher createVoucher() {
        return new Voucher();
    }

    /**
     * Create an instance of {@link GetVouchersRequestBody }
     *
     */
    public GetVouchersRequestBody createGetVouchersRequestBody() {
        return new GetVouchersRequestBody();
    }

    /**
     * Create an instance of {@link ArrayOfVoucherItem }
     *
     */
    public ArrayOfVoucherItem createArrayOfVoucherItem() {
        return new ArrayOfVoucherItem();
    }

    /**
     * Create an instance of {@link Filter }
     *
     */
    public Filter createFilter() {
        return new Filter();
    }

    /**
     * Create an instance of {@link ArrayOfNote }
     *
     */
    public ArrayOfNote createArrayOfNote() {
        return new ArrayOfNote();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     *
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link ArrayOfContact }
     *
     */
    public ArrayOfContact createArrayOfContact() {
        return new ArrayOfContact();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherTypesResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherTypesResponseBody")
    public JAXBElement<GetVoucherTypesResponseBody> createGetVoucherTypesResponseBody(GetVoucherTypesResponseBody value) {
        return new JAXBElement<GetVoucherTypesResponseBody>(_GetVoucherTypesResponseBody_QNAME, GetVoucherTypesResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutSession }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutSession")
    public JAXBElement<OutSession> createOutSession(OutSession value) {
        return new JAXBElement<OutSession>(_OutSession_QNAME, OutSession.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInvoicesRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetInvoices")
    public JAXBElement<GetInvoicesRequestBody> createGetInvoices(GetInvoicesRequestBody value) {
        return new JAXBElement<GetInvoicesRequestBody>(_GetInvoices_QNAME, GetInvoicesRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutCustomer")
    public JAXBElement<OutCustomer> createOutCustomer(OutCustomer value) {
        return new JAXBElement<OutCustomer>(_OutCustomer_QNAME, OutCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Account")
    public JAXBElement<Account> createAccount(Account value) {
        return new JAXBElement<Account>(_Account_QNAME, Account.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaleType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SaleType")
    public JAXBElement<SaleType> createSaleType(SaleType value) {
        return new JAXBElement<SaleType>(_SaleType_QNAME, SaleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnItemsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnItemsRequestBody")
    public JAXBElement<ReturnItemsRequestBody> createReturnItemsRequestBody(ReturnItemsRequestBody value) {
        return new JAXBElement<ReturnItemsRequestBody>(_ReturnItemsRequestBody_QNAME, ReturnItemsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CustomerState")
    public JAXBElement<CustomerState> createCustomerState(CustomerState value) {
        return new JAXBElement<CustomerState>(_CustomerState_QNAME, CustomerState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Invoice }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Invoice")
    public JAXBElement<Invoice> createInvoice(Invoice value) {
        return new JAXBElement<Invoice>(_Invoice_QNAME, Invoice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnItem")
    public JAXBElement<ReturnItem> createReturnItem(ReturnItem value) {
        return new JAXBElement<ReturnItem>(_ReturnItem_QNAME, ReturnItem.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Customer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Customer")
    public JAXBElement<Customer> createCustomer(Customer value) {
        return new JAXBElement<Customer>(_Customer_QNAME, Customer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Address")
    public JAXBElement<Address> createAddress(Address value) {
        return new JAXBElement<Address>(_Address_QNAME, Address.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDispatchType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfDispatchType")
    public JAXBElement<ArrayOfDispatchType> createArrayOfDispatchType(ArrayOfDispatchType value) {
        return new JAXBElement<ArrayOfDispatchType>(_ArrayOfDispatchType_QNAME, ArrayOfDispatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutSales }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutSales")
    public JAXBElement<OutSales> createOutSales(OutSales value) {
        return new JAXBElement<OutSales>(_OutSales_QNAME, OutSales.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSaleSubType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfSaleSubType")
    public JAXBElement<ArrayOfSaleSubType> createArrayOfSaleSubType(ArrayOfSaleSubType value) {
        return new JAXBElement<ArrayOfSaleSubType>(_ArrayOfSaleSubType_QNAME, ArrayOfSaleSubType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesVoucherResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesVoucherResponseBody")
    public JAXBElement<GetQueryTypesVoucherResponseBody> createGetQueryTypesVoucherResponseBody(GetQueryTypesVoucherResponseBody value) {
        return new JAXBElement<GetQueryTypesVoucherResponseBody>(_GetQueryTypesVoucherResponseBody_QNAME, GetQueryTypesVoucherResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInvoice }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfInvoice")
    public JAXBElement<ArrayOfInvoice> createArrayOfInvoice(ArrayOfInvoice value) {
        return new JAXBElement<ArrayOfInvoice>(_ArrayOfInvoice_QNAME, ArrayOfInvoice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesResponseBody")
    public JAXBElement<GetSalesResponseBody> createGetSalesResponseBody(GetSalesResponseBody value) {
        return new JAXBElement<GetSalesResponseBody>(_GetSalesResponseBody_QNAME, GetSalesResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReturnItemOptionsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetReturnItemOptionsResponse")
    public JAXBElement<GetReturnItemOptionsResponseBody> createGetReturnItemOptionsResponse(GetReturnItemOptionsResponseBody value) {
        return new JAXBElement<GetReturnItemOptionsResponseBody>(_GetReturnItemOptionsResponse_QNAME, GetReturnItemOptionsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SalesOutletList }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SalesOutletList")
    public JAXBElement<SalesOutletList> createSalesOutletList(SalesOutletList value) {
        return new JAXBElement<SalesOutletList>(_SalesOutletList_QNAME, SalesOutletList.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddress }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfAddress")
    public JAXBElement<ArrayOfAddress> createArrayOfAddress(ArrayOfAddress value) {
        return new JAXBElement<ArrayOfAddress>(_ArrayOfAddress_QNAME, ArrayOfAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutQueryTypes")
    public JAXBElement<OutQueryTypes> createOutQueryTypes(OutQueryTypes value) {
        return new JAXBElement<OutQueryTypes>(_OutQueryTypes_QNAME, OutQueryTypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVoucherDetails }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutVoucherDetails")
    public JAXBElement<OutVoucherDetails> createOutVoucherDetails(OutVoucherDetails value) {
        return new JAXBElement<OutVoucherDetails>(_OutVoucherDetails_QNAME, OutVoucherDetails.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnReason }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnReason")
    public JAXBElement<ReturnReason> createReturnReason(ReturnReason value) {
        return new JAXBElement<ReturnReason>(_ReturnReason_QNAME, ReturnReason.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEnabledFunctionsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetEnabledFunctionsResponseBody")
    public JAXBElement<GetEnabledFunctionsResponseBody> createGetEnabledFunctionsResponseBody(GetEnabledFunctionsResponseBody value) {
        return new JAXBElement<GetEnabledFunctionsResponseBody>(_GetEnabledFunctionsResponseBody_QNAME, GetEnabledFunctionsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutVouchers")
    public JAXBElement<OutVouchers> createOutVouchers(OutVouchers value) {
        return new JAXBElement<OutVouchers>(_OutVouchers_QNAME, OutVouchers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomerResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomerResponse")
    public JAXBElement<GetCustomerResponseBody> createGetCustomerResponse(GetCustomerResponseBody value) {
        return new JAXBElement<GetCustomerResponseBody>(_GetCustomerResponse_QNAME, GetCustomerResponseBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTax }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfTax")
    public JAXBElement<ArrayOfTax> createArrayOfTax(ArrayOfTax value) {
        return new JAXBElement<ArrayOfTax>(_ArrayOfTax_QNAME, ArrayOfTax.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInvoicesRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetInvoicesRequestBody")
    public JAXBElement<GetInvoicesRequestBody> createGetInvoicesRequestBody(GetInvoicesRequestBody value) {
        return new JAXBElement<GetInvoicesRequestBody>(_GetInvoicesRequestBody_QNAME, GetInvoicesRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link VoucherSummaryItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherSummaryItem")
    public JAXBElement<VoucherSummaryItem> createVoucherSummaryItem(VoucherSummaryItem value) {
        return new JAXBElement<VoucherSummaryItem>(_VoucherSummaryItem_QNAME, VoucherSummaryItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Note }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Note")
    public JAXBElement<Note> createNote(Note value) {
        return new JAXBElement<Note>(_Note_QNAME, Note.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomersResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomersResponse")
    public JAXBElement<GetCustomersResponseBody> createGetCustomersResponse(GetCustomersResponseBody value) {
        return new JAXBElement<GetCustomersResponseBody>(_GetCustomersResponse_QNAME, GetCustomersResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomerResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomerResponseBody")
    public JAXBElement<GetCustomerResponseBody> createGetCustomerResponseBody(GetCustomerResponseBody value) {
        return new JAXBElement<GetCustomerResponseBody>(_GetCustomerResponseBody_QNAME, GetCustomerResponseBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSalesOutlet }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfSalesOutlet")
    public JAXBElement<ArrayOfSalesOutlet> createArrayOfSalesOutlet(ArrayOfSalesOutlet value) {
        return new JAXBElement<ArrayOfSalesOutlet>(_ArrayOfSalesOutlet_QNAME, ArrayOfSalesOutlet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesRequestBody")
    public JAXBElement<GetSalesRequestBody> createGetSalesRequestBody(GetSalesRequestBody value) {
        return new JAXBElement<GetSalesRequestBody>(_GetSalesRequestBody_QNAME, GetSalesRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CustomerItem")
    public JAXBElement<CustomerItem> createCustomerItem(CustomerItem value) {
        return new JAXBElement<CustomerItem>(_CustomerItem_QNAME, CustomerItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetVoucherItemsCanReturnPropertyRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ResetVoucherItemsCanReturnProperty")
    public JAXBElement<ResetVoucherItemsCanReturnPropertyRequestBody> createResetVoucherItemsCanReturnProperty(ResetVoucherItemsCanReturnPropertyRequestBody value) {
        return new JAXBElement<ResetVoucherItemsCanReturnPropertyRequestBody>(_ResetVoucherItemsCanReturnProperty_QNAME, ResetVoucherItemsCanReturnPropertyRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sale }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Sale")
    public JAXBElement<Sale> createSale(Sale value) {
        return new JAXBElement<Sale>(_Sale_QNAME, Sale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomerRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomerRequestBody")
    public JAXBElement<GetCustomerRequestBody> createGetCustomerRequestBody(GetCustomerRequestBody value) {
        return new JAXBElement<GetCustomerRequestBody>(_GetCustomerRequestBody_QNAME, GetCustomerRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetVoucherItemsCanReturnPropertyResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ResetVoucherItemsCanReturnPropertyResponseBody")
    public JAXBElement<ResetVoucherItemsCanReturnPropertyResponseBody> createResetVoucherItemsCanReturnPropertyResponseBody(ResetVoucherItemsCanReturnPropertyResponseBody value) {
        return new JAXBElement<ResetVoucherItemsCanReturnPropertyResponseBody>(_ResetVoucherItemsCanReturnPropertyResponseBody_QNAME, ResetVoucherItemsCanReturnPropertyResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSessionResponseBody")
    public JAXBElement<GetSessionResponseBody> createGetSessionResponseBody(GetSessionResponseBody value) {
        return new JAXBElement<GetSessionResponseBody>(_GetSessionResponseBody_QNAME, GetSessionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfReturnItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfReturnItem")
    public JAXBElement<ArrayOfReturnItem> createArrayOfReturnItem(ArrayOfReturnItem value) {
        return new JAXBElement<ArrayOfReturnItem>(_ArrayOfReturnItem_QNAME, ArrayOfReturnItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutReturnItemOptions }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutReturnItemOptions")
    public JAXBElement<OutReturnItemOptions> createOutReturnItemOptions(OutReturnItemOptions value) {
        return new JAXBElement<OutReturnItemOptions>(_OutReturnItemOptions_QNAME, OutReturnItemOptions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomersResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomersResponseBody")
    public JAXBElement<GetCustomersResponseBody> createGetCustomersResponseBody(GetCustomersResponseBody value) {
        return new JAXBElement<GetCustomersResponseBody>(_GetCustomersResponseBody_QNAME, GetCustomersResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSaleType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfSaleType")
    public JAXBElement<ArrayOfSaleType> createArrayOfSaleType(ArrayOfSaleType value) {
        return new JAXBElement<ArrayOfSaleType>(_ArrayOfSaleType_QNAME, ArrayOfSaleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomersRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomersRequestBody")
    public JAXBElement<GetCustomersRequestBody> createGetCustomersRequestBody(GetCustomersRequestBody value) {
        return new JAXBElement<GetCustomersRequestBody>(_GetCustomersRequestBody_QNAME, GetCustomersRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesInformationRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesInformationRequestBody")
    public JAXBElement<GetSalesInformationRequestBody> createGetSalesInformationRequestBody(GetSalesInformationRequestBody value) {
        return new JAXBElement<GetSalesInformationRequestBody>(_GetSalesInformationRequestBody_QNAME, GetSalesInformationRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectedVouchersResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetConnectedVouchersResponse")
    public JAXBElement<GetConnectedVouchersResponseBody> createGetConnectedVouchersResponse(GetConnectedVouchersResponseBody value) {
        return new JAXBElement<GetConnectedVouchersResponseBody>(_GetConnectedVouchersResponse_QNAME, GetConnectedVouchersResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "BaseResponse")
    public JAXBElement<BaseResponse> createBaseResponse(BaseResponse value) {
        return new JAXBElement<BaseResponse>(_BaseResponse_QNAME, BaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomerRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomer")
    public JAXBElement<GetCustomerRequestBody> createGetCustomer(GetCustomerRequestBody value) {
        return new JAXBElement<GetCustomerRequestBody>(_GetCustomer_QNAME, GetCustomerRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVersion")
    public JAXBElement<GetVersionRequestBody> createGetVersion(GetVersionRequestBody value) {
        return new JAXBElement<GetVersionRequestBody>(_GetVersion_QNAME, GetVersionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ElectronicAddress }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ElectronicAddress")
    public JAXBElement<ElectronicAddress> createElectronicAddress(ElectronicAddress value) {
        return new JAXBElement<ElectronicAddress>(_ElectronicAddress_QNAME, ElectronicAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherDetailsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherDetailsRequestBody")
    public JAXBElement<GetVoucherDetailsRequestBody> createGetVoucherDetailsRequestBody(GetVoucherDetailsRequestBody value) {
        return new JAXBElement<GetVoucherDetailsRequestBody>(_GetVoucherDetailsRequestBody_QNAME, GetVoucherDetailsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MonetaryValue }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "MonetaryValue")
    public JAXBElement<MonetaryValue> createMonetaryValue(MonetaryValue value) {
        return new JAXBElement<MonetaryValue>(_MonetaryValue_QNAME, MonetaryValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErpCredential }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpCredential")
    public JAXBElement<ErpCredential> createErpCredential(ErpCredential value) {
        return new JAXBElement<ErpCredential>(_ErpCredential_QNAME, ErpCredential.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucherSummaryItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfVoucherSummaryItem")
    public JAXBElement<ArrayOfVoucherSummaryItem> createArrayOfVoucherSummaryItem(ArrayOfVoucherSummaryItem value) {
        return new JAXBElement<ArrayOfVoucherSummaryItem>(_ArrayOfVoucherSummaryItem_QNAME, ArrayOfVoucherSummaryItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSession")
    public JAXBElement<GetSessionRequestBody> createGetSession(GetSessionRequestBody value) {
        return new JAXBElement<GetSessionRequestBody>(_GetSession_QNAME, GetSessionRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Installment }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Installment")
    public JAXBElement<Installment> createInstallment(Installment value) {
        return new JAXBElement<Installment>(_Installment_QNAME, Installment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVouchersResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVouchersResponse")
    public JAXBElement<GetVouchersResponseBody> createGetVouchersResponse(GetVouchersResponseBody value) {
        return new JAXBElement<GetVouchersResponseBody>(_GetVouchersResponse_QNAME, GetVouchersResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInstallment }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfInstallment")
    public JAXBElement<ArrayOfInstallment> createArrayOfInstallment(ArrayOfInstallment value) {
        return new JAXBElement<ArrayOfInstallment>(_ArrayOfInstallment_QNAME, ArrayOfInstallment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfState")
    public JAXBElement<ArrayOfState> createArrayOfState(ArrayOfState value) {
        return new JAXBElement<ArrayOfState>(_ArrayOfState_QNAME, ArrayOfState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, (value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEnabledFunctionsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetEnabledFunctionsRequestBody")
    public JAXBElement<GetEnabledFunctionsRequestBody> createGetEnabledFunctionsRequestBody(GetEnabledFunctionsRequestBody value) {
        return new JAXBElement<GetEnabledFunctionsRequestBody>(_GetEnabledFunctionsRequestBody_QNAME, GetEnabledFunctionsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfInt")
    public JAXBElement<ArrayOfInt> createArrayOfInt(ArrayOfInt value) {
        return new JAXBElement<ArrayOfInt>(_ArrayOfInt_QNAME, ArrayOfInt.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherTypesResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherTypesResponse")
    public JAXBElement<GetVoucherTypesResponseBody> createGetVoucherTypesResponse(GetVoucherTypesResponseBody value) {
        return new JAXBElement<GetVoucherTypesResponseBody>(_GetVoucherTypesResponse_QNAME, GetVoucherTypesResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesCustomerRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesCustomerRequestBody")
    public JAXBElement<GetQueryTypesCustomerRequestBody> createGetQueryTypesCustomerRequestBody(GetQueryTypesCustomerRequestBody value) {
        return new JAXBElement<GetQueryTypesCustomerRequestBody>(_GetQueryTypesCustomerRequestBody_QNAME, GetQueryTypesCustomerRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInvoicesResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetInvoicesResponseBody")
    public JAXBElement<GetInvoicesResponseBody> createGetInvoicesResponseBody(GetInvoicesResponseBody value) {
        return new JAXBElement<GetInvoicesResponseBody>(_GetInvoicesResponseBody_QNAME, GetInvoicesResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfConceptCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfConceptCustomer")
    public JAXBElement<ArrayOfConceptCustomer> createArrayOfConceptCustomer(ArrayOfConceptCustomer value) {
        return new JAXBElement<ArrayOfConceptCustomer>(_ArrayOfConceptCustomer_QNAME, ArrayOfConceptCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesCustomerRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesCustomer")
    public JAXBElement<GetQueryTypesCustomerRequestBody> createGetQueryTypesCustomer(GetQueryTypesCustomerRequestBody value) {
        return new JAXBElement<GetQueryTypesCustomerRequestBody>(_GetQueryTypesCustomer_QNAME, GetQueryTypesCustomerRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SalesOutlet }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SalesOutlet")
    public JAXBElement<SalesOutlet> createSalesOutlet(SalesOutlet value) {
        return new JAXBElement<SalesOutlet>(_SalesOutlet_QNAME, SalesOutlet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherTypesRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherTypes")
    public JAXBElement<GetVoucherTypesRequestBody> createGetVoucherTypes(GetVoucherTypesRequestBody value) {
        return new JAXBElement<GetVoucherTypesRequestBody>(_GetVoucherTypes_QNAME, GetVoucherTypesRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherDetailsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherDetailsResponse")
    public JAXBElement<GetVoucherDetailsResponseBody> createGetVoucherDetailsResponse(GetVoucherDetailsResponseBody value) {
        return new JAXBElement<GetVoucherDetailsResponseBody>(_GetVoucherDetailsResponse_QNAME, GetVoucherDetailsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tax }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Tax")
    public JAXBElement<Tax> createTax(Tax value) {
        return new JAXBElement<Tax>(_Tax_QNAME, Tax.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSales")
    public JAXBElement<GetSalesRequestBody> createGetSales(GetSalesRequestBody value) {
        return new JAXBElement<GetSalesRequestBody>(_GetSales_QNAME, GetSalesRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerDetails }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CustomerDetails")
    public JAXBElement<CustomerDetails> createCustomerDetails(CustomerDetails value) {
        return new JAXBElement<CustomerDetails>(_CustomerDetails_QNAME, CustomerDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectedVouchersRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetConnectedVouchersRequestBody")
    public JAXBElement<GetConnectedVouchersRequestBody> createGetConnectedVouchersRequestBody(GetConnectedVouchersRequestBody value) {
        return new JAXBElement<GetConnectedVouchersRequestBody>(_GetConnectedVouchersRequestBody_QNAME, GetConnectedVouchersRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfPrice")
    public JAXBElement<ArrayOfPrice> createArrayOfPrice(ArrayOfPrice value) {
        return new JAXBElement<ArrayOfPrice>(_ArrayOfPrice_QNAME, ArrayOfPrice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReturnItemOptionsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetReturnItemOptionsResponseBody")
    public JAXBElement<GetReturnItemOptionsResponseBody> createGetReturnItemOptionsResponseBody(GetReturnItemOptionsResponseBody value) {
        return new JAXBElement<GetReturnItemOptionsResponseBody>(_GetReturnItemOptionsResponseBody_QNAME, GetReturnItemOptionsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "BaseCustomer")
    public JAXBElement<BaseCustomer> createBaseCustomer(BaseCustomer value) {
        return new JAXBElement<BaseCustomer>(_BaseCustomer_QNAME, BaseCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesInformationResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesInformationResponseBody")
    public JAXBElement<GetSalesInformationResponseBody> createGetSalesInformationResponseBody(GetSalesInformationResponseBody value) {
        return new JAXBElement<GetSalesInformationResponseBody>(_GetSalesInformationResponseBody_QNAME, GetSalesInformationResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnItemsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnItemsResponseBody")
    public JAXBElement<ReturnItemsResponseBody> createReturnItemsResponseBody(ReturnItemsResponseBody value) {
        return new JAXBElement<ReturnItemsResponseBody>(_ReturnItemsResponseBody_QNAME, ReturnItemsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReturnItemOptionsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetReturnItemOptions")
    public JAXBElement<GetReturnItemOptionsRequestBody> createGetReturnItemOptions(GetReturnItemOptionsRequestBody value) {
        return new JAXBElement<GetReturnItemOptionsRequestBody>(_GetReturnItemOptions_QNAME, GetReturnItemOptionsRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSessionResponse")
    public JAXBElement<GetSessionResponseBody> createGetSessionResponse(GetSessionResponseBody value) {
        return new JAXBElement<GetSessionResponseBody>(_GetSessionResponse_QNAME, GetSessionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VoucherItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherItem")
    public JAXBElement<VoucherItem> createVoucherItem(VoucherItem value) {
        return new JAXBElement<VoucherItem>(_VoucherItem_QNAME, VoucherItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVouchersResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVouchersResponseBody")
    public JAXBElement<GetVouchersResponseBody> createGetVouchersResponseBody(GetVouchersResponseBody value) {
        return new JAXBElement<GetVouchersResponseBody>(_GetVouchersResponseBody_QNAME, GetVouchersResponseBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesVoucherResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesVoucherResponse")
    public JAXBElement<GetQueryTypesVoucherResponseBody> createGetQueryTypesVoucherResponse(GetQueryTypesVoucherResponseBody value) {
        return new JAXBElement<GetQueryTypesVoucherResponseBody>(_GetQueryTypesVoucherResponse_QNAME, GetQueryTypesVoucherResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DispatchType")
    public JAXBElement<DispatchType> createDispatchType(DispatchType value) {
        return new JAXBElement<DispatchType>(_DispatchType_QNAME, DispatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMonetaryValue }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfMonetaryValue")
    public JAXBElement<ArrayOfMonetaryValue> createArrayOfMonetaryValue(ArrayOfMonetaryValue value) {
        return new JAXBElement<ArrayOfMonetaryValue>(_ArrayOfMonetaryValue_QNAME, ArrayOfMonetaryValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesVoucherRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesVoucher")
    public JAXBElement<GetQueryTypesVoucherRequestBody> createGetQueryTypesVoucher(GetQueryTypesVoucherRequestBody value) {
        return new JAXBElement<GetQueryTypesVoucherRequestBody>(_GetQueryTypesVoucher_QNAME, GetQueryTypesVoucherRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfReturnReason }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfReturnReason")
    public JAXBElement<ArrayOfReturnReason> createArrayOfReturnReason(ArrayOfReturnReason value) {
        return new JAXBElement<ArrayOfReturnReason>(_ArrayOfReturnReason_QNAME, ArrayOfReturnReason.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVersionResponse")
    public JAXBElement<GetVersionResponseBody> createGetVersionResponse(GetVersionResponseBody value) {
        return new JAXBElement<GetVersionResponseBody>(_GetVersionResponse_QNAME, GetVersionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSale }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfSale")
    public JAXBElement<ArrayOfSale> createArrayOfSale(ArrayOfSale value) {
        return new JAXBElement<ArrayOfSale>(_ArrayOfSale_QNAME, ArrayOfSale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQueryType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfQueryType")
    public JAXBElement<ArrayOfQueryType> createArrayOfQueryType(ArrayOfQueryType value) {
        return new JAXBElement<ArrayOfQueryType>(_ArrayOfQueryType_QNAME, ArrayOfQueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutSalesInformation }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutSalesInformation")
    public JAXBElement<OutSalesInformation> createOutSalesInformation(OutSalesInformation value) {
        return new JAXBElement<OutSalesInformation>(_OutSalesInformation_QNAME, OutSalesInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVoucherTypes }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutVoucherTypes")
    public JAXBElement<OutVoucherTypes> createOutVoucherTypes(OutVoucherTypes value) {
        return new JAXBElement<OutVoucherTypes>(_OutVoucherTypes_QNAME, OutVoucherTypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaleSubType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SaleSubType")
    public JAXBElement<SaleSubType> createSaleSubType(SaleSubType value) {
        return new JAXBElement<SaleSubType>(_SaleSubType_QNAME, SaleSubType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReturnItemOptionsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetReturnItemOptionsRequestBody")
    public JAXBElement<GetReturnItemOptionsRequestBody> createGetReturnItemOptionsRequestBody(GetReturnItemOptionsRequestBody value) {
        return new JAXBElement<GetReturnItemOptionsRequestBody>(_GetReturnItemOptionsRequestBody_QNAME, GetReturnItemOptionsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "QueryType")
    public JAXBElement<QueryType> createQueryType(QueryType value) {
        return new JAXBElement<QueryType>(_QueryType_QNAME, QueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VoucherType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherType")
    public JAXBElement<VoucherType> createVoucherType(VoucherType value) {
        return new JAXBElement<VoucherType>(_VoucherType_QNAME, VoucherType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherDetailsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherDetails")
    public JAXBElement<GetVoucherDetailsRequestBody> createGetVoucherDetails(GetVoucherDetailsRequestBody value) {
        return new JAXBElement<GetVoucherDetailsRequestBody>(_GetVoucherDetails_QNAME, GetVoucherDetailsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetVoucherItemsCanReturnPropertyRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ResetVoucherItemsCanReturnPropertyRequestBody")
    public JAXBElement<ResetVoucherItemsCanReturnPropertyRequestBody> createResetVoucherItemsCanReturnPropertyRequestBody(ResetVoucherItemsCanReturnPropertyRequestBody value) {
        return new JAXBElement<ResetVoucherItemsCanReturnPropertyRequestBody>(_ResetVoucherItemsCanReturnPropertyRequestBody_QNAME, ResetVoucherItemsCanReturnPropertyRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Price }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Price")
    public JAXBElement<Price> createPrice(Price value) {
        return new JAXBElement<Price>(_Price_QNAME, Price.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ConceptCustomer")
    public JAXBElement<ConceptCustomer> createConceptCustomer(ConceptCustomer value) {
        return new JAXBElement<ConceptCustomer>(_ConceptCustomer_QNAME, ConceptCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link State }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "State")
    public JAXBElement<State> createState(State value) {
        return new JAXBElement<State>(_State_QNAME, State.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfElectronicAddress }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfElectronicAddress")
    public JAXBElement<ArrayOfElectronicAddress> createArrayOfElectronicAddress(ArrayOfElectronicAddress value) {
        return new JAXBElement<ArrayOfElectronicAddress>(_ArrayOfElectronicAddress_QNAME, ArrayOfElectronicAddress.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfCustomer")
    public JAXBElement<ArrayOfCustomer> createArrayOfCustomer(ArrayOfCustomer value) {
        return new JAXBElement<ArrayOfCustomer>(_ArrayOfCustomer_QNAME, ArrayOfCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnItemsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnItems")
    public JAXBElement<ReturnItemsRequestBody> createReturnItems(ReturnItemsRequestBody value) {
        return new JAXBElement<ReturnItemsRequestBody>(_ReturnItems_QNAME, ReturnItemsRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectedVouchersResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetConnectedVouchersResponseBody")
    public JAXBElement<GetConnectedVouchersResponseBody> createGetConnectedVouchersResponseBody(GetConnectedVouchersResponseBody value) {
        return new JAXBElement<GetConnectedVouchersResponseBody>(_GetConnectedVouchersResponseBody_QNAME, GetConnectedVouchersResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEnabledFunctionsRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetEnabledFunctions")
    public JAXBElement<GetEnabledFunctionsRequestBody> createGetEnabledFunctions(GetEnabledFunctionsRequestBody value) {
        return new JAXBElement<GetEnabledFunctionsRequestBody>(_GetEnabledFunctions_QNAME, GetEnabledFunctionsRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomerItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfCustomerItem")
    public JAXBElement<ArrayOfCustomerItem> createArrayOfCustomerItem(ArrayOfCustomerItem value) {
        return new JAXBElement<ArrayOfCustomerItem>(_ArrayOfCustomerItem_QNAME, ArrayOfCustomerItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherTypesRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherTypesRequestBody")
    public JAXBElement<GetVoucherTypesRequestBody> createGetVoucherTypesRequestBody(GetVoucherTypesRequestBody value) {
        return new JAXBElement<GetVoucherTypesRequestBody>(_GetVoucherTypesRequestBody_QNAME, GetVoucherTypesRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesInformationResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesInformationResponse")
    public JAXBElement<GetSalesInformationResponseBody> createGetSalesInformationResponse(GetSalesInformationResponseBody value) {
        return new JAXBElement<GetSalesInformationResponseBody>(_GetSalesInformationResponse_QNAME, GetSalesInformationResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnItemsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnItemsResponse")
    public JAXBElement<ReturnItemsResponseBody> createReturnItemsResponse(ReturnItemsResponseBody value) {
        return new JAXBElement<ReturnItemsResponseBody>(_ReturnItemsResponse_QNAME, ReturnItemsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucher }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfVoucher")
    public JAXBElement<ArrayOfVoucher> createArrayOfVoucher(ArrayOfVoucher value) {
        return new JAXBElement<ArrayOfVoucher>(_ArrayOfVoucher_QNAME, ArrayOfVoucher.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesResponse")
    public JAXBElement<GetSalesResponseBody> createGetSalesResponse(GetSalesResponseBody value) {
        return new JAXBElement<GetSalesResponseBody>(_GetSalesResponse_QNAME, GetSalesResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutInvoices }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutInvoices")
    public JAXBElement<OutInvoices> createOutInvoices(OutInvoices value) {
        return new JAXBElement<OutInvoices>(_OutInvoices_QNAME, OutInvoices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutCustomers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OutCustomers")
    public JAXBElement<OutCustomers> createOutCustomers(OutCustomers value) {
        return new JAXBElement<OutCustomers>(_OutCustomers_QNAME, OutCustomers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVersionResponseBody")
    public JAXBElement<GetVersionResponseBody> createGetVersionResponseBody(GetVersionResponseBody value) {
        return new JAXBElement<GetVersionResponseBody>(_GetVersionResponseBody_QNAME, GetVersionResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetVoucherItemsCanReturnPropertyResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ResetVoucherItemsCanReturnPropertyResponse")
    public JAXBElement<ResetVoucherItemsCanReturnPropertyResponseBody> createResetVoucherItemsCanReturnPropertyResponse(ResetVoucherItemsCanReturnPropertyResponseBody value) {
        return new JAXBElement<ResetVoucherItemsCanReturnPropertyResponseBody>(_ResetVoucherItemsCanReturnPropertyResponse_QNAME, ResetVoucherItemsCanReturnPropertyResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVoucherDetailsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherDetailsResponseBody")
    public JAXBElement<GetVoucherDetailsResponseBody> createGetVoucherDetailsResponseBody(GetVoucherDetailsResponseBody value) {
        return new JAXBElement<GetVoucherDetailsResponseBody>(_GetVoucherDetailsResponseBody_QNAME, GetVoucherDetailsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSessionRequestBody")
    public JAXBElement<GetSessionRequestBody> createGetSessionRequestBody(GetSessionRequestBody value) {
        return new JAXBElement<GetSessionRequestBody>(_GetSessionRequestBody_QNAME, GetSessionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillingType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "BillingType")
    public JAXBElement<BillingType> createBillingType(BillingType value) {
        return new JAXBElement<BillingType>(_BillingType_QNAME, BillingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesCustomerResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesCustomerResponseBody")
    public JAXBElement<GetQueryTypesCustomerResponseBody> createGetQueryTypesCustomerResponseBody(GetQueryTypesCustomerResponseBody value) {
        return new JAXBElement<GetQueryTypesCustomerResponseBody>(_GetQueryTypesCustomerResponseBody_QNAME, GetQueryTypesCustomerResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucherType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfVoucherType")
    public JAXBElement<ArrayOfVoucherType> createArrayOfVoucherType(ArrayOfVoucherType value) {
        return new JAXBElement<ArrayOfVoucherType>(_ArrayOfVoucherType_QNAME, ArrayOfVoucherType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSalesInformationRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesInformation")
    public JAXBElement<GetSalesInformationRequestBody> createGetSalesInformation(GetSalesInformationRequestBody value) {
        return new JAXBElement<GetSalesInformationRequestBody>(_GetSalesInformation_QNAME, GetSalesInformationRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesCustomerResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesCustomerResponse")
    public JAXBElement<GetQueryTypesCustomerResponseBody> createGetQueryTypesCustomerResponse(GetQueryTypesCustomerResponseBody value) {
        return new JAXBElement<GetQueryTypesCustomerResponseBody>(_GetQueryTypesCustomerResponse_QNAME, GetQueryTypesCustomerResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInvoicesResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetInvoicesResponse")
    public JAXBElement<GetInvoicesResponseBody> createGetInvoicesResponse(GetInvoicesResponseBody value) {
        return new JAXBElement<GetInvoicesResponseBody>(_GetInvoicesResponse_QNAME, GetInvoicesResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryTypesVoucherRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesVoucherRequestBody")
    public JAXBElement<GetQueryTypesVoucherRequestBody> createGetQueryTypesVoucherRequestBody(GetQueryTypesVoucherRequestBody value) {
        return new JAXBElement<GetQueryTypesVoucherRequestBody>(_GetQueryTypesVoucherRequestBody_QNAME, GetQueryTypesVoucherRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEnabledFunctionsResponseBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetEnabledFunctionsResponse")
    public JAXBElement<GetEnabledFunctionsResponseBody> createGetEnabledFunctionsResponse(GetEnabledFunctionsResponseBody value) {
        return new JAXBElement<GetEnabledFunctionsResponseBody>(_GetEnabledFunctionsResponse_QNAME, GetEnabledFunctionsResponseBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpCredential }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfErpCredential")
    public JAXBElement<ArrayOfErpCredential> createArrayOfErpCredential(ArrayOfErpCredential value) {
        return new JAXBElement<ArrayOfErpCredential>(_ArrayOfErpCredential_QNAME, ArrayOfErpCredential.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Contact")
    public JAXBElement<Contact> createContact(Contact value) {
        return new JAXBElement<Contact>(_Contact_QNAME, Contact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchTypeList }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DispatchTypeList")
    public JAXBElement<DispatchTypeList> createDispatchTypeList(DispatchTypeList value) {
        return new JAXBElement<DispatchTypeList>(_DispatchTypeList_QNAME, DispatchTypeList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Voucher }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Voucher")
    public JAXBElement<Voucher> createVoucher(Voucher value) {
        return new JAXBElement<Voucher>(_Voucher_QNAME, Voucher.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectedVouchersRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetConnectedVouchers")
    public JAXBElement<GetConnectedVouchersRequestBody> createGetConnectedVouchers(GetConnectedVouchersRequestBody value) {
        return new JAXBElement<GetConnectedVouchersRequestBody>(_GetConnectedVouchers_QNAME, GetConnectedVouchersRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfContact")
    public JAXBElement<ArrayOfContact> createArrayOfContact(ArrayOfContact value) {
        return new JAXBElement<ArrayOfContact>(_ArrayOfContact_QNAME, ArrayOfContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomersRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomers")
    public JAXBElement<GetCustomersRequestBody> createGetCustomers(GetCustomersRequestBody value) {
        return new JAXBElement<GetCustomersRequestBody>(_GetCustomers_QNAME, GetCustomersRequestBody.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "GetVersionRequestBody")
    public JAXBElement<GetVersionRequestBody> createGetVersionRequestBody(GetVersionRequestBody value) {
        return new JAXBElement<GetVersionRequestBody>(_GetVersionRequestBody_QNAME, GetVersionRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVouchersRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVouchersRequestBody")
    public JAXBElement<GetVouchersRequestBody> createGetVouchersRequestBody(GetVouchersRequestBody value) {
        return new JAXBElement<GetVouchersRequestBody>(_GetVouchersRequestBody_QNAME, GetVouchersRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucherItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfVoucherItem")
    public JAXBElement<ArrayOfVoucherItem> createArrayOfVoucherItem(ArrayOfVoucherItem value) {
        return new JAXBElement<ArrayOfVoucherItem>(_ArrayOfVoucherItem_QNAME, ArrayOfVoucherItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filter }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "filter")
    public JAXBElement<Filter> createFilter(Filter value) {
        return new JAXBElement<Filter>(_Filter_QNAME, Filter.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfString")
    public JAXBElement<ArrayOfString> createArrayOfString(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_ArrayOfString_QNAME, ArrayOfString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNote }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArrayOfNote")
    public JAXBElement<ArrayOfNote> createArrayOfNote(ArrayOfNote value) {
        return new JAXBElement<ArrayOfNote>(_ArrayOfNote_QNAME, ArrayOfNote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVouchersRequestBody }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVouchers")
    public JAXBElement<GetVouchersRequestBody> createGetVouchers(GetVouchersRequestBody value) {
        return new JAXBElement<GetVouchersRequestBody>(_GetVouchers_QNAME, GetVouchersRequestBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = State.class)
    public JAXBElement<String> createStateDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, State.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesVoucherResult", scope = GetQueryTypesVoucherResponseBody.class)
    public JAXBElement<OutQueryTypes> createGetQueryTypesVoucherResponseBodyGetQueryTypesVoucherResult(OutQueryTypes value) {
        return new JAXBElement<OutQueryTypes>(_GetQueryTypesVoucherResponseBodyGetQueryTypesVoucherResult_QNAME, OutQueryTypes.class, GetQueryTypesVoucherResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Default", scope = DispatchTypeList.class)
    public JAXBElement<String> createDispatchTypeListDefault(String value) {
        return new JAXBElement<String>(_DispatchTypeListDefault_QNAME, String.class, DispatchTypeList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDispatchType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DispatchTypes", scope = DispatchTypeList.class)
    public JAXBElement<ArrayOfDispatchType> createDispatchTypeListDispatchTypes(ArrayOfDispatchType value) {
        return new JAXBElement<ArrayOfDispatchType>(_DispatchTypeListDispatchTypes_QNAME, ArrayOfDispatchType.class, DispatchTypeList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetEnabledFunctionsRequestBody.class)
    public JAXBElement<String> createGetEnabledFunctionsRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetEnabledFunctionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetEnabledFunctionsRequestBody.class)
    public JAXBElement<String> createGetEnabledFunctionsRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetEnabledFunctionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetEnabledFunctionsRequestBody.class)
    public JAXBElement<String> createGetEnabledFunctionsRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetEnabledFunctionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetSalesRequestBody.class)
    public JAXBElement<String> createGetSalesRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetSalesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "dateTo", scope = GetSalesRequestBody.class)
    public JAXBElement<String> createGetSalesRequestBodyDateTo(String value) {
        return new JAXBElement<String>(_GetSalesRequestBodyDateTo_QNAME, String.class, GetSalesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetSalesRequestBody.class)
    public JAXBElement<String> createGetSalesRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetSalesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetSalesRequestBody.class)
    public JAXBElement<String> createGetSalesRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetSalesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "dateFrom", scope = GetSalesRequestBody.class)
    public JAXBElement<String> createGetSalesRequestBodyDateFrom(String value) {
        return new JAXBElement<String>(_GetSalesRequestBodyDateFrom_QNAME, String.class, GetSalesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link State }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnState", scope = VoucherItem.class)
    public JAXBElement<State> createVoucherItemReturnState(State value) {
        return new JAXBElement<State>(_VoucherItemReturnState_QNAME, State.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "OrderNo", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemOrderNo(String value) {
        return new JAXBElement<String>(_VoucherItemOrderNo_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Information", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemInformation(String value) {
        return new JAXBElement<String>(_VoucherItemInformation_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ArticleDescription", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemArticleDescription(String value) {
        return new JAXBElement<String>(_VoucherItemArticleDescription_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "WholesalerArticleNumber", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemWholesalerArticleNumber(String value) {
        return new JAXBElement<String>(_VoucherItemWholesalerArticleNumber_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GenericArticleId", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemGenericArticleId(String value) {
        return new JAXBElement<String>(_VoucherItemGenericArticleId_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VehicleInfo", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemVehicleInfo(String value) {
        return new JAXBElement<String>(_VoucherItemVehicleInfo_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ShippingMode", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemShippingMode(String value) {
        return new JAXBElement<String>(_VoucherItemShippingMode_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnReason }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnReason", scope = VoucherItem.class)
    public JAXBElement<ReturnReason> createVoucherItemReturnReason(ReturnReason value) {
        return new JAXBElement<ReturnReason>(_ReturnReason_QNAME, ReturnReason.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherId", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemVoucherId(String value) {
        return new JAXBElement<String>(_VoucherItemVoucherId_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SupplierArticleNumber", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemSupplierArticleNumber(String value) {
        return new JAXBElement<String>(_VoucherItemSupplierArticleNumber_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "PlateId", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemPlateId(String value) {
        return new JAXBElement<String>(_VoucherItemPlateId_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "QuantityUnit", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemQuantityUnit(String value) {
        return new JAXBElement<String>(_VoucherItemQuantityUnit_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnInfo", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemReturnInfo(String value) {
        return new JAXBElement<String>(_VoucherItemReturnInfo_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SupplierId", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemSupplierId(String value) {
        return new JAXBElement<String>(_VoucherItemSupplierId_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPrice }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Prices", scope = VoucherItem.class)
    public JAXBElement<ArrayOfPrice> createVoucherItemPrices(ArrayOfPrice value) {
        return new JAXBElement<ArrayOfPrice>(_VoucherItemPrices_QNAME, ArrayOfPrice.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GenericArticle", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemGenericArticle(String value) {
        return new JAXBElement<String>(_VoucherItemGenericArticle_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SupplierName", scope = VoucherItem.class)
    public JAXBElement<String> createVoucherItemSupplierName(String value) {
        return new JAXBElement<String>(_VoucherItemSupplierName_QNAME, String.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTax }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Taxes", scope = VoucherItem.class)
    public JAXBElement<ArrayOfTax> createVoucherItemTaxes(ArrayOfTax value) {
        return new JAXBElement<ArrayOfTax>(_VoucherItemTaxes_QNAME, ArrayOfTax.class, VoucherItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVoucherDetails }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherDetailsResult", scope = GetVoucherDetailsResponseBody.class)
    public JAXBElement<OutVoucherDetails> createGetVoucherDetailsResponseBodyGetVoucherDetailsResult(OutVoucherDetails value) {
        return new JAXBElement<OutVoucherDetails>(_GetVoucherDetailsResponseBodyGetVoucherDetailsResult_QNAME, OutVoucherDetails.class, GetVoucherDetailsResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Url", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialUrl(String value) {
        return new JAXBElement<String>(_ErpCredentialUrl_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpCustomerId", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialErpCustomerId(String value) {
        return new JAXBElement<String>(_ErpCredentialErpCustomerId_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpPassword", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialErpPassword(String value) {
        return new JAXBElement<String>(_ErpCredentialErpPassword_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpCustomerId3", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialErpCustomerId3(String value) {
        return new JAXBElement<String>(_ErpCredentialErpCustomerId3_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpUsername", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialErpUsername(String value) {
        return new JAXBElement<String>(_ErpCredentialErpUsername_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpCustomerId2", scope = ErpCredential.class)
    public JAXBElement<String> createErpCredentialErpCustomerId2(String value) {
        return new JAXBElement<String>(_ErpCredentialErpCustomerId2_QNAME, String.class, ErpCredential.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetInvoicesRequestBody.class)
    public JAXBElement<String> createGetInvoicesRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetInvoicesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetInvoicesRequestBody.class)
    public JAXBElement<String> createGetInvoicesRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetInvoicesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetInvoicesRequestBody.class)
    public JAXBElement<String> createGetInvoicesRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetInvoicesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filter }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "filter", scope = GetInvoicesRequestBody.class)
    public JAXBElement<Filter> createGetInvoicesRequestBodyFilter(Filter value) {
        return new JAXBElement<Filter>(_GetInvoicesRequestBodyFilter_QNAME, Filter.class, GetInvoicesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetVouchersRequestBody.class)
    public JAXBElement<String> createGetVouchersRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetVouchersRequestBody.class)
    public JAXBElement<String> createGetVouchersRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetVouchersRequestBody.class)
    public JAXBElement<String> createGetVouchersRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filter }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "filter", scope = GetVouchersRequestBody.class)
    public JAXBElement<Filter> createGetVouchersRequestBodyFilter(Filter value) {
        return new JAXBElement<Filter>(_GetInvoicesRequestBodyFilter_QNAME, Filter.class, GetVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetCustomersRequestBody.class)
    public JAXBElement<String> createGetCustomersRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetCustomersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetCustomersRequestBody.class)
    public JAXBElement<String> createGetCustomersRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetCustomersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "query", scope = GetCustomersRequestBody.class)
    public JAXBElement<String> createGetCustomersRequestBodyQuery(String value) {
        return new JAXBElement<String>(_GetCustomersRequestBodyQuery_QNAME, String.class, GetCustomersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutReturnItemOptions }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetReturnItemOptionsResult", scope = GetReturnItemOptionsResponseBody.class)
    public JAXBElement<OutReturnItemOptions> createGetReturnItemOptionsResponseBodyGetReturnItemOptionsResult(OutReturnItemOptions value) {
        return new JAXBElement<OutReturnItemOptions>(_GetReturnItemOptionsResponseBodyGetReturnItemOptionsResult_QNAME, OutReturnItemOptions.class, GetReturnItemOptionsResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchTypeList }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DispatchTypeList", scope = CustomerDetails.class)
    public JAXBElement<DispatchTypeList> createCustomerDetailsDispatchTypeList(DispatchTypeList value) {
        return new JAXBElement<DispatchTypeList>(_DispatchTypeList_QNAME, DispatchTypeList.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNote }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Notes", scope = CustomerDetails.class)
    public JAXBElement<ArrayOfNote> createCustomerDetailsNotes(ArrayOfNote value) {
        return new JAXBElement<ArrayOfNote>(_CustomerDetailsNotes_QNAME, ArrayOfNote.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Account", scope = CustomerDetails.class)
    public JAXBElement<Account> createCustomerDetailsAccount(Account value) {
        return new JAXBElement<Account>(_Account_QNAME, Account.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomerItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CustomerItems", scope = CustomerDetails.class)
    public JAXBElement<ArrayOfCustomerItem> createCustomerDetailsCustomerItems(ArrayOfCustomerItem value) {
        return new JAXBElement<ArrayOfCustomerItem>(_CustomerDetailsCustomerItems_QNAME, ArrayOfCustomerItem.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "TourInfo", scope = CustomerDetails.class)
    public JAXBElement<String> createCustomerDetailsTourInfo(String value) {
        return new JAXBElement<String>(_CustomerDetailsTourInfo_QNAME, String.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErpCredential }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErpCredentials", scope = CustomerDetails.class)
    public JAXBElement<ArrayOfErpCredential> createCustomerDetailsErpCredentials(ArrayOfErpCredential value) {
        return new JAXBElement<ArrayOfErpCredential>(_CustomerDetailsErpCredentials_QNAME, ArrayOfErpCredential.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Contacts", scope = CustomerDetails.class)
    public JAXBElement<ArrayOfContact> createCustomerDetailsContacts(ArrayOfContact value) {
        return new JAXBElement<ArrayOfContact>(_CustomerDetailsContacts_QNAME, ArrayOfContact.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfConceptCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ConceptCustomer", scope = CustomerDetails.class)
    public JAXBElement<ArrayOfConceptCustomer> createCustomerDetailsConceptCustomer(ArrayOfConceptCustomer value) {
        return new JAXBElement<ArrayOfConceptCustomer>(_ConceptCustomer_QNAME, ArrayOfConceptCustomer.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CustomerGroup", scope = CustomerDetails.class)
    public JAXBElement<String> createCustomerDetailsCustomerGroup(String value) {
        return new JAXBElement<String>(_CustomerDetailsCustomerGroup_QNAME, String.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CooperationGroup", scope = CustomerDetails.class)
    public JAXBElement<String> createCustomerDetailsCooperationGroup(String value) {
        return new JAXBElement<String>(_CustomerDetailsCooperationGroup_QNAME, String.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SalesOutletList }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SalesOutletList", scope = CustomerDetails.class)
    public JAXBElement<SalesOutletList> createCustomerDetailsSalesOutletList(SalesOutletList value) {
        return new JAXBElement<SalesOutletList>(_SalesOutletList_QNAME, SalesOutletList.class, CustomerDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Default", scope = SalesOutletList.class)
    public JAXBElement<String> createSalesOutletListDefault(String value) {
        return new JAXBElement<String>(_DispatchTypeListDefault_QNAME, String.class, SalesOutletList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSalesOutlet }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SalesOutlets", scope = SalesOutletList.class)
    public JAXBElement<ArrayOfSalesOutlet> createSalesOutletListSalesOutlets(ArrayOfSalesOutlet value) {
        return new JAXBElement<ArrayOfSalesOutlet>(_SalesOutletListSalesOutlets_QNAME, ArrayOfSalesOutlet.class, SalesOutletList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutSession }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSessionResult", scope = GetSessionResponseBody.class)
    public JAXBElement<OutSession> createGetSessionResponseBodyGetSessionResult(OutSession value) {
        return new JAXBElement<OutSession>(_GetSessionResponseBodyGetSessionResult_QNAME, OutSession.class, GetSessionResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = BillingType.class)
    public JAXBElement<String> createBillingTypeDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, BillingType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetCustomerRequestBody.class)
    public JAXBElement<String> createGetCustomerRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetCustomerRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetCustomerRequestBody.class)
    public JAXBElement<String> createGetCustomerRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetCustomerRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetCustomerRequestBody.class)
    public JAXBElement<String> createGetCustomerRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetCustomerRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfReturnReason }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnReasons", scope = OutReturnItemOptions.class)
    public JAXBElement<ArrayOfReturnReason> createOutReturnItemOptionsReturnReasons(ArrayOfReturnReason value) {
        return new JAXBElement<ArrayOfReturnReason>(_OutReturnItemOptionsReturnReasons_QNAME, ArrayOfReturnReason.class, OutReturnItemOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnStates", scope = OutReturnItemOptions.class)
    public JAXBElement<ArrayOfState> createOutReturnItemOptionsReturnStates(ArrayOfState value) {
        return new JAXBElement<ArrayOfState>(_OutReturnItemOptionsReturnStates_QNAME, ArrayOfState.class, OutReturnItemOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetVoucherDetailsRequestBody.class)
    public JAXBElement<String> createGetVoucherDetailsRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetVoucherDetailsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetVoucherDetailsRequestBody.class)
    public JAXBElement<String> createGetVoucherDetailsRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetVoucherDetailsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetVoucherDetailsRequestBody.class)
    public JAXBElement<String> createGetVoucherDetailsRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetVoucherDetailsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "voucherId", scope = GetVoucherDetailsRequestBody.class)
    public JAXBElement<String> createGetVoucherDetailsRequestBodyVoucherId(String value) {
        return new JAXBElement<String>(_GetVoucherDetailsRequestBodyVoucherId_QNAME, String.class, GetVoucherDetailsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ErrorMessage", scope = BaseResponse.class)
    public JAXBElement<String> createBaseResponseErrorMessage(String value) {
        return new JAXBElement<String>(_BaseResponseErrorMessage_QNAME, String.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Information", scope = BaseResponse.class)
    public JAXBElement<String> createBaseResponseInformation(String value) {
        return new JAXBElement<String>(_VoucherItemInformation_QNAME, String.class, BaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetSalesInformationRequestBody.class)
    public JAXBElement<String> createGetSalesInformationRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetSalesInformationRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetSalesInformationRequestBody.class)
    public JAXBElement<String> createGetSalesInformationRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetSalesInformationRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetSalesInformationRequestBody.class)
    public JAXBElement<String> createGetSalesInformationRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetSalesInformationRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutCustomers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomersResult", scope = GetCustomersResponseBody.class)
    public JAXBElement<OutCustomers> createGetCustomersResponseBodyGetCustomersResult(OutCustomers value) {
        return new JAXBElement<OutCustomers>(_GetCustomersResponseBodyGetCustomersResult_QNAME, OutCustomers.class, GetCustomersResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucherItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherItems", scope = OutVoucherDetails.class)
    public JAXBElement<ArrayOfVoucherItem> createOutVoucherDetailsVoucherItems(ArrayOfVoucherItem value) {
        return new JAXBElement<ArrayOfVoucherItem>(_OutVoucherDetailsVoucherItems_QNAME, ArrayOfVoucherItem.class, OutVoucherDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutSales }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesResult", scope = GetSalesResponseBody.class)
    public JAXBElement<OutSales> createGetSalesResponseBodyGetSalesResult(OutSales value) {
        return new JAXBElement<OutSales>(_GetSalesResponseBodyGetSalesResult_QNAME, OutSales.class, GetSalesResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetVoucherTypesRequestBody.class)
    public JAXBElement<String> createGetVoucherTypesRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetVoucherTypesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetVoucherTypesRequestBody.class)
    public JAXBElement<String> createGetVoucherTypesRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetVoucherTypesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetVoucherTypesRequestBody.class)
    public JAXBElement<String> createGetVoucherTypesRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetVoucherTypesRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSaleSubType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SaleSubTypes", scope = SaleType.class)
    public JAXBElement<ArrayOfSaleSubType> createSaleTypeSaleSubTypes(ArrayOfSaleSubType value) {
        return new JAXBElement<ArrayOfSaleSubType>(_SaleTypeSaleSubTypes_QNAME, ArrayOfSaleSubType.class, SaleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = SaleType.class)
    public JAXBElement<String> createSaleTypeDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, SaleType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = VoucherType.class)
    public JAXBElement<String> createVoucherTypeDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, VoucherType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = ResetVoucherItemsCanReturnPropertyRequestBody.class)
    public JAXBElement<String> createResetVoucherItemsCanReturnPropertyRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, ResetVoucherItemsCanReturnPropertyRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = ResetVoucherItemsCanReturnPropertyRequestBody.class)
    public JAXBElement<String> createResetVoucherItemsCanReturnPropertyRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, ResetVoucherItemsCanReturnPropertyRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "voucherId", scope = ResetVoucherItemsCanReturnPropertyRequestBody.class)
    public JAXBElement<String> createResetVoucherItemsCanReturnPropertyRequestBodyVoucherId(String value) {
        return new JAXBElement<String>(_GetVoucherDetailsRequestBodyVoucherId_QNAME, String.class, ResetVoucherItemsCanReturnPropertyRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Sale.class)
    public JAXBElement<String> createSaleId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Sale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaleSubType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SaleSubType", scope = Sale.class)
    public JAXBElement<SaleSubType> createSaleSaleSubType(SaleSubType value) {
        return new JAXBElement<SaleSubType>(_SaleSubType_QNAME, SaleSubType.class, Sale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = Sale.class)
    public JAXBElement<String> createSaleDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, Sale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetReturnItemOptionsRequestBody.class)
    public JAXBElement<String> createGetReturnItemOptionsRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetReturnItemOptionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetReturnItemOptionsRequestBody.class)
    public JAXBElement<String> createGetReturnItemOptionsRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetReturnItemOptionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetReturnItemOptionsRequestBody.class)
    public JAXBElement<String> createGetReturnItemOptionsRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetReturnItemOptionsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherId", scope = VoucherSummaryItem.class)
    public JAXBElement<String> createVoucherSummaryItemVoucherId(String value) {
        return new JAXBElement<String>(_VoucherItemVoucherId_QNAME, String.class, VoucherSummaryItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = VoucherSummaryItem.class)
    public JAXBElement<String> createVoucherSummaryItemId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, VoucherSummaryItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = VoucherSummaryItem.class)
    public JAXBElement<String> createVoucherSummaryItemCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, VoucherSummaryItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = VoucherSummaryItem.class)
    public JAXBElement<String> createVoucherSummaryItemDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, VoucherSummaryItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetCustomerResult", scope = GetCustomerResponseBody.class)
    public JAXBElement<OutCustomer> createGetCustomerResponseBodyGetCustomerResult(OutCustomer value) {
        return new JAXBElement<OutCustomer>(_GetCustomerResponseBodyGetCustomerResult_QNAME, OutCustomer.class, GetCustomerResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ImageUrl", scope = ConceptCustomer.class)
    public JAXBElement<String> createConceptCustomerImageUrl(String value) {
        return new JAXBElement<String>(_ConceptCustomerImageUrl_QNAME, String.class, ConceptCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Text", scope = ConceptCustomer.class)
    public JAXBElement<String> createConceptCustomerText(String value) {
        return new JAXBElement<String>(_ConceptCustomerText_QNAME, String.class, ConceptCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "UrlText", scope = ConceptCustomer.class)
    public JAXBElement<String> createConceptCustomerUrlText(String value) {
        return new JAXBElement<String>(_ConceptCustomerUrlText_QNAME, String.class, ConceptCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Url", scope = ConceptCustomer.class)
    public JAXBElement<String> createConceptCustomerUrl(String value) {
        return new JAXBElement<String>(_ErpCredentialUrl_QNAME, String.class, ConceptCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "IconUrl", scope = ConceptCustomer.class)
    public JAXBElement<String> createConceptCustomerIconUrl(String value) {
        return new JAXBElement<String>(_ConceptCustomerIconUrl_QNAME, String.class, ConceptCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = ConceptCustomer.class)
    public JAXBElement<String> createConceptCustomerDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, ConceptCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSale }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Sales", scope = OutSales.class)
    public JAXBElement<ArrayOfSale> createOutSalesSales(ArrayOfSale value) {
        return new JAXBElement<ArrayOfSale>(_OutSalesSales_QNAME, ArrayOfSale.class, OutSales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ReturnItemsResult", scope = ReturnItemsResponseBody.class)
    public JAXBElement<BaseResponse> createReturnItemsResponseBodyReturnItemsResult(BaseResponse value) {
        return new JAXBElement<BaseResponse>(_ReturnItemsResponseBodyReturnItemsResult_QNAME, BaseResponse.class, ReturnItemsResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfQueryType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "QueryTypes", scope = OutQueryTypes.class)
    public JAXBElement<ArrayOfQueryType> createOutQueryTypesQueryTypes(ArrayOfQueryType value) {
        return new JAXBElement<ArrayOfQueryType>(_OutQueryTypesQueryTypes_QNAME, ArrayOfQueryType.class, OutQueryTypes.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCustomer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Customers", scope = OutCustomers.class)
    public JAXBElement<ArrayOfCustomer> createOutCustomersCustomers(ArrayOfCustomer value) {
        return new JAXBElement<ArrayOfCustomer>(_OutCustomersCustomers_QNAME, ArrayOfCustomer.class, OutCustomers.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutSalesInformation }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetSalesInformationResult", scope = GetSalesInformationResponseBody.class)
    public JAXBElement<OutSalesInformation> createGetSalesInformationResponseBodyGetSalesInformationResult(OutSalesInformation value) {
        return new JAXBElement<OutSalesInformation>(_GetSalesInformationResponseBodyGetSalesInformationResult_QNAME, OutSalesInformation.class, GetSalesInformationResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ResetVoucherItemsCanReturnPropertyResult", scope = ResetVoucherItemsCanReturnPropertyResponseBody.class)
    public JAXBElement<BaseResponse> createResetVoucherItemsCanReturnPropertyResponseBodyResetVoucherItemsCanReturnPropertyResult(BaseResponse value) {
        return new JAXBElement<BaseResponse>(_ResetVoucherItemsCanReturnPropertyResponseBodyResetVoucherItemsCanReturnPropertyResult_QNAME, BaseResponse.class, ResetVoucherItemsCanReturnPropertyResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SessionId", scope = OutSession.class)
    public JAXBElement<String> createOutSessionSessionId(String value) {
        return new JAXBElement<String>(_OutSessionSessionId_QNAME, String.class, OutSession.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = ReturnItemsRequestBody.class)
    public JAXBElement<String> createReturnItemsRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, ReturnItemsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = ReturnItemsRequestBody.class)
    public JAXBElement<String> createReturnItemsRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, ReturnItemsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = ReturnItemsRequestBody.class)
    public JAXBElement<String> createReturnItemsRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, ReturnItemsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfReturnItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "returnableItems", scope = ReturnItemsRequestBody.class)
    public JAXBElement<ArrayOfReturnItem> createReturnItemsRequestBodyReturnableItems(ArrayOfReturnItem value) {
        return new JAXBElement<ArrayOfReturnItem>(_ReturnItemsRequestBodyReturnableItems_QNAME, ArrayOfReturnItem.class, ReturnItemsRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = DispatchType.class)
    public JAXBElement<String> createDispatchTypeId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, DispatchType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = DispatchType.class)
    public JAXBElement<String> createDispatchTypeCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, DispatchType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = DispatchType.class)
    public JAXBElement<String> createDispatchTypeDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, DispatchType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Value", scope = ElectronicAddress.class)
    public JAXBElement<String> createElectronicAddressValue(String value) {
        return new JAXBElement<String>(_ElectronicAddressValue_QNAME, String.class, ElectronicAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = ElectronicAddress.class)
    public JAXBElement<String> createElectronicAddressDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, ElectronicAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = OutVouchers.class)
    public JAXBElement<String> createOutVouchersCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, OutVouchers.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucher }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Vouchers", scope = OutVouchers.class)
    public JAXBElement<ArrayOfVoucher> createOutVouchersVouchers(ArrayOfVoucher value) {
        return new JAXBElement<ArrayOfVoucher>(_OutVouchersVouchers_QNAME, ArrayOfVoucher.class, OutVouchers.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetEnabledFunctionsResult", scope = GetEnabledFunctionsResponseBody.class)
    public JAXBElement<ArrayOfString> createGetEnabledFunctionsResponseBodyGetEnabledFunctionsResult(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_GetEnabledFunctionsResponseBodyGetEnabledFunctionsResult_QNAME, ArrayOfString.class, GetEnabledFunctionsResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Salespoint", scope = Voucher.class)
    public JAXBElement<String> createVoucherSalespoint(String value) {
        return new JAXBElement<String>(_VoucherSalespoint_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DeliveryAddress", scope = Voucher.class)
    public JAXBElement<Address> createVoucherDeliveryAddress(Address value) {
        return new JAXBElement<Address>(_VoucherDeliveryAddress_QNAME, Address.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Information", scope = Voucher.class)
    public JAXBElement<String> createVoucherInformation(String value) {
        return new JAXBElement<String>(_VoucherItemInformation_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "PaymentInformation", scope = Voucher.class)
    public JAXBElement<String> createVoucherPaymentInformation(String value) {
        return new JAXBElement<String>(_VoucherPaymentInformation_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Voucher.class)
    public JAXBElement<String> createVoucherId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherDescription", scope = Voucher.class)
    public JAXBElement<String> createVoucherVoucherDescription(String value) {
        return new JAXBElement<String>(_VoucherVoucherDescription_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VoucherType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherType", scope = Voucher.class)
    public JAXBElement<VoucherType> createVoucherVoucherType(VoucherType value) {
        return new JAXBElement<VoucherType>(_VoucherType_QNAME, VoucherType.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DeliveryType", scope = Voucher.class)
    public JAXBElement<String> createVoucherDeliveryType(String value) {
        return new JAXBElement<String>(_VoucherDeliveryType_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Tour", scope = Voucher.class)
    public JAXBElement<String> createVoucherTour(String value) {
        return new JAXBElement<String>(_VoucherTour_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = Voucher.class)
    public JAXBElement<String> createVoucherCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "InvoiceAddress", scope = Voucher.class)
    public JAXBElement<Address> createVoucherInvoiceAddress(Address value) {
        return new JAXBElement<Address>(_VoucherInvoiceAddress_QNAME, Address.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucherSummaryItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherSummaryItems", scope = Voucher.class)
    public JAXBElement<ArrayOfVoucherSummaryItem> createVoucherVoucherSummaryItems(ArrayOfVoucherSummaryItem value) {
        return new JAXBElement<ArrayOfVoucherSummaryItem>(_VoucherVoucherSummaryItems_QNAME, ArrayOfVoucherSummaryItem.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Contacts", scope = Voucher.class)
    public JAXBElement<ArrayOfContact> createVoucherContacts(ArrayOfContact value) {
        return new JAXBElement<ArrayOfContact>(_CustomerDetailsContacts_QNAME, ArrayOfContact.class, Voucher.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = SalesOutlet.class)
    public JAXBElement<String> createSalesOutletId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, SalesOutlet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNote }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Notes", scope = SalesOutlet.class)
    public JAXBElement<ArrayOfNote> createSalesOutletNotes(ArrayOfNote value) {
        return new JAXBElement<ArrayOfNote>(_CustomerDetailsNotes_QNAME, ArrayOfNote.class, SalesOutlet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = SalesOutlet.class)
    public JAXBElement<String> createSalesOutletDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, SalesOutlet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Contacts", scope = SalesOutlet.class)
    public JAXBElement<ArrayOfContact> createSalesOutletContacts(ArrayOfContact value) {
        return new JAXBElement<ArrayOfContact>(_CustomerDetailsContacts_QNAME, ArrayOfContact.class, SalesOutlet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Address", scope = SalesOutlet.class)
    public JAXBElement<Address> createSalesOutletAddress(Address value) {
        return new JAXBElement<Address>(_Address_QNAME, Address.class, SalesOutlet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Street", scope = Address.class)
    public JAXBElement<String> createAddressStreet(String value) {
        return new JAXBElement<String>(_AddressStreet_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Country", scope = Address.class)
    public JAXBElement<String> createAddressCountry(String value) {
        return new JAXBElement<String>(_AddressCountry_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Address.class)
    public JAXBElement<String> createAddressId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "City", scope = Address.class)
    public JAXBElement<String> createAddressCity(String value) {
        return new JAXBElement<String>(_AddressCity_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "District", scope = Address.class)
    public JAXBElement<String> createAddressDistrict(String value) {
        return new JAXBElement<String>(_AddressDistrict_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "AlternativeRecipient", scope = Address.class)
    public JAXBElement<String> createAddressAlternativeRecipient(String value) {
        return new JAXBElement<String>(_AddressAlternativeRecipient_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ZIP", scope = Address.class)
    public JAXBElement<String> createAddressZIP(String value) {
        return new JAXBElement<String>(_AddressZIP_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = Address.class)
    public JAXBElement<String> createAddressDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "StreetExt", scope = Address.class)
    public JAXBElement<String> createAddressStreetExt(String value) {
        return new JAXBElement<String>(_AddressStreetExt_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "PostOfficeBox", scope = Address.class)
    public JAXBElement<String> createAddressPostOfficeBox(String value) {
        return new JAXBElement<String>(_AddressPostOfficeBox_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Phone", scope = Address.class)
    public JAXBElement<String> createAddressPhone(String value) {
        return new JAXBElement<String>(_AddressPhone_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVoucherTypes }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVoucherTypesResult", scope = GetVoucherTypesResponseBody.class)
    public JAXBElement<OutVoucherTypes> createGetVoucherTypesResponseBodyGetVoucherTypesResult(OutVoucherTypes value) {
        return new JAXBElement<OutVoucherTypes>(_GetVoucherTypesResponseBodyGetVoucherTypesResult_QNAME, OutVoucherTypes.class, GetVoucherTypesResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetConnectedVouchersResult", scope = GetConnectedVouchersResponseBody.class)
    public JAXBElement<OutVouchers> createGetConnectedVouchersResponseBodyGetConnectedVouchersResult(OutVouchers value) {
        return new JAXBElement<OutVouchers>(_GetConnectedVouchersResponseBodyGetConnectedVouchersResult_QNAME, OutVouchers.class, GetConnectedVouchersResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ImageUrl", scope = Note.class)
    public JAXBElement<String> createNoteImageUrl(String value) {
        return new JAXBElement<String>(_ConceptCustomerImageUrl_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Text", scope = Note.class)
    public JAXBElement<String> createNoteText(String value) {
        return new JAXBElement<String>(_ConceptCustomerText_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "UrlText", scope = Note.class)
    public JAXBElement<String> createNoteUrlText(String value) {
        return new JAXBElement<String>(_ConceptCustomerUrlText_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Url", scope = Note.class)
    public JAXBElement<String> createNoteUrl(String value) {
        return new JAXBElement<String>(_ErpCredentialUrl_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "IconUrl", scope = Note.class)
    public JAXBElement<String> createNoteIconUrl(String value) {
        return new JAXBElement<String>(_ConceptCustomerIconUrl_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = Note.class)
    public JAXBElement<String> createNoteDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = Price.class)
    public JAXBElement<String> createPriceCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, Price.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = Price.class)
    public JAXBElement<String> createPriceDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, Price.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutQueryTypes }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetQueryTypesCustomerResult", scope = GetQueryTypesCustomerResponseBody.class)
    public JAXBElement<OutQueryTypes> createGetQueryTypesCustomerResponseBodyGetQueryTypesCustomerResult(OutQueryTypes value) {
        return new JAXBElement<OutQueryTypes>(_GetQueryTypesCustomerResponseBodyGetQueryTypesCustomerResult_QNAME, OutQueryTypes.class, GetQueryTypesCustomerResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = SaleSubType.class)
    public JAXBElement<String> createSaleSubTypeId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, SaleSubType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = SaleSubType.class)
    public JAXBElement<String> createSaleSubTypeDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, SaleSubType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "PaymentType", scope = Invoice.class)
    public JAXBElement<String> createInvoicePaymentType(String value) {
        return new JAXBElement<String>(_InvoicePaymentType_QNAME, String.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DeliveryAddress", scope = Invoice.class)
    public JAXBElement<Address> createInvoiceDeliveryAddress(Address value) {
        return new JAXBElement<Address>(_VoucherDeliveryAddress_QNAME, Address.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Invoice.class)
    public JAXBElement<String> createInvoiceId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Information", scope = Invoice.class)
    public JAXBElement<String> createInvoiceInformation(String value) {
        return new JAXBElement<String>(_VoucherItemInformation_QNAME, String.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DeliveryType", scope = Invoice.class)
    public JAXBElement<String> createInvoiceDeliveryType(String value) {
        return new JAXBElement<String>(_VoucherDeliveryType_QNAME, String.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInstallment }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Installments", scope = Invoice.class)
    public JAXBElement<ArrayOfInstallment> createInvoiceInstallments(ArrayOfInstallment value) {
        return new JAXBElement<ArrayOfInstallment>(_InvoiceInstallments_QNAME, ArrayOfInstallment.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = Invoice.class)
    public JAXBElement<String> createInvoiceCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "InvoiceAddress", scope = Invoice.class)
    public JAXBElement<Address> createInvoiceInvoiceAddress(Address value) {
        return new JAXBElement<Address>(_VoucherInvoiceAddress_QNAME, Address.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfContact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Contacts", scope = Invoice.class)
    public JAXBElement<ArrayOfContact> createInvoiceContacts(ArrayOfContact value) {
        return new JAXBElement<ArrayOfContact>(_CustomerDetailsContacts_QNAME, ArrayOfContact.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link State }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "State", scope = Invoice.class)
    public JAXBElement<State> createInvoiceState(State value) {
        return new JAXBElement<State>(_State_QNAME, State.class, Invoice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerDetails }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Customer", scope = OutCustomer.class)
    public JAXBElement<CustomerDetails> createOutCustomerCustomer(CustomerDetails value) {
        return new JAXBElement<CustomerDetails>(_Customer_QNAME, CustomerDetails.class, OutCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInvoice }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Invoices", scope = OutInvoices.class)
    public JAXBElement<ArrayOfInvoice> createOutInvoicesInvoices(ArrayOfInvoice value) {
        return new JAXBElement<ArrayOfInvoice>(_OutInvoicesInvoices_QNAME, ArrayOfInvoice.class, OutInvoices.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "States", scope = OutInvoices.class)
    public JAXBElement<ArrayOfState> createOutInvoicesStates(ArrayOfState value) {
        return new JAXBElement<ArrayOfState>(_OutInvoicesStates_QNAME, ArrayOfState.class, OutInvoices.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = OutInvoices.class)
    public JAXBElement<String> createOutInvoicesCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, OutInvoices.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = Tax.class)
    public JAXBElement<String> createTaxDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, Tax.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = QueryType.class)
    public JAXBElement<String> createQueryTypeDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, QueryType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Contact.class)
    public JAXBElement<String> createContactId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "LastName", scope = Contact.class)
    public JAXBElement<String> createContactLastName(String value) {
        return new JAXBElement<String>(_ContactLastName_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ContactDescription", scope = Contact.class)
    public JAXBElement<String> createContactContactDescription(String value) {
        return new JAXBElement<String>(_ContactContactDescription_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "JobDescription", scope = Contact.class)
    public JAXBElement<String> createContactJobDescription(String value) {
        return new JAXBElement<String>(_ContactJobDescription_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "FirstName", scope = Contact.class)
    public JAXBElement<String> createContactFirstName(String value) {
        return new JAXBElement<String>(_ContactFirstName_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfElectronicAddress }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ElectronicAddresses", scope = Contact.class)
    public JAXBElement<ArrayOfElectronicAddress> createContactElectronicAddresses(ArrayOfElectronicAddress value) {
        return new JAXBElement<ArrayOfElectronicAddress>(_ContactElectronicAddresses_QNAME, ArrayOfElectronicAddress.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetSessionRequestBody.class)
    public JAXBElement<String> createGetSessionRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetSessionRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetSessionRequestBody.class)
    public JAXBElement<String> createGetSessionRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetSessionRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "username", scope = GetSessionRequestBody.class)
    public JAXBElement<String> createGetSessionRequestBodyUsername(String value) {
        return new JAXBElement<String>(_GetSessionRequestBodyUsername_QNAME, String.class, GetSessionRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "password", scope = GetSessionRequestBody.class)
    public JAXBElement<String> createGetSessionRequestBodyPassword(String value) {
        return new JAXBElement<String>(_GetSessionRequestBodyPassword_QNAME, String.class, GetSessionRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Account.class)
    public JAXBElement<String> createAccountId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Information", scope = Account.class)
    public JAXBElement<String> createAccountInformation(String value) {
        return new JAXBElement<String>(_VoucherItemInformation_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillingType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "BillingType", scope = Account.class)
    public JAXBElement<BillingType> createAccountBillingType(BillingType value) {
        return new JAXBElement<BillingType>(_BillingType_QNAME, BillingType.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = Account.class)
    public JAXBElement<String> createAccountCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "UsedCreditLimit", scope = Account.class)
    public JAXBElement<String> createAccountUsedCreditLimit(String value) {
        return new JAXBElement<String>(_AccountUsedCreditLimit_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMonetaryValue }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "MonetaryValueList", scope = Account.class)
    public JAXBElement<ArrayOfMonetaryValue> createAccountMonetaryValueList(ArrayOfMonetaryValue value) {
        return new JAXBElement<ArrayOfMonetaryValue>(_AccountMonetaryValueList_QNAME, ArrayOfMonetaryValue.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CreditLimit", scope = Account.class)
    public JAXBElement<String> createAccountCreditLimit(String value) {
        return new JAXBElement<String>(_AccountCreditLimit_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "BalanceDue", scope = Account.class)
    public JAXBElement<String> createAccountBalanceDue(String value) {
        return new JAXBElement<String>(_AccountBalanceDue_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "QueryValue", scope = Filter.class)
    public JAXBElement<String> createFilterQueryValue(String value) {
        return new JAXBElement<String>(_FilterQueryValue_QNAME, String.class, Filter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DateTo", scope = Filter.class)
    public JAXBElement<String> createFilterDateTo(String value) {
        return new JAXBElement<String>(_FilterDateTo_QNAME, String.class, Filter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Query", scope = Filter.class)
    public JAXBElement<QueryType> createFilterQuery(QueryType value) {
        return new JAXBElement<QueryType>(_FilterQuery_QNAME, QueryType.class, Filter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DateFrom", scope = Filter.class)
    public JAXBElement<String> createFilterDateFrom(String value) {
        return new JAXBElement<String>(_FilterDateFrom_QNAME, String.class, Filter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetQueryTypesCustomerRequestBody.class)
    public JAXBElement<String> createGetQueryTypesCustomerRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetQueryTypesCustomerRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetQueryTypesCustomerRequestBody.class)
    public JAXBElement<String> createGetQueryTypesCustomerRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetQueryTypesCustomerRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetQueryTypesCustomerRequestBody.class)
    public JAXBElement<String> createGetQueryTypesCustomerRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetQueryTypesCustomerRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherItemId", scope = ReturnItem.class)
    public JAXBElement<String> createReturnItemVoucherItemId(String value) {
        return new JAXBElement<String>(_ReturnItemVoucherItemId_QNAME, String.class, ReturnItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = Installment.class)
    public JAXBElement<String> createInstallmentId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, Installment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = Installment.class)
    public JAXBElement<String> createInstallmentCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, Installment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = Installment.class)
    public JAXBElement<String> createInstallmentDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, Installment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetConnectedVouchersRequestBody.class)
    public JAXBElement<String> createGetConnectedVouchersRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetConnectedVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetConnectedVouchersRequestBody.class)
    public JAXBElement<String> createGetConnectedVouchersRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetConnectedVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetConnectedVouchersRequestBody.class)
    public JAXBElement<String> createGetConnectedVouchersRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetConnectedVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "parentId", scope = GetConnectedVouchersRequestBody.class)
    public JAXBElement<String> createGetConnectedVouchersRequestBodyParentId(String value) {
        return new JAXBElement<String>(_GetConnectedVouchersRequestBodyParentId_QNAME, String.class, GetConnectedVouchersRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVersionResult", scope = GetVersionResponseBody.class)
    public JAXBElement<String> createGetVersionResponseBodyGetVersionResult(String value) {
        return new JAXBElement<String>(_GetVersionResponseBodyGetVersionResult_QNAME, String.class, GetVersionResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = BaseCustomer.class)
    public JAXBElement<String> createBaseCustomerId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, BaseCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Information", scope = BaseCustomer.class)
    public JAXBElement<String> createBaseCustomerInformation(String value) {
        return new JAXBElement<String>(_VoucherItemInformation_QNAME, String.class, BaseCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CompanyName", scope = BaseCustomer.class)
    public JAXBElement<String> createBaseCustomerCompanyName(String value) {
        return new JAXBElement<String>(_BaseCustomerCompanyName_QNAME, String.class, BaseCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddress }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Addresses", scope = BaseCustomer.class)
    public JAXBElement<ArrayOfAddress> createBaseCustomerAddresses(ArrayOfAddress value) {
        return new JAXBElement<ArrayOfAddress>(_BaseCustomerAddresses_QNAME, ArrayOfAddress.class, BaseCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfElectronicAddress }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "ElectronicAddresses", scope = BaseCustomer.class)
    public JAXBElement<ArrayOfElectronicAddress> createBaseCustomerElectronicAddresses(ArrayOfElectronicAddress value) {
        return new JAXBElement<ArrayOfElectronicAddress>(_ContactElectronicAddresses_QNAME, ArrayOfElectronicAddress.class, BaseCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "State", scope = BaseCustomer.class)
    public JAXBElement<CustomerState> createBaseCustomerState(CustomerState value) {
        return new JAXBElement<CustomerState>(_State_QNAME, CustomerState.class, BaseCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Values", scope = CustomerItem.class)
    public JAXBElement<ArrayOfString> createCustomerItemValues(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_CustomerItemValues_QNAME, ArrayOfString.class, CustomerItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Id", scope = CustomerItem.class)
    public JAXBElement<String> createCustomerItemId(String value) {
        return new JAXBElement<String>(_VoucherItemId_QNAME, String.class, CustomerItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = CustomerItem.class)
    public JAXBElement<String> createCustomerItemDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, CustomerItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutInvoices }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetInvoicesResult", scope = GetInvoicesResponseBody.class)
    public JAXBElement<OutInvoices> createGetInvoicesResponseBodyGetInvoicesResult(OutInvoices value) {
        return new JAXBElement<OutInvoices>(_GetInvoicesResponseBodyGetInvoicesResult_QNAME, OutInvoices.class, GetInvoicesResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutVouchers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "GetVouchersResult", scope = GetVouchersResponseBody.class)
    public JAXBElement<OutVouchers> createGetVouchersResponseBodyGetVouchersResult(OutVouchers value) {
        return new JAXBElement<OutVouchers>(_GetVouchersResponseBodyGetVouchersResult_QNAME, OutVouchers.class, GetVouchersResponseBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "DisplayModes", scope = OutSalesInformation.class)
    public JAXBElement<ArrayOfInt> createOutSalesInformationDisplayModes(ArrayOfInt value) {
        return new JAXBElement<ArrayOfInt>(_OutSalesInformationDisplayModes_QNAME, ArrayOfInt.class, OutSalesInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfInt }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Intervals", scope = OutSalesInformation.class)
    public JAXBElement<ArrayOfInt> createOutSalesInformationIntervals(ArrayOfInt value) {
        return new JAXBElement<ArrayOfInt>(_OutSalesInformationIntervals_QNAME, ArrayOfInt.class, OutSalesInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSaleType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "SaleTypes", scope = OutSalesInformation.class)
    public JAXBElement<ArrayOfSaleType> createOutSalesInformationSaleTypes(ArrayOfSaleType value) {
        return new JAXBElement<ArrayOfSaleType>(_OutSalesInformationSaleTypes_QNAME, ArrayOfSaleType.class, OutSalesInformation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = ReturnReason.class)
    public JAXBElement<String> createReturnReasonDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, ReturnReason.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "customerId", scope = GetQueryTypesVoucherRequestBody.class)
    public JAXBElement<String> createGetQueryTypesVoucherRequestBodyCustomerId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyCustomerId_QNAME, String.class, GetQueryTypesVoucherRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "sessionId", scope = GetQueryTypesVoucherRequestBody.class)
    public JAXBElement<String> createGetQueryTypesVoucherRequestBodySessionId(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodySessionId_QNAME, String.class, GetQueryTypesVoucherRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "language", scope = GetQueryTypesVoucherRequestBody.class)
    public JAXBElement<String> createGetQueryTypesVoucherRequestBodyLanguage(String value) {
        return new JAXBElement<String>(_GetEnabledFunctionsRequestBodyLanguage_QNAME, String.class, GetQueryTypesVoucherRequestBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "CurrencyCode", scope = MonetaryValue.class)
    public JAXBElement<String> createMonetaryValueCurrencyCode(String value) {
        return new JAXBElement<String>(_VoucherItemCurrencyCode_QNAME, String.class, MonetaryValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = MonetaryValue.class)
    public JAXBElement<String> createMonetaryValueDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, MonetaryValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfVoucherType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "VoucherTypes", scope = OutVoucherTypes.class)
    public JAXBElement<ArrayOfVoucherType> createOutVoucherTypesVoucherTypes(ArrayOfVoucherType value) {
        return new JAXBElement<ArrayOfVoucherType>(_OutVoucherTypesVoucherTypes_QNAME, ArrayOfVoucherType.class, OutVoucherTypes.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "DVSE.WebApp.CISService", name = "Description", scope = CustomerState.class)
    public JAXBElement<String> createCustomerStateDescription(String value) {
        return new JAXBElement<String>(_StateDescription_QNAME, String.class, CustomerState.class, value);
    }

}
