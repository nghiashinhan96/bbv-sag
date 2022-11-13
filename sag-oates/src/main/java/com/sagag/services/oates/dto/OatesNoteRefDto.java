package com.sagag.services.oates.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OatesNoteRefDto implements Serializable {

  private static final long serialVersionUID = 6573810364732743247L;

  private String id;

  private String noteClass;

  private String noteIndex;

}
