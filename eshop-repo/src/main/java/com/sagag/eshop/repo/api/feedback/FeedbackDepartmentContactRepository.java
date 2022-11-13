package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.FeedbackDepartmentContact;
import com.sagag.eshop.repo.enums.FeedbackDepartmentContactType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interfacing for {@link FeedbackDepartmentContact}.
 *
 */
public interface FeedbackDepartmentContactRepository
  extends JpaRepository<FeedbackDepartmentContact, Long> {

  @Query(value = "select distinct c.value "
      + "from FeedbackDepartmentContact c, FeedbackDepartment d, "
      + "FeedbackTopicDepartment td, FeedbackTopic t "
      + "where d.supportedAffiliateId = "
      + "(select aff.id from SupportedAffiliate aff where aff.shortName = :affiliateShortName) "
      + "and t.id = :topicId "
      + "and t.id = td.topicId "
      + "and td.departmentId = d.id "
      + "and d.id = c.departmentId and c.type = :type")
  String findContactByTopicIdAndAffiliateShortName(@Param("topicId") final int topicId,
      @Param("affiliateShortName") final String affiliateShortName,
      @Param("type") final FeedbackDepartmentContactType type);

  @Query(value = "select distinct c.value "
      + "from FeedbackDepartmentContact c, FeedbackDepartment d, "
      + "FeedbackTopicDepartment td, FeedbackTopic t , SupportedAffiliate aff "
      + "where d.supportedAffiliateId IS NULL "
      + "and t.id = :topicId "
      + "and t.id = td.topicId "
      + "and td.departmentId = d.id "
      + "and d.id = c.departmentId and c.type = :type")
  String findContactCrossAffiliateByTopicId(@Param("topicId") final int topicId,
      @Param("type") final FeedbackDepartmentContactType type);

  @Query(value = "select primaryEmail from Branch where branchNr = :branchNumber")
  String findBranchEmailByBranchNumber(@Param("branchNumber") final int branchNumber);
}
