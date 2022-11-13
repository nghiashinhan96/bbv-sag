package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractTasklet;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.utils.DbUtils;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.EqualsAndHashCode;

@Component
@CopyDbProfile
@EqualsAndHashCode(callSuper = false)
public class TruncateAllDestTablesTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String[] tables = new String[] {
        "USER_VEHICLE_HISTORY",
        "VEHICLE_HISTORY",
        "SUPPORTED_BRAND_PROMOTION",
        "SUPPORTED_AFFILIATE",
        "SHOP_ARTICLE",
        "USER_ORDER_HISTORY",
        "ORDER_HISTORY",
        "GROUP_USER",
        "LOGIN",
        "VIN_LOGGING",
        "FEEDBACK",
        "ESHOP_USER",
        "SALUTATION",
        "GROUP_ROLE",
        "ESHOP_ROLE",
        "ROLE_TYPE",
        "ROLE_PERMISSION",
        "PERM_FUNCTION",
        "ORGANISATION_SETTINGS",
        "USER_SETTINGS",
        "ORG_COLLECTION_SETTINGS",
        "ORGANISATION_GROUP",
        "ORGANISATION_ADDRESS",
        "COLLECTION_RELATION",
        "ORGANISATION_COLLECTION",
        "ORGANISATION",
        "CUSTOMER_SETTINGS",
        "PAYMENT_METHOD",
        "ORGANISATION_TYPE",
        "ORGANISATION_PROPERTY",
        "ORDER_TYPE",
        "ORDER_STATUS",
        "OFFER_POSITION",
        "OFFER_PERSON_PROPERTY",
        "OFFER_PERSON",
        "OFFER_ADDRESS",
        "OFFER",
        "MESSAGE_VISIBILITY",
        "MESSAGE_TYPE",
        "MESSAGE_SUB_AREA",
        "MESSAGE_STYLE",
        "MESSAGE_ROLE_TYPE",
        "MESSAGE_LOCATION_TYPE_ROLE_TYPE",
        "MESSAGE_LOCATION_TYPE",
        "MESSAGE_LOCATION",
        "MESSAGE_LANGUAGE",
        "MESSAGE_HIDING",
        "MESSAGE_AREA",
        "MESSAGE_ACCESS_RIGHT_ROLE",
        "MESSAGE_ACCESS_RIGHT_AREA",
        "MESSAGE_ACCESS_RIGHT",
        "MESSAGE",
        "MAPPING_USER_ID_EBL_CONNECT",
        "LICENSE",
        "LICENSE_SETTINGS",
        "LEGAL_DOCUMENT_MASTER",
        "LEGAL_DOCUMENT_CUSTOMER_ACCEPTED_LOG",
        "LEGAL_DOCUMENT_AFFILIATE_ASSIGNED_LOG",
        "LANGUAGES",
        "INVOICE_TYPE",
        "GROUP_PERMISSION",
        "FINAL_CUSTOMER_PROPERTY",
        "FINAL_CUSTOMER_ORDER_ITEM",
        "FINAL_CUSTOMER_ORDER",
        "FEEDBACK_TOPIC_DEPARTMENT",
        "FEEDBACK_TOPIC",
        "FEEDBACK_STATUS",
        "FEEDBACK_DEPARTMENT_CONTACT",
        "FEEDBACK_DEPARTMENT",
        "EXTERNAL_USER",
        "EXTERNAL_ORGANISATION",
        "ESHOP_RELEASE",
        "ESHOP_PERMISSION",
        "ESHOP_GROUP",
        "ESHOP_FUNCTION",
        "ESHOP_CLIENT_RESOURCE",
        "ESHOP_CLIENT",
        "ESHOP_CART_ITEM",
        "DELIVERY_TYPE",
        "CURRENCY",
        "COUPON_USE_LOG",
        "COUPON_CONDITIONS",
        "COUNTRY",
        "COLLECTIVE_DELIVERY",
        "COLLECTION_PERMISSION",
        "CLIENT_ROLE",
        "BUSINESS_LOG",
        "BASKET_HISTORY",
        "ARTICLE_HISTORY",
        "ALLOCATION_TYPE",
        "AFFILIATE_PERMISSION",
        "ADDRESS",
        "ADDRESS_TYPE",
        "AAD_ACCOUNTS",
    };
    DbUtils.truncateTables(session, Arrays.asList(tables));
    return finish(contribution);
  }
}
