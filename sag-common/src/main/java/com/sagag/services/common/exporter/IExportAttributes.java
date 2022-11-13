package com.sagag.services.common.exporter;

import java.util.List;

/**
 * Interface for attributes export.
 *
 * @param <T> the response attributes.
 */
@FunctionalInterface
public interface IExportAttributes<T> {

  List<T> getExportAttributes();

}
