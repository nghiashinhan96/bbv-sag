package com.sagag.services.rest.swagger.docs;

import lombok.experimental.UtilityClass;

/**
 * Swagger APIs description.
 */
public interface ApiDesc {

  /**
   * Http status code.
   */
  @UtilityClass
  class Code {
    public static final int OK = 200;

    public static final int NO_CONTENT = 204;

    public static final int BAD_REQUEST = 400;

    public static final int NOT_FOUND = 404;

    public static final int INTERNAL_SERVER_ERROR = 500;
  }

  @UtilityClass
  class Message {
    public static final String SUCCESSFUL = "Successful";

    public static final String NO_CONTENT = "No content";

    public static final String BAD_REQUEST = "Bad request";

    public static final String NOT_FOUND = "Not found any result";

    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
  }

  /**
   * Vin APIs specification.
   */
  @UtilityClass
  class Vin {
    public static final String ADD_LICENSE_PACKAGE_API_DESC = "Add license package";
    public static final String ADD_LICENSE_PACKAGE_API_NOTE =
        "The service will add license package to shopping cart.";
    public static final String GET_VIN_CALLS_LEFT_DESC = "View vin calls left";
    public static final String GET_VIN_CALLS_LEFT_NOTE =
        "The service will get total vin calls left for current customer";
    public static final String GET_VIN_PACKAGES_DESC = "Get vin packages";
    public static final String GET_VIN_PACKAGES_NOTE =
        "The service will get available vin packages to buy";
  }

  /**
   * License APIs specification.
   */
  @UtilityClass
  class License {
    public static final String GET_LICENSE_DESC = "Get All package";
    public static final String GET_LICENSE_NOTE = "The service will get all available licenses";
  }

  /**
   * Gtmotive APIs specification.
   */
  @UtilityClass
  class Gtmotive {
    public static final String GET_GTMOTIVE_API_DESC =
        "Get graphical information including the url and its request estimate id";
    public static final String GET_GTMOTIVE_API_NOTE =
        "The service to request to GTmotive server to get the url and request estimate id. "
            + "This will be used to include to ESHOP IFrame";
    public static final String GET_GRAPHICAL_SELECTED_PARTS_API_DESC =
        "Get graphical selected parts from the IFrame";
    public static final String GET_GRAPHICAL_SELECTED_PARTS_API_NOTE =
        "The service to request to GTmotive to get the selected parts from the IFrame";

    public static final String GTMOTIVE_VEHICLE_INFO_BY_VIN_API_DESC =
        "Returns the GTMotive vehicle info by VIN code";
    public static final String GTMOTIVE_VEHICLE_INFO_BY_VIN_API_NOTE =
        "The service to request GTMotive vehicle info from VIN code";


    public static final String GTMOTIVE_REFERENCES_SEARCH_API_DESC = "Search GTMotive references";
    public static final String GTMOTIVE_REFERENCES_SEARCH_API_NOTE =
        "The service will help to search GTMotive reference";

    public static final String GTMOTIVE_VEHICLE_INFO_BY_GT_INFO_API_DESC =
        "Returns the GTMotive vehicle info by GT info";
    public static final String GTMOTIVE_VEHICLE_INFO_BY_GT_INFO_NOTE =
        "The service to request GTMotive vehicle info by GT info";

    public static final String GTMOTIVE_GT_PART_LIST_SEARCH_API_DESC = "Search gtmotive part list";
    public static final String GTMOTIVE_GT_PART_LIST_SEARCH_API_NOTE =
        "The service will help to search gtmotive part list";

    public static final String GET_GTMOTIVE_VIN_SECURITY_CHECK_DESC = "Check vin security";
    public static final String GET_GTMOTIVE_VIN_SECURITY_CHECK_NOTE =
        "The service to check vin security";

    public static final String GET_GTMOTIVE_MULTI_PART_SEARCH_DESC = "Search multi part";
    public static final String GET_GTMOTIVE_MULTI_PART_SEARCH_NOTE =
        "The service to search multi part";

    public static final String GTMOTIVE_VIN_SEARCH_ERROR_LOGGING_DESC = "Log VIN search error";
    public static final String GTMOTIVE_VIN_SEARCH_ERROR_LOGGING_NOTE =
            "The service to log VIN search error";
    
  }

  /**
   * Orders APIs specification.
   */
  @UtilityClass
  class Order {
    public static final String CREATE_ORDERS_DESC = "Create order";
    public static final String CREATE_ORDERS_NOTE = "Place an order to ax to get order number";
    public static final String CREATE_BASKET_DESC = "Create basket";
    public static final String CREATE_BASKET_NOTE =
        "Place an order to ax to get ax url to view order";
    public static final String CREATE_OFFERS_DESC = "Create offer";
    public static final String CREATE_OFFERS_NOTE = "Make an offer to ax to get order number";
    public static final String GET_ORDER_HISTORY_DESC = "Get order history";
    public static final String GET_ORDER_HISTORY_NOTE = "Get all orders by filter conditions";
    public static final String GET_FILTER_CONDITION_DESC = "Get order history filter conditions";
    public static final String GET_FILTER_CONDITION_NOTE = "Get order history filter conditions";
    public static final String GET_SALE_ORDERS_DESC = "Get recent order by sale";
    public static final String GET_SALE_ORDERS_NOTE =
        "Get latest orders that sale has ordered on behalf";
    public static final String GET_DASHBOARD_OVERVIEW_DESC = "Get order dashboard overview";
    public static final String GET_DASHBOARD_OVERVIEW_NOTE = "Get order dashboard overview";
    public static final String GET_FINAL_CUSTOMER_ORDER_DESC = "Search final customer order";
    public static final String GET_FINAL_CUSTOMER_ORDER_NOTE =
        "Search final customer order for wholesaler";
    public static final String DELETE_ORDER_FINAL_CUSTOMER = "Delete an order of final customer";
    public static final String DELETE_ORDER_FINAL_CUSTOMER_NOTE =
        "Delete an order of final customer";
    public static final String UPDATE_FINAL_CUSTOMER_ORDERS_DESC =
        "Update order for final customer";
    public static final String UPDATE_FINAL_CUSTOMER_ORDERS_NOTE =
        "Update order for final customer";
  }


