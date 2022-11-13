package com.sagag.services.copydb.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TableDifference {

  private final String name;

  private List<String> differences = new ArrayList<>();

}
