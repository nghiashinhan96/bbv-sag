package com.sagag.services.ax.executor.helper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ItemApprovalTypeName;
import com.sagag.services.domain.sag.external.CustomerApprovalType;

@Service
@AxProfile
public class AxArticleSearchHelper {

  @Autowired
  @Qualifier("axErpArticleFilterCallableCreatorImpl")
  private ErpCallableCreator erpArticleFilterCallableCreator;

  public List<ArticleDocDto> filterArticles(final ArticleSearchCriteria criteria,
      final ServletRequestAttributes mainThreadAttrs) {
    try {
      return erpArticleFilterCallableCreator.create(criteria, mainThreadAttrs).call();
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public boolean isMatchingCertificates(List<ItemApprovalTypeName> itemApprovals,
      List<CustomerApprovalType> custApprovalTypes) {
    final Map<String, ItemApprovalTypeName> mapKeyItemApproval = CollectionUtils
        .emptyIfNull(itemApprovals).stream()
        .filter(itemApproval -> StringUtils.isNotEmpty(itemApproval.getApprovalTypename()))
        .collect(Collectors.toMap(ItemApprovalTypeName::getApprovalTypename, Function.identity()));

    return CollectionUtils.emptyIfNull(custApprovalTypes).stream()
        .filter(approvalType -> mapKeyItemApproval.containsKey(approvalType.getApprovalTypeName()))
        .count() == mapKeyItemApproval.size();
  }
}