  /**
   * Invoices APIs specification.
   */
  @UtilityClass
  class Invoice {
    public static final String GET_INVOICES_DESC = "Gets invoice list";
    public static final String GET_INVOICES_NOTE = "Gets all invoices by filter conditions";

    public static final String VIEW_INVOICES_DETAIL_DESC = "Views invoices in detail";
    public static final String VIEW_INVOICES_DETAIL_NOTE =
        "Views invoice in detail by invoice number";

    public static final String DOWLOAD_INVOICE_PDF_DESC = "Download invoice pdf";
    public static final String DOWLOAD_INVOICE_PDF_NOTE =
        "Download invoice pdf by a given invoice number";

    public static final String ADD_TO_CART_BY_INVOICE_NR_DESC =
        "Add invoice positions to shopping cart";
  }

  /**
   * Shopping cart APIs specification.
   */
  @UtilityClass
  class Cart {
    public static final String ADD_ARTICLE_TO_SHOPPING_CART_API_DESC =
        "Add article to shopping basket";
    public static final String ADD_ARTICLE_TO_SHOPPING_CART_NOTE = "Add article to shopping basket";

    public static final String UPDATE_ARTICLE_TO_SHOPPING_CART_API_DESC =
        "Update article from shopping basket";
    public static final String UPDATE_ARTICLE_TO_SHOPPING_CART_NOTE =
        "Update article from shopping basket";

    public static final String UPDATE_DISPLAYED_PRICE_API_DESC =
        "Update displayed price from shopping basket";
    public static final String UPDATE_DISPLAYED_PRICE_API_NOTE =
        "Update displayed price from shopping basket";

    public static final String REMOVE_ARTICLES_FROM_SHOPPING_CART_API_DESC =
        "Remove selected articles from shopping basket";
    public static final String REMOVE_ARTICLES_FROM_SHOPPING_CART_NOTE =
        "Remove selected articles from shopping basket";

    public static final String REMOVE_ALL_ARTICLES_FROM_SHOPPING_CART_API_DESC =
        "Remove all articles from shopping basket";
    public static final String REMOVE_ALL_ARTICLES_FROM_SHOPPING_CART_NOTE =
        "Remove all articles from shopping basket";

    public static final String VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC = "View shopping basket";
    public static final String VIEW_ARTICLE_TO_SHOPPING_CART_NOTE = "View shopping basket";

    public static final String QUICK_VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC =
        "Quick view shopping basket";
    public static final String QUICK_VIEW_ARTICLE_TO_SHOPPING_CART_NOTE =
        "Quick view shopping basket";

    public static final String COUNT_TOTAL_ITEMS_IN_SHOPPING_CART_API_DESC =
        "Count total items in shopping basket";
    public static final String COUNT_TOTAL_ITEMS_IN_SHOPPING_CART_NOTE =
        "Count total items shopping basket";

    public static final String ADD_SAVED_BASKET_TO_SHOPPING_CART_API_DESC =
        "The service to add the list of basket histories of user login or sales user by criteria to shopping cart";

    public static final String ADD_ORDERS_TO_BASKET_DESC = "Add order history to shopping basket";
    public static final String ADD_ORDERS_TO_BASKET_NOTE =
        "Add order history by orderId to shopping basket";

    public static final String UPDATE_PRICES_SHOPPING_CART =
        "The service to update article prices form ERP";

    public static final String UPDATE_SALES_INFORMATION_FOR_DVSE_USER =
        "Update sales information for customer";

    public static final String ADD_FINAL_CUSTOMER_ORDER_ID_TO_SHOPPING_CART_API_DESC =
        "The service to add the selected final customer order id to shopping cart";
  }

  /**
   * Offer APIs specification.
   */
  @UtilityClass
  class Offer {
    public static final String SEARCH_OFFERS_API_DESC = "Search offers";
    public static final String SEARCH_OFFERS_NOTE = "Search offers in its organisation";
    public static final String VIEW_OFFER_DETAIL_API_DESC = "View offer detail";
    public static final String VIEW_OFFER_DETAIL_NOTE = "View offer detail";
    public static final String CREATE_OFFER_API_DESC = "Create new offer";
    public static final String CREATE_OFFER_NOTE =
        "Create new offer and return the offer with new id";
    public static final String REMOVE_OFFER_API_DESC = "Remove offer";
    public static final String REMOVE_OFFER_NOTE = "Remove offer by offer id";
    public static final String UPDATE_OFFER_API_DESC =
        "Add articles in shopping basket to new offer";
    public static final String UPDATE_OFFER_NOTE = "Add articles shopping basket to new offer";
    public static final String ADD_ARTICLE_FROM_SHOPPING_BASKET_TO_OFFER_API_DESC =
        "Add articles from shopping basket to offer but don't save";
    public static final String ADD_ARTICLE_FROM_SHOPPING_BASKET_TO_OFFER_NOTE =
        "Add articles from shopping basket to offer but don't save";
    public static final String ORDER_OFFER_API_DESC = "Add articles from offer to shopping basket";
    public static final String ORDER_OFFER_NOTE = "Add articles from offer to shopping basket";
  }

