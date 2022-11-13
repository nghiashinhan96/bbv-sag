package com.sagag.eshop.repo.entity.legal_term;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "LEGAL_DOCUMENT")
@Entity
@Getter
@Setter
public class LegalDocument implements Serializable {

    private static final long serialVersionUID = 4172302398986062206L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEGAL_DOCTYPE_ID", referencedColumnName = "ID")
    @JsonBackReference
    private LegalDoctype doctype;

    private String country;

    private String language;

    private String documentName;

    private String summary;

    private String document;

    private String pdfUrl;

}
