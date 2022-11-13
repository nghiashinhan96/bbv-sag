package com.sagag.services.gtmotive.domain.request;

public enum GtmotiveRequestMode {

  VEHICLE, VIN, PARTS, SERVICE_SCHEDULE;

  public boolean isOpenGraphics() {
    return isVehicle() || isVin() || isServiceSchedule();
  }

  public boolean isCloseGraphics() {
    return this == PARTS;
  }

  /**
   * Checks if the request is VIN search.
   *
   * @return <code>true</code> if the request is kind of VIN search. Otherwise, return
   *         <code>false</code>
   */
  public boolean isVin() {
    return this == VIN;
  }

  /**
   * Checks if the request is VEHICLE search.
   *
   * @return <code>true</code> if the request is kind of VEHICLE search. Otherwise, return
   *         <code>false</code>
   */
  public boolean isVehicle() {
    return this == VEHICLE;
  }

  /**
   * Checks if the request is Services Schedule search.
   *
   * @return <code>true</code> if the request is kind of Services Schedule search. Otherwise, return
   *         <code>false</code>
   */
  public boolean isServiceSchedule() {
    return this == SERVICE_SCHEDULE;
  }

  /**
   * Checks if the request mode is to click on Graphical tab or service schedule tab.
   *
   * @return <code>true</code> if the request is kind of clicking on those tabs. Otherwise, return
   *         <code>false</code>
   */
  public boolean isTabClicking() {
    return this.isVehicle() || this.isServiceSchedule();
  }

}
