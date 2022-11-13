package com.sagag.eshop.repo.entity.legal_term;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "LEGAL_DOCTYPE")
@Entity
@Getter
@Setter
public class LegalDoctype implements Serializable {

    private static final long serialVersionUID = 8285133334227071633L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int doctypeRef;

    private Long createdUserId;

    private LocalDateTime createdDate;

    private Long modifiedUserId;

    private LocalDateTime modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctype")
    private Set<LegalDocument> documents;

}
