package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.ArticleHistory;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Article history JPA Repository interface.
 */
public interface ArticleHistoryRepository extends JpaRepository<ArticleHistory, Long> {

}
