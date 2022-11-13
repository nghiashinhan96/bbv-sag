package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePart;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

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

public class GtmotivePartsListResponseBuilder {

  private static final String FUNCTIONAL_GROUP_DESCRIPTION_TAG = "functionalGroupDescription";
  private static final String FUNCTIONAL_GROUP_TAG = "functionalGroup";
  private static final String PART_DESCRIPTION_TAG = "partDescription";
  private static final String PART_CODE_TAG = "partCode";
  private String unescapseXmlData;

  public GtmotivePartsListResponseBuilder unescapseXmlData(final String unescapseXmlData) {
    this.unescapseXmlData = unescapseXmlData;
    return this;
  }

  public GtmotivePartsListResponse build()
      throws GtmotiveXmlResponseProcessingException, XPathExpressionException {

    final Optional<Document> doc =
        XmlUtils.createXmlDocument(unescapseXmlData, StandardCharsets.UTF_16);
    if (!doc.isPresent()) {
      throw new GtmotiveXmlResponseProcessingException("Invalid XML response");
    }
    return transferFromDocument(doc.get());
  }

  private GtmotivePartsListResponse transferFromDocument(final Document doc)
      throws XPathExpressionException {
    GtmotivePartsListResponse response = new GtmotivePartsListResponse();
    final XPathFactory xPathfactory = XPathFactory.newInstance();
    final XPath xpath = xPathfactory.newXPath();
    response.setParts(buildPartList(doc, xpath));
    return response;
  }

  private List<GtmotivePart> buildPartList(Document doc, XPath xpath)
      throws XPathExpressionException {

    final XPathExpression partExpr = xpath.compile("/response/requestedPartsList/partsList/part");
    NodeList partNodes = (NodeList) partExpr.evaluate(doc, XPathConstants.NODESET);
    List<GtmotivePart> parts = new ArrayList<>();

    for (int i = 0; i <= partNodes.getLength() - 1; i++) {
      parts.add(fromElementToGtmotivePart((Element) partNodes.item(i)));
    }
    return parts;
  }

  private GtmotivePart fromElementToGtmotivePart(Element ele) {
    GtmotivePart part = new GtmotivePart();

    Node partCodeNode = ele.getElementsByTagName(PART_CODE_TAG).item(0);
    if (Objects.nonNull(partCodeNode) && StringUtils.isNotEmpty(partCodeNode.getTextContent())) {
      part.setPartCode(partCodeNode.getTextContent());
    }

    Node partDescriptionNode = ele.getElementsByTagName(PART_DESCRIPTION_TAG).item(0);
    if (Objects.nonNull(partDescriptionNode)
        && StringUtils.isNotEmpty(partDescriptionNode.getTextContent())) {
      part.setPartDescription(partDescriptionNode.getTextContent());
    }

    Node functionalGroupNode = ele.getElementsByTagName(FUNCTIONAL_GROUP_TAG).item(0);
    if (Objects.nonNull(functionalGroupNode)
        && StringUtils.isNotEmpty(functionalGroupNode.getTextContent())) {
      part.setFunctionalGroup(functionalGroupNode.getTextContent());
    }

    Node functionalGroupDescriptionNode =
        ele.getElementsByTagName(FUNCTIONAL_GROUP_DESCRIPTION_TAG).item(0);
    if (Objects.nonNull(functionalGroupDescriptionNode)
        && StringUtils.isNotEmpty(functionalGroupDescriptionNode.getTextContent())) {
      part.setFunctionalGroupDescription(functionalGroupDescriptionNode.getTextContent());
    }

    return part;
  }
}
