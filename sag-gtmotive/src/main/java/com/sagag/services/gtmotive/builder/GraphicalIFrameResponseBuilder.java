package com.sagag.services.gtmotive.builder;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.domain.response.GraphicalIFrameResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@Slf4j
public class GraphicalIFrameResponseBuilder {

  private String unescapseXmlData;

  private String estimateId;

  public GraphicalIFrameResponseBuilder unescapseXmlData(final String unescapseXmlData) {
    this.unescapseXmlData = unescapseXmlData;
    return this;
  }

  public GraphicalIFrameResponseBuilder estimateId(final String estimateId) {
    this.estimateId = estimateId;
    return this;
  }

  public GraphicalIFrameResponse build() throws GtmotiveXmlResponseProcessingException {
    final Optional<Document> doc =
        XmlUtils.createXmlDocument(unescapseXmlData, StandardCharsets.UTF_8);
    if (!doc.isPresent()) {
      throw new GtmotiveXmlResponseProcessingException("Invalid XML response");
    }
    return transferFromDocument(doc.get(), estimateId);
  }

  private static GraphicalIFrameResponse transferFromDocument(final Document doc,
    final String estimateId)
    throws GtmotiveXmlResponseProcessingException {

    final GraphicalIFrameResponse response = new GraphicalIFrameResponse();
    try {
      final XPathFactory xPathfactory = XPathFactory.newInstance();
      final XPath xpath = xPathfactory.newXPath();
      response.setUrl(String.valueOf(
        xpath.compile("/codeurl/url/text()").evaluate(doc, XPathConstants.STRING)).trim());
      if (StringUtils.isBlank(response.getUrl())) { // error while getting URL
        final String errorDesc =
          String.valueOf(
            xpath.compile("/error/errorInfo/description/text()").evaluate(doc,
              XPathConstants.STRING)).trim();
        // Keep this error message for tracking only
        response.setErrorMessage(errorDesc);
        final Number errorCode =
          (Number) xpath.compile("/error/errorInfo/code/text()").evaluate(doc,
            XPathConstants.NUMBER);
        response.setErrorCode(errorCode.intValue());
      }
    } catch (XPathExpressionException xpathEx) {
      log.error("Error while building the response from XML", xpathEx);
      throw new GtmotiveXmlResponseProcessingException("Xpath expression error", xpathEx);
    }
    response.setEstimateId(estimateId);
    return response;
  }
}
