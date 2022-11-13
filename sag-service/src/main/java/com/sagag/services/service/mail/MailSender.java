package com.sagag.services.service.mail;

import com.sagag.eshop.service.api.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;

public abstract class MailSender<T> {

  @Autowired
  protected TemplateEngine templateEngine;

  @Autowired
  protected MessageSource messageSource;

  @Autowired
  protected MailService mailService;

  /**
   * The interface function to implement send mail with custom template.
   *
   * @param criteria The custom criteria to build data for template
   *
   * @throws MessagingException
   *
   */
  public abstract void send(T criteria) throws MessagingException;

}
