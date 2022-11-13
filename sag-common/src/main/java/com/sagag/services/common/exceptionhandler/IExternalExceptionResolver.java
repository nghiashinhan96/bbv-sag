package com.sagag.services.common.exceptionhandler;

import com.sagag.services.common.exception.ServiceException;

@FunctionalInterface
public interface IExternalExceptionResolver<E> {

  /**
   * Handles the exception response with external response.
   *
   * @param exResponse the external response.
   * @throws if has any exceptions please throw with specified exception of Connect.
   * example: as <code>ServiceException</code>
   */
  void resolve(E exResponse) throws ServiceException;

}
