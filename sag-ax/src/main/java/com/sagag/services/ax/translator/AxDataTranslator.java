package com.sagag.services.ax.translator;

import com.sagag.services.common.translator.IDataTranslator;

/**
 * <p>
 * The translator interface to map AX definition to Connect definition.
 * </p>
 *
 * @param <S> the generic object of Ax connection
 * @param <R> the generic object of e-Connect
 *
 */
public interface AxDataTranslator<S, R> extends IDataTranslator<S, R> {

  /**
   * <p>
   * The service to implement translator from Connect to Ax .
   * </p>
   *
   * @param source the object of {@link S}
   * @return the result object of {@link R}
   *
   */
  S translateToAx(R source);

}
