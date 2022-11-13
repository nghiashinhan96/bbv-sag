package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceShopArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
@OracleProfile
public interface SourceShopArticleRepository extends JpaRepository<SourceShopArticle, Long> {
}
