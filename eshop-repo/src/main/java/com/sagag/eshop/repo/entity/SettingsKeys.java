package com.sagag.eshop.repo.entity;

import org.apache.commons.lang3.StringUtils;

public interface SettingsKeys {

  interface Affiliate {
    // @formatter:off
    enum Theme {
      LOGO_IMAGE,
      DEFAULT_LOGO,
      BACKGROUND_IMAGE,
      DEFAULT_BACKGROUND,
      DESCRIPTION,
      THEME_COLOR_1,
      THEME_COLOR_2,
      FONT_COLOR_1,
      FONT_COLOR_2,
      BACK_TO_TOP_ARROW_COLOR,
      FOOTER_COLOR,
      FOOTER_TEXT_COLOR,
      BUTTON_COLOR,
      BUTTON_TEXT_COLOR,
      SLIDE_SHOW_PAGING_COLOR,
      SLIDE_SHOW_PAGING_COLOR_INACTIVE,
      BUTTON_CART_COLOR,
      SECTION_ARROW_HOVER_COLOR,
      AVAILABILITY_STATUS_COLOR_1,
      AVAILABILITY_STATUS_COLOR_2,
      AVAILABILITY_STATUS_COLOR_3,
      INFORMATION_ICON_COLOR,
      VIN_NOTIFICATION_COLOR,
      CAROUSEL_BACKGROUND,
      ICO_CART,
      LOGIN_IMAGE,
      PRE_LOADER;

      /**
       * Returns the name of this enumeration constant in lower case mode.
       *
       * @return the lower case name.
       */
      public String toLowerName() {
        return StringUtils.lowerCase(this.name());
      }
    }

    enum Settings {
      DEFAULT_EMAIL,
      EH_DEFAULT_EMAIL,
      GOOGLE_ANALYTICS_TRACKING_CODE,
      SHOW_TYRES_DISCOUNT,
      SHOW_TYRES_GROSS_PRICE_HEADER,
      DEFAULT_VAT_RATE,
      MARKETING_DEPT_EMAIL,
      DEFAULT_URL,
      EH_PORTAL_URL,
      OCI_EWB_SCHEMA_TYPE,
      OCI_VENDORID,
      FILIALE,
      SETTING_LOCALE,
      IS_CUSTOMER_ABS_ENABLED,
      IS_SALES_ABS_ENABLED,
      SEND_FEEDBACK_NOTIFICATION_EMAIL,
      SSO_CASE_ONE,
      VAT_TYPE_DISPLAY,
      CUSTOMER_BRAND_PRIORITY_AVAIL_FILTER,
      C4S_BRAND_PRIORITY_AVAIL_FILTER,
      TITLE,
      AVAILABILITY_ICON,
      IS_KSO_ENABLED,
      IS_CUSTOMER_BRAND_FILTER_ENABLED,
      IS_SALES_BRAND_FILTER_ENABLED,
      IS_ADDITIONAL_RECOMMENDATION_ENABLED,
      INVOICE_REQUEST_ALLOWED,
      INVOICE_REQUEST_EMAIL,
      GOOGLE_TAG_MANAGER_SETTING,
      OPTIMIZELY_ID,
      DISABLED_BRAND_PRIORITY_AVAILABILITY,
      EXTERNAL_PART;

      /**
       * Returns the name of this enumeration constant in lower case mode.
       *
       * @return the lower case name.
       */
      public String toLowerName() {
        return StringUtils.lowerCase(this.name());
      }
    }

      enum Availability {
        AVAILABILITY_DISPLAY,
        AVAILABILITY_ICON,
        DROP_SHIPMENT_AVAILABILITY,
        DETAIL_AVAIL_TEXT,
        LIST_AVAIL_TEXT
        ;

        /**
         * Returns the name of this enumeration constant in lower case mode.
         *
         * @return the lower case name.
         */
        public String toLowerName() {
          return StringUtils.lowerCase(this.name());
        }

        /**
         * Append the enum with other string in lower case mode.
         *
         * @return append string.
         */
        public String appendWithString(String appendString) {
          return StringUtils.lowerCase(StringUtils.join(this.name(), appendString));
        }
    }
  }

  interface FinalCustomer {

    enum Settings {
      TYPE,
      STATUS,
      // SHOW_NET_PRICE,
      TD_WORKSHOP,
      CUSTOMER_NUMBER,
      SALUTATION,
      SURNAME,
      FIRSTNAME,
      STREET,
      ADDRESS_1,
      ADDRESS_2,
      PO_BOX,
      POSTCODE,
      PLACE,
      PHONE,
      FAX,
      EMAIL
    }
  }
}
