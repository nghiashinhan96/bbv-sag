package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.VehicleGenArtService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtDoc;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test class for Elasticsearh service for Vehicle Generic Article.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class VehicleGenArtServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private VehicleGenArtService vehicleGenArtService;

  @Test
  public void testGetVehicleGenArts() {
    final String vehicleId = "V25287M2500";
    final List<VehicleGenArtDoc> vehicleGenArtDocs =
        vehicleGenArtService.getVehicleGenArts(vehicleId);
    Assert.assertThat(vehicleGenArtDocs.size(), Matchers.is(1));
  }

  @Test
  public void testFindGenericArticlesByVehicle_V25287M2500() {
    final String vehicleId = "V25287M2500";
    // gaids: [2, 321, 322, 455, 572, 757, 2048, 1862]
    Assert.assertThat(vehicleGenArtService.findGenericArticlesByVehicle(vehicleId).size(),
        Matchers.greaterThanOrEqualTo(12));
  }

  @Test
  public void testFindGenericArticlesByVehicle_V25577M0() {
    final String vehicleId = "V16869M17060";
    Assert.assertThat(vehicleGenArtService.findGenericArticlesByVehicle(vehicleId).size(),
        Matchers.greaterThanOrEqualTo(43));
  }

  @Test
  public void testFindGenericArticlesByVehicle_V25448M24882() {
    final String vehicleId = "V25448M24882";
    Assert.assertThat(vehicleGenArtService.findGenericArticlesByVehicle(vehicleId).size(),
        Matchers.greaterThanOrEqualTo(119));
  }
}
