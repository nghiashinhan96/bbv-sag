package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sagag.services.common.enums.ArticleSearchMode;
import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ARTICLE_HISTORY")
@NamedQueries(value = {
    @NamedQuery(name = "UserArticleHistory.findAll", query = "SELECT g FROM UserArticleHistory g"),
    @NamedQuery(name = "UserArticleHistory.findByUserId",
        query = "SELECT g FROM UserArticleHistory g WHERE g.userId = :userid ORDER BY g.selectDate DESC") })
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(of = { "id", "articleHistory" })
public class UserArticleHistory implements Serializable {

  private static final long serialVersionUID = 5498693632831112993L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "ART_HISTORY_ID")
  private ArticleHistory articleHistory;

  @Column(name = "USER_ID")
  private long userId;

  @Column(name = "SELECT_DATE")
  private Date selectDate;

  @Column(name = "SEARCH_TERM")
  private String searchTerm;

  @Column(name = "RAW_SEARCH_TERM")
  private String rawSearchTerm;

  @Column(name = "SEARCH_MODE")
  @Enumerated(EnumType.STRING)
  private ArticleSearchMode searchMode;

  @Column(name = "FROM_SOURCE")
  @Enumerated(EnumType.STRING)
  private UserHistoryFromSource fromSource;

}
