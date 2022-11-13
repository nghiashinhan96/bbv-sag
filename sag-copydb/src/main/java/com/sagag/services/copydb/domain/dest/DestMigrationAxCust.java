package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;


/**
 * The persistent class for the MIGRATION_AX_CUST database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_AX_CUST")
@NamedQuery(name = "DestMigrationAxCust.findAll", query = "SELECT m FROM DestMigrationAxCust m")
@Data
public class DestMigrationAxCust implements Serializable {

  private static final long serialVersionUID = 3106085851228816995L;

  @Id
  private long id;
  
  @Column(name = "ADDRESSCOUNTRYREGIONID")
  private String addresscountryregionid;

  @Column(name = "CENTRALBANKPURPOSECODE")
  private String centralbankpurposecode;

  @Column(name = "CHARGESGROUPID")
  private String chargesgroupid;

  @Column(name = "COMMISSIONSALESGROUPID")
  private String commissionsalesgroupid;

  @Column(name = "CREDITLIMIT")
  private String creditlimit;

  @Column(name = "CREDITLIMITISMANDATORY")
  private String creditlimitismandatory;

  @Column(name = "[?CUSTOMERACCOUNT]")
  private String _customeraccount;

  @Column(name = "CUSTOMERGROUPID")
  private String customergroupid;

  @Column(name = "CUSTOMERTMAGROUPID")
  private String customertmagroupid;

  @Column(name = "DEFAULTDIMENSIONDISPLAYVALUE")
  private String defaultdimensiondisplayvalue;

  @Column(name = "DELIVERYMODE")
  private String deliverymode;

  @Column(name = "DELIVERYTERMS")
  private String deliveryterms;

  @Column(name = "EMPLOYEERESPONSIBLENUMBER")
  private String employeeresponsiblenumber;

  @Column(name = "GWSCOMMERCIALREGISTER")
  private String gwscommercialregister;

  @Column(name = "INVOICEADDRESS")
  private String invoiceaddress;

  @Column(name = "LANGUAGEID")
  private String languageid;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAMEALIAS")
  private String namealias;

  @Column(name = "ONHOLDSTATUS")
  private String onholdstatus;

  @Column(name = "ORGANIZATIONNUMBER")
  private String organizationnumber;

  @Column(name = "PARTYTYPE")
  private String partytype;

  @Column(name = "PAYMENTBANKACCOUNT")
  private String paymentbankaccount;

  @Column(name = "PAYMENTCASHDISCOUNT")
  private String paymentcashdiscount;

  @Column(name = "PAYMENTMETHOD")
  private String paymentmethod;

  @Column(name = "PAYMENTTERMS")
  private String paymentterms;

  @Column(name = "SAGGWSCUSTCLASSIFICATIONID")
  private String saggwscustclassificationid;

  @Column(name = "SAGGWSEDITABLEBRAND")
  private String saggwseditablebrand;

  @Column(name = "SAGGWSPRICEDISCFILTER")
  private String saggwspricediscfilter;

  @Column(name = "SALESCURRENCYCODE")
  private String salescurrencycode;

  @Column(name = "SALESMEMO")
  private String salesmemo;

  @Column(name = "SALESORDERPOOLID")
  private String salesorderpoolid;

  @Column(name = "SALESTAXGROUP")
  private String salestaxgroup;

  @Column(name = "SITEID")
  private String siteid;

  @Column(name = "TAXEXEMPTNUMBER")
  private String taxexemptnumber;

  @Column(name = "WAREHOUSEID")
  private String warehouseid;

}
