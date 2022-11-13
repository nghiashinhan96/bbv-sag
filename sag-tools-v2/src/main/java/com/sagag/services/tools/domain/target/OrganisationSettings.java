package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "OrganisationSettings.findAll", query = "SELECT o FROM OrganisationSettings o")
@Data
@Builder
@Table(name = "ORGANISATION_SETTINGS")
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationSettings implements Serializable {

  private static final long serialVersionUID = 1986402506031474387L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "ORGANISATION_ID")
  @JsonBackReference
  private Organisation organisation;

  @Column(name = "SETTING_KEY")
  private String settingKey;

  @Column(name = "SETTING_VALUE")
  private String settingValue;

  /**
   * Constructs the organisation settings from key and value.
   * 
   * @param key the setting key
   * @param value the setting value
   */
  public OrganisationSettings(final String key, final String value) {
    this.settingKey = key;
    this.settingValue = value;
  }
}
