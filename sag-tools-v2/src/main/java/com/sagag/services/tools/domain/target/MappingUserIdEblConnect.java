package com.sagag.services.tools.domain.target;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "MAPPING_USER_ID_EBL_CONNECT")
public class MappingUserIdEblConnect implements Serializable {

  private static final long serialVersionUID = -563293623465323258L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @Column(name = "EBL_USER_ID")
  @CsvBindByName(column = "EBL_USER_ID", locale = "US")
  private Long eblUserId;

  @Column(name = "CONNECT_USER_ID")
  @CsvBindByName(column = "CONNECT_USER_ID", locale = "US")
  private Long connectUserId;

  @Column(name = "EBL_ORG_ID")
  @CsvBindByName(column = "EBL_ORG_ID", locale = "US")
  private Long eblOrgId;

  @Column(name = "CONNECT_ORG_ID")
  @CsvBindByName(column = "CONNECT_ORG_ID", locale = "US")
  private Integer connectOrgId;

  public MappingUserIdEblConnect(Long eblUId, Long cUId, Long eblOrgId, Integer connectOrgId) {
    setEblUserId(eblUId);
    setConnectUserId(cUId);
    setEblOrgId(eblOrgId);
    setConnectOrgId(connectOrgId);
  }

}
