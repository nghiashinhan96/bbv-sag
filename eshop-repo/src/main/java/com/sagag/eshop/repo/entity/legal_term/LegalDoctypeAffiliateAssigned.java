package com.sagag.eshop.repo.entity.legal_term;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "LEGAL_DOCTYPE_AFFILIATE_ASSIGNED_LOG")
@Entity
@Getter
@Setter
public class LegalDoctypeAffiliateAssigned implements Serializable {

    private static final long serialVersionUID = 2034129248487716422L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long affiliateId;

    private Long legalDoctypeId;

    private int sort;

    private LocalDate dateValidFrom;

    private int acceptancePeriodDays;

    private int status;

    private Long createdUserId;

    private LocalDateTime createdDate;

    private Long modifiedUserId;

    private LocalDateTime modifiedDate;

}
