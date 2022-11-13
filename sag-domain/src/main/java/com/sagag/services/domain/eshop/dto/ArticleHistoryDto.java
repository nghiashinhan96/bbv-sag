package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Article history Dto class.
 */
@Data
@Builder
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@AllArgsConstructor
public class ArticleHistoryDto implements Serializable {

  private static final long serialVersionUID = -4674474846243895328L;

  private Long id;

  @JsonProperty("article_id")
  private String articleId;

  @JsonProperty("search_term")
  private String searchTerm;

  @JsonProperty("artnr_display")
  private String artnrDisplay;

  private String searchMode;

  @JsonProperty("select_date")
  private Date selectDate;

  private String fullName;

  private boolean createdBySales;

  private String searchTermWithArtNr;

  private String rawSearchTerm;

  /**
   * Constructs the article dto from article info.
   *
   * @param articleId the article id
   * @param articleName the article name
   * @param selectDate the select date
   */
  public ArticleHistoryDto(final String articleId, final String artnrDisplay,
      final Date selectDate) {
    this.articleId = articleId;
    this.artnrDisplay = artnrDisplay;
    this.selectDate = selectDate;
  }

  public ArticleHistoryDto(final String articleId, final String artnrDisplay, final Date selectDate,
      String searchTerm, String searchMode, UserHistoryFromSource fromSource) {
    this(articleId, artnrDisplay, selectDate);
    this.searchTerm = StringUtils.defaultString(searchTerm);
    this.searchMode = searchMode;
    this.createdBySales = fromSource.isSalesOnbehalfMode();
  }

  public ArticleHistoryDto(String articleId, String artnrDisplay, Date selectDate,
      String searchTerm, String searchMode, String fullName,
      UserHistoryFromSource fromSource) {
    this(articleId, artnrDisplay, selectDate, searchTerm, searchMode, fromSource);
    this.fullName = fullName;
  }

  public ArticleHistoryDto(String articleId, String artnrDisplay, Date selectDate,
      String searchTerm, String searchMode, String fullName,
      UserHistoryFromSource fromSource, String searchTermWithArtNr) {
    this(articleId, artnrDisplay, selectDate, searchTerm, searchMode, fullName, fromSource);
    this.searchTermWithArtNr = searchTermWithArtNr;
  }

  public ArticleHistoryDto(long id, String articleId, String artnrDisplay, Date selectDate,
      String searchTerm, String searchMode, String fullName,
      UserHistoryFromSource fromSource, String searchTermWithArtNr) {
    this(articleId, artnrDisplay, selectDate, searchTerm, searchMode, fullName, fromSource, searchTermWithArtNr);
    this.id = id;
  }

  public ArticleHistoryDto(long id, String articleId, String artnrDisplay, Date selectDate,
      String searchTerm, String searchMode, String fullName,
      UserHistoryFromSource fromSource, String searchTermWithArtNr, String rawSearchTerm) {
    this(id, articleId, artnrDisplay, selectDate, searchTerm, searchMode, fullName, fromSource, searchTermWithArtNr);
    this.rawSearchTerm = rawSearchTerm;
  }
}
