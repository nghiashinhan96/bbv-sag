-- NOTE: this script should run at the second time to work perfectly
USE [sag]
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

INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_MR', 'Mr.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_MRS', 'Mrs.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_MS', 'Ms.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_DR', 'Dr.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_PROF', 'Prof.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_SIR', 'Sir.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_MADAM', 'Madame.')
INSERT INTO [dbo].[SALUTATION] (CODE, DESCRIPTION) VALUES ('SALUTATION_OTHER', 'Other')
--SELECT * FROM SALUTATION

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
INSERT INTO [dbo].DELIVERY_TYPE (DESC_CODE, TYPE, DESCRIPTION) VALUES ('PICKUP', 'Abholung in Filiale', 'Abholung in Filiale');
INSERT INTO [dbo].DELIVERY_TYPE (DESC_CODE, TYPE, DESCRIPTION) VALUES ('TOUR', 'Lieferung gemäss Tourenplan', 'Lieferung gemäss Tourenplan');
SELECT * FROM DELIVERY_TYPE
INSERT INTO [dbo].PAYMENT_METHOD (DESC_CODE, PAY_METHOD, DESCRIPTION) VALUES ('CREDIT', 'Rechnung', 'Rechnung');
INSERT INTO [dbo].PAYMENT_METHOD (DESC_CODE, PAY_METHOD, DESCRIPTION) VALUES ('CASH', 'Barzahlung', 'Barzahlung');
--SELECT * FROM PAYMENT_METHOD
INSERT INTO [dbo].COLLECTIVE_DELIVERY (DESC_CODE, TYPE, DESCRIPTION) VALUES ('COLLECTIVE_DELIVERY1', 'Ich benötige die verfügbare Ware dringend. Bitte senden Sie mir die verfügbaren Artikel schnellst möglich.', 'Ich benötige die verfügbare Ware dringend. Bitte senden Sie mir die verfügbaren Artikel schnellst möglich.');
INSERT INTO [dbo].COLLECTIVE_DELIVERY (DESC_CODE, TYPE, DESCRIPTION) VALUES ('COLLECTIVE_DELIVERY2', 'Die Ware wird nicht dringend benötigt. Bitte senden Sie mir die Ware in einer Lieferung, sobald sie verfügbar ist.', 'Die Ware wird nicht dringend benötigt. Bitte senden Sie mir die Ware in einer Lieferung, sobald sie verfügbar ist.');
--SELECT * FROM PAYMENT_METHOD
INSERT INTO [dbo].INVOICE_TYPE (INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('SINGLE_INVOICE', 'EINZELFAKT', 'Einzelfaktura');
INSERT INTO [dbo].INVOICE_TYPE (INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('TWO_WEEKLY_INVOICE', '2WSAMFAKT', '2 Wochensammel');
INSERT INTO [dbo].INVOICE_TYPE (INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('WEEKLY_INVOICE', 'WSAMFAKT', 'Wochensammel');
INSERT INTO [dbo].INVOICE_TYPE (INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('DAILY_INVOICE', 'TAGSAMFAKT', 'Tagessammel');
INSERT INTO [dbo].INVOICE_TYPE (INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('ACCUMULATIVE_INVOICE', 'ALLESAM', 'Alle Sammelfakturen');
INSERT INTO [dbo].INVOICE_TYPE (INVOICE_TYPE_CODE, INVOICE_TYPE_NAME, INVOICE_TYPE_DESC) VALUES ('MONTHLY_INVOICE', '1MSAMFAKT', 'Monatssammel');
--SELECT * FROM INVOICE_TYPE

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

-- ORGANISATION TABLE --
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Swiss-Automotive-Group', '000000', 1, 0, 'This is Swiss Automotive Group organisation', 'sag', NULL);

-- Prepare for Affiliates
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Technomag-Switzerland', '100000', 2, 1, 'This is Technomag organisation', 'technomag', NULL);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Derendinger-Switzerland', '200000', 2, 1, 'This is Derendinger organisation', 'derendinger-ch', NULL);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Derendinger-Austria', '300000', 2, 1, 'This is Derendinger Austria organisation', 'derendinger-at', NULL);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Matik-Austria', '400000', 2, 1, 'This is Matik Austria organisation', 'matik-at', NULL);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Walchli-Bollier-Bulach', '500000', 2, 1, 'This is Walchli Bollier Bulach organisation', 'wbb', NULL);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Remco-Belgium', '600000', 2, 1, 'This is Remco Belgium organisation', 'rbe', NULL);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Matik-Switzerland', '700000', 2, 1, 'This is Matik Switzerland organisation', 'matik-ch', NULL);

-- Affiliate settings
-- INSERT VALUES INTO ORGANISATION_SETTINGS TABLE--
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Swiss-Automotive-Group', 1, 'title', 'SAG');

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
    VALUES ('Default-Settings', 1, 'default_email', 'info@sag-ag.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Default-Settings', 1, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Default-Settings', 1, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Default-Settings', 1, 'show_tyres_gross_price_header', 'true');

-- Insert settings for Derendinger-Switzerland affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'logo_image', '../images/logo/logo-der.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'default_logo', '../images/logo/logo-der.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'background_image', '../images/background/bg-der.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'default_background', '../images/background/bg-der.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'description', 'Derendinger-Switzerland settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'theme_color_1', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'theme_color_2', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'font_color_1', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'back_to_top_arrow_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'footer_color', '#c0d1e3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'footer_text_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'button_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'button_text_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'login_image', '../images/login-derenginger.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'default_email', 'shop@derendinger.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'show_tyres_gross_price_header', 'true');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'default_url', 'https://d-store.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'default_vat_rate', '8');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'title', 'D-store');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Switzerland', 2, 'payment_additional_credit_direct_invoice', '0');

-- Insert settings for Technomag-Switzerland affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'logo_image', '../images/logo/logo-tech.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'default_logo', '../images/logo/logo-tech.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'background_image', '../images/background/bg-tech.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'default_background', '../images/background/bg-tech.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'description', 'Technomag-Switzerland settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'theme_color_1', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'theme_color_2', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'font_color_1', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'font_color_2', 'white');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'back_to_top_arrow_color', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'footer_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'footer_text_color', 'white');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'button_color', '#e3000b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'button_text_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'section_arrow_hover_color', 'darkgray');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'carousel_background', '../images/carousel-arrows-technomag.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'ico_cart', '../images/ico-cart-technomag.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'login_image', '../images/login-technomag.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'pre_loader', '../images/logo/TS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'default_email', 'eshop@technomag.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'show_tyres_gross_price_header', 'true');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'default_url', 'https://techno-store.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'default_vat_rate', '8');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'title', 'Techno-store');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Technomag-Switzerland', 3, 'payment_additional_credit_direct_invoice', '0');

