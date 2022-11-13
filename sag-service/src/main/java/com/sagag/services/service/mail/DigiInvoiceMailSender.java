package com.sagag.services.service.mail;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;

@Component
public class DigiInvoiceMailSender extends MailSender<DigiInvoiceCriteria> {

  private static final String SUBMISSION_INVOICE_REQUEST_TO_CUSTOMER =
          "email/submission-invoice-request-to-customer-service-template";

  private static final String SUBMISSION_INVOICE_REQUEST_TO_REQUEST_EMAIL =
          "email/submission-invoice-request-to-request-email-template";

  private static final String SUBMISSION_TO_CUSTOMER_SUBJECT_CODE = "mail.digi-invoice.submission_customer.subject";

  private static final String SUBMISSION_TO_REQUEST_SUBJECT_CODE = "mail.digi-invoice.submission_request_email.subject";

  public void send(DigiInvoiceCriteria criteria)  {
    Assert.hasText(criteria.getAffiliateEmail(), "The given affiliate email must not be empty");
    Assert.hasText(criteria.getInvoiceRecipientEmail(), "The given invoice recipient email must not be empty");
    Assert.hasText(criteria.getInvoiceRequestEmail(), "The given invoice request email must not be empty");
    Assert.notNull(criteria.getCustomerAddress(), "The given customer address must not be null");
    Assert.notNull(criteria.getLocale(), "The given locale must not be null");

    final Context contextCustomer = new Context(criteria.getLocale());
    contextCustomer.setVariable("customerAddress", criteria.getCustomerAddress());
    contextCustomer.setVariable("invoiceRecipientEmail", criteria.getInvoiceRecipientEmail());

    final String toCustomerSubject =
        messageSource.getMessage(SUBMISSION_TO_CUSTOMER_SUBJECT_CODE, null, criteria.getLocale());
    final String bodyMailToCustomer = templateEngine.process(SUBMISSION_INVOICE_REQUEST_TO_CUSTOMER, contextCustomer);
    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getInvoiceRequestEmail(),
            toCustomerSubject, bodyMailToCustomer, true);

    final Context contextRequestEmail = new Context(criteria.getLocale());
    contextRequestEmail.setVariable("customerAddress", criteria.getCustomerAddress());
    contextRequestEmail.setVariable("invoiceRecipientEmail", criteria.getInvoiceRecipientEmail());

    final String toRequestEmail =
            messageSource.getMessage(SUBMISSION_TO_REQUEST_SUBJECT_CODE, null, criteria.getLocale());
    final String bodyMailToRequest = templateEngine.process(SUBMISSION_INVOICE_REQUEST_TO_REQUEST_EMAIL, contextRequestEmail);
    mailService.sendEmail(criteria.getAffiliateEmail(), criteria.getInvoiceRecipientEmail(),
            toRequestEmail, bodyMailToRequest, true);


  }

}
