package com.sagag.services.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.domain.eshop.dto.TypeItem;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleCode;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.rest.app.RestApplication;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Integration tests class for Vehicle REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class VehicleControllerIT extends AbstractControllerIT {

  private static final String SEARCH_ROOT_URL = "/search";

  private static final String CONTENT = "content";

  @Autowired
  @Qualifier("makeCacheServiceImpl")
  private CacheDataProcessor makeCacheDataProcessor;
  @Autowired
  @Qualifier("modelCacheServiceImpl")
  private CacheDataProcessor modelCacheDataProcessor;

  @Before
  public void initCache() {
    makeCacheDataProcessor.refreshCacheAll();
    modelCacheDataProcessor.refreshCacheAll();
  }

  @Test
  public void shouldForbiddenSearchVehicleByVehicleCode() throws Exception {
    performGet(SEARCH_ROOT_URL + "/vehicles/1000000020967995717").andExpect(status().isForbidden());
  }

  @Test
  public void shouldSearchVehicleByVehicleCode() throws Exception {
    performGet(SEARCH_ROOT_URL + "/vehicles/25980").andExpect(status().isOk());

  }

  @Test
  public void testSearchVehicleByVehId() throws Exception {
    performGet(SEARCH_ROOT_URL + "/vehicles/type/Id/V866M4759").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.refs", Matchers.hasSize(1)))
        .andExpect(jsonPath("$.refs[0].vehid", Is.is("V866M4759")));
  }

  @Test
  public void testGetMakeList() throws Exception {

    String result = performGet(SEARCH_ROOT_URL + "/vehicles/makes").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
        .getContentAsString();
    JSONArray contentJsonArray = new JSONArray(result);
    List<MakeItem> makeItems = SagJSONUtil.parse(contentJsonArray, MakeItem.class);

    List<MakeItem> makeItemsExpect = new ArrayList<>();
    makeItemsExpect.addAll(makeItems.stream()
        .filter(makeItem -> Objects.equals(makeItem.getMakeId(), 25)).collect(Collectors.toList()));
    Assert.assertThat(makeItemsExpect.size(), Matchers.greaterThan(0));
    Assert.assertThat(makeItemsExpect.size(), Matchers.is(1));
    Assert.assertThat(makeItemsExpect.get(0).getMake(), Matchers.is("DAIHATSU"));
  }

  @Test
  public void shouldForbiddenGetMakeList() throws Exception {
    performGet(SEARCH_ROOT_URL + "/vehicles/makes").andExpect(status().isForbidden());
  }

  @Test
  public void testGetModelList() throws Exception {
    String result = performGet(SEARCH_ROOT_URL + "/vehicles/84/models").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
        .getContentAsString();

    JSONArray contentJsonArray = new JSONArray(result);
    List<ModelItem> modelItems = SagJSONUtil.parse(contentJsonArray, ModelItem.class);

    List<ModelItem> vehicleTypesExpect = new ArrayList<>();
    vehicleTypesExpect
        .addAll(modelItems.stream().filter(modelItem -> Objects.equals(modelItem.getModelId(), 41))
            .collect(Collectors.toList()));
    Assert.assertThat(vehicleTypesExpect.size(), Matchers.greaterThan(0));

  }

  @Test
  public void shouldForbiddenGetModelList() throws Exception {
    performGet(SEARCH_ROOT_URL + "/vehicles/84/models").andExpect(status().isForbidden());
  }

  @Test
  public void testGetTypeList() throws Exception {
    String result =
        performGet(SEARCH_ROOT_URL + "/vehicles/types?make=84&model=371").andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn()
            .getResponse().getContentAsString();

    JSONArray contentJsonArray = new JSONArray(result);
    List<TypeItem> vehicleTypes = SagJSONUtil.parse(contentJsonArray, TypeItem.class);

    Assert.assertNotNull(vehicleTypes);
    Assert.assertNotNull(vehicleTypes.get(0));
    Assert.assertNotNull(vehicleTypes.get(0).getVehId());
    Assert.assertThat(vehicleTypes.get(0).getVehId(), Matchers.is("V805M4723"));


  }

  @Test
  public void shouldForbiddenGetTypeList() throws Exception {
    performGet(SEARCH_ROOT_URL + "/vehicles/types?make=84&model=371")
        .andExpect(status().isForbidden());
  }

  @Test
  public void testSearchVehicleByVehicleCode() throws Exception {
    String result =
        performGet(SEARCH_ROOT_URL + "/vehicles/1000000000193488024").andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn()
            .getResponse().getContentAsString();

    JSONObject contentJson = new JSONObject(result);
    JSONArray contentJsonArray = contentJson.getJSONArray(CONTENT);
    List<VehicleDoc> vehicleDocs = SagJSONUtil.parse(contentJsonArray, VehicleDoc.class);
    List<VehicleCode> vehicleCodes = new ArrayList<>();
    vehicleDocs.stream()
        .forEach(vehicleDoc -> vehicleCodes.addAll(vehicleDoc.getCodes().stream()
            .filter(ref -> Objects.equals("1000000000193488024", ref.getVehCodeValue()))
            .collect(Collectors.toList())));
    Assert.assertThat(vehicleCodes.size(), Matchers.greaterThan(0));

  }

  @Test
  public void testSearchVehicleByPlateNumber() throws Exception {
    String result =
        performGet(SEARCH_ROOT_URL + "/plate-number/SZ104289").andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn()
            .getResponse().getContentAsString();
    JSONObject contentJson = new JSONObject(result);
    JSONArray contentJsonArray = contentJson.getJSONArray(CONTENT);

    List<VehicleDoc> vehicleDocs = SagJSONUtil.parse(contentJsonArray, VehicleDoc.class);

    List<VehicleCode> vehicleCodes = new ArrayList<>();
    vehicleDocs.stream()
        .forEach(vehicleDoc -> vehicleCodes.addAll(vehicleDoc.getCodes().stream()
            .filter(ref -> Objects.equals("1VE619", ref.getVehCodeValue()))
            .collect(Collectors.toList())));
    Assert.assertThat(vehicleCodes.size(), Matchers.greaterThan(0));
  }

  @Test
  public void testSearchMultipleVehiclesByPlateNumber() throws Exception {
    String result = performGet(SEARCH_ROOT_URL + "/plate-number/ZH56541").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
        .getContentAsString();
    JSONObject contentJson = new JSONObject(result);
    JSONArray contentJsonArray = contentJson.getJSONArray(CONTENT);

    List<VehicleDoc> vehicleDocs = SagJSONUtil.parse(contentJsonArray, VehicleDoc.class);

    List<VehicleCode> vehicleCodes = new ArrayList<>();
    vehicleDocs.stream()
        .forEach(
            vehicleDoc -> vehicleCodes.addAll(vehicleDoc.getCodes().stream()
                .filter(ref -> Objects.equals("1AA311", ref.getVehCodeValue())
                    || Objects.equals("1SA276", ref.getVehCodeValue()))
                .collect(Collectors.toList())));
    Assert.assertThat(vehicleCodes.size(), Matchers.greaterThan(1));
  }
}
