package com.sagag.services.service.digi_invoice.impl;

import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.dto.DigiInvoiceChangeMailRequestDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.service.digi_invoice.DigiInvoiceService;
import com.sagag.services.service.mail.DigiInvoiceCriteria;
import com.sagag.services.service.mail.DigiInvoiceMailSender;
import com.sagag.services.service.user.password.reset.AbstractResetPasswordHandler;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class DigiInvoiceServiceImpl extends AbstractResetPasswordHandler
    implements DigiInvoiceService {

  @Autowired
  private DigiInvoiceMailSender digiInvoiceMailSender;

  @Override
  public void sendMailConfirmChangeElectronicInvoice(DigiInvoiceChangeMailRequestDto requestDto, UserInfo user) {
    final PasswordResetToken passwordResetToken = passwordTokenRepo
        .findByTokenAndHashUsernameCode(requestDto.getToken(), requestDto.getHashUsernameCode());
    
    if (passwordResetToken == null) {
      throw (new NoSuchElementException("INVALID TOKEN"));
    }
    
    final String affiliateEmail = findDefaultAffiliateEmail(user.getAffiliateShortName(), false);
    final String companyName = user.isFinalUserRole() ? user.getFinalCustomer().getName() : user.getCustomer().getCompanyName();
    final String customerAddress =
        new StringBuilder(user.getCustNrStr()).append(SagConstants.HYPHEN)
            .append(StringUtils.defaultString(companyName, StringUtils.EMPTY))
            .append(SagConstants.COMMA)
            .append(user.getAddresses().stream().filter(address -> address.isPrimary())
                .map(Address::getCity).findFirst().orElse(StringUtils.EMPTY))
            .toString();

    DigiInvoiceCriteria criteria =
        DigiInvoiceCriteria.buildCriteria(requestDto.getInvoiceRecipientEmail(),
            localeContextHelper.toLocale(user.getLanguage()), affiliateEmail,
            requestDto.getInvoiceRequestEmail(), customerAddress);
    digiInvoiceMailSender.send(criteria);

    // Remove token after send mail successful
    passwordTokenRepo.delete(passwordResetToken);
    log.debug("Token deleted is {}", passwordResetToken.getToken());
  }
}
