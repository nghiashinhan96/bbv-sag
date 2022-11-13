package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

@Component
@GtmotiveProfile
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GtmotivePartsThreeReferencesCaseThreeHandler
    extends AbstractGtmotivePartsThreeReferencesHandler {

  private static final String REFERENCE_CODE_EXPR =
      "/response/requestedData/partDetails/reference/referenceCode";

  private static final String INCLUDED_PART_EXPR =
      "/response/requestedData/partDetails/reference/includedParts/includedPart";

  private static final String[] TEXT_REFERENCE =
      { "AUFLISTUNG EINZELTEILE", "VEDERE VISTA ESPLOSA", "VOIR DÉGARNISSAGE" };

  public boolean check(Document doc, XPath xpath) throws XPathExpressionException {
    final XPathExpression newReferenceExpr = xpath.compile(REFERENCE_CODE_EXPR);
    Node node = (Node) newReferenceExpr.evaluate(doc, XPathConstants.NODE);
    return !Objects.isNull(node) && Arrays.asList(TEXT_REFERENCE).contains(node.getTextContent());
  }

  @Override
  protected List<GtmotivePartsThreeReference> achieveReferences(Document doc, XPath xpath)
      throws XPathExpressionException {
    if (check(doc, xpath)) {
      return achieveReferences(INCLUDED_PART_EXPR, doc, xpath);
    }
    return Collections.emptyList();
  }
}