-- Insert settings for Derendinger-Austria affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'logo_image', '../images/logo/logo-der.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'default_logo', '../images/logo/logo-der.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'background_image', '../images/background/bg-der.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'default_background', '../images/background/bg-der.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'description', 'Derendinger-Switzerland settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'theme_color_1', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'theme_color_2', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'font_color_1', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'back_to_top_arrow_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'footer_color', '#c0d1e3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'footer_text_color', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'button_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'button_text_color', '#0073be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'login_image', '../images/login-derenginger.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'default_email', 'shop@derendinger.at');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'show_tyres_gross_price_header', 'true');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'title', 'D-store Austria');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'default_vat_rate', 20);
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Derendinger-Austria', 4, 'payment_additional_credit_direct_invoice', '1000');

-- Insert settings for Matik-Switzerland affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'logo_image', '../images/logo/logo-mch.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'default_logo', '../images/logo/logo-mch.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'background_image', '../images/background/bg-mch.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'default_background', '../images/background/bg-mch.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'description', 'Matik-Switzerland settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'theme_color_1', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'theme_color_2', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'font_color_1', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'back_to_top_arrow_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'footer_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'footer_text_color', '#fff');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'button_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'button_text_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'login_image', '../images/login-derenginger.jpg'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'default_email', 'info@matik.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'title', 'Matik AG');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
  VALUES ('Matik-Switzerland', 138, 'default_url', 'https://www.matik.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'default_vat_rate', '8');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Switzerland', 138, 'payment_additional_credit_direct_invoice', '0');

-- Insert settings for Matik-Austria affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'logo_image', '../images/logo/logo-matik.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'default_logo', '../images/logo/logo-matik.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'background_image', '../images/background/bg-matik.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'default_background', '../images/background/bg-matik.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'description', 'Matik-Switzerland settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'theme_color_1', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'theme_color_2', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'font_color_1', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'back_to_top_arrow_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'footer_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'footer_text_color', '#fff');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'button_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'button_text_color', '#6087a0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'login_image', '../images/login-derenginger.jpg'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'default_email', 'info@matik.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'title', 'Matik AG');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
  VALUES ('Matik-Austria', 11, 'default_url', 'https://www.matik.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'default_vat_rate', '20');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Matik-Austria', 11, 'payment_additional_credit_direct_invoice', '0');

-- Insert settings for Walchli-Bollier-Bulach affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'logo_image', '../images/logo/logo-wbb.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'default_logo', '../images/logo/logo-wbb.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'background_image', '../images/background/bg-wbb.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'default_background', '../images/background/bg-wbb.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'description', 'Walchli-Bollier-Bulach settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'theme_color_1', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'theme_color_2', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'font_color_1', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'back_to_top_arrow_color', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'footer_color', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'footer_text_color', '#fff');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'button_color', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'button_text_color', '#0052A0');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'login_image', '../images/login-derenginger.jpg'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'default_email', 'shop@derendinger.ch');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'title', 'Walchli Bollier Bulach');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'default_vat_rate', '8');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Walchli-Bollier-Bulach', 135, 'payment_additional_credit_direct_invoice', '0');

-- Insert settings for Remco-Belgium affiliate --
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'logo_image', '../images/logo/logo-rbe.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'default_logo', '../images/logo/logo-rbe.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'background_image', '../images/background/bg-rbe.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'default_background', '../images/background/bg-rbe.jpg');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'description', 'Remco-Belgium settings');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'theme_color_1', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'theme_color_2', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'font_color_1', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'font_color_2', 'black');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'back_to_top_arrow_color', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'footer_color', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'footer_text_color', '#fff');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'button_color', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'button_text_color', '#ac353b');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'slide_show_paging_color', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'slide_show_paging_color_inactive', 'blue');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'button_cart_color', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'section_arrow_hover_color', '#666666');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'availability_status_color_1', '#f6a60c');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'availability_status_color_2', '#20bc5d');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'availability_status_color_3', 'red');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'information_icon_color', '#787878');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'vin_notification_color', '#339900');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'carousel_background', '../images/carousel-arrows-derenginger.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'ico_cart', '../images/ico-cart-derenginger.png');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'login_image', '../images/login-derenginger.jpg'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'pre_loader', '../images/logo/DS-Loader_small.gif');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'default_email', 'info@remco.be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'google_analytics_tracking_code', 'UA-84230083-3');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'show_tyres_discount', 'false');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'show_tyres_gross_price_header', 'true'); -- on DEV environment, just for test only
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'title', 'Remco');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE) 
  VALUES ('Remco-Belgium', 136, 'default_url', 'https://shop.remco.be');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'default_vat_rate', '8');
INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
    VALUES ('Remco-Belgium', 136, 'payment_additional_credit_direct_invoice', '0');

-- It has been decided that we will take the % VAT rate from the eShop and not AX currently.
-- Therefore, we need an Affiliate Setting for "defaultVATRate"
-- This setting is just for AX: currently we have Matik-Austria and Derendinger-Austria
-- TODO: In the future: if any affiliate that uses ax resource it should be added that affiliate vat rate
-- set default-vat for Matik-Austria organisation
--INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
--    VALUES ('Matik-Austria', 22, 'default_vat_rate', 20);
-- set default-vat for Derendinger-Austria organisation
--INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
--    VALUES ('Derendinger-Austria', 2, 'default_vat_rate', 20);

-- Add PAYMENT_ADDITIONAL_CREDIT_LIMIT_DIRECT_INVOICE derendinger at ax
--INSERT INTO [dbo].[ORGANISATION_SETTINGS] (NAME, ORGANISATION_ID, SETTING_KEY, SETTING_VALUE)
--SELECT 'Derendinger-Austria', ID, 'payment_additional_credit_direct_invoice', 1000
--FROM ORGANISATION WHERE NAME = 'Derendinger-Austria';

-- CUSTOMER_SETTINGS TABLE --
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
SELECT * FROM CUSTOMER_SETTINGS

-- Prepare for Demo Customer
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Garage-A', '4130675', 3, 3, 'This is Garage A belongs to Technomag', 'customer-4130675', 4);
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Garage-B', '4130641', 3, 3, 'This is Garage B belongs to Technomag', 'customer-4130641', 5);
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
 VALUES ('Garage-C', '469743', 3, 2, 'This is Garage C belongs to Derendinger', 'customer-469743', 6);
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID)
VALUES ('Garage-D', '318088', 3, 2, 'This is Garage D belongs to Derendinger', 'customer-318088', 7);
--SELECT * FROM [dbo].[ORGANISATION]


-- ADDRESS TABLE -- need to improve this
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 1);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 2);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 3);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 4);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 5);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 1);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 2);
--SELECT * FROM [dbo].[ADDRESS]

