package com.sagag.services.service.digi_invoice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.forgotpassword.PasswordResetTokenRepository;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.DigiInvoiceChangeMailRequestDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.digi_invoice.impl.DigiInvoiceServiceImpl;
import com.sagag.services.service.mail.DigiInvoiceCriteria;
import com.sagag.services.service.mail.DigiInvoiceMailSender;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class DigiInvoiceServiceImplTest {

  @InjectMocks
  private DigiInvoiceServiceImpl handler;

  @Mock
  private OrganisationService organisationService;

  @Mock
  private DigiInvoiceMailSender digiInvoiceMailSender;

  @Mock
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Mock
  protected LocaleContextHelper localeContextHelper;

  @Mock
  private UserInfo userInfo;

  @Before
  public void setup() {
    userInfo = new UserInfo();
    userInfo.setId(1l);
    userInfo.setAffiliateShortName("derendinger-ch");
    userInfo.setLanguage("de");
    userInfo.setCustomer(new Customer());
  }

  @Test
  public void shouldSendEmailConfirmSuccessfullyGivenValidModel() {
    DigiInvoiceChangeMailRequestDto model = new DigiInvoiceChangeMailRequestDto();
    model.setToken("token");
    model.setHashUsernameCode("#1234");
    model.setInvoiceRecipientEmail("cungnguyen@bbv.ch");
    model.setInvoiceRequestEmail("system@bbv.ch");
    PasswordResetToken resetToken = new PasswordResetToken();
    when(organisationService.findOrgSettingByKey(eq("derendinger-ch"),
        eq(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())))
            .thenReturn(Optional.of("affiliate@mail.com"));
    when(passwordResetTokenRepository.findByTokenAndHashUsernameCode(model.getToken(),
        model.getHashUsernameCode())).thenReturn(resetToken);
    when(localeContextHelper.toLocale(userInfo.getLanguage())).thenReturn(Locale.GERMANY);
    doNothing().when(passwordResetTokenRepository).delete(resetToken);
    doNothing().when(digiInvoiceMailSender).send(any(DigiInvoiceCriteria.class));
    handler.sendMailConfirmChangeElectronicInvoice(model, userInfo);

    verify(digiInvoiceMailSender, times(1)).send(any(DigiInvoiceCriteria.class));
  }

  @Test(expected = NoSuchElementException.class)
  public void shouldThrowExceptionWithInvalidToken(){
    DigiInvoiceChangeMailRequestDto model = new DigiInvoiceChangeMailRequestDto();
    model.setToken("123");
    model.setHashUsernameCode("#1234");
    model.setInvoiceRecipientEmail("cungnguyen@bbv.ch");
    model.setInvoiceRequestEmail("system@bbv.ch");
    when(organisationService.findOrgSettingByKey(eq("derendinger-ch"),
            eq(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())))
            .thenReturn(Optional.of("affiliate@mail.com"));
    when(passwordResetTokenRepository.findByTokenAndHashUsernameCode(model.getToken(),
            model.getHashUsernameCode())).thenReturn(null);
    when(localeContextHelper.toLocale(userInfo.getLanguage())).thenReturn(Locale.GERMANY);
    handler.sendMailConfirmChangeElectronicInvoice(model, userInfo);

    verify(digiInvoiceMailSender, times(0)).send(any(DigiInvoiceCriteria.class));
  }

}
