package com.sagag.services.rest.app;

import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Avoiding memory leaks
 * <p>
 * If your application is deployed in a web-server or
 * an application server,the registration of an JMXConfigurator instance creates
 * a reference from the system class loader into your application
 * which will prevent it from being garbage collected
 * when it is stopped or re-deployed,resulting in a severe memory leak.
 * Thus,unless your application is a standalone Java application,
 * you MUST unregister the JMXConfigurator instance from the JVM's Mbeans server.
 * Invoking the reset() method of the appropriate LoggerContext will automatically
 * unregister any JMXConfigurator instance. A good place to reset
 * the logger context is in the contextDestroyed() method of a javax.servlet.ServletContextListener.
 * https://logback.qos.ch/manual/jmxConfig.html
 */
public class LoggerContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    lc.stop();
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // intentionally do nothing
  }
}