-- ORGANISATION_ADDRESS TABLE --
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (1, 1);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (2, 2);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (3, 3);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (4, 4);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (5, 5);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (6, 6);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (7, 7);
--SELECT * FROM [dbo].[ORGANISATION_ADDRESS]

-- ESHOP_GROUP TABLE --
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('SYSTEM_ADMIN' , 'SAG System admin group', '000000', 1);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('TECHNOMAG_ADMIN' , 'Admin group for technomag affiliate', '100000', 2);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('DERENDINGER_ADMIN' , 'Admin group for derendinger affiliate', '200000', 3);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('SALES_ASSISTANT' , 'Sales assistant group', '000000', 4);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('MARKETING_ASSISTANT' , 'Marketing group', '000000', 5);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_A_USER_ADMIN' , 'User admin group of Garage A', '4130675', 3);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_B_USER_ADMIN' , 'User admin group of Garage B', '4130641', 3);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_C_USER_ADMIN' , 'User admin group of Garage C', '469743', 3);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_D_USER_ADMIN' , 'User admin group of Garage D', '4408511', 3);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_A_NORMAL_USER' , 'Normal user group of Garage A','4130675', 6);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_B_NORMAL_USER' , 'Normal user group of Garage B', '4130641', 6);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_C_NORMAL_USER' , 'Normal user group of Garage C', '469743', 6);
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION, ORG_CODE, ROLE_ID) VALUES ('GARAGE_D_NORMAL_USER' , 'Normal user group of Garage D', '4408511', 6);
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

INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (1, 2, 1, 2);
INSERT INTO [dbo].[USER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_METHOD, COLLECTIVE_DELIVERY_ID) VALUES (2, 1, 2, 1);
--SELECT * FROM [USER_SETTINGS]

-- ESHOP_USER TABLE --
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Cletus','Regnier','admin@sagag.com','admin','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Harlan','Vizcaino','admin@technomag.ch','technomag','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Paris','Rafter','admin@derendinger.ch','derendinger','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Aiko','Hinkel','marketing@sagag.com','marketing','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Hallie','Blanchette','sales@sagag.com','sales','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Douglas','Franklin','ga.admin@garage-a.ch','ga.admin','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Steve','Parker','gb.admin@garage-b.ch','gb.admin','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'David','Mason','gc.admin@garage-c.ch','gc.admin','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Graig','Standley','gd.admin@garage-d.ch','gd.admin','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Charles','Woods','nu1.ga@garage-a.ch','nu1.ga','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Jeffrey','Murphy','nu2.ga@garage-a.ch','nu2.ga','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Arthur','Robinson','nu1.gb@garage-b.ch','nu1.gb','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Carl','Daniel','nu1.gc@garage-c.ch','nu1.gc','123456789', 1, 1, 11, 1, 1, 1, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, VAT_CONFIRM)
     VALUES(1,'Randy','Barnes','nu1.gd@garage-d.ch','nu1.gd','123456789', 1, 1, 11, 1, 1, 1, 1);
--SELECT * FROM [dbo].[ESHOP_USER]

-- LOGIN TABLE --
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 1);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 2);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 3);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 4);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 5);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 6);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 7);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 8);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 9);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 10);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 11);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 12);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 13);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 14);
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
-- Assign Group TECHNOMAG_ADMIN to organisation Technomag-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (2,3);
-- Assign Group DERENDINGER_ADMIN to organisation Derendinger-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (3,2);
-- Assign Users technomag to Group TECHNOMAG_ADMIN, derendinger to Group DERENDINGER_ADMIN
INSERT INTO GROUP_USER (GROUP_ID, USER_ID) VALUES (2,2);
INSERT INTO GROUP_USER (GROUP_ID, USER_ID) VALUES (3,3);
-- Assign group TECHNOMAG_ADMIN to Role GROUP_ADMIN
-- Assign group DERENDINGER_ADMIN to Role GROUP_ADMIN
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (2,2);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (3,2);
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
-- USER_ADMIN
-- Assign Group GARAGE_A_USER_ADMIN to organisation Garage-A
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (6,4);
-- Assign Group GARAGE_B_USER_ADMIN to organisation Garage-B
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (7,5);
-- Assign Group GARAGE_C_USER_ADMIN to organisation Garage-C
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (8,6);
-- Assign Group GARAGE_D_USER_ADMIN to organisation Garage-D
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (9,7); -- NOT WORK! select * from organisation
-- Assign Users ga.admin to group GARAGE_A_USER_ADMIN,
-- user gb.admin to group GARAGE_B_USER_ADMIN,
-- user gc.admin to group GARAGE_C_USER_ADMIN,
-- user gd.admin to group GARAGE_D_USER_ADMIN
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (6, 6);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (7, 7);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (8, 8);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (9, 9);
-- Assign group GARAGE_A_USER_ADMIN to Role USER_ADMIN
-- Assign group GARAGE_B_USER_ADMIN to Role USER_ADMIN
-- Assign group GARAGE_C_USER_ADMIN to Role USER_ADMIN
-- Assign group GARAGE_D_USER_ADMIN to Role USER_ADMIN
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (6,3);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (7,3);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (8,3);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (9,3);
-- Assign role USER_ADMIN to permissions:
----> ManageCustomersSettings,
----> ManageUsers,
----> ManageUsersPermissions,
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (3,8);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (3,9);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (3,10);
-- Assign permission ManageCustomersSettings to functions:
----> UpdateSettings
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (8, 2);
-- Assign permission ManageUsers to functions:
----> CreateNormalUser
----> UpdateNormalUser
----> DeactiveNormalUser
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (9, 15);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (9, 16);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (9, 17);
-- Assign permission ManageUsersPermissions to functions:
----> UpdateNormalUsersPermissions
-- note that the customer also can create permissions for its employees -> permission: ManageSystemPermissions
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (10, 8);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (10, 9);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (10, 10);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (10, 18);

-------------------------------------------------------------------------------
-- SALES_ASSISTANT
-- Assign Group SALES_ASSISTANT to organisation Technomag-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (4,3);
-- Assign Group SALES_ASSISTANT to organisation Derendinger-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (4,2);
-- Assign user sales to Group SALES_ASSISTANT
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (5,4);
-- Assign Group SALES_ASSISTANT to Role SALES_ASSISTANT
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (4,4);
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
-- Assign Group MARKETING_ASSISTANT to organisation Technomag-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (5,3);
-- Assign Group MARKETING_ASSISTANT to organisation Derendinger-Switzerland
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (5,2);
-- Assign user marketing to Group MARKETING_ASSISTANT
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (4,5);
-- Assign Group MARKETING_ASSISTANT to Role MARKETING_ASSISTANT
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (5,5);
-- Assign role MARKETING_ASSISTANT to permissions:
----> ManageUploads,
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (5,12);
-- Assign permission ManageUploads to functions:
----> UploadCoupons
----> UploadClusters
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (12, 25);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (12, 26);

