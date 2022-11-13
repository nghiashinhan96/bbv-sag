package com.sagag.services.tools.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sagag.services.tools.domain.elasticsearch.VehicleDoc;

@Repository
public interface VehicleSearchRepostory extends ElasticsearchRepository<VehicleDoc, String> {
}
