package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.ArticleHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHistoryRepository extends JpaRepository<ArticleHistory, Long> {
}
