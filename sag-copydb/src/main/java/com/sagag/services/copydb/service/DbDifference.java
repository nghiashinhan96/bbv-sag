package com.sagag.services.copydb.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DbDifference {

  private final DifferenceSummary summary;

  private List<TableDifference> minuses = new ArrayList<>();

  private List<TableDifference> pluses = new ArrayList<>();

}
