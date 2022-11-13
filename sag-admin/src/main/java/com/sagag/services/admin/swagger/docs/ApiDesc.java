package com.sagag.services.admin.swagger.docs;

import lombok.experimental.UtilityClass;

/**
 * Swagger APIs description for admin.
 */
public interface ApiDesc {

  /**
   * Branches APIs specification.
   */
  @UtilityClass
  class Branch {
    public static final String CREATE_NEW_BRANCH_DESC = "This api allow admin to create new branch";
    public static final String CREATE_NEW_BRANCH_NOTE = "Create new branch";

    public static final String UPDATE_BRANCH_DESC =
        "This api allow admin to update existing branch";
    public static final String UPDATE_BRANCH_NOTE = "Update existing branch";

    public static final String REMOVE_BRANCH_DESC =
        "This api allow admin to remove existing branch";
    public static final String REMOVE_BRANCH_NOTE = "Remove existing branch";

    public static final String SEARCH_BRANCH_DESC =
        "This api allow admin to search existing branch";
    public static final String SEARCH_BRANCH_NOTE = "Search existing branch";

    public static final String GET_BRANCH_DETAIL_DESC =
        "This api allow admin to get existing branch detail by branch number";
    public static final String GET_BRANCH_DETAIL_NOTE =
        "Get existing branch detail by branch number";

    public static final String GET_BRANCH_LIST_DESC =
        "This api allow admin to get all branches";
    public static final String GET_BRANCH_LIST_NOTE = "Get all existing branches";

    public static final String GET_BRANCHES_BY_COUNTRY_DESC =
        "This api allow admin to get branches by country";
    public static final String GET_BRANCHES_BY_COUNTRY_NOTE = "Get existing branches by country";

  }

  /**
   * Aad acounts APIs specification.
   */
  @UtilityClass
  class AadAccounts {
    public static final String CREATE_NEW_AAD_ACCOUNT_DESC =
        "This service will help us to create new aad account";
    public static final String CREATE_NEW_AAD_ACCOUNT_NOTE = "Create new aad account";

    public static final String UPDATE_AAD_ACCOUNT_DESC =
        "This service will help us to update existing aad account";
    public static final String UPDATE_AAD_ACCOUNT_NOTE = "Update existing aad account";

    public static final String SEARCH_AAD_ACCOUNT_DESC =
        "This service will help us to search aad accounts";
    public static final String SEARCH_AAD_ACCOUNT_NOTE = "Search existing aad accounts";

    public static final String FIND_AAD_ACCOUNT_BY_ID_DESC =
        "This service will help us to find aad accounts by id";
    public static final String FIND_AAD_ACCOUNT_BY_ID_NOTE = "Find existing aad accounts by id";
  }

  /**
   * Affiliates APIs specification.
   */
  @UtilityClass
  class Affiliate {
    public static final String GET_AFFILIATE_SETTINGS_NOTE =
        "The service will get affiliate settings";
    public static final String GET_AFFILIATE_SETTINGS_DESC = "Get affiliate settings";
    public static final String GET_AFFILIATE_INFO_DESC = "Get affiliate informations";
    public static final String GET_AFFILIATE_INFO_NOTE =
        "The service returns affiliate informations";

    public static final String GET_AFFILIATE_SHORT_INFO_DESC = "Get affiliate short informations";
    public static final String GET_AFFILIATE_SHORT_INFO_NOTE =
        "The service returns affiliate short informations";

    public static final String UPDATE_AFFILIATE_SETTINGS_NOTE =
        "The service will update affiliate settings";
    public static final String UPDATE_AFFILIATE_SETTINGS_DESC = "Update affiliate settings";

    public static final String GET_AFFILIATE_SHORT_INFO_BY_COUNTRY_DESC =
        "Get affiliate short informations by country";
    public static final String GET_AFFILIATE_SHORT_INFO_BY_COUNTRY_NOTE =
        "The service returns affiliate short informations by contry";
    public static final String GET_AVAILABILITY_SETTING_MASTER_DATA_DESC = "Get master data for availability setting of affiliate";
    public static final String GET_AVAILABILITY_SETTING_MASTER_DATA = "Return related master data for availability setting for specific affilicate";
  }

  /**
   * Customers APIs specification.
   */
  @UtilityClass
  class Customer {

    public static final String GET_CUSTOMER_SETTINGS_DESC = "Get customer settings";
    public static final String GET_CUSTOMER_SETTINGS_NOTE =
        "The service will get customer settings from Database";

