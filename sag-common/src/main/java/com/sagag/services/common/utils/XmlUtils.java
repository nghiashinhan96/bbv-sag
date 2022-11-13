package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@UtilityClass
@Slf4j
public final class XmlUtils {

  private static final DocumentBuilderFactory DOC_FACTORY = DocumentBuilderFactory.newInstance();

  public static <T> String marshal(T data) {
    return marshal(data, false);
  }

  public static <T> String marshalWithPrettyMode(T data) {
    return marshal(data, true);
  }

  public static <T> String marshalWithPrettyMode(JAXBContext context, T data) {
    return marshal(context, data, true);
  }

  private static <T> String marshal(T data, boolean prettyMode) {
    try {
      final JAXBContext context = JAXBContext.newInstance(data.getClass());
      return marshal(context, data, prettyMode);
    } catch (JAXBException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private static <T> String marshal(final JAXBContext context, T data, boolean prettyMode) {
    try {
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyMode);
      final StringWriter writer = new StringWriter();
      m.marshal(data, writer);

      return writer.toString();
    } catch (JAXBException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T unmarshal(Class<T> clazz, String xml) {

    try {
      final JAXBContext context = JAXBContext.newInstance(clazz);
      final Unmarshaller um = context.createUnmarshaller();

      return (T) um.unmarshal(new StringReader(xml));
    } catch (JAXBException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static Optional<Document> createXmlDocument(final String unescapseXmlData,
      Charset charset) {
    if (StringUtils.isBlank(unescapseXmlData)) {
      return Optional.empty();
    }
    InputStream xmlFile = null;
    try {
      final DocumentBuilder builder = DOC_FACTORY.newDocumentBuilder();
      xmlFile = IOUtils.toInputStream(unescapseXmlData, charset);
      return Optional.of(builder.parse(xmlFile));
    } catch (ParserConfigurationException | SAXException | IOException ex) {
      log.error("Create XML Document has error", ex);
    } finally {
      IOUtils.closeQuietly(xmlFile);
    }
    return Optional.empty();
  }

  /**
   * Returns the optional value from JAXBElement.
   *
   * @param element
   * @return the optional value
   */
  public <T> Optional<T> getValueOpt(JAXBElement<T> element) {
    return Optional.ofNullable(element).map(JAXBElement::getValue);
  }
}
