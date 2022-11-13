package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.Message;
import com.sagag.services.domain.eshop.message.dto.MessageDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Repository interfacing for {@link Message}.
 *
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query(
      value = "select NEW com.sagag.services.domain.eshop.message.dto.MessageDto("
          + "m.id, mLanguage.content, mType.type, mArea.area, mSubArea.subArea, "
          + "mSubArea.sort, mStyle.style, mVisibility.visibility, mLocationType.locationType, m.ssoTraining) "
          + "from Message m "
          + "join m.messageLocationRelation mlr "
          + "join mlr.messageLocation mLocation "
          + "join mLocation.messageLocationType mLocationType "

          + "join m.messageLanguages mLanguage "
          + "join m.messageType mType "
          + "join m.messageSubArea mSubArea "
          + "join mSubArea.messageArea mArea "
          + "join m.messageStyle mStyle "
          + "join m.messageVisibility mVisibility "

          + "where m.active = 1 "
          + "and mLanguage.langIso =:langIso "
          + "and mArea.auth = 0 "
          + "and current_date() BETWEEN m.dateValidFrom and m.dateValidTo "
          + "and ( "
          + "   (mLocationType.locationType = 'AFFILIATE' and mLocation.value= :affiliate) "
          + ")"
      )
  List<MessageDto> findNoAuthedMessages(@Param("langIso") String langIso, @Param("affiliate") String affiliate);

  @Query(
      value = "select NEW com.sagag.services.domain.eshop.message.dto.MessageDto("
          + "m.id, mLanguage.content, mType.type, mArea.area, mSubArea.subArea, "
          + "mSubArea.sort, mStyle.style, mVisibility.visibility, mLocationType.locationType, m.ssoTraining) "
          + "from Message m "
          + "join m.messageAccessRight mAccessRight "
          + "join mAccessRight.messageAccessRightRoles mAccessRightRole "
          + "join mAccessRightRole.eshopRole r "
          + "join m.messageLocationRelation mlr "
          + "join mlr.messageLocation mLocation "
          + "join mLocation.messageLocationType mLocationType "

          + "join m.messageLanguages mLanguage "
          + "join m.messageType mType "
          + "join m.messageSubArea mSubArea "
          + "join mSubArea.messageArea mArea "
          + "join m.messageStyle mStyle "
          + "join m.messageVisibility mVisibility "

          + "where m.active = 1 "
          + "and mLanguage.langIso =:langIso "
          + "and r.name IN :roleNames "
          + "and current_date() BETWEEN m.dateValidFrom and m.dateValidTo "
          + "and ( "
          + "   (mLocationType.locationType = 'CUSTOMER' and mLocation.value= :customerNr) "
          + "   or "
          + "   (mLocationType.locationType = 'AFFILIATE' and mLocation.value= :affiliate) "
          + ")"

      )
  List<MessageDto> findAuthedMessages(
      @Param("roleNames") List<String> roleNames,
      @Param("langIso") String langIso,
      @Param("affiliate") String affiliate,
      @Param("customerNr") String customerNr
      );

  @Query(value = "select case when count(m) > 0 then false else true end "
      + "from Message m "
      + "join m.messageLocationRelation mlr "
      + "join mlr.messageLocation l "
      + "join l.messageLocationType lt "
      + "join m.messageAccessRight ar "
      + "join m.messageSubArea sa "
      + "join m.messageType t "
      + "where lt.id=:locationTypeId and "
      + "l.value in :locationValue and "
      + "ar.id=:accessRightId and "
      + "t.id=:typeId and "
      + "sa.id=:subAreaId and "
      + "not(:dateValidTo <= m.dateValidFrom or :dateValidFrom >= m.dateValidTo)")
  boolean isValidPeriod(
      @Param("locationTypeId") Integer locationTypeId,
      @Param("locationValue") List<String> locationValue,
      @Param("accessRightId") Integer accessRightId,
      @Param("subAreaId") Integer subAreaId,
      @Param("typeId") Integer typeId,
      @Param("dateValidFrom") Date dateValidFrom,
      @Param("dateValidTo") Date dateValidTo);

  @Query(value = "select case when count(m) > 0 then false else true end "
      + "from Message m "
      + "join m.messageLocationRelation mlr "
      + "join mlr.messageLocation l "
      + "join l.messageLocationType lt "
      + "join m.messageAccessRight ar "
      + "join m.messageSubArea sa "
      + "join m.messageType t "
      + "where "
      + "m.id !=:messageId and "
      + "lt.id=:locationTypeId and "
      + "l.value in :locationValue and "
      + "ar.id=:accessRightId and "
      + "t.id=:typeId and "
      + "sa.id=:subAreaId and "
      + "not(:dateValidTo <= m.dateValidFrom or :dateValidFrom >= m.dateValidTo)")
  boolean isValidPeriod(
      @Param("locationTypeId") Integer locationTypeId,
      @Param("locationValue") List<String> locationValue,
      @Param("accessRightId") Integer accessRightId,
      @Param("subAreaId") Integer subAreaId,
      @Param("typeId") Integer typeId,
      @Param("dateValidFrom") Date dateValidFrom,
      @Param("dateValidTo") Date dateValidTo,
      @Param("messageId") Long messageId);
}
