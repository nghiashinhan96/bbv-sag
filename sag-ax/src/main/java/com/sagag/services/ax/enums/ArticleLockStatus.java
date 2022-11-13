package com.sagag.services.ax.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration class to define article lock status from AX.
 *
 */
@Getter
@AllArgsConstructor
public enum ArticleLockStatus {

  TRUE("Article locked"), FALSE("Article not locked");

  private String desc;
}