-------------------------------------------------------------------------------
-- NORMAL_USER
-- Assign Group GARAGE_A_NORMAL_USER to organisation Garage-A
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (10,4);
-- Assign Group GARAGE_B_NORMAL_USER to organisation Garage-B
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (11,5);
-- Assign Group GARAGE_C_NORMAL_USER to organisation Garage-C
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (12,6);
-- Assign Group GARAGE_D_NORMAL_USER to organisation Garage-D
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (13,7);
-- Assign Users nu1.ga, nu2.ga to Group GARAGE_A_NORMAL_USER
-- user nu1.gb to Group GARAGE_B_NORMAL_USER,
-- user nu1.gc to Group GARAGE_C_NORMAL_USER,
-- user nu1.gd to Group GARAGE_D_NORMAL_USER
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (10, 10);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (11, 10);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (12, 11);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (13, 12);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (14, 13);
-- Assign Group GARAGE_A_NORMAL_USER to Role NORMAL_USER
-- Assign Group GARAGE_B_NORMAL_USER to Role NORMAL_USER
-- Assign Group GARAGE_C_NORMAL_USER to Role NORMAL_USER
-- Assign Group GARAGE_D_NORMAL_USER to Role NORMAL_USER
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (10,6);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (11,6);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (12,6);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (13,6);
-- Assign role NORMAL_USER to permissions:
----> ManageUsersSettings,
----> OrderParts
----> ManageMyOrders
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (6,13);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (6,14);
INSERT INTO ROLE_PERMISSION (ROLE_ID, PERM_ID) VALUES (6,15);
-- Assign permission ManageUsersSettings to functions:
----> UpdateSettings
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (13, 2);
-- Assign permission OrderParts to functions:
----> SearchParts
----> ViewPartDetails
----> AddPartToShoppingBasket
----> ViewShoppingBasket
-->> this permission has been mapped to function from sales assistant, line 410

-- Assign permission ManageMyOrders to functions:
----> SearchOrders
----> ViewOrder
----> CreateOrder
----> UpdateOrder
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (15, 19);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (15, 20);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (15, 21);
INSERT INTO PERM_FUNCTION (PERM_ID, FUNCTION_ID) VALUES (15, 22);

-- Additional customers
-- Insert more test garage and users for Derendinger-ch and Techmomag
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_ID, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);
INSERT INTO [dbo].[CUSTOMER_SETTINGS] (ALLOCATION_ID, DELIVERY_ID, PAYMENT_ID, COLLECTIVE_DELIVERY_ID) VALUES (1, 1, 1, 1);

INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, SETTINGS_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID) VALUES ('Garage-dt.ch', '469737', 3, 2, 8, 'This is Garage-dt.ch belongs to Derendinger', 'garage-dt.ch', 8);
INSERT INTO [dbo].[ORGANISATION] (NAME, ORG_CODE, ORGTYPE_ID, PARENT_ID, SETTINGS_ID, DESCRIPTION, SHORTNAME, ORDER_SETTINGS_ID) VALUES ('Garage-tt.ch', '4408511', 3, 3, 9, 'This is Garage-tt.ch belongs to Technomag', 'garage-tt.ch', 9);

INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 1);
INSERT INTO [dbo].[ADDRESS] (LINE1, LINE2, LINE3, COUNTRYISO, STATE, CITY, ZIPCODE, ADDRESS_TYPE_ID) VALUES ('Line 1', 'Line 2', 'Line 3', 'CHE', 'State', 'City', '999999', 2);

INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (8, 8);
INSERT INTO [dbo].[ORGANISATION_ADDRESS] (ORGANISATION_ID, ADDRESS_ID) VALUES (9, 9);

INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
 VALUES(1,'Lila_A','Karlou_A','gdt.admin@garage-dt.ch','gdt.admin','123456789', 1, 1, 11, 1, 1, 1, 40);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
 VALUES(1,'Lila_U','Karlou_U','nu1.gdt@garage-dt.ch','nu1.gdt','123456789', 1, 1, 11, 1, 1, 1, 36);

INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
 VALUES(1,'Lila_A','Karlou_A','gtt.admin@garage-tt.ch','gtt.admin','123456789', 1, 1, 11, 1, 1, 1, 40);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
 VALUES(1,'Lila_U','Karlou_U','nu1.gtt@garage-tt.ch','nu1.gtt','123456789', 1, 1, 11, 1, 1, 1, 36);

INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 15);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 16);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 17);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 18);

INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION) VALUES ('GARAGE-DT.CH_USER_ADMIN' , 'User admin group of GARAGE-DT.CH');
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION) VALUES ('GARAGE-TT.CH_USER_ADMIN' , 'User admin group of GARAGE-TT.CH');
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION) VALUES ('GARAGE-DT.CH_NORMAL_USER' , 'Normal user group of GARAGE-DT.CH');
INSERT INTO [dbo].[ESHOP_GROUP] (NAME, DESCRIPTION) VALUES ('GARAGE-TT.CH_NORMAL_USER' , 'Normal user group of GARAGE-TT.CH');

INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (14,8);
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (16,8);
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (15,9);
INSERT INTO ORGANISATION_GROUP (GROUP_ID, ORGANISATION_ID) VALUES (17,9);

INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (14,3);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (15,3);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (16,6);
INSERT INTO GROUP_ROLE (GROUP_ID, ROLE_ID) VALUES (17,6);

INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (15, 14);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (16, 16);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (17, 15);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (18, 17);

-- Insert new user for For reset password test cases
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
VALUES(1,'Randy','Barnes','thi.nguyen@swissitbridge.com','testmail.normal_user','123456789', 1, 1, 11, 1, 1, 1, 50);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
VALUES(1,'Randy','Barnes','thi.nguyen@swissitbridge.com','testmail.sales_assisstant','123456789', 1, 1, 11, 1, 1, 1, 50);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
VALUES(1,'Randy','Barnes','thi.nguyen@swissitbridge.com','testmail.group_admin','123456789', 1, 1, 11, 1, 1, 1, 50);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE)
VALUES(1,'Randy','Barnes','thi.nguyen@swissitbridge.com','testmail.user_admin','123456789', 1, 1, 11, 1, 1, 1, 50);

INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 19);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 20);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 21);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 22);

INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (19, 10);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (20, 4);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (21, 2);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (22, 6);



