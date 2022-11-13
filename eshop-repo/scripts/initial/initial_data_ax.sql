-- NOTE: this script should run at the second time to work perfectly
USE [ax]
GO
-- REMOVE ALL DATA FOR RESET CASES --

DELETE FROM [dbo].[ORGANISATION_GROUP]
DBCC CHECKIDENT('[dbo].[ORGANISATION_GROUP]', RESEED, 0);

DELETE FROM [dbo].[GROUP_USER]
DBCC CHECKIDENT('[dbo].[GROUP_USER]', RESEED, 0);

DELETE FROM [dbo].[GROUP_ROLE]
DBCC CHECKIDENT('[dbo].[GROUP_ROLE]', RESEED, 0);

-- ROLE_PERMISSION TABLE --
DELETE FROM [dbo].[ROLE_PERMISSION]
DBCC CHECKIDENT('[dbo].[ROLE_PERMISSION]', RESEED, 0);

-- PER_FUNCTION TABLE --
DELETE FROM [dbo].[PERM_FUNCTION]
DBCC CHECKIDENT('[dbo].[PERM_FUNCTION]', RESEED, 0);

-- ORGANISATION_ADDRESS TABLE --
DELETE FROM [dbo].[ORGANISATION_ADDRESS]
DBCC CHECKIDENT('[dbo].[ORGANISATION_ADDRESS]', RESEED, 0);

-- ESHOP_PERMISSION TABLE --
DELETE FROM [dbo].[ESHOP_PERMISSION]
DBCC CHECKIDENT('[dbo].[ESHOP_PERMISSION]', RESEED, 0);

DELETE FROM [dbo].[ESHOP_GROUP]
DBCC CHECKIDENT('[dbo].[ESHOP_GROUP]', RESEED, 0);

-- USER_VEHICLE_HISTORY TABLE --
DELETE FROM [dbo].[USER_VEHICLE_HISTORY]
DBCC CHECKIDENT('[dbo].[USER_VEHICLE_HISTORY]', RESEED, 0);

-- LOGIN TABLE --
DELETE FROM [dbo].[LOGIN]
DBCC CHECKIDENT('[dbo].[LOGIN]', RESEED, 0);

-- ESHOP_USER TABLE --
DELETE FROM [dbo].[ESHOP_USER]
DBCC CHECKIDENT('[dbo].[ESHOP_USER]', RESEED, 0);

-- ESHOP_ROLE TABLE --
DELETE FROM [dbo].[ESHOP_ROLE]
DBCC CHECKIDENT('[dbo].[ESHOP_ROLE]', RESEED, 0);

-- ROLE_TYPE TABLE --
DELETE FROM [dbo].[ROLE_TYPE]
DBCC CHECKIDENT('[dbo].[ROLE_TYPE]', RESEED, 0);

-- ESHOP_FUNCTION TABLE --
DELETE FROM [dbo].[ESHOP_FUNCTION]
DBCC CHECKIDENT('[dbo].[ESHOP_FUNCTION]', RESEED, 0);

-- ADDRESS TABLE --
DELETE FROM [dbo].[ADDRESS]
DBCC CHECKIDENT('[dbo].[ADDRESS]', RESEED, 0);

-- ADDRESSTYPE TABLE --
DELETE FROM [dbo].[ADDRESS_TYPE]
DBCC CHECKIDENT('[dbo].[ADDRESS_TYPE]', RESEED, 0);

-- ORGANISATION TABLE --
DELETE FROM [dbo].[ORGANISATION]
DBCC CHECKIDENT('[dbo].[ORGANISATION]', RESEED, 0);

-- ORDER SETTINGS TABLE --
DELETE FROM [dbo].[CUSTOMER_SETTINGS]
DBCC CHECKIDENT('[dbo].[CUSTOMER_SETTINGS]', RESEED, 0);

-- USER SETTINGS TABLE --
DELETE FROM [dbo].[USER_SETTINGS]
DBCC CHECKIDENT('[dbo].[USER_SETTINGS]', RESEED, 0);

-- SETTINGS TABLE --
DELETE FROM [dbo].[ORGANISATION_SETTINGS]
DBCC CHECKIDENT('[dbo].[ORGANISATION_SETTINGS]', RESEED, 0);

-- ORGANISATION_TYPE TABLE --
DELETE FROM [dbo].[ORGANISATION_TYPE]
DBCC CHECKIDENT('[dbo].[ORGANISATION_TYPE]', RESEED, 0);

-- ALLOCATION_TYPE TABLE --
DELETE FROM [dbo].[ALLOCATION_TYPE]
DBCC CHECKIDENT('[dbo].[ALLOCATION_TYPE]', RESEED, 0);

-- PAYMENT_METHOD TABLE --
DELETE FROM [dbo].[PAYMENT_METHOD]
DBCC CHECKIDENT('[dbo].[PAYMENT_METHOD]', RESEED, 0);

-- DELIVERY_TYPE TABLE --
DELETE FROM [dbo].[DELIVERY_TYPE]
DBCC CHECKIDENT('[dbo].[DELIVERY_TYPE]', RESEED, 0);

-- COLLECTIVE_DELIVERY TABLE --
DELETE FROM [dbo].[COLLECTIVE_DELIVERY]
DBCC CHECKIDENT('[dbo].[COLLECTIVE_DELIVERY]', RESEED, 0);

-- INVOICE_TYPE TABLE --
DELETE FROM [dbo].[INVOICE_TYPE]
DBCC CHECKIDENT('[dbo].[INVOICE_TYPE]', RESEED, 0);

-- LANGUAGES TABLE --
DELETE FROM [dbo].[LANGUAGES]
DBCC CHECKIDENT('[dbo].[LANGUAGES]', RESEED, 0);

-- SALUTATION TABLE --
DELETE FROM [dbo].[SALUTATION]
DBCC CHECKIDENT('[dbo].[SALUTATION]', RESEED, 0);

-- VEHICLE_HISTORY TABLE --
DELETE FROM [dbo].[VEHICLE_HISTORY]
DBCC CHECKIDENT('[dbo].[VEHICLE_HISTORY]', RESEED, 0);

-- COUPON CONDITION --
DELETE FROM [dbo].[COUPON_CONDITIONS]
DBCC CHECKIDENT('[dbo].[COUPON_CONDITIONS]', RESEED, 0);

-- VIN LOG TABLE --
DELETE FROM [dbo].[VIN_LOGGING]
DBCC CHECKIDENT('[dbo].[VIN_LOGGING]', RESEED, 0);

-- INSERT DATA TO TABLES --

