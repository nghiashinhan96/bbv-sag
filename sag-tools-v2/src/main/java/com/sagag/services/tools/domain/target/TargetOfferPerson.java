package com.sagag.services.tools.domain.target;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "OFFER_PERSON")
public class TargetOfferPerson implements Serializable {

  private static final long serialVersionUID = -7399037110121888746L;

  @Id
  @GeneratedValue(generator = "specificIdGenerator")
  @GenericGenerator(name = "specificIdGenerator", strategy = "com.sagag.services.tools.support.SpecificIdentityGenerator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "OFFER_COMPANY_NAME")
  private String offerCompanyName;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "HOURLY_RATE")
  private Integer hourlyRate;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "LANGUAGE_ID")
  private Integer languageId;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "VERSION")
  private Integer version;

}
