package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.EmailAttachment;

public interface MailService {

  void sendEmail(final String from, final String to, final String subject, final String body,
      final boolean isHtmlMode, EmailAttachment... attachments);

  void sendEmailIncludeCc(final String from, final String to, final String[] cc, final String subject,
                          final String body, final boolean isHtmlMode, EmailAttachment... attachments);
}
