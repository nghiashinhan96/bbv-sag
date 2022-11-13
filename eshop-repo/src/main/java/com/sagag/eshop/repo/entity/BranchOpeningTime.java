package com.sagag.eshop.repo.entity;

import com.sagag.services.common.enums.WeekDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NamedQuery(name = "BranchOpeningTime.findAll", query = "SELECT bo FROM BranchOpeningTime bo")
@Data
@Builder
@Table(name = "BRANCH_OPENING_TIME")
@NoArgsConstructor
@AllArgsConstructor
public class BranchOpeningTime implements Serializable {

  private static final long serialVersionUID = 8183212810091754151L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "BRANCH_ID")
  private Integer branchId;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "WEEK_DAY")
  private WeekDay weekDay;

  @Column(name = "OPENING_TIME", nullable = false)
  private Time openingTime;

  @Column(name = "CLOSING_TIME", nullable = false)
  private Time closingTime;

  @Column(name = "LUNCH_START_TIME")
  private Time lunchStartTime;

  @Column(name = "LUNCH_END_TIME")
  private Time lunchEndTime;

}