    public static final String UPDATE_CUSTOMER_SETTINGS_DESC = "Update customer settings";
    public static final String UPDATE_CUSTOMER_SETTINGS_NOTE =
        "The service will get customer settings from Database";

    public static final String GET_CUSTOMER_ERP_INFO_DESC = "Get customer infomation from ERP";
    public static final String GET_CUSTOMER_ERP_INFO_NOTE =
        "The service will Update customer infomation from ERP";

    public static final String GET_CUSTOMER_LICENSE_DESC = "Gets licenses of customer";
    public static final String GET_CUSTOMER_LICENSE_NOTE =
        "The service will get all licenses of customer";

    public static final String ASSIGN_LICENSE_DESC = "Assign license to customer";
    public static final String ASSIGN_LICENSE_NOTE = "The service will sssign license to customer";

    public static final String UPDATE_LICENSE_DESC = "Update license for customer";
    public static final String UPDATE_LICENSE_NOTE = "The service will update license for customer";

    public static final String GET_LICENSE_DESC = "Get All package";
    public static final String GET_LICENSE_NOTE = "The service will get all available licenses";

    public static final String DELETE_LICENSE_DESC = "Delete license for customer";
    public static final String DELETE_LICENSE_NOTE = "The service will delete license for customer";

    public static final String RETRIEVE_CUSTOMER_API_NOTE =
        "Validate and retrieve customer information";
    public static final String RETRIEVE_CUSTOMER_API_DESC =
        "Validate and etrieve customer information before registering new customer";

    public static final String CREATE_NEW_CUSTOMER_DESC = "Create new customer";
    public static final String CREATE_NEW_CUSTOMER_NOTE = "The service will Create new customer";

    public static final String GET_ORG_COLLECTION_PERMISSION_DESC =
        "Get organisation collection permission base on selected collection";
    public static final String GET_ORG_COLLECTION_PERMISSION_NOTE =
        "The service will get organisation collection permission from database base on selected collection";
  }


  /**
   * Users APIs specification.
   */
  @UtilityClass
  class User {

    public static final String BO_DELETE_DVSE_EXTERNAL_USER_NOTE =
        "Deletes Dvse and external user by id if affiliate is belongs to Austria";
    public static final String BO_DELETE_USER_NOTE =
        "Deactives user login by id to prevent user can login to e-Connect system.";

    public static final String BO_DELETE_DVSE_EXTERNAL_USER_DESC =
        "Delete Dvse and external user for back office";
    public static final String BO_DELETE_USER_DESC = "Delete user for back office";

    public static final String BO_UPDATE_USER_CREDENTIALS_DESC = "Admin update user's password";
    public static final String BO_UPDATE_USER_CREDENTIALS_NOTE =
        "The service will help System Admin update user's password";

    public static final String BO_GET_USER_SETTING_DESC = "Admin get user settings";
    public static final String BO_GET_USER_SETTING_NOTE =
        "The service will help System Admin get user's settings";

    public static final String BO_UPDATE_USER_SETTING_DESC = "Admin get user settings";
    public static final String BO_UPDATE_USER_SETTING_NOTE =
        "The service will help System Admin get user's settings";

    public static final String EXPORT_EXCEL_DESC = "Export user list";
    public static final String EXPORT_EXCEL_NOTE =
        "The service will export user list into excel file";

    public static final String BO_UPDATE_SYSTEM_ADMIN_USER_PASS_DESC =
        "Update system admin password";
    public static final String BO_UPDATE_SYSTEM_ADMIN_USER_PASS_NOTE =
        "The service will help to update system admin password";

    public static final String BO_GET_USER_DETAIL_DESC = "Admin get user details";
    public static final String BO_GET_USER_DETAIL_NOTE =
        "The service will help System Admin get user's detail";

    public static final String BO_SEARCH_USER_DESC = "Search user for back office";
    public static final String BO_SEARCH_USER_NOTE =
        "Search active users, return sortable and pageble results";

    public static final String BO_RESET_SYSTEM_ADMIN_USER_PASS_DESC = "Reset system admin password";
    public static final String BO_RESET_SYSTEM_ADMIN_USER_PASS_NOTE =
        "The service will help to Reset system admin password";

    public static final String BO_RETRIEVE_USER_API_DESC = "Retrieve default user profile information";
    public static final String BO_RETRIEVE_USER_API_NOTE =
        "Retrieve default user profile information before create a new user for the affiliate";

    public static final String BO_CREATE_USER_API_DESC = "System Admin create user";
    public static final String BO_CREATE_USER_API_NOTE = "System admin perform action create user for customer";
  }

