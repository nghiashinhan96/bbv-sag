package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "WssBranch.findAll", query = "SELECT w FROM WssBranch w")
@Data
@Builder
@Table(name = "WSS_BRANCH")
@NoArgsConstructor
@AllArgsConstructor
public class WssBranch implements Serializable {

  private static final long serialVersionUID = 8437924851111524219L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "BRANCH_NUMBER", nullable = false)
  private Integer branchNr;

  @Column(name = "BRANCH_CODE", nullable = false)
  private String branchCode;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "OPENING_TIME", nullable = false)
  private Time openingTime;

  @Column(name = "CLOSING_TIME", nullable = false)
  private Time closingTime;

  @Column(name = "LUNCH_START_TIME")
  private Time lunchStartTime;

  @Column(name = "LUNCH_END_TIME")
  private Time lunchEndTime;

  @OneToMany(mappedBy = "wssBranch")
  private List<WssDeliveryProfile> wssDeliveryProfile;

  @OneToMany(mappedBy = "wssBranch")
  @JsonBackReference
  private List<WssBranchOpeningTime> wssBranchOpeningTimes;

  public WssBranch(Integer branchNr, String branchCode) {
    this.branchNr = branchNr;
    this.branchCode = branchCode;
  }
}