-- Update additional data for Languages and Salutations
INSERT INTO [dbo].[LANGUAGES] (LANGCODE, LANGISO, DESCRIPTION) VALUES ('LANG_DE','DE','German')
INSERT INTO [dbo].[LANGUAGES] (LANGCODE, LANGISO, DESCRIPTION) VALUES ('LANG_FR','FR','French')
INSERT INTO [dbo].[LANGUAGES] (LANGCODE, LANGISO, DESCRIPTION) VALUES ('LANG_IT','IT','Italian')
--SELECT * FROM [dbo].[LANGUAGES]

-- ADDRESS_TYPE TABLE --
INSERT INTO [dbo].[ADDRESS_TYPE] (TYPE, DESCRIPTION) VALUES ('DEFAULT', 'DEFAULT');
INSERT INTO [dbo].[ADDRESS_TYPE] (TYPE, DESCRIPTION) VALUES ('OFFICE', 'Office');
INSERT INTO [dbo].[ADDRESS_TYPE] (TYPE, DESCRIPTION) VALUES ('BRANCH', 'Branch');
INSERT INTO [dbo].[ADDRESS_TYPE] (TYPE, DESCRIPTION) VALUES ('SHOWROOM', 'Showroom');
INSERT INTO [dbo].[ADDRESS_TYPE] (TYPE, DESCRIPTION) VALUES ('GARAGE', 'Garage');
--SELECT * FROM [dbo].[ADDRESS_TYPE]

-- ORGANISATION_TYPE TABLE --
INSERT INTO [dbo].[ORGANISATION_TYPE] (NAME, LEVEL, DESCRIPTION) VALUES ('SYSTEM', 1, 'This is SAG System organisation');
INSERT INTO [dbo].[ORGANISATION_TYPE] (NAME, LEVEL, DESCRIPTION) VALUES ('AFFILIATE', 2, 'This is Affiliate organisation');
INSERT INTO [dbo].[ORGANISATION_TYPE] (NAME, LEVEL, DESCRIPTION) VALUES ('CUSTOMER', 3, 'This is Customer organisation');
--SELECT * FROM [dbo].[ORGANISATION_TYPE];

-- ORDER SETTINGS TABLE --
INSERT INTO [dbo].ALLOCATION_TYPE (DESC_CODE, TYPE, DESCRIPTION) VALUES ('ALLOCATION_TYPE1', 'Verrechnung gemäss Vereinbarung', 'Verrechnung gemäss Vereinbarung');
INSERT INTO [dbo].ALLOCATION_TYPE (DESC_CODE, TYPE, DESCRIPTION) VALUES ('ALLOCATION_TYPE2', 'Verrechnung als Einzelfaktura', 'Verrechnung als Einzelfaktura');
--SELECT * FROM ALLOCATION_TYPE

INSERT INTO [dbo].PAYMENT_METHOD (DESC_CODE, PAY_METHOD, DESCRIPTION) VALUES ('CREDIT', 'Rechnung', 'Rechnung');
INSERT INTO [dbo].PAYMENT_METHOD (DESC_CODE, PAY_METHOD, DESCRIPTION) VALUES ('CASH', 'Barzahlung', 'Barzahlung');
--SELECT * FROM PAYMENT_METHOD
INSERT INTO [dbo].COLLECTIVE_DELIVERY (DESC_CODE, TYPE, DESCRIPTION) VALUES ('COLLECTIVE_DELIVERY1', 'Ich benötige die verfügbare Ware dringend. Bitte senden Sie mir die verfügbaren Artikel schnellst möglich.', 'Ich benötige die verfügbare Ware dringend. Bitte senden Sie mir die verfügbaren Artikel schnellst möglich.');
INSERT INTO [dbo].COLLECTIVE_DELIVERY (DESC_CODE, TYPE, DESCRIPTION) VALUES ('COLLECTIVE_DELIVERY2', 'Die Ware wird nicht dringend benötigt. Bitte senden Sie mir die Ware in einer Lieferung, sobald sie verfügbar ist.', 'Die Ware wird nicht dringend benötigt. Bitte senden Sie mir die Ware in einer Lieferung, sobald sie verfügbar ist.');
--SELECT * FROM PAYMENT_METHOD

-- ROLE_TYPE TABLE --
INSERT INTO [dbo].[ROLE_TYPE] (NAME, DESCRIPTION) VALUES ('ADMIN', 'Administrator role, including sag admin, group admin, user admin');
INSERT INTO [dbo].[ROLE_TYPE] (NAME, DESCRIPTION) VALUES ('ASSISTANT', 'Assistant role, including marketing and sales');
INSERT INTO [dbo].[ROLE_TYPE] (NAME, DESCRIPTION) VALUES ('NORMAL_USER', 'Normal user role, including customer and its employees');
--SELECT * FROM [dbo].[ROLE_TYPE]

-- ESHOP_ROLE TABLE --
INSERT INTO [dbo].[ESHOP_ROLE] (NAME, ROLE_TYPE_ID, DESCRIPTION) VALUES ('SYSTEM_ADMIN', 1, 'SAG System admin role');
INSERT INTO [dbo].[ESHOP_ROLE] (NAME, ROLE_TYPE_ID, DESCRIPTION) VALUES ('GROUP_ADMIN', 1, 'Group admin role');
INSERT INTO [dbo].[ESHOP_ROLE] (NAME, ROLE_TYPE_ID, DESCRIPTION) VALUES ('USER_ADMIN', 1, 'User admin role');
INSERT INTO [dbo].[ESHOP_ROLE] (NAME, ROLE_TYPE_ID, DESCRIPTION) VALUES ('SALES_ASSISTANT', 2, 'Sales assistant role');
INSERT INTO [dbo].[ESHOP_ROLE] (NAME, ROLE_TYPE_ID, DESCRIPTION) VALUES ('MARKETING_ASSISTANT', 2, 'Marketing assistant role');
INSERT INTO [dbo].[ESHOP_ROLE] (NAME, ROLE_TYPE_ID, DESCRIPTION) VALUES ('NORMAL_USER', 3, 'User Role');
--SELECT * FROM [dbo].[ESHOP_ROLE]

-- CUSTOMER_SETTINGS TABLE --
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
--SELECT * FROM CUSTOMER_SETTINGS

-- ORGANISATION TABLE --
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID) 
VALUES ('Swiss-Automotive-Group', '000000', 1, 0, 'This is Swiss Automotive Group organisation', 'sag', 1);
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID) 
VALUES ('Derendinger-Austria', '300000', 2, 1, 'This is Derendinger Austria organisation', 'derendinger-at', 2);
--SELECT * FROM [dbo].[ORGANISATION]