  /**
   * Messages APIs specification.
   */
  @UtilityClass
  class Message {
    public static final String UPDATES_NEW_MESSAGE_NOTE = "Update existing message";
    public static final String FIND_MESSAGE_DESC = "This api help to find the message by it's id";
    public static final String FIND_MESSAGE_NOTE = "Find message by id";
    public static final String DELETE_MESSAGE_DESC = "This api help to delete message base on id";
    public static final String DELETE_MESSAGE_NOTE = "Delete message base on id";
    public static final String GET_FILTERING_OPTIONS_MASTER_DATA_FOR_SEARCHING_MESSAGE_DESC =
        "This api help to get filtering options master data for searching message";
    public static final String GET_FILTERING_OPTIONS_MASTER_DATA_FOR_SEARCHING_MESSAGE_NOTE =
        "Get filtering options master data for searching message";
    public static final String SEARCH_MESSAGE_DESC =
        "This api help amdin to search message base on criteria";
    public static final String SEARCH_MESSAGE_NOTE = "Search message base on criteria";
    public static final String GET_MESSGAE_MASTER_DATA_DESC =
        "This api help admin to get messaging master data";
    public static final String GET_MESSAGE_MASTER_DATA_NOTE = "Get master data";
    public static final String CREATES_NEW_MESSAGE_DESC =
        "This api help admin to create new message";
    public static final String CREATES_NEW_MESSAGE_NOTE = "Creates new message";
    public static final String UPDATES_NEW_MESSAGE_DESC = "This api help to update existing message";
  }

  /**
   * Countries APIs specification.
   */
  @UtilityClass
  class Country {
    public static final String GET_COUNTRY_INFO_DESC = "Get country informations";
    public static final String GET_COUNTRY_INFO_NOTE = "The service returns country informations";

    public static final String GET_COUNTRY_INFO_BY_CODE_DESC = "Get country informations by code";
    public static final String GET_COUNTRY_INFO_BY_CODE_NOTE =
        "The service returns country informations by code";

    public static final String GET_COUNTRY_SHORT_INFO_DESC = "Get country short informations";
    public static final String GET_COUNTRY_SHORT_INFO_NOTE =
        "The service returns country short informations";
  }

  /**
   * Opening days calendar APIs specification.
   */
  @UtilityClass
  class OpeningDaysCalendar {
    public static final String GET_WORKING_DAY_CODES_DESC = "Get all working day codes";
    public static final String GET_WORKING_DAY_CODES_NOTE =
        "The service returns all working day codes";

    public static final String CREATE_NEW_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to create new opening days calendar";
    public static final String CREATE_NEW_OPENING_DAYS_CALENDAR_NOTE =
        "Create new opening days calendar";

    public static final String UPDATE_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to update existing opening days calendar";
    public static final String UPDATE_OPENING_DAYS_CALENDAR_NOTE =
        "Update existing opening days calendar";

    public static final String REMOVE_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to remove existing opening days calendar";
    public static final String REMOVE_OPENING_DAYS_CALENDAR_NOTE =
        "Remove existing opening days calendar";

    public static final String GET_OPENING_DAYS_CALENDAR_DETAIL_DESC =
        "This api allow admin to get existing opening days calendar detail by id";
    public static final String GET_OPENING_DAYS_CALENDAR_DETAIL_NOTE =
        "Get existing opening days calendar detail by id";

    public static final String SEARCH_OPENING_DAYS_CALENDAR_DESC =
        "This api allow admin to search existing opening days calendar by criteria";
    public static final String SEARCH_OPENING_DAYS_CALENDAR_NOTE =
        "Search existing opening days calendar by criteria";

    public static final String IMPORT_OPENING_DAYS_CALENDAR_FROM_CSV_DESC =
        "This api allow admin to import opening days calendar from csv file";
    public static final String IMPORT_OPENING_DAYS_CALENDAR_FROM_CSV_NOTE =
        "Import opening days calendar from csv file";

  }

  /**
   * Collection management APIs specification
   */
  @UtilityClass
  class OrganisationCollection {
    public static final String GET_COLLECTION_TEMPLATE_DESC =
        "This api allow admin to get collection creation template";
    public static final String GET_COLLECTION_TEMPLATE_NOTE = "Get template for collection creation";

    public static final String CREATE_COLLECTION_DESC = "This api allow admin to create a collection";
    public static final String CREATE_COLLECTION_NOTE = "Create a collection";

    public static final String GET_COLLECTION_DESC = "This api allow admin to get collection information";

    public static final String GET_COLLECTION_NOTE = "Get collection information for admin";

