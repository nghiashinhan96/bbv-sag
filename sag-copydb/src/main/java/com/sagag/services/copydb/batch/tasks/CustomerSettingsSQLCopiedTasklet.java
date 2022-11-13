package com.sagag.services.copydb.batch.tasks;

import com.sagag.services.copydb.batch.AbstractTasklet;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.utils.DbUtils;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;

@Component
@CopyDbProfile
@EqualsAndHashCode(callSuper = false)
public class CustomerSettingsSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.CUSTOMER_SETTINGS";
    final String toTable = "econnectAxPCH_temp.dbo.CUSTOMER_SETTINGS";
    final String copiedColumns = "ADDRESS_ID,ALLOCATION_ID,ALLOW_NET_PRICE_CHANGED,ALLOW_SHOW_DISCOUNT_CHANGED,BILLING_ADDRESS_ID,COLLECTIVE_DELIVERY_ID,DELIVERY_ADDRESS_ID,DELIVERY_ID,EMAIL_NOTIFICATION_ORDER,HOME_BRANCH,ID,INVOICE_TYPE,NET_PRICE_CONFIRM,NET_PRICE_VIEW,NORMAUTO_DISPLAY,PAYMENT_METHOD,SESSION_TIMEOUT_SECONDS,SHOW_DISCOUNT,SHOW_OCI_VAT,USE_DEFAULT_SETTING,VIEW_BILLING";
    final String toColumns = "ADDRESS_ID,ALLOCATION_ID,ALLOW_NET_PRICE_CHANGED,ALLOW_SHOW_DISCOUNT_CHANGED,BILLING_ADDRESS_ID,COLLECTIVE_DELIVERY_ID,DELIVERY_ADDRESS_ID,DELIVERY_ID,EMAIL_NOTIFICATION_ORDER,HOME_BRANCH,ID,INVOICE_TYPE,NET_PRICE_CONFIRM,NET_PRICE_VIEW,NORMAUTO_DISPLAY,PAYMENT_METHOD,SESSION_TIMEOUT_SECONDS,SHOW_DISCOUNT,SHOW_OCI_VAT,USE_DEFAULT_SETTING,VIEW_BILLING";
    DbUtils.turnOnIdentityInsert(session, "CUSTOMER_SETTINGS");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