-- Initial vehicle history
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V2712M9400', 'HYUNDAI', 'ACCENT Kasten (X-2)', 'HYUNDAI ACCENT Kasten (X-2) i 62 kW G4DJ', 'HYUNDAI ACCENT Kasten (X-2) i 62 kW G4DJ', 'HYUNDAI ACCENT Kasten (X-2) i 62 kW G4DJ','1N4BL2EP7AC112978')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V55598M28035', 'AUDI', 'A3 Sportback (8VA, 8VF)', '2.0 TDI quattro 135 kW CUNA', 'AUDI A3 Sportback (8VA, 8VF) 2.0 TDI quattro 135 kW CUNA', 'AUDI A3 Sportback (8VA, 8VF) 2.0 TDI quattro 135 kW CUNA','WBAPH5G59ANM35638')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V57408M33424', 'CHEVROLET', 'MALIBU (V300)', '1.6 T 135 kW LLU', 'CHEVROLET MALIBU (V300) 1.6 T 135 kW LLU', 'CHEVROLET MALIBU (V300) 1.6 T 135 kW LLU','3GNBABDB5AS585804')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V21643M21832', 'CADILLAC', 'ALLANTE Coupe', '4.5 150 kW LQ6', 'CADILLAC ALLANTE Coupe 4.5 150 kW LQ6', 'CADILLAC ALLANTE Coupe 4.5 150 kW LQ6','JTDBU4EE1AJ077058')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V119512M0', 'ABARTH', '124 Spider (348_)', '1.4 125 kW RFJ (EW10A)', 'ABARTH 124 Spider (348_) 1.4 125 kW RFJ (EW10A)', 'ABARTH 124 Spider (348_) 1.4 125 kW RFJ (EW10A)', '2G1FK1EJXA9191258')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V59048M28240', 'ALFA ROMEO', '4C (960_)', '1.8 TBi 177 kW 960 A1.000', 'ALFA ROMEO 4C (960_) 1.8 TBi 177 kW 960 A1.000', 'ALFA ROMEO 4C (960_) 1.8 TBi 177 kW 960 A1.000','WVWMP7AN6AE545558')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V49187M3775', 'TOYOTA', 'CAMRY Stufenheck (CV1_, _V1_)', '2.0 71 kW 2S-ELC', 'TOYOTA CAMRY Stufenheck (CV1_, _V1_) 2.0 71 kW 2S-ELC', 'TOYOTA CAMRY Stufenheck (CV1_, _V1_) 2.0 71 kW 2S-ELC', '19XFA1F5XAE006834')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V25577M0' ,'CHEVROLET', 'ESTATE Kombi', '2.0 88 kW F 20 D2', 'CHEVROLET ESTATE Kombi 2.0 88 kW F 20 D2', 'CHEVROLET ESTATE Kombi 2.0 88 kW F 20 D2' ,'1N4BL2EP7AC112978')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V18763M16516' ,'AUDI', 'A8 (4E_)', '3.0 160 kW BBJ', 'AUDI A8 (4E_) 3.0 160 kW BBJ', 'AUDI A8 (4E_) 3.0 160 kW BBJ' ,'1G1JD69P7FK129418')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V25287M2500' ,'NISSAN', 'SKYLINE (R32)', '2.0 Turbo 4x4 158 kW RB20DET', 'NISSAN SKYLINE (R32) 2.0 Turbo 4x4 158 kW RB20DET', 'NISSAN SKYLINE (R32) 2.0 Turbo 4x4 158 kW RB20DET' ,'1N4BL2EP7AC112978')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V25254M4715', 'NISSAN', 'CEFIRO Stufenheck', '2.4 86 kW Z24i', 'NISSAN CEFIRO Stufenheck 2.4 86 kW Z24i', 'NISSAN CEFIRO Stufenheck 2.4 86 kW Z24i','4T1BF3EK3AU057298')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V35646M28314', 'BMW', '3 (E21) 320', 'i 75 kW M10 B18 (184EA)', 'BMW 3 (E21) 320 i 75 kW M10 B18 (184EA)', 'BMW 3 (E21) 320 i 75 kW M10 B18 (184EA)','1FAHP3EN1AW193753')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V15894M17055', 'BMW', '3 (E46) 316', 'i 77 kW M43 B16 (164E3)', 'BMW 3 (E46) 316 i 77 kW M43 B16 (164E3)', 'BMW 3 (E46) 316 i 77 kW M43 B16 (164E3)','5TETU4GN6AZ719696')
INSERT INTO VEHICLE_HISTORY (VEH_ID, VEH_MAKE, VEH_MODEL, VEH_TYPE, VEH_NAME, VEH_INFO, VEH_VIN) VALUES
('V25448M24882', 'HYUNDAI', 'i30 CW (FD)', '1.6 90 kW G4FC', 'HYUNDAI i30 CW (FD) 1.6 90 kW G4FC', 'HYUNDAI i30 CW (FD) 1.6 90 kW G4FC','2FMDK3GC6ABB32236')

INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,1)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,2)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,3)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,4)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,5)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,6)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,7)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,8)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,9)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(10 ,10)

INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,6)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,7)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,8)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,9)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,10)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,11)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,12)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,13)
INSERT INTO [dbo].[USER_VEHICLE_HISTORY] ([USER_ID] ,[VEH_HISTORY_ID]) VALUES(13 ,14)

-- insert into coupons condition
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEV0001', '2016-10-01', '2016-10-31', 5, 2,
   25, 1000000025343032417, 200.00, 'CH',
  'TECHNOMAG', '1',
   GETDATE(), '1', GETDATE()
);

-- case 2 : Fully used count
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEV0002', '2016-11-26', '2017-11-26', 5, 5,
   25, 1000000025343032417, 200.00, 'CH',
  'GROUP', '1',
   GETDATE(), '1', GETDATE()
);

-- case 3 : country test
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVCH0001', '2016-11-26', '2017-11-26', 5, 5,
   25, 1000000025343032417, 200.00, 'CH',
  'DERENDINGER-CH', '1',
   GETDATE(), '1', GETDATE()
);

-- case 4 : Country test- negative
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVCH0002', '2016-11-26', '2017-11-26', 5, 5,
   25, 1000000025343032417, 200.00, 'AT',
  'DERENDINGER-CH', '1',
   GETDATE(), '1', GETDATE()
);

-- case 5 : Country affiliate
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVDDCH0001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032417, 200.00, 'CH',
  'DERENDINGER-CH', '1',
   GETDATE(), '1', GETDATE()
);

-- case 6 : Country affiliate-neg
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVDDAT0001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032417, 200.00, 'CH',
  'DERENDINGER-AT', '1',
   GETDATE(), '1', GETDATE()
);

-- case 7 : Country affiliate-neg
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVGROUP0001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032417, 200.00, 'CH',
  'GROUP', '1',
   GETDATE(), '1', GETDATE()
);

-- case 8 : customer
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE,CUSTOMER_NR, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVCUST0001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032759, 200.00, 'CH',
  'GROUP', 469743, '1',
   GETDATE(), '1', GETDATE()
);

