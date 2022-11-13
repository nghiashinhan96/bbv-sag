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
public class OfferPositionSQLCopiedTasklet extends AbstractTasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    final String fromTable = "econnectP_bk.dbo.OFFER_POSITION";
    final String toTable = "econnectAxPCH_temp.dbo.OFFER_POSITION";
    final String copiedColumns = "ACTION_TYPE,ACTION_VALUE,ARTICLE_DESCRIPTION,ARTICLE_ENHANCED_DESCRIPTION,ARTICLE_NUMBER,AW_NUMBER,CALCULATED,CATALOG_PATH,CONNECT_VEHICLE_ID,CONTEXT,CREATED_DATE,CREATED_USER_ID,CURRENCY_ID,DELIVERY_DATE,DELIVERY_TYPE_ID,GROSS_PRICE,ID,MAKE_ID,MODEL_ID,MODIFIED_DATE,MODIFIED_USER_ID,NET_PRICE,OFFER_ID,PRICED_QUANTITY,PRICED_UNIT,QUANTITY,REMARK,SAGSYS_ID,SEQUENCE,SHOP_ARTICLE_ID,TECSTATE,TOTAL_GROSS_PRICE,TYPE,UMSART_ID,UOM_ISO,VEHICLE_BOM_DESCRIPTION,VEHICLE_BOM_ID,VEHICLE_DESCRIPTION,VEHICLE_ID,VEHICLE_TYPE_CODE,VERSION";
    final String toColumns = "ACTION_TYPE,ACTION_VALUE,ARTICLE_DESCRIPTION,ARTICLE_ENHANCED_DESCRIPTION,ARTICLE_NUMBER,AW_NUMBER,CALCULATED,CATALOG_PATH,CONNECT_VEHICLE_ID,CONTEXT,CREATED_DATE,CREATED_USER_ID,CURRENCY_ID,DELIVERY_DATE,DELIVERY_TYPE_ID,GROSS_PRICE,ID,MAKE_ID,MODEL_ID,MODIFIED_DATE,MODIFIED_USER_ID,NET_PRICE,OFFER_ID,PRICED_QUANTITY,PRICED_UNIT,QUANTITY,REMARK,SAGSYS_ID,SEQUENCE,SHOP_ARTICLE_ID,TECSTATE,TOTAL_GROSS_PRICE,TYPE,UMSART_ID,UOM_ISO,VEHICLE_BOM_DESCRIPTION,VEHICLE_BOM_ID,VEHICLE_DESCRIPTION,VEHICLE_ID,VEHICLE_TYPE_CODE,VERSION";
    DbUtils.turnOnIdentityInsert(session, "OFFER_POSITION");
    DbUtils.copyTable(session, fromTable, copiedColumns, toTable, toColumns);
    return finish(contribution);
  }
}
