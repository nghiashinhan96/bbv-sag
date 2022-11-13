package com.sagag.eshop.repo.entity.user_history;

import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_USER_ARTICLE_HISTORY")
@Data
public class VUserArticleHistory implements Serializable {

  private static final long serialVersionUID = 7785795987727039210L;

  @Id
  private Long id;

  private Long userId;

  private String userName;

  private String firstName;

  private String lastName;

  private String fullName;

  private Integer orgId;

  private String orgCode;

  private String articleId;

  private String artnrDisplay;

  private String searchTerm;

  private String rawSearchTerm;

  private String searchTermWithArtNr;

  private String searchMode;

  @Enumerated(EnumType.STRING)
  private UserHistoryFromSource fromSource;

  private Date selectDate;

}