-- case 9 : customer neg
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE,CUSTOMER_NR, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVCUSTNO001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032759, 200.00, 'CH',
  'GROUP', 4130641, '1',
   GETDATE(), '1', GETDATE()
);

-- case 10 : Categories: Bremscheibe(82), Stossdampfer()
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE,ARTICLE_CATEGORIES, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVCAT0001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032759, 200.00, 'CH',
  'GROUP', '82,854', '1',
   GETDATE(), '1', GETDATE()
);

-- case 11 : ArticleID's
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, ARTICLE_ID, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVART00001', '2016-11-26', '2017-11-26', 5, 2,
   25, 10000000056789, 200.00, 'CH',
  'GROUP', '1000000025343032759,1000000001263649220', '1',
   GETDATE(), '1', GETDATE()
);

-- case 12 : Brands: bosch, hella,jurid
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, BRANDS, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVBRAND001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032759, 200.00, 'CH',
  'GROUP', '30,2,48', '1',
   GETDATE(), '1', GETDATE()
);

-- case 13 : BulkQuantity
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, BULK_QUANTITY_TRIGGER, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVBULK001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032759, 200.00, 'CH',
  'GROUP', 4, '1',
   GETDATE(), '1', GETDATE()
);

-- case 13 : gen
INSERT INTO [dbo].[COUPON_CONDITIONS]
(COUPON_CODE, DATE_START, DATE_END, USAGE_COUNT,
  USED_COUNT, AMOUNT, DISCOUNT_ARTICLE_ID,
  MINIMUM_ORDER_AMOUNT, COUNTRY, AFFILIATE, UPDATED_BY,
  DATE_OF_LAST_UPDATE, CREATED_BY, DATE_OF_CREATION
  )

VALUES ('DEVGEN0001', '2016-11-26', '2017-11-26', 5, 2,
   25, 1000000025343032759, 200.00, 'CH',
  'GROUP', '1',
   GETDATE(), '1', GETDATE()
);

GO

-- Create a new user 'demo.t' (Technomag - Garage A) and 'demo.d' (Derendinger - Garage C) for sales demo
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE, VAT_CONFIRM)
VALUES(1,'Neil','Brown','demo.d@garage-c.ch','demo.d','123456789', 1, 1, 11, 1, 1, 1, 1, 1, 1, 51, 1);
INSERT INTO [dbo].[ESHOP_USER] (SALUTATION, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE, LANGUAGE, TYPE, HOURLY_RATE, EMAIL_CONFIRMATION, NEWSLETTER, SETTING, AGE, VAT_CONFIRM)
VALUES(1,'Edward','Jones','demo.t@garage-a.ch','demo.t','123456789', 1, 1, 11, 1, 1, 1, 1, 1, 1, 50, 1);

INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 23);
INSERT INTO [dbo].[LOGIN] (PASSWORD, USER_ID) VALUES ('Fdl8lcev2gXKE', 24);

INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (23, 12);
INSERT INTO GROUP_USER (USER_ID, GROUP_ID) VALUES (24, 10);

-- Change password for user 'nu1.gdt' to 'Abc123'
UPDATE LOGIN SET PASSWORD = '$2a$10$kcYZjt.ES7spk08ApW.A9ebkSY.eyFIcifZWKTNuzdZDD2z/R7Qbm'
WHERE USER_ID IN (SELECT ID FROM ESHOP_USER WHERE USERNAME = 'nu1.gdt')




--update field UMAR_ID for COUPON_CONDITIONS
UPDATE [dbo].[COUPON_CONDITIONS] set UMAR_ID = 1000000025343032500 where DISCOUNT_ARTICLE_ID = 1000000025343032417;
UPDATE [dbo].[COUPON_CONDITIONS] set UMAR_ID = 1000000025343032760 where DISCOUNT_ARTICLE_ID = 1000000025343032759;

-- dummy data for vin license package
INSERT INTO [dbo].[LICENSE_SETTINGS]
(PACK_ID, PACK_NAME, product_Parameters, PACK_ARTICLE_ID, PACK_ARTICLE_NO, PACK_UMAR_ID, QUANTITY, LAST_UPDATE, LAST_UPDATED_BY)
VALUES
(1111, 'VIN-50', 'product_Parameters 1', 1000000025371353045, 'PACK_ARTICLE_NO 1', 1000000025371353060, 50, '2016-10-01', '1');

INSERT INTO [dbo].[LICENSE_SETTINGS]
(PACK_ID, PACK_NAME, product_Parameters, PACK_ARTICLE_ID, PACK_ARTICLE_NO, PACK_UMAR_ID, QUANTITY, LAST_UPDATE, LAST_UPDATED_BY)
VALUES
(2222, 'VIN-20', 'product_Parameters 2', 1000000025371352959, 'PACK_ARTICLE_NO 2', 1000000025371352989, 20, '2016-10-01', '2');


--Insert Data for HaynesPro - LICENSE_SETTINGS #946
INSERT INTO [dbo].[LICENSE_SETTINGS]
(PACK_ID, PACK_NAME, product_Parameters, PACK_ARTICLE_ID, PACK_ARTICLE_NO, PACK_UMAR_ID, QUANTITY, LAST_UPDATE, LAST_UPDATED_BY)
VALUES
(3333, 'HP-PREMIUM-06', 'product_Parameters HP-PREMIUM-06', 1000000027291389734, 'PACK_HP-PREMIUM-6', 1000000027291390545, 50, '2016-10-01', 1);

INSERT INTO [dbo].[LICENSE_SETTINGS]
(PACK_ID, PACK_NAME, product_Parameters, PACK_ARTICLE_ID, PACK_ARTICLE_NO, PACK_UMAR_ID, QUANTITY, LAST_UPDATE, LAST_UPDATED_BY)
VALUES
(4444, 'HP-PREMIUM-12', 'product_Parameters HP-PREMIUM-12', 1000000027291367719, 'PACK_HP-PREMIUM-12', 1000000027291369420, 20, '2016-10-01', 2);

--Insert Data for HaynesPro - licence #946
INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '3333', 'HP-PREMIUM-06', 469743, 13, '2017-02-27 04:55:49.000', '2099-12-31 16:59:59.000', 50, 0, '', GETDATE(), 13);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '3333', 'HP-PREMIUM-06', 469743, 8, '2017-09-27 04:55:49.000', '2099-12-31 16:59:59.000', 50, 0, '', GETDATE(), 8);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 469743, 8, '2017-09-27 04:55:49.000', '2099-12-31 16:59:59.000', 50, 0, '', GETDATE(), 8);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 469743, 13, '2017-01-20 04:55:49.000', '2099-12-31 16:59:59.000', 30, 0, '', GETDATE(), 13);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 4130675, 10, '2017-01-20 04:55:49.000', '2099-12-31 16:59:59.000', 30, 0, '', GETDATE(), 10);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 4130641, 12, '2017-01-20 04:55:49.000', '2099-12-31 16:59:59.000', 30, 0, '', GETDATE(), 12);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 318088, 14, '2017-01-20 04:55:49.000', '2099-12-31 16:59:59.000', 30, 0, '', GETDATE(), 14);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 469737, 15, '2017-01-20 04:55:49.000', '2099-12-31 16:59:59.000', 30, 0, '', GETDATE(), 15);

