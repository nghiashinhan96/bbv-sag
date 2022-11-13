package com.sagag.services.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@UtilityClass
@Slf4j
public final class SagJSONUtil {

  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();

    // Configuration for ignore unknown field when convert json string to object
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static <T> List<T> parse(JSONArray contentJsonArray, Class<T> objType) throws IOException {
    List<T> results = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    for (int i = 0; i < contentJsonArray.length(); i++) {
      try {
        results.add(objectMapper.readValue(contentJsonArray.getJSONObject(i).toString(), objType));
      } catch (JSONException ex) {
        log.error("JSON read value error {}", ex);
        // stop program when error even 1 item in the array
        throw new IllegalArgumentException();
      }
    }
    return results;
  }

  /**
   * Returns the object from json value.
   *
   * @return the data object
   */
  public static <T> T jsonToObject(final String json, final Class<T> clazz) {
    if (StringUtils.isBlank(json)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (IOException ioe) {
      log.error("Error while Json processing", ioe);
      return null;
    }
  }

  /**
   * Convert object to json string.
   *
   * @param object {@link Object}
   * @return json string
   *
   */
  public static String convertObjectToJson(Object object) {
    if (Objects.isNull(object)) {
      return StringUtils.EMPTY;
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException ex) {
      log.error("Convert list objects to json array has exception: ", ex);
      return StringUtils.EMPTY;
    }
  }

  /**
   * Convert object to json string.
   *
   * @param object {@link Object}
   * @return json string
   *
   */
  public static String convertObjectToPrettyJson(Object object) {
    if (Objects.isNull(object)) {
      return StringUtils.EMPTY;
    }
    try {
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException ex) {
      log.error("Convert list objects to json array has exception: ", ex);
      return StringUtils.EMPTY;
    }
  }

  public static String prettyLogJson(Object object) {
    return convertObjectToPrettyJson(object);
  }

  /**
   * Convert json string to a list object.
   *
   * @param json json string
   * @param clazz class
   * @return a list of clazz
   */
  public static <T> List<T> convertArrayJsonToList(String json, Class<T> clazz) {
    if (StringUtils.isBlank(json)) {
      return Collections.emptyList();
    }
    try {
      return OBJECT_MAPPER.readValue(json,
          OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (IOException ex) {
      log.error("Convert Json to list objects has exception: ", ex);
      return Collections.emptyList();
    }
  }

  /**
   * Convert json string to object.
   *
   * @param json json string
   * @param clazz class
   * @return {@link Class<T>}
   */
  public static <T> T convertJsonToObject(String json, Class<T> clazz) {

    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (IOException ex) {
      log.error("Convert Json to object has exception: ", ex);
      return null;
    }
  }

  /**
   * Returns the object from json value.
   *
   * @return the data object
   */
  public static <K, V> Map<K, V> jsonToMap(final String json) {
    if (StringUtils.isBlank(json)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, new TypeReference<Map<K, V>>() {});
    } catch (IOException ioe) {
      log.error("Error while Json processing", ioe);
      return null;
    }
  }

  public static <T> T readJsonFromUrl(String url, Class<T> clazz)
      throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      return OBJECT_MAPPER.readValue(is, clazz);
    } catch (IOException ex) {
      log.error("Convert Json to object has exception: ", ex);
      return null;
    } finally {
      is.close();
    }
  }

}
