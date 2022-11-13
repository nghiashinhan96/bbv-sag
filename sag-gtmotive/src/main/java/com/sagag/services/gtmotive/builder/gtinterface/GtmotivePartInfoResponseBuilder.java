package com.sagag.services.gtmotive.builder.gtinterface;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotivePartInfoOperation;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotivePartInfoResponse;
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

public class GtmotivePartInfoResponseBuilder {

  private static final String SHORT_NUMBER_TAG = "shortNumber";
  private static final String PART_NUMBER_TAG = "partNumber";
  private static final String DESCRIPTION_TAG = "description";
  private static final String IS_MULTI_REFERENCE_TAG = "isMultiReference";
  private static final String REFERENCE_TAG = "reference";

  private String unescapseXmlData;

  public GtmotivePartInfoResponseBuilder unescapseXmlData(final String unescapseXmlData) {
    this.unescapseXmlData = unescapseXmlData;
    return this;
  }

  public GtmotivePartInfoResponse build()
      throws GtmotiveXmlResponseProcessingException, XPathExpressionException {
    final Optional<Document> doc =
        XmlUtils.createXmlDocument(unescapseXmlData, StandardCharsets.UTF_8);
    if (!doc.isPresent()) {
      throw new GtmotiveXmlResponseProcessingException("Invalid XML response");
    }
    return transferFromDocument(doc.get());
  }

  private GtmotivePartInfoResponse transferFromDocument(final Document doc)
      throws XPathExpressionException {
    GtmotivePartInfoResponse response = new GtmotivePartInfoResponse();
    final XPathFactory xPathfactory = XPathFactory.newInstance();
    final XPath xpath = xPathfactory.newXPath();
    response.setOperations(buildOperations(doc, xpath));
    return response;
  }

  private List<GtmotivePartInfoOperation> buildOperations(Document doc, XPath xpath)
      throws XPathExpressionException {
    final XPathExpression partExpr = xpath.compile("/estimate/operationList/operation");
    NodeList operationNodes = (NodeList) partExpr.evaluate(doc, XPathConstants.NODESET);
    List<GtmotivePartInfoOperation> operations = new ArrayList<>();
    for (int i = 0; i <= operationNodes.getLength() - 1; i++) {
      operations.add(fromElementToOperation((Element) operationNodes.item(i)));
    }
    return operations;
  }

  private GtmotivePartInfoOperation fromElementToOperation(Element ele) {
    GtmotivePartInfoOperation operation = new GtmotivePartInfoOperation();

    Node shortNumberNode = ele.getElementsByTagName(SHORT_NUMBER_TAG).item(0);
    if (Objects.nonNull(shortNumberNode)
        && StringUtils.isNotBlank(shortNumberNode.getTextContent())) {
      operation.setShortNumber(shortNumberNode.getTextContent());
    }

    Node partNumberNode = ele.getElementsByTagName(PART_NUMBER_TAG).item(0);
    if (Objects.nonNull(partNumberNode)
        && StringUtils.isNotBlank(partNumberNode.getTextContent())) {
      operation.setPartNumber(partNumberNode.getTextContent());
    }

    Node isMultiReferenceNode = ele.getElementsByTagName(IS_MULTI_REFERENCE_TAG).item(0);
    if (Objects.nonNull(isMultiReferenceNode)
        && StringUtils.isNotBlank(isMultiReferenceNode.getTextContent())) {
      operation.setMultiReference(new Boolean(isMultiReferenceNode.getTextContent()));
    }

    Node descriptionNode = ele.getElementsByTagName(DESCRIPTION_TAG).item(0);
    if (Objects.nonNull(descriptionNode)) {
      operation.setDescription(buildSimpleAttributeValue((Element) descriptionNode));
    }

    Node referenceNode = ele.getElementsByTagName(REFERENCE_TAG).item(0);
    if (Objects.nonNull(referenceNode) && StringUtils.isNotBlank(referenceNode.getTextContent())) {
      operation.setReference(referenceNode.getTextContent());
    }

    return operation;
  }

  private String buildSimpleAttributeValue(final Element element) {
    final Node valueNode = element.getElementsByTagName("value").item(0);
    if (Objects.isNull(valueNode) || StringUtils.isBlank(valueNode.getTextContent())) {
      return StringUtils.EMPTY;
    }
    return valueNode.getTextContent();
  }
}
