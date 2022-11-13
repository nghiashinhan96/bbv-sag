package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentData;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionBaseItem;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class GtmotiveEquipmentOptionsResponseBuilder {

  private static final String CODE_TAG = "code";
  private static final String DESCRIPTION_TAG = "description";
  private static final String APPLICABILITY_TAG = "applicability";
  private static final String FAMILY_TAG = "family";
  private static final String SUB_FAMILY_TAG = "subfamily";
  private static final String MANUFACTURER_TAG = "manufacturerCode";
  private static final String INCOMPATIBILITY_GROUP_TAG = "incompatibilityGroup";
  private static final String INCOMPATIBILITY_GROUP_LIST_TAG = "incompatibilityGroupList";
  private static final String EQUIPMENT_LIST_EXPR =
      "/response/responseInfo/equipmentList/equipmentData";


  private String unescapseXmlData;

  public GtmotiveEquipmentOptionsResponseBuilder unescapseXmlData(final String unescapseXmlData) {
    this.unescapseXmlData = unescapseXmlData;
    return this;
  }

  public GtmotiveEquipmentOptionsResponse build()
      throws GtmotiveXmlResponseProcessingException, XPathExpressionException {
    final Optional<Document> doc =
        XmlUtils.createXmlDocument(unescapseXmlData, StandardCharsets.UTF_16);
    if (!doc.isPresent()) {
      throw new GtmotiveXmlResponseProcessingException("Invalid XML response");
    }
    return transferFromDocument(doc.get());
  }

  private GtmotiveEquipmentOptionsResponse transferFromDocument(final Document doc)
      throws XPathExpressionException {
    GtmotiveEquipmentOptionsResponse response = new GtmotiveEquipmentOptionsResponse();
    final XPathFactory xPathfactory = XPathFactory.newInstance();
    final XPath xpath = xPathfactory.newXPath();
    response.setEquipmentDatas(buildEquipmentList(doc, xpath));
    return response;
  }

  private List<GtmotiveEquipmentData> buildEquipmentList(Document doc, XPath xpath)
      throws XPathExpressionException {
    final XPathExpression equipmentExpr = xpath.compile(EQUIPMENT_LIST_EXPR);
    NodeList equipmentNodes = (NodeList) equipmentExpr.evaluate(doc, XPathConstants.NODESET);
    List<GtmotiveEquipmentData> equipments = new ArrayList<>();
    for (int i = 0; i <= equipmentNodes.getLength() - 1; i++) {
      equipments.add(buildEquipmentData((Element) equipmentNodes.item(i)));
    }
    return equipments;
  }

  private GtmotiveEquipmentData buildEquipmentData(Element ele) {
    GtmotiveEquipmentData equipmentData = new GtmotiveEquipmentData();

    Node applicabilityNode = ele.getElementsByTagName(APPLICABILITY_TAG).item(0);
    if (Objects.nonNull(applicabilityNode)) {
      GtmotiveEquipmentOptionBaseItem applicabilityItem =
          buildBaseItem((Element) applicabilityNode);
      if (!applicabilityItem.isEmpty()) {
        equipmentData.setApplicability(buildBaseItem((Element) applicabilityNode));
      }
    }

    Node familyNode = ele.getElementsByTagName(FAMILY_TAG).item(0);
    if (Objects.nonNull(familyNode)) {
      equipmentData.setFamily(buildBaseItem((Element) familyNode));
    }

    Node subFamilyNode = ele.getElementsByTagName(SUB_FAMILY_TAG).item(0);
    if (Objects.nonNull(subFamilyNode)) {
      equipmentData.setSubfamily(buildBaseItem((Element) subFamilyNode));
    }

    Node manufacturerCodeNode = ele.getElementsByTagName(MANUFACTURER_TAG).item(0);
    if (Objects.nonNull(manufacturerCodeNode)) {
      String manufacturerCode = ((Element) manufacturerCodeNode).getTextContent();
      if (StringUtils.isNotBlank(manufacturerCode)) {
        equipmentData.setManufacturerCode(manufacturerCode);
      }
    }

    Node incompatibilityGroupNode = ele.getElementsByTagName(INCOMPATIBILITY_GROUP_TAG).item(0);
    if (Objects.nonNull(incompatibilityGroupNode)) {
      String incompatibilityGroup = ((Element) incompatibilityGroupNode).getTextContent();
      if (StringUtils.isNotBlank(incompatibilityGroup)) {
        equipmentData.setIncompatibilityGroup(incompatibilityGroup);
      }
    }

    Node incompatibilityGroupListNode =
        ele.getElementsByTagName(INCOMPATIBILITY_GROUP_LIST_TAG).item(0);

    if (Objects.nonNull(incompatibilityGroupListNode)) {
      List<String> incompatibilityGroupList =
          buildIncompatibilityGroupList((Element) incompatibilityGroupListNode);
      if (CollectionUtils.isNotEmpty(incompatibilityGroupList)) {
        equipmentData.setIncompatibilityGroupList(incompatibilityGroupList);
      }
    }

    return equipmentData;
  }

  private GtmotiveEquipmentOptionBaseItem buildBaseItem(Element ele) {
    GtmotiveEquipmentOptionBaseItem item = new GtmotiveEquipmentOptionBaseItem();
    Node codeNode = ele.getElementsByTagName(CODE_TAG).item(0);
    if (Objects.nonNull(codeNode) && StringUtils.isNotEmpty(codeNode.getTextContent())) {
      item.setCode(codeNode.getTextContent());
    }

    Node descriptionNode = ele.getElementsByTagName(DESCRIPTION_TAG).item(0);
    if (Objects.nonNull(descriptionNode)
        && StringUtils.isNotEmpty(descriptionNode.getTextContent())) {
      item.setDescription(descriptionNode.getTextContent());
    }
    return item;
  }

  private List<String> buildIncompatibilityGroupList(Element ele) {
    List<String> incompatibilityGroupList = new ArrayList<>();
    NodeList incompatibilityGroupNodes = ele.getElementsByTagName(INCOMPATIBILITY_GROUP_TAG);
    for (int i = 0; i <= incompatibilityGroupNodes.getLength() - 1; i++) {
      String incompatibilityGroup = incompatibilityGroupNodes.item(i).getTextContent();
      if (StringUtils.isNotBlank(incompatibilityGroup)) {
        incompatibilityGroupList.add(incompatibilityGroup);
      }
    }
    return incompatibilityGroupList;
  }
}