  /**
   * Offer Person APIs specification.
   */
  @UtilityClass
  class OfferPerson {
    public static final String CREATE_OFFER_PERSON_API_DESC = "Create offer person";
    public static final String CREATE_OFFER_PERSON_NOTE = "Create offer person";
    public static final String SEARCH_OFFER_PERSON_API_DESC = "Search offer person";
    public static final String SEARCH_OFFER_PERSON_NOTE = "Search offer person";
    public static final String UPDATE_OFFER_PERSON_API_DESC = "Update offer person";
    public static final String UPDATE_OFFER_PERSON_NOTE = "Update offer person";
    public static final String GET_OFFER_PERSON_DETAILS_API_DESC = "Get offer person details";
    public static final String GET_OFFER_PERSON_DETAILS_NOTE = "Get offer person details";
    public static final String REMOVE_OFFER_PERSON_API_DESC = "Remove offer person";
    public static final String REMOVE_OFFER_PERSON_API_NOTE = "Remove offer person";
    public static final String GET_SALUTATION_LIST_API_DESC = "Get list salutation for offer";
    public static final String GET_SALUTATION_LIST_API_NOTE = "Get list salutation for offer";
  }

  /**
   * SAG Context APIs specification.
   */
  @UtilityClass
  class SAGContext {
    public static final String CLEAR_CONTEXT_IN_CACHE_API_DESC = "Clear context in cache by user";
    public static final String CLEAR_CONTEXT_IN_CACHE_NOTE = "Clear context in cache by user";
    public static final String REFRESH_NEXT_WORKING_DATE_API_DESC =
        "Refresh next working date cache";
    public static final String REFRESH_NEXT_WORKING_DATE_NOTE = "Refresh next working date cache";

    public static final String GET_ESHOP_CONTEXT_API_DESC = "Get Eshop Context";
    public static final String GET_ESHOP_CONTEXT_API_NOTE = "Get Eshop Context";

    public static final String UPDATE_ESHOP_CONTEXT_API_DESC = "Update Eshop Context By Key";
    public static final String UPDATE_ESHOP_CONTEXT_API_NOTE = "Update Eshop Context By Key";

    public static final String REFRESH_ESHOP_CONTEXT_API_DESC = "Refresh Eshop Context By Key";
    public static final String REFRESH_ESHOP_CONTEXT_API_NOTE = "Refresh Eshop Context By Key";
  }

  /**
   * Vehicle Search APIs specification.
   */
  @UtilityClass
  class VehicleSearch {
    public static final String SEARCH_VEHICLE_BY_VEHID_API_DESC = "Search vehicle by vehicle Id";
    public static final String SEARCH_VEHICLE_BY_VEHID_API_NOTE =
        "The service will search vehicle by vehicle Id";

    public static final String SEARCH_VEHICLE_MAKE_API_DESC = "Get vehicle make";
    public static final String SEARCH_VEHICLE_MAKE_API_NOTE = "The service will get vehicle make";

    public static final String SEARCH_VEHICLE_MODEL_API_DESC = "Get vehicle model";
    public static final String SEARCH_VEHICLE_MODEL_API_NOTE = "The service will get vehicle model";

    public static final String SEARCH_VEHICLE_TYPE_API_DESC = "Get vehicle type";
    public static final String SEARCH_VEHICLE_TYPE_API_NOTE = "The service will get vehicle type";
  }

  /**
   * Article Search APIs specification.
   */
  @UtilityClass
  class ArticleSearch {
    public static final String SEARCH_ARTICLE_BY_NUMBER_API_DESC = "Search an article by number";
    public static final String SEARCH_ARTICLE_BY_NUMBER_API_NOTE =
        "The service will search an article by number";

    public static final String SEARCH_ARTICLE_BY_AMOUNT_API_DESC =
        "Search an article by Order Amount";
    public static final String SEARCH_ARTICLE_BY_AMOUNT_API_NOTE =
        "The service will search one article by Order Amount";

    public static final String SEARCH_ARTICLES_BY_CATEGORIES_AND_VEHICLES_API_DESC =
        "Search parts by category Ids and vehicle Ids";
    public static final String SEARCH_ARTICLES_BY_CATEGORIES_AND_VEHICLES_API_NOTE =
        "The service will search parts by category Ids and vehicle Ids";

    public static final String SEARCH_VEHICLE_USAGE_BY_ARTID_API_DESC =
        "Search a vehicle usage by artId";
    public static final String SEARCH_VEHICLE_USAGE_BY_ARTID_API_NOTE =
        "The service will search a vehicle usage by artId";

    public static final String SEARCH_ARTICLE_BY_BAR_CODE_API_DESC =
        "Search an article by bar code";
    public static final String SEARCH_ARTICLE_BY_BAR_CODE_API_NOTE =
        "The service will search an article by bar code";

    public static final String SEARCH_ARTICLE_DISPLAY_PRICES_DESC =
        "Search an article display prices";

    public static final String SEARCH_ARTICLE_DISPLAY_PRICES_NOTE =
        "The service will search article display prices";
  }

  /**
   * Article Search APIs specification.
   */
  @UtilityClass
  class ArticleSearchV2 {
    public static final String SEARCH_ARTICLES_BY_UNIVERSAL_PART_LEAF_API_DESC =
        "Search articles by universal part leaf";
    public static final String SEARCH_ARTICLES_BY_UNIVERSAL_PART_LEAF_API_NOTE =
        "The service will search articles by universal part leaf";
  }

  /**
   * Customer APIs specification.
   */
  @UtilityClass
  class Customer {
    public static final String REGISTER_POTENTIAL_CUSTOMER_API_DESC =
        "Create new potential customer";
    public static final String REGISTER_POTENTIAL_CUSTOMER_API_NOTE =
        "The service will help to create new potential customer";

    public static final String RETRIEVE_CUSTOMER_API_NOTE =
        "Validate and retrieve customer information";
    public static final String RETRIEVE_CUSTOMER_API_DESC =
        "Validate and etrieve customer information before registering new customer";
  }

