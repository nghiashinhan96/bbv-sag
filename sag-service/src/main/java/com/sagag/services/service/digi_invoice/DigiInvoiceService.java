package com.sagag.services.service.digi_invoice;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.DigiInvoiceChangeMailRequestDto;

public interface DigiInvoiceService {

    void sendMailConfirmChangeElectronicInvoice(DigiInvoiceChangeMailRequestDto requestDto, UserInfo user);
}
