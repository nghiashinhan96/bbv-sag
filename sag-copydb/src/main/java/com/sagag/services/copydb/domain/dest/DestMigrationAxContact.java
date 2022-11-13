package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_AX_CONTACT database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_AX_CONTACT")
@NamedQuery(name = "DestMigrationAxContact.findAll", query = "SELECT m FROM DestMigrationAxContact m")
@Data
public class DestMigrationAxContact implements Serializable {

  private static final long serialVersionUID = -5593742178875848398L;

  @Id
  private long id;
  
  @Column(name = "CONTACTINFORMATIONLANGUAGEID")
  private String contactinformationlanguageid;

  @Column(name = "CONTACTPERSONPARTYTYPE")
  private String contactpersonpartytype;

  @Column(name = "DISPLAYNAMESEQUENCEPATTERNNAME")
  private String displaynamesequencepatternname;

  @Column(name = "EMPLOYMENTJOBFUNCTIONNAME")
  private String employmentjobfunctionname;

  @Column(name = "FIRSTNAME")
  private String firstname;

  @Column(name = "GENDER")
  private String gender;

  @Column(name = "ISPRIMARYEMAILADDRESSIMENABLED")
  private String isprimaryemailaddressimenabled;

  @Column(name = "ISPRIMARYPHONENUMBERMOBILE")
  private String isprimaryphonenumbermobile;

  @Column(name = "LASTNAME")
  private String lastname;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "PRIMARYEMAILADDRESS")
  private String primaryemailaddress;

  @Column(name = "PRIMARYEMAILADDRESSDESCRIPTION")
  private String primaryemailaddressdescription;

  @Column(name = "PRIMARYEMAILADDRESSPURPOSE")
  private String primaryemailaddresspurpose;

  @Column(name = "PRIMARYFAXNUMBER")
  private String primaryfaxnumber;

  @Column(name = "PRIMARYFAXNUMBERDESCRIPTION")
  private String primaryfaxnumberdescription;

  @Column(name = "PRIMARYFAXNUMBERPURPOSE")
  private String primaryfaxnumberpurpose;

  @Column(name = "PRIMARYPHONENUMBER")
  private String primaryphonenumber;

  @Column(name = "PRIMARYPHONENUMBERDESCRIPTION")
  private String primaryphonenumberdescription;

  @Column(name = "PRIMARYPHONENUMBERPURPOSE")
  private String primaryphonenumberpurpose;

  @Column(name = "PRIMARYURL")
  private String primaryurl;

  @Column(name = "PRIMARYURLDESCRIPTION")
  private String primaryurldescription;

  @Column(name = "PRIMARYURLPURPOSE")
  private String primaryurlpurpose;

  @Column(name = "[?SAGGWSACCOUNTNUMBER]")
  private String _saggwsaccountnumber;

  @Column(name = "SAGGWSACCOUNTTYPE")
  private String saggwsaccounttype;

}