  /**
   * Financial Card APIs specification.
   */
  @UtilityClass
  class FinancialCard {
    public static final String GET_FINANCIAL_CARD_HISTORY_DESC =
        "Get financial card history payment list for customer";
    public static final String GET_FINANCIAL_CARD_HISTORY_NOTE =
        "The service will help to get data history payment of customer";

    public static final String GET_FINANCIAL_CARD_AMOUNT_DESC =
        "Get financial card amount for customer";
    public static final String GET_FINANCIAL_CARD_AMOUNT_NOTE =
        "The service will help to get data total payment amount of customer";

    public static final String VIEW_FINANCIAL_CARD_DETAIL_DESC = "Views financial card in detail";
    public static final String VIEW_FINANCIAL_CARD_DETAIL_NOTE =
        "Views financial card in detail by document number";
  }

  /**
   * Feedback APIs specifications.
   */
  @UtilityClass
  class Feedback {
    public static final String GET_FEEDBACK_CUSTOMER_USER_DATA_API_DESC =
        "This api help get user data for customer";
    public static final String GET_FEEDBACK_CUSTOMER_USER_DATA_API_NOTE =
        "Get user data for customer";
    public static final String GET_FEEDBACK_SALES_ONBEHALF_USER_DATA_API_DESC =
        "This api help get user data for sales on behalf";
    public static final String GET_FEEDBACK_SALES_ONBEHALF_USER_DATA_API_NOTE =
        "Get user data for sales on behalf";
    public static final String GET_FEEDBACK_SALES_NOT_ONBEHALF_USER_DATA_API_DESC =
        "This api help get user data for sales not on behalf";
    public static final String GET_FEEDBACK_SALES_NOT_ONBEHALF_USER_DATA_API_NOTE =
        "Get user data for sales not on behalf";


    public static final String CREATE_CUSTOMER_FEEDBACK_MESSAGE_API_DESC =
        "This api help to create customer feedback";
    public static final String CREATE_CUSTOMER_FEEDBACK_MESSAGE_API_NOTE =
        "Create customer feedback message";

    public static final String CREATE_SALES_ONBEHALF_FEEDBACK_MESSAGE_API_DESC =
        "This api help to create sales on behalf feedback";
    public static final String CREATE_SALES_ONBEHALF_FEEDBACK_MESSAGE_API_NOTE =
        "Create sales on behalf feedback message";

    public static final String CREATE_SALES_NOT_ONBEHALF_FEEDBACK_MESSAGE_API_DESC =
        "This api help to create sales not on behalf feedback";
    public static final String CREATE_SALES_NOT_ONBEHALF_FEEDBACK_MESSAGE_API_NOTE =
        "Create sales not on behalf feedback message";
  }

  /**
   * Categories APIs specifications.
   */
  @UtilityClass
  class Category {
    public static final String ALL_CATEGORIES_VEHICLE_API_DESC = "Get vehicles categories";
    public static final String ALL_CATEGORIES_VEHICLE_API_NOTE =
        "The service will get all vehicle categories";

    public static final String SEARCH_CATEGORIES_VEHICLE_API_DESC = "Search categories vehicles";
    public static final String SEARCH_CATEGORIES_VEHICLE_API_NOTE =
        "The service will search categories in vehicle";

    public static final String QUICK_CLICK_CATEGORIES_VEHICLE_API_DESC =
        "Get vehicle quick click categories";
    public static final String QUICK_CLICK_CATEGORIES_VEHICLE_API_NOTE =
        "The service will get quick click vehicle categories ";
  }

  /**
   * Unitrees APIs specifications.
   */
  @UtilityClass
  class Unitree {
    public static final String ALL_COMPACT_UNITREE_API_DESC = "Get Compact Unitrees ";
    public static final String ALL_COMPACT_UNITREE_API_NOTE =
        "This service will get all compact unitrees";

    public static final String GET_UNITREE_DETAIL_API_DESC = "Get Unitree Detail ";
    public static final String GET_UNITREE_DETAIL_API_NOTE =
        "This service will get uitree node list of the Unitree.";
    public static final String GET_UNITREE_BY_LEAF_ID_DESC = "Get Unitree By LeafId";
    public static final String GET_UNITREE_BY_LEAF_ID_NOTE =
        "Get Unitree by leaf id which included in any member of node tree";
  }

  /**
   * EshopFavorite APIs specifications.
   */
  @UtilityClass
  class EshopFavorite {
    public static final String PROCESS_FAVORITE_ITEM_API_DESC = "Process Favorite Item";
    public static final String PROCESS_FAVORITE_ITEM_API_NOTE =
        "This service will add new or delete favorite item.";

    public static final String GET_FAVORITE_ITEM_LIST_API_DESC = "Get Favorite Item List";
    public static final String GET_FAVORITE_ITEM_LIST_API_NOTE =
        "This service will get favorite item list.";

    public static final String UPDATE_FAVORITE_ITEM_API_DESC = "Update Favorite Item";
    public static final String UPDATE_FAVORITE_ITEM_API_NOTE =
        "This service will update favorite item info.";

    public static final String GET_FAVORITE_ITEM_INFO_BY_LIST_API_DESC =
        "Get Favorite Items info by List";
    public static final String GET_FAVORITE_ITEM_INFO_BY_LIST_API_NOTE =
        "This service will get favorite items info by list.";

    public static final String GET_LASTEST_FAVORITE_COMMENTS_API_DESC = "Get latest favorite comments";
    public static final String GET_LASTEST_FAVORITE_COMMENTS_API_NOTE =
        "This service will get latest favorite comments.";


    public static final String SEARCH_FAVORITE_ITEM_API_DESC = "Search Favorite Item";
    public static final String SEARCH_FAVORITE_ITEM_API_NOTE =
        "This service will search favorite item info.";
  }


