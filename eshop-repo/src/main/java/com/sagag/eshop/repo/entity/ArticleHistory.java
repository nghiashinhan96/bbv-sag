package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ARTICLE_HISTORY")
@NamedQueries(value = {
    @NamedQuery(name = "ArticleHistory.findAll", query = "SELECT v FROM ArticleHistory v") })
public class ArticleHistory implements Serializable {

  private static final long serialVersionUID = 7005251857358758657L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private long id;

  @Column(name = "ART_NUMBER")
  private String articleNumber;

  @Column(name = "ART_ID")
  private String articleId;

  @Column(name = "ART_SAG_SYS_ID")
  private String articleIdSagSys;

  @Column(name = "MANUFACTURE")
  private String manufacture;

  @Column(name = "ART_IMG")
  private String articleImage;

  @Column(name = "VEHICLE_INFO")
  private String vehicleInfo;

  @OneToMany(mappedBy = "articleHistory")
  @JsonManagedReference
  private List<UserArticleHistory> userArticleHistory;

}