-- ADDRESS TABLE -- need to improve this
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 1);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 2);
--SELECT * FROM [dbo].[ADDRESS]

-- ORGANISATION_ADDRESS TABLE --
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (1, 1);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (2, 2);
--SELECT * FROM [dbo].[ORGANISATION_ADDRESS]

-- ESHOP_GROUP TABLE --
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('SYSTEM_ADMIN' , 'SAG System admin group', '000000', 1);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('DERENDINGER_AT_ADMIN' , 'Admin group for derendinger affiliate', '300000', 2);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('SALES_ASSISTANT' , 'Sales assistant group', '000000', 4);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('MARKETING_ASSISTANT' , 'Marketing group', '000000', 5);
--SELECT * FROM ESHOP_GROUP

--SELECT * FROM [dbo].[ESHOP_GROUP]

-- ESHOP_PERMISSION TABLE --
-- Permission for system admin
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageSystemSettings' , 'Create, update, delete system settings', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageAffiliates' , 'Create, update, deactivate affiliates', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageAffiliatesPermissions' , 'Update affiliates permissions', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageSystemPermissions' , 'Create, update, delete permissions', 'admin');
-- Permission for group admin
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageAffiliatesSettings' , 'Update affiliates settings', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageCustomers' , 'Create, update, deactive garages', 'admin'); -- not sure, supported?
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageCustomersPermissions' , 'Create, update, delete garages permissions', 'admin');
-- Permission for user admin
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageCustomersSettings' , 'Update garages settings', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageUsers' , 'Create, update, deactive employees of garage', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageUsersPermissions' , 'Create, update, delete employees of garage permissions', 'admin');
-- Permission for sales assistants
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageOrders' , 'Create, update, delete, search orders,...', 'admin');
-- Permission for marketing assistants
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageUploads' , 'Upload coupons, upload clusters,...', 'admin');
-- Permission for normal user, employees of garages
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageUsersSettings' , 'Update garages employees settings', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('OrderParts' , 'Search parts, add parts to shopping baskets, view baskets, view orders,...', 'admin');
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY) VALUES ('ManageMyOrders' , 'Create, update search orders,...', 'admin');
--SELECT * FROM [dbo].[ESHOP_PERMISSION]

-- ESHOP_FUNCTION TABLE --
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('CreateSettings' , 'Create settings', '/admin/settings/create');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateSettings' , 'Update settings', '/admin/settings/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeleteSettings' , 'Delete settings', '/admin/settings/delete');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('CreateAffiliate' , 'Create Affiliate', '/admin/affiliates/create');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateAffiliate' , 'Update Affiliate', '/admin/affiliates/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeactiveAffiliate' , 'De-active Affiliate', '/admin/affiliates/deactive');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateAffiliatesPermissions' , 'Update affiliates permissions', '/admin/affiliates/permissions/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('CreatePermissions' , 'Create permissions', '/admin/permissions/create');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdatePermissions' , 'Update permissions', '/admin/permissions/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeletePermissions' , 'Delete permissions', '/admin/permissions/delete');

INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('CreateCustomer' , 'Create the garage', '/affiliate/customers/create');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateCustomer' , 'Update the garage', '/affiliate/customers/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeactiveCustomer' , 'De-active the garage', '/affiliate/customers/deactive');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateCustomersPermissions' , 'Update garages permissions', '/affiliate/customers/permissions/update');

INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('CreateNormalUser' , 'Create the garage employee', '/customer/users/create');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateNormalUser' , 'Update the garage employee', '/customer/users/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeactiveNormalUser' , 'De-active the garage employee', '/customer/users/deactive');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateNormalUsersPermissions' , 'Update the garage employee permissions', '/customer/users/permissions/update');

INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('SearchOrders' , 'Search the orders', '/search/orders');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('ViewOrder' , 'View the order', '/order/view');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('CreateOrder' , 'Create the order', '/order/create');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UpdateOrder' , 'Update the order', '/order/update');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeactiveOrder' , 'Deactive the order', '/order/deactive');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('DeletePermanentOrder' , 'Delete permanent the order', '/order/delete');

INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UploadCoupons' , 'Upload the coupons', '/marketing/coupons/upload');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('UploadClusters' , 'Upload the clusters', '/marketing/clusters/upload');

INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('SearchParts' , 'Search parts', '/search/parts');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('ViewPartDetails' , 'View part details', '/part/view');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('AddPartToShoppingBasket' , 'Add part to shopping basket', '/part/addToCart');
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('ViewShoppingBasket' , 'View shopping basket', '/cart/view');
--SELECT * FROM [dbo].[ESHOP_FUNCTION]

-- INSERT VALUES INTO ORGANISATION_SETTINGS TABLE--
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'logo_image', '../images/logo/logo-tech.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'default_logo', '../images/logo/logo-tech.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'background_image', '../images/background/bg-tech.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'default_background', '../images/background/bg-tech.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'description', 'Default-Settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'theme_color_1', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'theme_color_2', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'font_color_1', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'font_color_2', 'white');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'back_to_top_arrow_color', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'footer_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'footer_text_color', 'white');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'button_color', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'button_text_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'section_arrow_hover_color', 'darkgray');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'information_icon_color', '#787878'); 
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'carousel_background', '../images/carousel-arrows-technomag.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'ico_cart', '../images/ico-cart-technomag.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'login_image', '../images/login-technomag.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'pre_loader', '../images/logo/TS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'default_email', '');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
	VALUES ('Default-Settings', 1, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only
-- INSERT SETTINGS FOR Derendinger-Austria affiliate ---
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'logo_image', '../images/logo/logo-der.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'default_logo', '../images/logo/logo-der.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'background_image', '../images/background/bg-der.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'default_background', '../images/background/bg-der.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'description', 'Derendinger-Switzerland settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'theme_color_1', '#0073be'); 
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'theme_color_2', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'font_color_1', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'back_to_top_arrow_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'footer_color', '#c0d1e3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'footer_text_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'button_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'button_text_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'information_icon_color', '#787878'); 
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'login_image', '../images/login-derenginger.jpg'); -- on DEV environment, just for test only    
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'default_email', 'shop@derendinger.at');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Derendinger-Austria', 2, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only 
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
	VALUES ('Derendinger-Austria', 2, 'title', 'D-store Austria');
--SELECT * FROM [dbo].[ORGANISATION_SETTINGS]

-------------------------------------------------------------------------------
-- USER_SETTINGS
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);

