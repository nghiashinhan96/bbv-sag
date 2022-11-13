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
public class UserSettingsSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.USER_SETTINGS";
    final String toTable = "econnectAxPCH_temp.dbo.USER_SETTINGS";
    final String copiedColumns = "ADDRESS_ID,ALLOCATION_ID,ALLOW_NET_PRICE_CHANGED,BILLING_ADDRESS_ID,CLASSIC_CATEGORY_VIEW,COLLECTIVE_DELIVERY_ID,CURRENT_STATE_NET_PRICE_VIEW,DELIVERY_ADDRESS_ID,DELIVERY_ID,EMAIL_NOTIFICATION_ORDER,HAS_PARTNER_PROGRAM_LOGIN,HAS_PARTNER_PROGRAM_VIEW,ID,INVOICE_TYPE,NET_PRICE_CONFIRM,NET_PRICE_VIEW,PAYMENT_METHOD,SALE_ON_BEHALF_OF,SHOW_DISCOUNT,USE_DEFAULT_SETTING,VIEW_BILLING";
    final String toColumns = "ADDRESS_ID,ALLOCATION_ID,ALLOW_NET_PRICE_CHANGED,BILLING_ADDRESS_ID,CLASSIC_CATEGORY_VIEW,COLLECTIVE_DELIVERY_ID,CURRENT_STATE_NET_PRICE_VIEW,DELIVERY_ADDRESS_ID,DELIVERY_ID,EMAIL_NOTIFICATION_ORDER,ACCEPT_HAPPY_POINT_TERM,SHOW_HAPPY_POINTS,ID,INVOICE_TYPE,NET_PRICE_CONFIRM,NET_PRICE_VIEW,PAYMENT_METHOD,SALE_ON_BEHALF_OF,SHOW_DISCOUNT,USE_DEFAULT_SETTING,VIEW_BILLING";
    DbUtils.turnOnIdentityInsert(session, "USER_SETTINGS");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
