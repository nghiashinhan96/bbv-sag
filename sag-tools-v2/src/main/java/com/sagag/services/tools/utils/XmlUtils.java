package com.sagag.services.tools.utils;

import lombok.experimental.UtilityClass;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

@UtilityClass
public final class XmlUtils {

  public static <T> String marshal(T data) {

    return marshal(data, false);
  }

  public static <T> String marshalWithPrettyMode(T data) {

    return marshal(data, true);
  }

  private static <T> String marshal(T data, boolean prettyMode) {

    try {
      final JAXBContext context = JAXBContext.newInstance(data.getClass());
      final Marshaller m = context.createMarshaller();
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

}