    public static final String UPDATE_COLLECTION_DESC = "This api allow admin to update a collection";
    public static final String UPDATE_COLLECTION_NOTE = "Update a collection";
    public static final String B0_SEARCH_COLLECTION = "Search collection for back office";
    public static final String B0_SEARCH_COLLECTION_NOTES =
        "This service search collection for back office with condition: affiliate, customer number and collection name";
    public static final String B0_GET_CUSTOMER = "Get customer of collection selected for back office";
    public static final String B0_GET_CUSTOMER_NOTES =
        "This service get customer of collection selected for back office ";
  }

  @UtilityClass
  class AdminFileUpload {
    public static final String UPLOAD_FILE_DESC = "This api allow admin to upload and store a file";
    public static final String UPLOAD_FILE_NOTE = "Admin Upload and store a file";

    public static final String REMOVE_FILE_DESC =
        "This api allow admin to remove a file by filename";

    public static final String REMOVE_FILE_NOTE = "Admin remove a filename";
  }

  class AdminExternalVendor {
    public static final String IMPORT_EXTERNAL_VENDOR =
        "This api allow admin to import external vendor via BO.";
    public static final String IMPORT_EXTERNAL_VENDOR_NOTE = "Import external vendor by csv file.";
    public static final String SEARCH_EXTERNAL_VENDOR =
        "This api allow admin to search external vendor via BO.";
    public static final String SEARCH_EXTERNAL_VENDOR_NOTE = "Search external vendor by csv file.";
    public static final String CREATE_MASTER_DATA_EXTERNAL_VENDOR =
        "This api allow admin to init data for external vendor.";
    public static final String CREATE_MASTER_DATA_EXTERNAL_VENDOR_NOTE =
        "Init data for external vendor.";
    public static final String CREATE_EXTERNAL_VENDOR =
        "This api allow admin to create new external vendor.";
    public static final String CREATE_EXTERNAL_VENDOR_NOTE = "Create new external vendor.";
    public static final String EDIT_EXTERNAL_VENDOR =
        "This api allow admin to edit external vendor.";
    public static final String EDIT_EXTERNAL_VENDOR_NOTE = "Edit the external vendor.";
    public static final String DELETE_EXTERNAL_VENDOR =
        "This api allow admin to delete external vendor.";
    public static final String DELETE_EXTERNAL_VENDOR_NOTE = "Delete the external vendor.";
    public static final String FIND_EXTERNAL_VENDOR =
        "This api allow admin to find external vendor by id.";
    public static final String FIND_EXTERNAL_VENDOR_NOTE = "Find the external vendor by id.";
  }

  class AdminDeliveryProfile {
    public static final String IMPORT_DELIVERY_PROFILE =
        "This api allow admin to import delivery profile via BO.";
    public static final String IMPORT_DELIVERY_PROFILE_NOTE =
        "Import delivery profile by csv file.";
    public static final String SEARCH_DELIVERY_PROFILE =
        "This api allow admin to search delivery profile via BO.";
    public static final String SEARCH_DELIVERY_PROFILE_NOTE =
        "Search delivery profile by csv file.";
    public static final String CREATE_MASTER_DATA_DELIVERY_PROFILE =
        "This api allow admin to init data for delivery profile.";
    public static final String CREATE_MASTER_DATA_DELIVERY_PROFILE_NOTE =
        "Init data for delivery profile.";
    public static final String EDIT_DELIVERY_PROFILE =
        "This api allow admin to edit external vendor.";
    public static final String EDIT_DELIVERY_PROFILE_NOTE = "Edit the delivery profile.";
    public static final String DELETE_DELIVERY_PROFILE =
        "This api allow admin to delete external vendor.";
    public static final String DELETE_DELIVERY_PROFILE_NOTE = "delete the delivery profile by id.";
    public static final String FIND_DELIVERY_PROFILE =
        "This api allow admin to find delivery profile by id.";
    public static final String FIND_DELIVERY_PROFILE_NOTE = "Find the delivery profile by id.";
    public static final String CREATE_DELIVERY_PROFILE =
        "This api allow admin to create new delivery profile.";
    public static final String CREATE_DELIVERY_PROFILE_NOTE = "Create new delivery profile.";
    public static final String FIND_DELIVERY_PROFILE_BY_DELIVERY_PROFILE_ID =
        "This api allow admin to find delivery profile by delivery profile id.";
    public static final String FIND_DELIVERY_PROFILE_BY_DELIVERY_PROFILE_ID_NOTE =
        "Find the delivery profile by delivery profile id.";
  }
}
