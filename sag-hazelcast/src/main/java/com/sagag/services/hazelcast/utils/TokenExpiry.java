package com.sagag.services.hazelcast.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class TokenExpiry implements Delayed, Serializable {

  private static final long serialVersionUID = 1766161776316713709L;

  private final long expiry;

  private final String value;

  public TokenExpiry(String value, Date date) {
    this.value = value;
    this.expiry = date.getTime();
  }

  public int compareTo(Delayed other) {
    if (this == other) {
      return 0;
    }
    long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
    return (diff == 0 ? 0 : ((diff < 0) ? -1 : 1));
  }

  public long getDelay(TimeUnit unit) {
    return expiry - System.currentTimeMillis();
  }

  public String getValue() {
    return value;
  }
}
