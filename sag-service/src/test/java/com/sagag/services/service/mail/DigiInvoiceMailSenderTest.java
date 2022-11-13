package com.sagag.services.service.mail;

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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UT for {@link ChangePasswordMailSender}.
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore("for now the final engine process method can not be mocked, "
        + "finding another way with powermock")
public class DigiInvoiceMailSenderTest {

  @InjectMocks
  private DigiInvoiceMailSender digiInvoiceMailSender;

  @Mock
  protected MessageSource messageSource;

  @Mock
  protected MailService mailService;

  @Mock
  protected TemplateEngine templateEngine;

  @Before
  public void setup() throws MessagingException {
    Mockito.doNothing().when(mailService)
            .sendEmail(anyString(), anyString(), anyString(), anyString(), anyBoolean());
    when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("");
    when(templateEngine.process(anyString(), any(IContext.class))).thenReturn("");
  }

  @Test
  public void testSendCode() throws Exception {
	 DigiInvoiceCriteria criteria = DigiInvoiceCriteria
			  .buildCriteria("cungnguyen@bbv.ch", Locale.GERMAN, "ddch@shop.com",
					  "system@bbv.ch","1234 HCM city");
	 digiInvoiceMailSender.send(criteria);
	 verify(mailService, times(2)).
             sendEmail(anyString(), anyString(), anyString(), anyString(), anyBoolean());
  }
  
}