-- ESHOP_USER TABLE --
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Cletus','Regnier','admin@sagag.com','admin','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Harlan','Vizcaino','admin@technomag.ch','technomag-at1','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Paris','Rafter','admin@derendinger.at','derendinger-at2','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Aiko','Hinkel','marketing@sagag.com','marketing','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales@sagag.com','sales','123456789', 1, 1, 11, 1, 1, 1, 1);
--SELECT * FROM [dbo].[ESHOP_USER]

-- LOGIN TABLE --
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 2);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 3);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 4);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 5);
--SELECT * FROM [dbo].[LOGIN]

-------------------------------------------------------------------------------
-- SYSTEM_ADMIN
-- Assign organisation Swiss-Automotive-Group to Group SYSTEM_ADMIN
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (1,1);
-- Assign User admin@sagag.com, to Group SYSTEM_ADMIN
INSERT INTO GROUP_USER (GROUP_ID, USER_ID) VALUES (1,1);
-- Assign Role SYSTEM_ADMIN to Group SYSTEM_ADMIN
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (1,1);
-- Assign role SYSTEM_ADMIN to permissions:
----> ManageSystemSettings, 
----> ManageAffiliates, 
----> ManageAffiliatesPermissions, 
----> ManageSystemPermissions
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (1,1);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (1,2);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (1,3);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (1,4);
-- Assign permission ManageSystemSettings to functions:
----> CreateSettings
----> UpdateSettings
----> DeleteSettings
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (1, 1);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (1, 2);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (1, 3);

-- Assign permission ManageAffiliates to functions:
----> CreateAffiliate
----> UpdateAffiliate
----> DeactiveAffiliate
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (2, 4);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (2, 5);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (2, 6);

-- Assign permission ManageAffiliatesPermissions to functions:
----> UpdateAffiliatesPermissions
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (3, 7);

-- Assign permission ManageSystemPermissions to functions:
----> CreatePermissions
----> UpdatePermissions
----> DeletePermissions
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (4, 8);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (4, 9);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (4, 10);

-------------------------------------------------------------------------------
-- GROUP_ADMIN
-- Assign Group DERENDINGER_ADMIN to organisation Derendinger-Austria
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (2,2);
-- Assign Users derendinger-at1 and derendinger-at2 to Group DERENDINGER_AT_ADMIN
INSERT INTO GROUP_USER (GROUP_ID, USER_ID) VALUES (2,2);
INSERT INTO GROUP_USER (GROUP_ID, USER_ID) VALUES (2,3);
-- Assign group DERENDINGER_ADMIN to Role GROUP_ADMIN
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (2,2);
-- Assign role GROUP_ADMIN to permissions:
----> ManageAffiliatesSettings, 
----> ManageCustomers, 
----> ManageCustomersPermissions,
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (2,5);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (2,6);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (2,7);

-- Assign permission ManageAffiliatesSettings to functions:
----> UpdateSettings
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (5, 2);
-- Assign permission ManageCustomers to functions:
----> CreateCustomer
----> UpdateCustomer
----> DeactiveCustomer
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (6, 11);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (6, 12);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (6, 13);
-- Assign permission ManageCustomersPermissions to functions:
----> UpdateCustomersPermissions
-- note that the affiliate also can create permissions for its customers -> permission: ManageSystemPermissions
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (7, 8);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (7, 9);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (7, 10);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (7, 14);

-------------------------------------------------------------------------------
-- SALES_ASSISTANT
-- Assign Group SALES_ASSISTANT to organisation Derendinger-Austria
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (3,2);
-- Assign user sales to Group SALES_ASSISTANT
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (5,3);
-- Assign Group SALES_ASSISTANT to Role SALES_ASSISTANT
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (3,4);
-- Assign role SALES_ASSISTANT to permissions:
----> ManageOrders, 
----> OrderParts 
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (4,11);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (4,14);
-- Assign permission ManageOrders to functions:
----> SearchOrders
----> ViewOrder
----> CreateOrder
----> UpdateOrder
----> DeactiveOrder
----> DeletePermanentOrder
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (11, 19);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (11, 20);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (11, 21);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (11, 22);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (11, 23);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (11, 24);
-- Assign permission OrderParts to functions:
----> SearchParts
----> ViewPartDetails
----> AddPartToShoppingBasket
----> ViewShoppingBasket
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (14, 27);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (14, 28);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (14, 29);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (14, 30);

-------------------------------------------------------------------------------
-- MARKETING_ASSISTANT
-- Assign Group MARKETING_ASSISTANT to organisation Derendinger-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (4,2);
-- Assign user marketing to Group MARKETING_ASSISTANT
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (5,4);
-- Assign Group MARKETING_ASSISTANT to Role MARKETING_ASSISTANT 
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (4,5);
-- Assign role MARKETING_ASSISTANT to permissions:
----> ManageUploads, 
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (5,12);
-- Assign permission ManageUploads to functions:
----> UploadCoupons
----> UploadClusters
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (12, 25);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (12, 26);

-- Address Salutation
-- Attention: This is data of AX enumeration. Just input here as the ticket work.
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_DR', 'Dr.');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_I', 'I');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_II', 'II');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_III', 'III');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_JR', 'Jr.');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_MISS', 'Miss');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_MR', 'Mr');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_MRS', 'Mrs.');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_MS', 'Ms.');
--INSERT INTO SALUTATION(CODE, DESCRIPTION) VALUES ('SALUTATION_SR', 'Sr.');
--SELECT * FROM SALUTATION

