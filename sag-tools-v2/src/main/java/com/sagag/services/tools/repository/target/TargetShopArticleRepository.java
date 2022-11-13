package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.TargetShopArticle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetShopArticleRepository extends CrudRepository<TargetShopArticle, Long> {
}