  /**
   * HaynesPro APIs specifications.
   */
  @UtilityClass
  class HaynesPro {

    public static final String CHECK_HAYNESPRO_LICENSE_API_DESC = "Check HAYNESPRO license";

    public static final String CHECK_HAYNESPRO_LICENSE_API_NOTE =
        "Check HAYNESPRO license if it return true the request can pass to HAYNESPRO, else we can not accept the HAYNESPRO link";

    public static final String GET_HAYNESPRO_LICENSE_API_DESC = "Get customer HAYNESPRO license";

    public static final String GET_HAYNESPRO_LICENSE_API_NOTE =
        "Get customer HAYNESPRO license, it return ULTIMATE or PROFESSIONAL";

    public static final String GET_HAYNESPRO_API_DESC =
        "Calling “registerHaynesProVisitByDistributor”. "
            + "This operation is based on company credentials and an identifying part for a customer. "
            + "The username and password are unique to each\n"
            + "distributor (to each company identification). The licence is specific to each customer's username. "
            + "It is useful because the distributor does not need\n"
            + "to know the password of each user";

    public static final String GET_HAYNESPRO_API_NOTE =
        "Single Signon Services for Touch V2.The purpose of this service is to authenticate an user "
            + "to the Workshop Online application directly from another web-site (as a portal). The service\n"
            + "requires an identification of the company (you), a username and, depending on the contract, the password of the user.\n"
            + "The service returns a link that, when used, logs the user in and sets the preset parameters (language, VAT rate and others). "
            + "The link can be used\n"
            + "only once, and the user can use the application until he or she closes the browser window. "
            + "If the user needs to access the application again after\n"
            + "closing the browser window, the service must be called again and the new provided link should be used.  ";

    public static final String GET_HAYNESPRO_DISPLAY_SMART_CART_RESPONSE_TO_PART_LIST_DESC =
        "Get smart cart response to display on part list";
    public static final String GET_HAYNESPRO_DISPLAY_SMART_CART_RESPONSE_TO_PART_LIST_NOTE =
        "Note of get smart cart response to display on part list";

    public static final String GET_HAYNESPRO_LABOUR_TIME_DESC = "Get Haynes Pro labour time";
    public static final String GET_HAYNESPRO_LABOUR_TIME_NOTE =
        "Get Haynes Pro labour time by user and vehicle info to display on shopping card";

    public static final String REMOVE_HAYNESPRO_LABOUR_TIME_DESC = "Removes Haynes Pro labour time";
    public static final String REMOVE_HAYNESPRO_LABOUR_TIME_NOTE =
        "Removes Haynes Pro labour time by user and vehicle info and awNumber";

    public static final String REQUEST_TRIAL_HAYNESPRO_LICENSE_API_DESC =
        "Request trial HAYNESPRO license";
    public static final String REQUEST_TRIAL_HAYNESPRO_LICENSE_API_NOTE =
        "Request trial HAYNESPRO license";
  }

  /**
   * Unicat APIs specifications.
   */
  @UtilityClass
  class Unicat {
    public static final String GET_UNICAT_OPEN_CATALOG_DESC = "Get Unicat Open Cataloge URI";
    public static final String GET_UNICAT_OPEN_CATALOG_NOTE =
        "This service will return Unicat URI included nessesarry param to authen to Unicat";
  }

  /**
   * Return article Search APIs specification.
   */
  @UtilityClass
  class ReturnOrder {
    public static final String SEARCH_RETURN_ORDERS_API_DESC =
        "Returns return order details from AX SWS";
    public static final String SEARCH_RETURN_ORDERS_API_NOTE =
        "The service will search an article by search key input by user. ";

    public static final String CREATE_RETURN_ORDER_API_DESC = "Creates return order";
    public static final String CREATE_RETURN_ORDER_API_NOTE =
        "The service will create return order to AX SWS";

    public static final String REASON_API_DESC = "Returns the reason list";
    public static final String REASON_API_NOTE = "The service will return the reason list";

    public static final String GET_RETURN_ORDER_BATCH_JOB_STATUS_API_DESC =
        "Returns return order batch job status from AX SWS";
    public static final String GET_RETURN_ORDER_BATCH_JOB_STATUS_API_NOTE =
        "The service will get return order batch job status";

    public static final String GET_RETURN_ORDER_BATCH_JOB_RESULT_API_DESC =
        "Returns return order batch job result from AX SWS";
    public static final String GET_RETURN_ORDER_BATCH_JOB_RESULT_API_NOTE =
        "The service will get return order batch job result";
  }

  /**
   * OCI APIs specifications.
   */
  @UtilityClass
  class Oci {
    public static final String EXPORT_OCI_API_DESC = "Exports OCI Data form";
    public static final String EXPORT_OCI_API_NOTE = "Exports OCI Data form from shopping basket";
  }

  /**
   * Incentive APIs specification.
   */
  @UtilityClass
  class Incentive {

    public static final String GET_HAPPY_POINTS_DESC = "Get happy points";
    public static final String GET_HAPPY_POINTS_NOTE =
        "The service will help user get his happy points";

    public static final String SAVE_HAPPY_POINTS_TERM_DESC = "Save happy point term";
    public static final String SAVE_HAPPY_POINTS_TERM_NOTE =
        "The service will save user-setting for accept happy point term";

    public static final String GET_MILES_INCENTIVE_URL_DESC = "Get miles incentive url";
    public static final String GET_MILES_INCENTIVE_URL_NOTE =
        "The service will Get miles incentive url";

    public static final String GET_OUTLET_URL_DESC = "Get out url";
    public static final String GET_OUTLET_URL_NOTE = "The service will get outlet url";
  }

  /**
   * User Accounts APIs specification.
   */
  @UtilityClass
  class UserAccounts {