INSERT INTO [dbo].[LICENSE]
(TYPE_OF_LICENSE, PACK_ID, PACK_NAME, CUSTOMER_NR, USER_ID, BEGIN_DATE, END_DATE, QUANTITY, QUANTITY_USED, LAST_USED, LAST_UPDATE, LAST_UPDATED_BY )
VALUES
('HaynesPro', '4444', 'HP-PREMIUM-12', 4408511, 17, '2017-01-20 04:55:49.000', '2099-12-31 16:59:59.000', 30, 0, '', GETDATE(), 17);


-- dummy data for view order list -gdt.admin
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40173783, 125321, 'ALFA ROMEO 156 Sportwagon (932_) 1.9 JTD (932B2B, 932B2C) 85 kW 937 A2.000; HYUNDAI i30 (GD) 1.4 74 kW G4LC', 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172899, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172779, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172561, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172560, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172519, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172270, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172269, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172268, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172267, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40172266, NULL, NULL, 69, 469737)
INSERT [dbo].[ORDER_HISTORY] ([ORDER_NUMBER], [TRANS_NUMBER], [VEHICLE_INFO], [USER_ID], [CUSTOMER_NUMBER]) VALUES (40173119, NULL, NULL, 13, 469737)

-- LOG USER SIGN IN DATETIME
UPDATE [dbo].[ESHOP_USER] SET  SIGN_IN_DATE = GETDATE()
WHERE ID IN (
    SELECT USER_NAME FROM OAUTH_ACCESS_TOKEN
)

INSERT INTO ESHOP_RELEASE VALUES ('Development', '1.1.33.0.1', GETDATE())

---Update permission
DELETE FROM [ESHOP_FUNCTION]
DELETE FROM [ESHOP_PERMISSION]
DELETE FROM [PERM_FUNCTION]

INSERT INTO [dbo].[ESHOP_PERMISSION] (PERMISSION, DESCRIPTION, CREATED_BY, CREATED_DATE) VALUES ('ManageOffer' , 'Manage offer', 1, GETDATE());
INSERT INTO [dbo].[ESHOP_FUNCTION] (FUNCTION_NAME, DESCRIPTION, RELATIVE_URL) VALUES ('ManageOffer' , 'Manage offer', '/offer**');
INSERT INTO PERM_FUNCTION(PERM_ID, FUNCTION_ID) VALUES(17, 32);

-- Init data for CURRENT_STATE_NET_PRICE_VIEW
UPDATE [dbo].[USER_SETTINGS] SET [CURRENT_STATE_NET_PRICE_VIEW] = [NET_PRICE_VIEW]

-- remove duplicated record in group_user
DELETE FROM GROUP_USER WHERE ID IN
(SELECT ID FROM GROUP_USER
LEFT JOIN (
   SELECT MIN(ID) as ROWID, GROUP_ID, USER_ID
   FROM GROUP_USER
   GROUP BY GROUP_ID, USER_ID
) AS KEEPROWS ON
   GROUP_USER.ID = KEEPROWS.ROWID
WHERE
   KEEPROWS.ROWID IS NULL
);
-- #2458 AX does not have setting for netprice, we handle netpriceView in connect app
UPDATE CUSTOMER_SETTINGS SET ALLOW_NET_PRICE_CHANGED = 1

--Create permission for home
INSERT INTO ESHOP_FUNCTION(FUNCTION_NAME,DESCRIPTION,RELATIVE_URL) VALUES ('Home','Home','/home')
INSERT INTO ESHOP_PERMISSION(PERMISSION,DESCRIPTION,CREATED_BY) VALUES('Home','Home',1)
INSERT INTO PERM_FUNCTION(PERM_ID,FUNCTION_ID)VALUES(22,37)

-- enable home for sales role
INSERT INTO ROLE_PERMISSION(ROLE_ID,PERM_ID) VALUES(4,22)

-- update permission key for offer,home
UPDATE ESHOP_PERMISSION SET PERMISSION_KEY='OFFER' WHERE id=17
UPDATE ESHOP_PERMISSION SET PERMISSION_KEY='HOME' WHERE id=22

-- Update ORDER_DISPLAY for PAYMENT_METHOD
UPDATE PAYMENT_METHOD SET ORDER_DISPLAY = 1 WHERE DESC_CODE = 'CREDIT'
UPDATE PAYMENT_METHOD SET ORDER_DISPLAY = 2 WHERE DESC_CODE = 'CASH'
UPDATE PAYMENT_METHOD SET ORDER_DISPLAY = 4 WHERE DESC_CODE = 'CARD'
UPDATE PAYMENT_METHOD SET ORDER_DISPLAY = 3 WHERE DESC_CODE = 'DIRECT_INVOICE'

-- Insert default url
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Matik-Austria',3,'default_url','https://shop.matik.at/mat-ax/') 
INSERT INTO ORGANISATION_SETTINGS(NAME,ORGANISATION_ID,SETTING_KEY,SETTING_VALUE) VALUES('Derendinger-Austria',2,'default_url','https://shop.derendinger.at/dat-ax/') 

UPDATE ORDER_HISTORY SET TYPE = 'ORDER'
--Initialize data for MESSAGE
--Initialize data for MESSAGE_ACCESS_RIGHT
INSERT INTO MESSAGE_ACCESS_RIGHT(USER_GROUP,USER_GROUP_KEY,MESSAGE_ROLE_TYPE_ID,DESCRIPTION) VALUES('SALES','SALES',2,'Just for sales user')
INSERT INTO MESSAGE_ACCESS_RIGHT(USER_GROUP,USER_GROUP_KEY,MESSAGE_ROLE_TYPE_ID,DESCRIPTION) VALUES('CUSTOMER_ADMIN','ADMIN',1,'Just for customer admin')
INSERT INTO MESSAGE_ACCESS_RIGHT(USER_GROUP,USER_GROUP_KEY,MESSAGE_ROLE_TYPE_ID,DESCRIPTION) VALUES('CUSTOMER_NORMAL','NORMAL_USER',1,'Just for customer normal user')
INSERT INTO MESSAGE_ACCESS_RIGHT(USER_GROUP,USER_GROUP_KEY,MESSAGE_ROLE_TYPE_ID,DESCRIPTION) VALUES('CUSTOMER_ALL','ALL',1,'Just for customer admin and customer normal user')
INSERT INTO MESSAGE_ACCESS_RIGHT(USER_GROUP,USER_GROUP_KEY,MESSAGE_ROLE_TYPE_ID,DESCRIPTION) VALUES('ALL','ALL',3,'For all users')

