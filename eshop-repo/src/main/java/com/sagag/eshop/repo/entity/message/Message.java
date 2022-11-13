package com.sagag.eshop.repo.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_PROPERTIES.
 */
@Entity
@Table(name = "MESSAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

  private static final long serialVersionUID = 9071120092350256676L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String title;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ACCESS_RIGHT_ID", nullable = false)
  private MessageAccessRight messageAccessRight;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_TYPE_ID", nullable = false)
  private MessageType messageType;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_SUB_AREA_ID", nullable = false)
  private MessageSubArea messageSubArea;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_STYLE_ID", nullable = false)
  private MessageStyle messageStyle;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_VISIBILITY_ID", nullable = false)
  private MessageVisibility messageVisibility;

  @OneToMany(mappedBy = "message")
  private List<MessageLanguage> messageLanguages;

  @OneToMany(mappedBy = "message")
  private List<MessageHiding> messageHidings;

  @OneToMany(mappedBy = "message")
  private List<MessageLocationRelation> messageLocationRelation;

  @Column(nullable = false)
  private Boolean active;

  @Column(nullable = false)
  private Date dateValidFrom;

  @Column(nullable = false)
  private Date dateValidTo;

  @Column(nullable = false)
  private long createdUserId;

  @Column(nullable = false)
  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  private Boolean ssoTraining;
}
