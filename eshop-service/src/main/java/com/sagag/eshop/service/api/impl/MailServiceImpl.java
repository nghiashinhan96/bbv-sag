package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.MailService;
import com.sagag.eshop.service.config.EnableMailService;
import com.sagag.eshop.service.dto.EmailAttachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@EnableMailService
@Slf4j
public class MailServiceImpl implements MailService {

  private static final String LOG_MSG = "Email from = {} - to = {} - subject = {} - \\nbody = {}";

  private static final String LOG_MSG_CC = "Email from = {} - to = {} - cc = {} - subject = {} - \\nbody = {}";

  private static final String ERROR_LOG_MSG =
      "Error while composing the email message from = {}, to = {}, subject = {}, isHtmlMode = {}\n"
      + "body = {}";

  private static final String ERROR_LOG_MSG_CC =
          "Error while composing the email message from = {}, to = {}, cc = {}, subject = {}, isHtmlMode = {}\n"
                  + "body = {}";

  @Autowired
  private JavaMailSender javaMailSender;

  /**
   * Send mail to client.
   *
   */
  @Override
  public void sendEmail(final String from, final String to, final String subject, final String body,
      final boolean isHtmlMode, EmailAttachment... attachments) {
    // Set email information
    log.debug(LOG_MSG, from, to, subject, body);
    final MimeMessage message = buildMailMessage(from, to, null, subject, body, isHtmlMode, attachments);
    // Send mail to user
    javaMailSender.send(message);
    log.info("Email was sent to: {}, \nsubject: {}", to, subject);
  }

  @Override
  public void sendEmailIncludeCc(String from, String to, String[] cc, String subject, String body, boolean isHtmlMode, EmailAttachment... attachments) {
    // Set email information
    log.debug(LOG_MSG_CC, from, to, cc, subject, body);
    final MimeMessage message = buildMailMessage(from, to, cc, subject, body, isHtmlMode, attachments);
    // Send mail to user
    javaMailSender.send(message);
    log.info("Email was sent to: {}, Cc: {}, \nsubject: {}", to, cc, subject);
  }

  private MimeMessage buildMailMessage(final String from, final String to, String[] cc, final String subject,
      final String body, final boolean isHtmlMode, EmailAttachment... attachments) {
    try {
      final MimeMessage message = javaMailSender.createMimeMessage();
      message.setFrom(new InternetAddress(from));
      final MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      if (!ArrayUtils.isEmpty(cc)) {
        helper.setCc(cc);
      }
      helper.setSubject(subject);
      helper.setText(body, isHtmlMode);
      if (ArrayUtils.isNotEmpty(attachments)) {
        for (EmailAttachment attachment : attachments) {
          helper.addAttachment(attachment.getName(), attachment.getContent());
        }
      }
      return message;
    } catch (MessagingException me) {
      if (!ArrayUtils.isEmpty(cc)) {
        log.error(ERROR_LOG_MSG_CC, from, to, cc, subject, body, isHtmlMode);
      } else {
        log.error(ERROR_LOG_MSG, from, to, subject, body, isHtmlMode);
      }
      throw new MailPreparationException("Error while sending email.", me);
    }
  }

}
