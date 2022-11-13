package com.sagag.services.service.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sagag.eshop.service.api.MailService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;

import javax.mail.MessagingException;

/**
 * UT for {@link ChangePasswordMailSender}.
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore("for now the final engine process method can not be mocked, "
        + "finding another way with powermock")
public class ChangePasswordMailSenderTest {

  @InjectMocks
  private ChangePasswordMailSender changePasswordMailSender;

  @Mock
  protected MessageSource messageSource;

  @Mock
  protected MailService mailService;

  @Before
  public void setup() throws MessagingException {
    Mockito.doNothing().when(mailService)
            .sendEmail(anyString(), anyString(), anyString(), anyString(), anyBoolean());
    when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("");
  }

  @Test
  @Ignore
  public void testSendCode() throws Exception {
    ChangePasswordCriteria criteria =
            ChangePasswordCriteria.builder().toEmail("long@bbv.ch")
                    .affiliateEmail("shop@dat.at")
                    .username("long.nguyen")
                    .redirectUrl("http://shop.at")
                    .code("234324")
                    .locale(Locale.GERMAN)
                    .changePassOk(false)
                    .build();

            changePasswordMailSender.send(criteria);
  }

  @Test
  @Ignore
  public void testDefaultChangePass() throws Exception {
    ChangePasswordCriteria criteria =
            ChangePasswordCriteria.builder().toEmail("long@bbv.ch")
                    .affiliateEmail("shop@dat.at")
                    .username("long.nguyen")
                    .redirectUrl("http://shop.at")
                    .code("234324")
                    .locale(Locale.GERMAN)
                    .changePassOk(false)
                    .build();
    changePasswordMailSender.send(criteria);
  }

  @Test
  @Ignore
  public void testSendSuccess() throws Exception {
    ChangePasswordCriteria criteria =
            ChangePasswordCriteria.builder().toEmail("long@bbv.ch")
                    .affiliateEmail("shop@dat.at")
                    .username("long.nguyen")
                    .redirectUrl("http://shop.at")
                    .code(null)
                    .locale(Locale.GERMAN)
                    .changePassOk(true)
                    .build();
    changePasswordMailSender.send(criteria);
  }
}
