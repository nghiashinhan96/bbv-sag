package com.sagag.services.tools.batch.customer.settings;

import com.sagag.services.tools.domain.target.CustomerSettings;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.persistence.Column;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.ReflectionUtils;

@UtilityClass
public class CustomerSettingsTypeMapper {

  private static final Class<?> CLAZZ = CustomerSettings.class;

  private static final Class<Column> ANNOTATION_CLS = Column.class;

  private static final Map<Class<?>, Function<String, Object>> typeMappers = loadTypeMappers();

  private static Map<Class<?>, Function<String, Object>> loadTypeMappers() {
    Map<Class<?>, Function<String, Object>> map = new HashMap<>();
    map.put(String.class, obj -> String.valueOf(obj));

    final Function<String, Object> booleanMapper = BooleanUtils::toBoolean;
    map.put(Boolean.class, booleanMapper);
    map.put(boolean.class, booleanMapper);

    final Function<String, Object> intMapper = obj -> new BigDecimal(obj).intValue();
    map.put(int.class, intMapper);
    map.put(Integer.class, intMapper);

    final Function<String, Object> longMapper = obj -> new BigDecimal(obj).longValue();
    map.put(long.class, longMapper);
    map.put(Long.class, longMapper);

    final Function<String, Object> floatMapper = obj -> new BigDecimal(obj).floatValue();
    map.put(float.class, floatMapper);
    map.put(Float.class, floatMapper);

    final Function<String, Object> doubleMapper = obj -> new BigDecimal(obj).doubleValue();
    map.put(double.class, doubleMapper);
    map.put(Double.class, doubleMapper);
    return map;
  }

  public static Method findMethod(String settingColumn) {
    Optional<Field> fieldOpt = findField(settingColumn);
    if (!fieldOpt.isPresent()) {
      return null;
    }
    return fieldOpt
      .map(field -> {
        String methodName = "set" + StringUtils.capitalize(field.getName());
        return ReflectionUtils.findMethod(CLAZZ, methodName, field.getType());
      }).orElse(null);
  }

  public static Function<String, Object> findTypeMapper(String settingColumn) {
    Optional<Field> fieldOpt = findField(settingColumn);
    if (!fieldOpt.isPresent()) {
      return null;
    }
    Field field = fieldOpt.get();
    return typeMappers.entrySet().stream()
      .filter(entry -> entry.getKey().equals(field.getType()))
      .findFirst().map(Map.Entry::getValue)
      .orElseThrow(() -> new IllegalArgumentException("Not support this type"));
  }

  private static Optional<Field> findField(String settingColumn) {
    return Stream.of(FieldUtils.getFieldsWithAnnotation(CLAZZ, ANNOTATION_CLS))
      .filter(f -> StringUtils.equals(f.getAnnotation(ANNOTATION_CLS).name(), settingColumn))
      .findFirst();
  }
}