    public static final String VIEW_PROFILE_DESC = "View user profile";
    public static final String VIEW_PROFILE_NOTE = "The service will get detail of user profile.";

    public static final String VIEW_USER_PROFILE_DESC = "Customer Admin view user profile";
    public static final String VIEW_USER_PROFILE_NOTE =
        "The service will help Customer Admin view user profile.";

    public static final String PROFILE_UPDATE_INFORMATION_DESC =
        "Update user's profile information";
    public static final String PROFILE_UPDATE_INFORMATION_NOTE =
        "The service will update user profile information";

    public static final String GET_ALL_USER_SAME_ORGANISATION_DESC =
        "Get all user same organisation";
    public static final String GET_ALL_USER_SAME_ORGANISATION_NOTE =
        "The service return all user same organisation";

    public static final String DELETE_USER_DESC = "Delete user";
    public static final String DELETE_USER_NOTE = "The service will delete";

    public static final String DELETE_DVSE_USER_DESC = "Delete MDM user only";
    public static final String DELETE_DVSE_USER_NOTE = "The service will delete only MDM user";

    public static final String PROFILE_UPDATE_USER_INFORMATION_DESC =
        "Customer admin update user's profile information";
    public static final String PROFILE_UPDATE_USER_INFORMATION_NOTE =
        "The service will help Customer Admin update user's profile information";

    public static final String PROFILE_UPDATE_PAYMENT_DESC = "Update user's setting";
    public static final String PROFILE_UPDATE_PAYMENT_NOTE =
        "The service will update user's setting";

    public static final String UPDATE_USER_PRICE_SETTING_DESC = "Update user price setting";
    public static final String UPDATE_USER_PRICE_SETTING_NOTE =
        "The service is for updating user's price setting";

    public static final String PROFILE_UPDATE_USER_PAYMENT_DESC =
        "Customer admin update user's setting";
    public static final String PROFILE_UPDATE_USER_PAYMENT_NOTE =
        "The service will help Customer admin update user's setting";

    public static final String GET_SETTINGS_DESC = "Get user setting";
    public static final String GET_SETTINGS_NOTE = "The service will get user setting";

    public static final String GET_PAYMENT_SETTINGS_DESC = "Get user payment settings";
    public static final String GET_PAYMENT_SETTINGS_NOTE =
        "The service will get user payment settings";

    public static final String GET_USER_SETTINGS_DESC = "Customer admin get user setting";
    public static final String GET_USER_SETTINGS_NOTE =
        "The service will help Customer admin get user setting";

    public static final String GET_USER_PAYMENT_SETTINGS_DESC =
        "Customer admin get user's payment setting";
    public static final String GET_USER_PAYMENT_SETTINGS_NOTE =
        "The service will help Customer admin get user's payment setting";

    public static final String GET_EMPLOYEE_INFO_DESC = "Request for employee information";
    public static final String GET_EMPLOYEE_INFO_NOTE = "Request for employee information";

    public static final String CREATE_USER_DESC = "Create user";
    public static final String CREATE_USER_NOTE =
        "The service will help Customer Admin create a new user";
  }

  /**
   * Changes Password APIs specification.
   */
  @UtilityClass
  class ChangePassword {
    public static final String PROFILE_UPDATE_PW_DESC = "Update himself's password";
    public static final String PROFILE_UPDATE_PW_NOTE =
        "The service will update himself's password";

    public static final String PROFILE_UPDATE_USER_PW_DESC =
        "Customer admin update other user's password";
    public static final String PROFILE_UPDATE_USER_PW_NOTE =
        "The service will help Customer Admin update user's password";
  }

  @UtilityClass
  class ExternalUserSession {
    public static final String GET_INITIATE_SESSION = "Get initiate connect session from autonet";
    public static final String GET_INITIATE_SESSION_NOTE =
        "Get initiate connect session from autonet";
  }

  /**
   * Adds Buyers Guide APIs specification from Thule Third-party.
   */
  @UtilityClass
  class Thule {
    public static final String TRANSFER_BASKET_DESC = "Transfer basket from Thule to eConnect";
    public static final String TRANSFER_BASKET_NOTE =
        "The service to transfer the basket back to eConnect application from Thule shop. ";
  }

  /**
   * WSS Opening days calendar APIs specification.
   */
  @UtilityClass
  class WssOpeningDaysCalendar {
    public static final String GET_WSS_WORKING_DAY_CODES_DESC = "Get all WSS working day codes";
    public static final String GET_WSS_WORKING_DAY_CODES_NOTE =
        "The service returns all WSS working day codes";

    public static final String CREATE_NEW_WSS_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to create new WSS opening days calendar";
    public static final String CREATE_NEW_WSS_OPENING_DAYS_CALENDAR_NOTE =
        "Create new WSS opening days calendar";

    public static final String UPDATE_WSS_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to update existing WSS opening days calendar";
    public static final String UPDATE_WSS_OPENING_DAYS_CALENDAR_NOTE =
        "Update existing WSS opening days calendar";

    public static final String REMOVE_WSS_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to remove existing WSS opening days calendar";
    public static final String REMOVE_WSS_OPENING_DAYS_CALENDAR_NOTE =
        "Remove existing WSS opening days calendar";

    public static final String GET_WSS_OPENING_DAYS_CALENDAR_DETAIL_DESC =
        "This api allow admin to get existing WSS opening days calendar detail by id";
    public static final String GET_WSS_OPENING_DAYS_CALENDAR_DETAIL_NOTE =
        "Get existing WSS opening days calendar detail by id";

    public static final String SEARCH_WSS_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to search existing WSS opening days calendar by criteria";
    public static final String SEARCH_WSS_OPENING_DAYS_CALENDAR_NOTE =
        "Search existing WSS opening days calendar by criteria";

