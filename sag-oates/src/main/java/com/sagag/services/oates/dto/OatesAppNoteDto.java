package com.sagag.services.oates.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OatesAppNoteDto implements Serializable {

  private static final long serialVersionUID = -2704975950715160230L;

  private String id;

  private String noteClass;

  private String noteIndex;

  private String text;

}