-- Invoice Type
UPDATE INVOICE_TYPE set INVOICE_TYPE_NAME = 'EINZELFAKT', INVOICE_TYPE_DESC = 'Fakturierung pro Auftrag' WHERE INVOICE_TYPE_CODE = 'SINGLE_INVOICE';
UPDATE INVOICE_TYPE set INVOICE_TYPE_NAME = '2WFAKT ', INVOICE_TYPE_DESC = '2 Wochen Faktura mit Gutschriftstrennung' WHERE INVOICE_TYPE_CODE = 'TWO_WEEKLY_INVOICE';
UPDATE INVOICE_TYPE set INVOICE_TYPE_NAME = 'WOCHENFAKT', INVOICE_TYPE_DESC = 'Wochenfaktura' WHERE INVOICE_TYPE_CODE = 'WEEKLY_INVOICE';
UPDATE INVOICE_TYPE set INVOICE_TYPE_NAME = 'TAGESFAKT', INVOICE_TYPE_DESC = 'Tagesfaktura' WHERE INVOICE_TYPE_CODE = 'DAILY_INVOICE';
UPDATE INVOICE_TYPE set INVOICE_TYPE_NAME = 'MONATSFAKT', INVOICE_TYPE_DESC = 'Monatsfaktura' WHERE INVOICE_TYPE_CODE = 'MONTHLY_INVOICE';
INSERT INTO INVOICE_TYPE(INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION', '2WFAKTGT', '2 Wochen Faktura mit Gutschriftstrennung');
INSERT INTO INVOICE_TYPE(INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('SINGLE_INVOICE_WITH_CREDIT_SEPARATION', 'EINZELFAGT', 'Fakturierung pro Auftrag mit Gutschriftstennung');
INSERT INTO INVOICE_TYPE(INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('MONTHLY_INVOICE_WITH_CREDIT_SEPARATION', 'MONATSFAGT', 'Monatsfaktura mit Gutschriftstrennung');
INSERT INTO INVOICE_TYPE(INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('DAILY_INVOICE_WITH_CREDIT_SEPARATION', 'TAGESFAGT', 'Tagesfaktura mit Gutschriftstrennung');
INSERT INTO INVOICE_TYPE(INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('WEEKLY_INVOICE_WITH_CREDIT_SEPARATION', 'WOCHENFAGT', 'Wochenfaktura mit Gutschriftstrennung');

--SELECT * FROM INVOICE_TYPE

-- Payment Type 
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Bar', 'Barzahlung, petty cash');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Betreibung', 'Betreibung, operation');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Fin', 'Finanzierung, financing');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Karte', 'Kartenzahlung, card payment');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Keine', 'Keine, none');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sak', 'Schuldanerkennung, acknowledgment of debt');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Selbstzahl', 'Selbstzahler, self-payer');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sepa B Fin', 'Sepa B Finanzierung, financing');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sepa B Sak', 'Sepa B Schuldanerkennung, acknowledgment of debt');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sepa B2B', 'Sepa B2B');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sepa C Fin', 'Sepa C Finanzierung, financing');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sepa C Sak', 'Sepa C Schuldanerkennung, acknowledgment of debt');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sepa Core', 'Sepa Core (inkl. Cor1)');
INSERT INTO PAYMENT_TYPE(CODE, DESCRIPTION) VALUES ('Sofort', 'Sofort fällige Rechnung, immediate due invoice');

-- Delivery Type
-- Attention: This is data of AX enumeration. Just input here as the ticket work.
--INSERT INTO DELIVERY_TYPE(TYPE, DESC_CODE, DESCRIPTION) VALUES ('ABH', 'Abholung durch den Kunden', 'Abholung durch den Kunden');
--INSERT INTO DELIVERY_TYPE(TYPE, DESC_CODE, DESCRIPTION) VALUES ('GL', 'Kundenbelieferung durch Gebietsleiter', 'Kundenbelieferung durch Gebietsleiter');
--INSERT INTO DELIVERY_TYPE(TYPE, DESC_CODE, DESCRIPTION) VALUES ('MONT', 'Kundenbelieferung durch Monteur', 'Kundenbelieferung durch Monteur');
--INSERT INTO DELIVERY_TYPE(TYPE, DESC_CODE, DESCRIPTION) VALUES ('SPED', 'Kundenbelieferung durch Spedition', 'Kundenbelieferung durch Spedition');
--INSERT INTO DELIVERY_TYPE(TYPE, DESC_CODE, DESCRIPTION) VALUES ('TOUR', 'Kundenbelieferung durch Auslieferungstour', 'Kundenbelieferung durch Auslieferungstour');
--SELECT * FROM DELIVERY_TYPE

-- Order Type
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('PRICE_INQUIRY', 'ANF', 'Preisanfrage');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('STANDING_ORDER', 'DAU', 'Daueraufträge');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('SALES_INVESTMENTS', 'IGN', 'Investitionsgüter (Verkauf)');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('REPAIR_INVESTMENTS', 'IGR', 'Investitionsgüter (Reparatur)');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('SERVICE_INVESTMENTS', 'IGS', 'Investitionsgüter (Service)');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('PURCHASE_CONTRACT', 'KAU', 'Kaufvertrag');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('RETURNS', 'RET', 'Retoure');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('REAR_SALE_SUPPLIER', 'RVL', 'Rückverkauf-Lieferant');
INSERT INTO ORDER_TYPE(ORDER_TYPE, DESC_CODE, DESCRIPTION) VALUES ('STANDARD', 'STD', 'Standard (Tour/Abholung)');

-- State
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('B', 'Burgenland');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('K', 'Kärnten');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('N', 'Niederösterreich');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('O', 'Oberösterreich');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('Sa', 'Salzburg');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('St', 'Steiermark');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('T', 'Tirol');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('V', 'Voralberg');
INSERT INTO STATE(CODE, DESCRIPTION) VALUES ('W', 'Wien');