    public static final String IMPORT_WSS_OPENING_DAYS_CALENDAR_FROM_CSV_DESC =
        "This api allow admin to import WSS opening days calendar from csv file";
    public static final String IMPORT_WSS_OPENING_DAYS_CALENDAR_FROM_CSV_NOTE =
        "Import WSS opening days calendar from csv file";

    public static final String GET_COUNTRY_INFO_BY_CODE_DESC = "Get country informations by code";
    public static final String GET_COUNTRY_INFO_BY_CODE_NOTE =
        "The service returns country informations by code";
  }

  /**
   * WSS Branches APIs specification.
   */
  @UtilityClass
  class WssBranch {
    public static final String CREATE_NEW_WSS_BRANCH_DESC =
        "This api allow whole saler admin to create new branch";
    public static final String CREATE_NEW_WSS_BRANCH_NOTE = "Create new WSS branch";

    public static final String UPDATE_WSS_BRANCH_DESC =
        "This api allow whole saler admin to update existing WSS branch";
    public static final String UPDATE_WSS_BRANCH_NOTE = "Update existing WSS branch";

    public static final String REMOVE_WSS_BRANCH_DESC =
        "This api allow whole saler admin to remove existing WSS branch";
    public static final String REMOVE_WSS_BRANCH_NOTE = "Remove existing WSS branch";

    public static final String SEARCH_WSS_BRANCH_DESC =
        "This api allow whole saler admin to search existing WSS branch";
    public static final String SEARCH_WSS_BRANCH_NOTE = "Search existing WSS branch";

    public static final String GET_WSS_BRANCH_DETAIL_DESC =
        "This api allow whole saler admin to get existing WSS branch detail by branch number";
    public static final String GET_WSS_BRANCH_DETAIL_NOTE =
        "Get existing branch detail by branch number";

    public static final String GET_WSS_BRANCH_LIST_DESC =
        "This api allow whole saler admin to get all WSS branches";
    public static final String GET_WSS_BRANCH_LIST_NOTE = "Get all existing WSS branches";

    public static final String GET_WSS_BRANCHES_BY_ORGANISATION_DESC =
        "This api allow whole saler admin to get WSS branches by his organisation";
    public static final String GET_WSS_BRANCHES_BY_ORGANISATION_NOTE =
        "Get existing WSS branches by his organisation";

  }

  /**
   * WSS Tours APIs specification.
   */
  @UtilityClass
  class WssTour {
    public static final String CREATE_NEW_WSS_TOUR_DESC =
        "This api allow whole saler admin to create new tour";
    public static final String CREATE_NEW_WSS_TOUR_NOTE = "Create new WSS tour";

    public static final String UPDATE_WSS_TOUR_DESC =
        "This api allow whole saler admin to update existing WSS tour";
    public static final String UPDATE_WSS_TOUR_NOTE = "Update existing WSS tour";

    public static final String REMOVE_WSS_TOUR_DESC =
        "This api allow whole saler admin to remove existing WSS tour";
    public static final String REMOVE_WSS_TOUR_NOTE = "Remove existing WSS tour";

    public static final String SEARCH_WSS_TOUR_DESC =
        "This api allow whole saler admin to search existing WSS tour";
    public static final String SEARCH_WSS_TOUR_NOTE = "Search existing WSS tour";

    public static final String GET_WSS_TOUR_BY_ID_DESC =
        "This api allow whole saler admin to get existing WSS tour by id";
    public static final String GET_WSS_TOUR_BY_ID_NOTE = "Get existing WSS tour by id";

  }

  /**
   * WSS Delivery Profile APIs specification.
   */
  @UtilityClass
  class WssDeliveryProfile {
    public static final String CREATE_NEW_WSS_DELIVERY_PROFILE_DESC =
        "This api allow whole saler admin to create new WSS delivery profile";
    public static final String CREATE_NEW_WSS_DELIVERY_PROFILE_NOTE =
        "Create new WSS delivery profile";

    public static final String UPDATE_WSS_DELIVERY_PROFILE_DESC =
        "This api allow whole saler admin to update existing WSS delivery profile";
    public static final String UPDATE_WSS_DELIVERY_PROFILE_NOTE =
        "Update existing WSS delivery profile";

    public static final String REMOVE_WSS_DELIVERY_PROFILE_DESC =
        "This api allow whole saler admin to remove existing WSS delivery profile";
    public static final String REMOVE_WSS_DELIVERY_PROFILE_NOTE =
        "Remove existing WSS delivery profile";

    public static final String SEARCH_WSS_DELIVERY_PROFILE_DESC =
        "This api allow whole saler admin to search existing WSS delivery profile";
    public static final String SEARCH_WSS_DELIVERY_PROFILE_NOTE =
        "Search existing WSS delivery profile";

    public static final String ADD_WSS_DELIVERY_PROFILE_TOUR_DESC =
        "This api allow whole saler admin to add tour to existing WSS delivery profile";
    public static final String ADD_WSS_DELIVERY_PROFILE_TOUR_NOTE =
        "Add tour to  existing WSS delivery profile";

    public static final String UPDATE_WSS_DELIVERY_PROFILE_TOUR_DESC =
        "This api allow whole saler admin to update tour of existing WSS delivery profile";
    public static final String UPDATE_WSS_DELIVERY_PROFILE_TOUR_NOTE =
        "Update tour of existing WSS delivery profile";

    public static final String REMOVE_WSS_DELIVERY_PROFILE_TOUR_DESC =
        "This api allow whole saler admin to remove tour of existing WSS delivery profile";
    public static final String REMOVE_WSS_DELIVERY_PROFILE_TOUR_NOTE =
        "Remove tour of existing WSS delivery profile";

    public static final String GET_WSS_DELIVERY_PROFILE_TOUR_DETAIL_DESC =
        "This api allow whole saler admin to get detail of existing WSS delivery profile";
    public static final String GET_WSS_DELIVERY_PROFILE_TOUR_DETAIL_NOTE =
        "Get detail of existing WSS delivery profile";

  }

