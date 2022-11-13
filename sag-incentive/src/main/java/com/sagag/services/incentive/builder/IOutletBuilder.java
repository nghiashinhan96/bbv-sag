package com.sagag.services.incentive.builder;

@FunctionalInterface
public interface IOutletBuilder {

  /**
   * Builds Outlet URL.
   *
   * @return the outlet URL.
   */
  String buildOutlet();

}