--Initialize data for MESSAGE_ROLE_TYPE
INSERT INTO MESSAGE_ROLE_TYPE(ROLE_TYPE) VALUES('CUSTOMER')
INSERT INTO MESSAGE_ROLE_TYPE(ROLE_TYPE) VALUES('SALES')
INSERT INTO MESSAGE_ROLE_TYPE(ROLE_TYPE) VALUES('ALL')

--Initialize data for MESSAGE_ACCESS_RIGHT_ROLE
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(1,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(2,3)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(3,6)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(4,3)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(4,6)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(5,3)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(5,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_ROLE(MESSAGE_ACCESS_RIGHT_ID,ESHOP_ROLE_ID) VALUES(5,6)

--Initialize data for MESSAGE_STYLE
INSERT INTO MESSAGE_STYLE(STYLE,DESCRIPTION) VALUES('DEFAULT','Default type')
INSERT INTO MESSAGE_STYLE(STYLE,DESCRIPTION) VALUES('PRIMARY','Primary type')
INSERT INTO MESSAGE_STYLE(STYLE,DESCRIPTION) VALUES('SUCCESS','Success type')
INSERT INTO MESSAGE_STYLE(STYLE,DESCRIPTION) VALUES('WARNING','Warning type')
INSERT INTO MESSAGE_STYLE(STYLE,DESCRIPTION) VALUES('DANGER','Alert type')

--Initialize data for MESSAGE_TYPE
INSERT INTO MESSAGE_TYPE(TYPE,DESCRIPTION) VALUES('PANEL','Defined per shop')
INSERT INTO MESSAGE_TYPE(TYPE,DESCRIPTION) VALUES('BANNER','Shown at the very top of the page on all pages except login')

--Initialize data for MESSAGE_AREA
INSERT INTO MESSAGE_AREA(AREA,AUTH,DESCRIPTION) VALUES('LOGIN_PAGE',0,'For login page only')
INSERT INTO MESSAGE_AREA(AREA,AUTH,DESCRIPTION) VALUES('CUSTOMER_HOME_PAGE',1,'For customer home page only')
INSERT INTO MESSAGE_AREA(AREA,AUTH,DESCRIPTION) VALUES('SALES_HOME_PAGE',1,'For sales-home page only')
INSERT INTO MESSAGE_AREA(AREA,AUTH,DESCRIPTION) VALUES('SHOPPING_BASKET_PAGE',1,'For shopping basket page only')
INSERT INTO MESSAGE_AREA(AREA,AUTH,DESCRIPTION) VALUES('ORDER_CONFIRMATION_PAGE',1,'For order confirmation page only')
INSERT INTO MESSAGE_AREA(AREA,AUTH,DESCRIPTION) VALUES('ALL',1,'For all interal pages except login')

--Initialize data for MESSAGE_SUB_AREA
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('LOGIN_PAGE_1',1,1,'Sub area 1 of login page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('CUSTOMER_HOME_PAGE_1',1,2,'Sub area 1 of customer home page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('CUSTOMER_HOME_PAGE_2',2,2,'Sub area 2 of customer home page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('CUSTOMER_HOME_PAGE_3',3,2,'Sub area 3 of customer home page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('SHOPPING_BASKET_PAGE_1',1,4,'Sub area 1 of shopping basket page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('ORDER_CONFIRMATION_PAGE_1',1,5,'Sub area 1 of confimation page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('SALES_HOME_PAGE_1',1,3,'Sub area 1 of sales home page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('SALES_HOME_PAGE_2',2,3,'Sub area 2 of sales home page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('SALES_HOME_PAGE_3',3,3,'Sub area 3 of sales home page')
INSERT INTO MESSAGE_SUB_AREA(SUB_AREA,SORT,MESSAGE_AREA_ID,DESCRIPTION) VALUES('ALL',1,6,'For all sub areas except login')

--Initialize data for MESSAGE_LOCATION_TYPE
INSERT INTO MESSAGE_LOCATION_TYPE(LOCATION_TYPE,DESCRIPTION) VALUES('AFFILIATE','Affiliate scope - use affiliate shortname')
INSERT INTO MESSAGE_LOCATION_TYPE(LOCATION_TYPE,DESCRIPTION) VALUES('CUSTOMER','Customer scope - use customer number')
--Ignore for the moment because FILLIALE table not available in DB
--INSERT INTO MESSAGE_LOCATION_TYPE(LOCATION_TYPE,DESCRIPTION) VALUES('FILLIALE','Filliale scope - use filliale number')

--Initialize data for MESSAGE_VISIBILITY
INSERT INTO MESSAGE_VISIBILITY(VISIBILITY,DESCRIPTION) VALUES('ONCE','Message is shown only once')
INSERT INTO MESSAGE_VISIBILITY(VISIBILITY,DESCRIPTION) VALUES('UNTIL_CLOSE','Show again after refresh app')
INSERT INTO MESSAGE_VISIBILITY(VISIBILITY,DESCRIPTION) VALUES('UNTIL_CLOSE_AS_USER_SECTION','Show again after re-login')

-- Init data for MESSAGE_ACCESS_RIGHT_AREA
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(1,3)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(1,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(1,5)

INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(2,2)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(2,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(2,5)

INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(3,2)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(3,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(3,5)

INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(4,2)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(4,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(4,5)

INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(5,1)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(5,2)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(5,3)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(5,4)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(5,5)
INSERT INTO MESSAGE_ACCESS_RIGHT_AREA(MESSAGE_ACCESS_RIGHT_ID,MESSAGE_AREA_ID) VALUES(5,6)

--Initialize data for MESSAGE_LOCATION_TYPE_ROLE_TYPE
INSERT INTO MESSAGE_LOCATION_TYPE_ROLE_TYPE(MESSAGE_LOCATION_TYPE_ID,MESSAGE_ROLE_TYPE_ID) VALUES(1,1)
INSERT INTO MESSAGE_LOCATION_TYPE_ROLE_TYPE(MESSAGE_LOCATION_TYPE_ID,MESSAGE_ROLE_TYPE_ID) VALUES(1,2)
INSERT INTO MESSAGE_LOCATION_TYPE_ROLE_TYPE(MESSAGE_LOCATION_TYPE_ID,MESSAGE_ROLE_TYPE_ID) VALUES(1,3)

INSERT INTO MESSAGE_LOCATION_TYPE_ROLE_TYPE(MESSAGE_LOCATION_TYPE_ID,MESSAGE_ROLE_TYPE_ID) VALUES(2,1)
