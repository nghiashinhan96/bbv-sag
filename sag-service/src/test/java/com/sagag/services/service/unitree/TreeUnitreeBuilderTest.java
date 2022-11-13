package com.sagag.services.service.unitree;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.FunctionDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.unitree.dto.TileExternalDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.elasticsearch.domain.unitree.Criteria;
import com.sagag.services.elasticsearch.domain.unitree.Exclude;
import com.sagag.services.elasticsearch.domain.unitree.Filter;
import com.sagag.services.elasticsearch.domain.unitree.Include;
import com.sagag.services.elasticsearch.domain.unitree.Tile;
import com.sagag.services.elasticsearch.domain.unitree.TileExternal;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeBarFilter;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeNode;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.ivds.converter.unitree.UnitreeConverter;
import com.sagag.services.service.uniparts.unitree.impl.TreeUnitreeBuilderImpl;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

// TODO (@cungnguyen): Please maintain testcases
@RunWith(SpringRunner.class)
@Slf4j
@Ignore("Need maintain testcase")
public class TreeUnitreeBuilderTest {

  private final UnitreeDoc unitreeDoc = buildDoc();

  @InjectMocks
  private TreeUnitreeBuilderImpl unitreeBuilder;

  @Mock
  private UserInfo user;

  @Mock
  private BrandPriorityCacheService brandPriorityCacheService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    SupportedAffiliate wbbAffiliate = SupportedAffiliate.WBB;
    List<PermissionDto> perms = Arrays
        .asList(
            PermissionDto.builder()
                .functions(
                    Arrays.asList(FunctionDto.builder().functionName("OIL_URL_ACCESS").build()))
                .build(),
            PermissionDto.builder()
                .functions(
                    Arrays.asList(FunctionDto.builder().functionName("BATTERY_URL_ACCESS").build()))
                .build());
    when(user.getOriginalUserId()).thenReturn(1l);
    when(user.getPermissions()).thenReturn(perms);
    when(user.getSupportedAffiliate()).thenReturn(wbbAffiliate);
    UnitreeNodeDto node = new UnitreeNodeDto();
    node.setId(StringUtils.EMPTY);
    node.setParentId(StringUtils.EMPTY);

