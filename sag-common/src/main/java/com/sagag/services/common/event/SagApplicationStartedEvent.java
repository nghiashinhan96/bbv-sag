package com.sagag.services.common.event;

import com.sagag.services.common.profiles.AtProfile;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.common.profiles.ChProfile;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;
import com.sagag.services.common.profiles.EnableShoppingCartMapStore;
import com.sagag.services.common.profiles.ErpProfile;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class SagApplicationStartedEvent implements ApplicationListener<ApplicationStartedEvent> {

  private static final Class<?>[] COUNTRY_ANNOTATIONS = {
      AtProfile.class,
      ChProfile.class,
      CzProfile.class,
      AutonetProfile.class
  };

  private static final Class<?>[] COUNTRY_CONFIG_ANNOTATIONS = {
      ErpProfile.class,
      EnableShoppingCartMapStore.class,
      EnablePriceDiscountPromotion.class
  };

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    final ApplicationContext context = event.getApplicationContext();
    printRegisteredBean(context);
  }

  private static void printRegisteredBean(final ApplicationContext context) {
    log.info("List out all SAG bean objects are used for application");
    final List<Class<?>> annotations = new ArrayList<>();
    annotations.addAll(Arrays.asList(COUNTRY_ANNOTATIONS));
    annotations.addAll(Arrays.asList(COUNTRY_CONFIG_ANNOTATIONS));

    final List<String> beansByCustomAnnotation = new ArrayList<>();

    printCustomAnnotatedRegisteredBean(beansByCustomAnnotation, annotations, context);

    printEshopBean(beansByCustomAnnotation, context);

  }

  private static void printCustomAnnotatedRegisteredBean(final List<String> beansByCustomAnnotation,
      final List<Class<?>> annotations, final ApplicationContext context) {
    for (Class<?> anno : annotations) {
      log.info("|-> Registered Bean of annotation = {}", anno.getCanonicalName());
      final String[] beanNames =
          context.getBeanNamesForAnnotation(anno.asSubclass(Annotation.class));
      if (ArrayUtils.isEmpty(beanNames)) {
        log.info("\t| -> Registered Bean of annotation is EMPTY");
        continue;
      }
      beansByCustomAnnotation.addAll(Arrays.asList(beanNames));
      for (String bean : beanNames) {
        beansByCustomAnnotation.add(bean);
        log.info("\t| -> Registered Bean of annotation = {}", bean);
      }
    }
  }

  private static void printEshopBean(final List<String> beansByCustomAnnotation,
      final ApplicationContext context) {
    final String[] allBeans = context.getBeanDefinitionNames();
    for (String bean : allBeans) {
      if (!beansByCustomAnnotation.contains(bean) && !bean.contains("org.springframework")) {
        log.info("\t|-> Registered Bean =\t {}", bean);
      }
    }
  }

  public void printAnnotatedRegisteredBean(final Class<?>[] customAnnotations,
      final ApplicationContext context) {
    if (ArrayUtils.isEmpty(customAnnotations)) {
      return;
    }
    printCustomAnnotatedRegisteredBean(new ArrayList<>(), Arrays.asList(customAnnotations),
        context);
  }
}
