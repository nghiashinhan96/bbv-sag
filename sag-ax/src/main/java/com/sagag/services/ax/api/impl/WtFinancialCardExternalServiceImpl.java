package com.sagag.services.ax.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.request.FinancialCardDetailRequest;
import com.sagag.services.article.api.request.FinancialCardHistoryRequest;
import com.sagag.services.ax.api.WtFinancialCardExternalService;
import com.sagag.services.ax.client.AxFinancialCardClient;
import com.sagag.services.ax.converter.AxFinancialCardAmountConverter;
import com.sagag.services.ax.converter.AxFinancialCardDetailConverter;
import com.sagag.services.ax.converter.AxFinancialCardDocConverter;
import com.sagag.services.ax.domain.financialcard.AxFinancialCarDoc;
import com.sagag.services.ax.domain.financialcard.AxFinancialCardAmount;
import com.sagag.services.ax.domain.financialcard.AxFinancialCardDetail;
import com.sagag.services.ax.domain.financialcard.AxFinancialCardHistory;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.sag.financialcard.FinancialCardAmountDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDetailDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDocDto;

@Service
@SbProfile
public class WtFinancialCardExternalServiceImpl extends AxProcessor
    implements WtFinancialCardExternalService {

  private static final Integer DF_FINANCIAL_CARD_HISTORY_PAGE_SIZE = 20;

  @Autowired
  private AxFinancialCardClient axFinancialCardClient;

  @Autowired
  private AxFinancialCardDetailConverter axFinancialCardDetailConverter;

  @Autowired
  private AxFinancialCardDocConverter axFinancialCardDocConverter;

  @Autowired
  private AxFinancialCardAmountConverter axFinancialCardAmountConverter;

  @Override
  public Optional<FinancialCardDetailDto> getFinancialCardDetail(final String compName,
      final String custNr, final String documentNr, final FinancialCardDetailRequest request) {
    Function<String, ResponseEntity<AxFinancialCardDetail>> function =
        token -> getOrDefaultThrow(() -> axFinancialCardClient.getFinancialCardDetail(token,
            compName, custNr, documentNr, request));
    final ResponseEntity<AxFinancialCardDetail> axFinancialCardDetail =
        retryIfExpiredToken(function);
    if (!axFinancialCardDetail.hasBody()) {
      return Optional.empty();
    }
    return Optional
        .ofNullable(axFinancialCardDetailConverter.apply(axFinancialCardDetail.getBody()));
  }

  @Override
  public Page<FinancialCardDocDto> getFinancialCardHistory(String companyName, String custNr,
      FinancialCardHistoryRequest request) {
    Function<String, ResponseEntity<AxFinancialCardHistory>> function = token -> getOrDefaultThrow(
        () -> axFinancialCardClient.getFinancialCardHistory(token, companyName, custNr, request));

    final ResponseEntity<AxFinancialCardHistory> axFinancialCardHistory =
        retryIfExpiredToken(function);
    if (!axFinancialCardHistory.hasBody()) {
      return Page.empty();
    }

    final List<AxFinancialCarDoc> finCardDocs = axFinancialCardHistory.getBody().getDocuments();
    if (CollectionUtils.isEmpty(finCardDocs)) {
      return Page.empty();
    }

    final Pageable pageable =
        PageUtils.defaultPageable(request.getPage(), DF_FINANCIAL_CARD_HISTORY_PAGE_SIZE);

    final long totalElements = finCardDocs.stream().findFirst()
        .map(AxFinancialCarDoc::getDocumentCount).orElse(NumberUtils.INTEGER_ZERO).longValue();

    final List<FinancialCardDocDto> finacialCardDocDtos = finCardDocs.stream()
        .map(axFinancialCardDocConverter).collect(Collectors.toList());

    return new PageImpl<>(finacialCardDocDtos, pageable, totalElements);
  }

  @Override
  public Optional<FinancialCardAmountDto> getFinancialCardAmount(String companyName, String custNr,
      String paymentMethod) {
    Function<String, ResponseEntity<AxFinancialCardAmount>> function =
        token -> getOrDefaultThrow(() -> axFinancialCardClient.getFinancialCardAmount(token,
            companyName, custNr, paymentMethod));
    final ResponseEntity<AxFinancialCardAmount> axFinancialCardAmount =
        retryIfExpiredToken(function);
    if (!axFinancialCardAmount.hasBody()) {
      return Optional.empty();
    }
    return Optional
        .ofNullable(axFinancialCardAmountConverter.apply(axFinancialCardAmount.getBody()));
  }
}