  /**
   * WSS Margin by Brand APIs specification.
   */
  @UtilityClass
  class MarginByBrand {
    public static final String CREATE_MARGIN_BY_BRAND_DESC = "This api to create margin by brand";
    public static final String CREATE_MARGIN_BY_BRAND_NOTE = "Create margin by brand";

    public static final String UPDATE_MARGIN_BY_BRAND_DESC =
        "This api to update created margin by brand";
    public static final String UPDATE_MARGIN_BY_BRAND_NOTE = "Update margin by brand";

    public static final String DELETE_MARGIN_BY_BRAND_DESC = "This api to remove margin by brand";
    public static final String DELETTE_MARGIN_BY_BRAND_NOTE = "Remove margin by brand";
    public static final String SEARCH_BRAND_BY_NAME = "Search brand by name";
    public static final String SEARCH_BRAND_BY_NAME_NOTE = "Search brand by name";
    public static final String SEARCH_MARGIN_BY_BRAND_NOTE = "Search margin brand by criteria";
    public static final String SEARCH_MARGIN_BY_BRAND_DESC =
        "This api to search brand magin by specific criteria";
    public static final String GET_DEFAULT_MARGIN_BY_BRAND_NOTE =
        "Get default margin brand by criteria";
    public static final String GET_DEFAULT_MARGIN_BY_BRAND_DESC =
        "This api to get default brand magin by specific criteria";
    public static final String IMPORT_WSS_MARGIN_BY_BRAND_DESC =
        "This api to import WSS margin by brand";
    public static final String IMPORT_WSS_MARGIN_BY_BRAND_NOTE = "import WSS margin by brand";

  }

  /**
   * WSS Margin by Brand APIs specification.
   */
  @UtilityClass
  class MarginByArticleGroup {
    public static final String CREATE_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to create margin by article group";
    public static final String CREATE_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "Create margin by article group";

    public static final String UPDATE_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to update created margin by article group";
    public static final String UPDATE_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "Update margin by article group";

    public static final String DELETE_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to remove margin by article group";
    public static final String DELETTE_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "Remove margin by article group";

    public static final String SEARCH_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to search margin by article group from system";
    public static final String SEARCH_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "search article group from system";

    public static final String SEARCH_MARGIN_BY_ARTICLE_GROUP_INDEX_DESC =
        "This api to search margin by article group from elastic search";
    public static final String SEARCH_MARGIN_BY_ARTICLE_GROUP_INDEX_NOTE =
        "search article group from elasticsearch";

    public static final String FIND_DEFAULT_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to find default margin by article group";
    public static final String FIND_DEFAULT_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "find default margin by article group";

    public static final String SEARCH_ROOT_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to search root margin by article group from system";
    public static final String SEARCH_ROOT_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "search root article group from system";

    public static final String SEARCH_CHILD_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to search child margin by article group from system";
    public static final String SEARCH_CHILD_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "search child article group from system";

    public static final String UPDATE_WSS_SHOW_NET_PRICE_SETTING_DESC =
        "This api to update WSS show net price setting";
    public static final String UPDATE_WSS_SHOW_NET_PRICE_SETTING_NOTE =
        "update WSS show net price setting";

    public static final String IMPORT_WSS_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to import WSS margin by article group";
    public static final String IMPORT_WSS_MARGIN_BY_ARTICLE_GROUP_NOTE =
        "import WSS margin by article group";

    public static final String UNMAP_MARGIN_BY_ARTICLE_GROUP_DESC =
        "This api to unmap margin by article group";
    public static final String UNMAP_MARGIN_BY_ARTICLE_GROUP_NOTE = "unmap margin by article group";
  }

  /**
   * Article History APIs specification.
   */
  @UtilityClass
  class ArticleHistory {
    public static final String SEARCH_LATEST_ARTICLE_HISTORY_API_DESC =
        "Search latest article history";
    public static final String SEARCH_LATEST_ARTICLE_HISTORY_API_NOTE =
        "The service will search latest article history";

    public static final String SEARCH_ARTICLE_HISTORY_API_DESC = "Search article history";
    public static final String SEARCH_ARTICLE_HISTORY_API_NOTE =
        "The service will search article history";

    public static final String ADD_ARTICLE_HISTORY_API_DESC = "Add article history";
    public static final String ADD_ARTICLE_HISTORY_API_NOTE =
        "The service will add article history";

    public static final String UPDATE_ARTICLE_HISTORY_API_DESC = "Update article history";
    public static final String UPDATE_ARTICLE_HISTORY_API_NOTE =
        "The service will update article history";
  }

  /**
   * Legal Term APIs specification.
   */
  @UtilityClass
  class LegalTerm {
    public static final String RETRIEVE_LEGAL_TERMS_API_DESC =
            "Retrieve user's legal terms";
    public static final String RETRIEVE_LEGAL_TERMS_API_NOTE =
            "The service will retrieve legal terms";

    public static final String ACCEPT_LEGAL_TERM_API_DESC =
            "Accept a legal term";
    public static final String ACCEPT_LEGAL_TERM_API_NOTE =
            "The service will accept a legal term";

    public static final String HAS_EXPIRED_TERMS_API_DESC =
            "Check if has any expired terms";
    public static final String HAS_EXPIRED_TERMS_API_NOTE =
            "The service will check if has any expired terms";

    public static final String RETRIEVE_UNACCEPTED_LEGAL_TERMS_API_DESC =
        "Retrieve unaccepted legal terms (normal user only)";
    public static final String RETRIEVE_UNACCEPTED_LEGAL_TERMS_API_NOTE =
        "The service will retrieve unaccepted legal terms (normal user only)";
  }


}
