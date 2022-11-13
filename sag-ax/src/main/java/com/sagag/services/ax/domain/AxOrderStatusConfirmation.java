package com.sagag.services.ax.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * Class to receive sales updated order status changed from Dynamic AX ERP.
 *
 */
@Data
public class AxOrderStatusConfirmation implements Serializable {

  private static final long serialVersionUID = 6516418100503791910L;

  private boolean processStatusChanged;

}
