package com.sagag.services.ivds.request;

import com.sagag.services.domain.article.ArticleAccessoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Request body class for WSP search.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessorySearchRequest implements Serializable{
  private static final long serialVersionUID = 9086273805804218999L;
  
  private List<ArticleAccessoryDto> accessoryList;
}
