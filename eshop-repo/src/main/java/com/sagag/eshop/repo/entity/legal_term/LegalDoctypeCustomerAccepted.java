package com.sagag.eshop.repo.entity.legal_term;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "LEGAL_DOCTYPE_CUSTOMER_ACCEPTED_LOG")
@Entity
@Getter
@Setter
public class LegalDoctypeCustomerAccepted implements Serializable {

    private static final long serialVersionUID = -4331183974546395963L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long affiliateId;

    private Long legalDoctypeId;

    private Long customerId;

    private Long userId;

    private LocalDateTime timeAccepted;

}