-- Country
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ABW', 'Aruba', 'Aruba');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AFG', 'Afghanistan', 'Afghanistan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AGO', 'Angola', 'Angola');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AIA', 'Anguilla', 'Anguilla');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ALA', 'Åland Islands', 'Åland Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ALB', 'Albania', 'Albania');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AND', 'Andorra', 'Andorra');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ANT', 'Netherlands Antilles', 'Netherlands Antilles');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ARE', 'United Arab Emirates', 'United Arab Emirates');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ARG', 'Argentina', 'Argentina');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ARM', 'Armenia', 'Armenia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ASM', 'American Samoa', 'American Samoa');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ATA', 'Antarctica', 'Antarctica');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ATF', 'French Southern and Antarctic Lands', 'French Southern and Antarctic Lands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ATG', 'Antigua and Barbuda', 'Antigua and Barbuda');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AUS', 'Australia', 'Australia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AUT', 'Austria', 'Austria');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('AZE', 'Azerbaijan', 'Azerbaijan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BDI', 'Burundi', 'Burundi');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BEL', 'Belgium', 'Belgium');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BEN', 'Benin', 'Benin');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BFA', 'Burkina Faso', 'Burkina Faso');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BGD', 'Bangladesh', 'Bangladesh');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BGR', 'Bulgaria', 'Bulgaria');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BHR', 'Bahrain', 'Bahrain');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BHS', 'Bahamas', 'Bahamas');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BIH', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BLM', 'Saint Barthélemy', 'Saint Barthélemy');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BLR', 'Belarus', 'Belarus');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BLZ', 'Belize', 'Belize');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BMU', 'Bermuda', 'Bermuda');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BOL', 'Bolivia', 'Bolivia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BRA', 'Brazil', 'Brazil');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BRB', 'Barbados', 'Barbados');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BRN', 'Brunei', 'Brunei');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BTN', 'Bhutan', 'Bhutan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BVT', 'Bouvet Island', 'Bouvet Island');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('BWA', 'Botswana', 'Botswana');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CAF', 'Central African Republic', 'Central African Republic');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CAN', 'Canada', 'Canada');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CCK', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CHE', 'Switzerland', 'Switzerland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CHL', 'Chile', 'Chile');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CHN', 'China', 'China');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CIV', 'Côte d’Ivoire', 'Côte d’Ivoire');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CMR', 'Cameroon', 'Cameroon');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('COD', 'Congo (DRC)', 'Congo (DRC)');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('COG', 'Congo', 'Congo');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('COK', 'Cook Islands', 'Cook Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('COL', 'Colombia', 'Colombia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('COM', 'Comoros', 'Comoros');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CPV', 'Cape Verde', 'Cape Verde');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CRI', 'Costa Rica', 'Costa Rica');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CUB', 'Cuba', 'Cuba');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CXR', 'Christmas Island', 'Christmas Island');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CYM', 'Cayman Islands', 'Cayman Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CYP', 'Cyprus', 'Cyprus');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('CZE', 'Czech Republic', 'Czech Republic');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('DEU', 'Germany', 'Germany');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('DJI', 'Djibouti', 'Djibouti');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('DMA', 'Dominica', 'Dominica');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('DNK', 'Denmark', 'Denmark');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('DOM', 'Dominican Republic', 'Dominican Republic');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('DZA', 'Algeria', 'Algeria');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ECU', 'Ecuador', 'Ecuador');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('EGY', 'Egypt', 'Egypt');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ERI', 'Eritrea', 'Eritrea');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ESP', 'Spain', 'Spain');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('EST', 'Estonia', 'Estonia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ETH', 'Ethiopia', 'Ethiopia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('FIN', 'Finland', 'Finland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('FJI', 'Fiji', 'Fiji');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('FLK', 'Falkland Islands (Islas Malvinas)', 'Falkland Islands (Islas Malvinas)');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('FRA', 'France', 'France');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('FRO', 'Faroe Islands', 'Faroe Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('FSM', 'Micronesia', 'Micronesia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GAB', 'Gabon', 'Gabon');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GBR', 'United Kingdom', 'United Kingdom');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GEO', 'Georgia', 'Georgia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GGY', 'Guernsey', 'Guernsey');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GHA', 'Ghana', 'Ghana');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GIB', 'Gibraltar', 'Gibraltar');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GIN', 'Guinea', 'Guinea');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GLP', 'Guadeloupe', 'Guadeloupe');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GMB', 'Gambia', 'Gambia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GNB', 'Guinea-Bissau', 'Guinea-Bissau');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GNQ', 'Equatorial Guinea', 'Equatorial Guinea');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GRC', 'Greece', 'Greece');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GRD', 'Grenada', 'Grenada');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GRL', 'Greenland', 'Greenland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GTM', 'Guatemala', 'Guatemala');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GUF', 'French Guiana', 'French Guiana');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GUM', 'Guam', 'Guam');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('GUY', 'Guyana', 'Guyana');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('HKG', 'Hong Kong SAR', 'Hong Kong SAR');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('HMD', 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('HND', 'Honduras', 'Honduras');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('HRV', 'Croatia', 'Croatia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('HTI', 'Haiti', 'Haiti');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('HUN', 'Hungary', 'Hungary');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IDN', 'Indonesia', 'Indonesia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IMN', 'Isle of Man', 'Isle of Man');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IND', 'India', 'India');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IOT', 'British Indian Ocean Territory', 'British Indian Ocean Territory');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IRL', 'Ireland', 'Ireland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IRN', 'Iran', 'Iran');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('IRQ', 'Iraq', 'Iraq');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ISL', 'Iceland', 'Iceland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ISR', 'Israel', 'Israel');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ITA', 'Italy', 'Italy');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('JAM', 'Jamaica', 'Jamaica');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('JEY', 'Jersey', 'Jersey');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('JOR', 'Jordan', 'Jordan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('JPN', 'Japan', 'Japan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KAZ', 'Kazakhstan', 'Kazakhstan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KEN', 'Kenya', 'Kenya');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KGZ', 'Kyrgyzstan', 'Kyrgyzstan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KHM', 'Cambodia', 'Cambodia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KIR', 'Kiribati', 'Kiribati');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KNA', 'Saint Kitts and Nevis', 'Saint Kitts and Nevis');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KOR', 'Korea', 'Korea');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('KWT', 'Kuwait', 'Kuwait');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LAO', 'Laos', 'Laos');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LBN', 'Lebanon', 'Lebanon');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LBR', 'Liberia', 'Liberia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LBY', 'Libya', 'Libya');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LCA', 'Saint Lucia', 'Saint Lucia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LIE', 'Liechtenstein', 'Liechtenstein');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LKA', 'Sri Lanka', 'Sri Lanka');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LSO', 'Lesotho', 'Lesotho');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LTU', 'Lithuania', 'Lithuania');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LUX', 'Luxembourg', 'Luxembourg');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('LVA', 'Latvia', 'Latvia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MAC', 'Macao SAR', 'Macao SAR');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MAF', 'Saint Martin', 'Saint Martin');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MAR', 'Morocco', 'Morocco');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MCO', 'Monaco', 'Monaco');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MDA', 'Moldova', 'Moldova');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MDG', 'Madagascar', 'Madagascar');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MDV', 'Maldives', 'Maldives');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MEX', 'Mexico', 'Mexico');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MHL', 'Marshall Islands', 'Marshall Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MKD', 'Macedonia', 'Macedonia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MLI', 'Mali', 'Mali');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MLT', 'Malta', 'Malta');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MMR', 'Myanmar', 'Myanmar');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MNE', 'Montenegro', 'Montenegro');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MNG', 'Mongolia', 'Mongolia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MNP', 'Northern Mariana Islands', 'Northern Mariana Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MOZ', 'Mozambique', 'Mozambique');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MRT', 'Mauritania', 'Mauritania');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MSR', 'Montserrat', 'Montserrat');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MTQ', 'Martinique', 'Martinique');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MUS', 'Mauritius', 'Mauritius');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MWI', 'Malawi', 'Malawi');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MYS', 'Malaysia', 'Malaysia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('MYT', 'Mayotte', 'Mayotte');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NAM', 'Namibia', 'Namibia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NCL', 'New Caledonia', 'New Caledonia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NER', 'Niger', 'Niger');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NFK', 'Norfolk Island', 'Norfolk Island');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NGA', 'Nigeria', 'Nigeria');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NIC', 'Nicaragua', 'Nicaragua');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NIU', 'Niue', 'Niue');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NLD', 'Netherlands', 'Netherlands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NOR', 'Norway', 'Norway');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NPL', 'Nepal', 'Nepal');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NRU', 'Nauru', 'Nauru');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('NZL', 'New Zealand', 'New Zealand');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('OMN', 'Oman', 'Oman');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PAK', 'Pakistan', 'Pakistan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PAN', 'Panama', 'Panama');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PCN', 'Pitcairn Islands', 'Pitcairn Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PER', 'Peru', 'Peru');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PHL', 'Philippines', 'Philippines');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PLW', 'Palau', 'Palau');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PNG', 'Papua New Guinea', 'Papua New Guinea');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('POL', 'Poland', 'Poland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PRI', 'Puerto Rico', 'Puerto Rico');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PRK', 'North Korea', 'North Korea');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PRT', 'Portugal', 'Portugal');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PRY', 'Paraguay', 'Paraguay');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PSE', 'Palestinian Authority', 'Palestinian Authority');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('PYF', 'French Polynesia', 'French Polynesia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('QAT', 'Qatar', 'Qatar');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('REU', 'Reunion', 'Reunion');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ROU', 'Romania', 'Romania');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('RUS', 'Russia', 'Russia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('RWA', 'Rwanda', 'Rwanda');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SAU', 'Saudi Arabia', 'Saudi Arabia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SDN', 'Sudan', 'Sudan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SEN', 'Senegal', 'Senegal');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SGP', 'Singapore', 'Singapore');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SGS', 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SHN', 'Saint Helena', 'Saint Helena');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SJM', 'Svalbard and Jan Mayen Island', 'Svalbard and Jan Mayen Island');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SLB', 'Solomon Islands', 'Solomon Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SLE', 'Sierra Leone', 'Sierra Leone');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SLV', 'El Salvador', 'El Salvador');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SMR', 'San Marino', 'San Marino');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SOM', 'Somalia', 'Somalia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SPM', 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SRB', 'Serbia', 'Serbia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('STP', 'São Tomé and Príncipe', 'São Tomé and Príncipe');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SUR', 'Suriname', 'Suriname');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SVK', 'Slovakia', 'Slovakia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SVN', 'Slovenia', 'Slovenia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SWE', 'Sweden', 'Sweden');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SWZ', 'Swaziland', 'Swaziland');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SYC', 'Seychelles', 'Seychelles');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('SYR', 'Syria', 'Syria');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TCA', 'Turks and Caicos Islands', 'Turks and Caicos Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TCD', 'Chad', 'Chad');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TGO', 'Togo', 'Togo');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('THA', 'Thailand', 'Thailand');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TJK', 'Tajikistan', 'Tajikistan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TKL', 'Tokelau', 'Tokelau');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TKM', 'Turkmenistan', 'Turkmenistan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TLS', 'Timor-Leste', 'Timor-Leste');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TON', 'Tonga', 'Tonga');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TTO', 'Trinidad and Tobago', 'Trinidad and Tobago');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TUN', 'Tunisia', 'Tunisia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TUR', 'Turkey', 'Turkey');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TUV', 'Tuvalu', 'Tuvalu');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TWN', 'Taiwan', 'Taiwan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('TZA', 'Tanzania', 'Tanzania');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('UGA', 'Uganda', 'Uganda');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('UKR', 'Ukraine', 'Ukraine');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('UMI', 'U.S. Minor Outlying Islands', 'U.S. Minor Outlying Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('URY', 'Uruguay', 'Uruguay');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('USA', 'United States', 'United States');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('UZB', 'Uzbekistan', 'Uzbekistan');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VAT', 'Vatican City', 'Vatican City');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VCT', 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VEN', 'Venezuela', 'Venezuela');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VGB', 'British Virgin Islands', 'British Virgin Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VIR', 'Virgin Islands', 'Virgin Islands');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VNM', 'Vietnam', 'Vietnam');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('VUT', 'Vanuatu', 'Vanuatu');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('WLF', 'Wallis and Futuna', 'Wallis and Futuna');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('WSM', 'Samoa', 'Samoa');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('YEM', 'Yemen', 'Yemen');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ZAF', 'South Africa', 'South Africa');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ZMB', 'Zambia', 'Zambia');
INSERT INTO COUNTRY(CODE, SHORT_NAME, FULL_NAME) VALUES ('ZWE', 'Zimbabwe', 'Zimbabwe');



INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales01@sagag.com','sales01','123456789', 1, 1, 11, 1, 1, 8, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 8);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (8,3);


INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales02@sagag.com','sales02','123456789', 1, 1, 11, 1, 1, 9, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 9);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (9,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales03@sagag.com','sales03','123456789', 1, 1, 11, 1, 1, 10, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 10);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (10,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales04@sagag.com','sales04','123456789', 1, 1, 11, 1, 1, 11, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 11);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (11,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales05@sagag.com','sales05','123456789', 1, 1, 11, 1, 1, 12, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 12);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (12,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales06@sagag.com','sales06','123456789', 1, 1, 11, 1, 1, 13, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 13);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (13,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales07@sagag.com','sales07','123456789', 1, 1, 11, 1, 1, 14, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 14);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (14,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales08@sagag.com','sales08','123456789', 1, 1, 11, 1, 1, 15, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 15);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (15,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales09@sagag.com','sales09','123456789', 1, 1, 11, 1, 1, 16, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 16);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (16,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales10@sagag.com','sales10','123456789', 1, 1, 11, 1, 1, 17, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 17);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (17,3);


INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales11@sagag.com','sales11','123456789', 1, 1, 11, 1, 1, 18, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 18);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (18,3);


INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales12@sagag.com','sales12','123456789', 1, 1, 11, 1, 1, 19, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 19);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (19,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales13@sagag.com','sales13','123456789', 1, 1, 11, 1, 1, 20, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 20);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (20,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales14@sagag.com','sales14','123456789', 1, 1, 11, 1, 1, 21, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 21);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (21,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales15@sagag.com','sales15','123456789', 1, 1, 11, 1, 1, 22, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 22);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (22,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales16@sagag.com','sales16','123456789', 1, 1, 11, 1, 1, 23, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 23);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (23,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales17@sagag.com','sales17','123456789', 1, 1, 11, 1, 1, 24, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 24);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (24,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales18@sagag.com','sales18','123456789', 1, 1, 11, 1, 1, 25, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 25);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (25,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales19@sagag.com','sales19','123456789', 1, 1, 11, 1, 1, 26, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 26);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (26,3);

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales20@sagag.com','sales20','123456789', 1, 1, 11, 1, 1, 27, 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 27);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (27,3);

