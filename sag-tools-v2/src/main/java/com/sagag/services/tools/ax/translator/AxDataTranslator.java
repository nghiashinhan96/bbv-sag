package com.sagag.services.tools.ax.translator;

/**
 * <p>
 * The translator interface to map AX definition to Connect definition.
 * </p>
 *
 * @param <S> the generic object of Ax connection
 * @param <R> the generic object of e-Connect
 *
 */
public interface AxDataTranslator<S, R> {

  /**
   * <p>
   * The service to implement translator from Ax to Connect .
   * </p>
   *
   * @param source the object of {@link S}
   * @return the result object of {@link R}
   *
   */
  R translateToConnect(S source);

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