    node.setChildren(new PriorityQueue<>(
        Lists.newArrayList(SagBeanUtils.map(node, UnitreeNodeDto.class))));
    when(UnitreeConverter.toDto(any())).thenReturn(node);
  }

  private UnitreeDoc buildDoc() {
    UnitreeDoc doc = unitreeDoc;
    if (doc == null) {
      doc = new UnitreeDoc();
    }

    doc.setId("1");
    doc.setTreeName("Tree");
    doc.setTiles(buildTiles());
    doc.setUnitreeNodes(buildNodes());
    return doc;
  }

  @Test
  public void testBuildUnitreeSuccess() {
    final UnitreeNodeDto actual = unitreeBuilder.apply(unitreeDoc, user);
    log.debug("Response Unitree = {}", SagJSONUtil.convertObjectToPrettyJson(actual));
    Assert.assertThat(actual, Matchers.notNullValue());

    // Node Parent
    Assert.assertEquals("10", actual.getId());
    Assert.assertThat(actual.getTiles().size(), Matchers.equalTo(1));
    Assert.assertEquals("t1", actual.getTiles().peek().getId());
    Assert.assertEquals("Tile1", actual.getTiles().peek().getTileName());
    List<TileExternalDto> externalDtos = actual.getTiles().peek().getTileExternals().stream()
        .filter(
            x -> Objects.equals(x.getTileLink(), "te1") || Objects.equals(x.getTileLink(), "te2"))
        .collect(Collectors.toList());
    Assert.assertThat(externalDtos.size(), Matchers.equalTo(2));

    Assert.assertEquals(externalDtos.get(0).isActiveLink(), true);
    Assert.assertEquals(externalDtos.get(1).isActiveLink(), false);
    Assert.assertEquals(true, externalDtos.get(0).isActiveLink());
    Assert.assertEquals(false, externalDtos.get(1).isActiveLink());

    // Node child ID: 20
    Assert.assertThat(actual.getChildren().size(), Matchers.equalTo(1));
    UnitreeNodeDto node20 = actual.getChildren().peek();
    Assert.assertEquals("20", node20.getId());
    Assert.assertEquals("Node2", node20.getNodeName());

    Assert.assertThat(node20.getTiles().size(), Matchers.equalTo(1));
    Assert.assertEquals("t2", node20.getTiles().peek().getId());
    Assert.assertEquals("Tile2", node20.getTiles().peek().getTileName());
    Assert.assertEquals(true, node20.isActiveLink());
    Assert.assertEquals(true, node20.isActiveLink());

    Assert.assertEquals(2, node20.getInclude().getCriteria().size());
    Assert.assertEquals("cid1", node20.getInclude().getCriteria().get(0).getCid());


    // Node child ID : 30
    Assert.assertThat(node20.getChildren().size(), Matchers.equalTo(1));
    UnitreeNodeDto node30 = node20.getChildren().peek();
    Assert.assertEquals("30", node30.getId());
    Assert.assertEquals("Node3", node30.getNodeName());
    Assert.assertEquals(true, node30.isActiveLink());
    Assert.assertEquals(2, node30.getFilters().size());
    Assert.assertEquals("gaid1", node30.getFilters().get(0).getGaid());
    Assert.assertEquals("filterBar1", node30.getFilters().get(0).getFilter().get(0).getFilterBar());
  }

  private List<Tile> buildTiles() {
    Tile tile1 = new Tile();
    Tile tile2 = new Tile();

    tile1.setId("t1");
    tile1.setTileName("Tile1");
    tile1.setTileNodeLink("10");
    tile1.setTileExternals(buildTileExternals());

    tile2.setId("t2");
    tile2.setTileName("Tile2");
    tile2.setTileNodeLink("20");
    tile2.setTileExternals(buildTileExternals());

    return Arrays.asList(tile1, tile2);
  }

  private List<TileExternal> buildTileExternals() {
    TileExternal tile1 = new TileExternal();
    TileExternal tile2 = new TileExternal();

    tile1.setTileLink("te1");
    tile1.setTileLinkText("Oil-Shop");
    tile1.setTileLinkType("shop");
    tile1.setTileLinkAttr("oil");

    tile2.setTileLink("te2");
    tile2.setTileLinkText("Tyre - Shop");
    tile2.setTileLinkType("shop");
    tile2.setTileLinkAttr("tyre");

    return Arrays.asList(tile1, tile2);
  }

  private List<Criteria> buildCriteria() {
    Criteria criteria1 = new Criteria();
    Criteria criteria2 = new Criteria();
    criteria1.setCid("cid1");
    criteria1.setCvp("cvp1");
    criteria2.setCid("cid2");
    criteria2.setCvp("cvp2");

    return Arrays.asList(criteria1, criteria2);
  }

  private List<Filter> buildFilter() {
    Filter filter1 = new Filter();
    Filter filter2 = new Filter();
    UnitreeBarFilter barFilter1 = new UnitreeBarFilter();
    barFilter1.setFilterBar("filterBar1");
    filter1.setGaid("gaid1");
    filter1.setFilter(Arrays.asList(barFilter1));
    filter2.setGaid("gaid2");
    filter2.setFilter(Collections.emptyList());
    return Arrays.asList(filter1, filter2);
  }

  private List<UnitreeNode> buildNodes() {
    UnitreeNode node1 = new UnitreeNode();
    UnitreeNode node2 = new UnitreeNode();
    UnitreeNode node3 = new UnitreeNode();

    node1.setLeafId("10");
    node1.setParentId("10");
    node1.setNodeName("Node1");
    node1.setInclude(new Include());
    node1.setExclude(new Exclude());

    node2.setLeafId("20");
    node2.setParentId("10");
    node2.setNodeName("Node2");
    node2.setInclude(new Include());
    node2.getInclude().setCriteria(buildCriteria());
    node2.setExclude(new Exclude());

    node3.setLeafId("30");
    node3.setParentId("20");
    node3.setNodeExternalType("shop");
    node3.setNodeExternalServiceAttribute("battery");
    node3.setNodeName("Node3");
    node3.setInclude(new Include());
    node3.setExclude(new Exclude());
    node3.setFilters(buildFilter());
    return Arrays.asList(node1, node2, node3);
  }
}