-- FEATURE SSO, assign AX user to AX SALES group in eConnect to allow Login & search customer 
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (17,3);


-- Add more payment method for only sales
INSERT INTO [dbo].PAYMENT_METHOD (DESC_CODE, PAY_METHOD, DESCRIPTION) VALUES ('CARD', 'Kartenzahlung', 'Kartenzahlung');
INSERT INTO [dbo].PAYMENT_METHOD (DESC_CODE, PAY_METHOD, DESCRIPTION) VALUES ('DIRECT_INVOICE', 'Sofortrechnung', 'Sofortrechnung');
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (17,3);

UPDATE ESHOP_USER SET EMAIL = 'adriano.sinigoi@derendinger.at' WHERE ID = 5;
UPDATE ESHOP_USER SET EMAIL = 'albert.salfinger@derendinger.at' WHERE ID = 46;
UPDATE ESHOP_USER SET EMAIL = 'adreas.kaltenbrunner@derendinger.at' WHERE ID = 125;

-- Create new affiliate Matik-Austria
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID) VALUES 
('Matik-Austria', '400000', 2, 1, 'This is Matik Austria organisation', 'matik-at', NULL);

-- INSERT SETTINGS FOR Derendinger-Austria affiliate ---
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'logo_image', '../images/logo/logo-matik.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'default_logo', '../images/logo/logo-matik.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'background_image', '../images/background/bg-matik.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'default_background', '../images/background/bg-matik.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'description', 'Matik-Austria settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'theme_color_1', '#6087a0'); 
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'theme_color_2', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'font_color_1', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'back_to_top_arrow_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'footer_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'footer_text_color', '#fff');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'button_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'button_text_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'information_icon_color', '#787878'); 
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'login_image', '../images/login-derenginger.jpg'); -- on DEV environment, just for test only    
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'default_email', 'shop@derendinger.at');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
    VALUES ('Matik-Austria', 24777, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 24777, 'title', 'Matik Austria'); 

-- SUPPORTED AFFILIATE Initilization
INSERT INTO [dbo].[SUPPORTED_AFFILIATE] (SHORT_NAME, COMPANY_NAME, COUNTRY_ID) VALUES
('derendinger-ch', 'Derendinger-Switzerland', 42),
('derendinger-at', 'Derendinger-Austria', 17),
('technomag', 'Technomag-Switzerland', 42),
('matik-ch', 'Matik-Switzerland', 42),
('matik-at', 'Matik-Austria', 17),
('wbb', 'Walchli-Bollier-Bulach', 42);

-- SUPPORTED AFFILIATE Update Data more columns
UPDATE SUPPORTED_AFFILIATE set ES_SHORT_NAME = 'dch', SALES_ORIGIN_ID = 'eshop-dch' WHERE SHORT_NAME = 'derendinger-ch';
UPDATE SUPPORTED_AFFILIATE set ES_SHORT_NAME = 'dat', SALES_ORIGIN_ID = 'eshop-dat' WHERE SHORT_NAME = 'derendinger-at';
UPDATE SUPPORTED_AFFILIATE set ES_SHORT_NAME = 'tm', SALES_ORIGIN_ID = 'eshop-tm' WHERE SHORT_NAME = 'technomag';
UPDATE SUPPORTED_AFFILIATE set ES_SHORT_NAME = 'mch', SALES_ORIGIN_ID = 'eshop-mch' WHERE SHORT_NAME = 'matik-ch';
UPDATE SUPPORTED_AFFILIATE set ES_SHORT_NAME = 'mat', SALES_ORIGIN_ID = 'eshop-mat' WHERE SHORT_NAME = 'matik-at';
UPDATE SUPPORTED_AFFILIATE set ES_SHORT_NAME = 'wb', SALES_ORIGIN_ID = 'eshop-wbb' WHERE SHORT_NAME = 'wbb';

-- Add one more Invoice type for MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION
INSERT INTO INVOICE_TYPE(INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION', 'MONATSFAGW', '');

-- Update SALUTATION
INSERT INTO dbo.SALUTATION (CODE, DESCRIPTION, TYPE) VALUES ('GENERAL_SALUTATION_COMPANY','Company','OFFER')
INSERT INTO dbo.SALUTATION (CODE, DESCRIPTION, TYPE) VALUES ('GENERAL_SALUTATION_MALE','Male','OFFER')
INSERT INTO dbo.SALUTATION (CODE, DESCRIPTION, TYPE) VALUES ('GENERAL_SALUTATION_FEMALE','Female','OFFER')
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 1
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 2
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 3
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 4
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 5
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 6
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 7
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 8;
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 9;
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 10;
UPDATE dbo.SALUTATION SET TYPE = 'PROFILE' WHERE ID = 11;

--- START #2153: update permission
INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY, MODIFIED_BY)
  VALUES ('ManageOfferModule' , 'Permission to manage the offer', 8, 8); -- ID for test : 18
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL)
  VALUES ('SearchOffer' , 'Search the offer', '/offer/search'); -- ID for test : 33

-- Initial SUPPORTED_AFFILIATE
UPDATE dbo.SUPPORTED_AFFILIATE SET SHOW_PFAND_ARTICLE = 1;
UPDATE dbo.SUPPORTED_AFFILIATE SET SHOW_PFAND_ARTICLE = 0 WHERE SHORT_NAME IN ('derendinger-ch', 'matik-ch');

-- Add remco affiliate
INSERT INTO [dbo].[SUPPORTED_AFFILIATE] (SHORT_NAME, COMPANY_NAME, COUNTRY_ID, ES_SHORT_NAME, SALES_ORIGIN_ID)
  VALUES ('rbe' , 'Remco-Belgium', 20, 'rbe', 'eshop-rbe');
  
-- Update logo link for supported affiliate
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-der.png' WHERE SHORT_NAME = 'derendinger-ch'
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-der-at.png' WHERE SHORT_NAME = 'derendinger-at'
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-tech.png' WHERE SHORT_NAME = 'technomag'
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-matik.png' WHERE SHORT_NAME = 'matik-ch'
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-matik-at.png' WHERE SHORT_NAME = 'matik-at'
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-wbb.jpg' WHERE SHORT_NAME = 'wbb'
UPDATE dbo.SUPPORTED_AFFILIATE SET LOGO_LINK = 'public/images/logo/logo-rbe.png' WHERE SHORT_NAME = 'rbe'


