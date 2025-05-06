package rs.teslaris.assessment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.assessment.model.DocumentAssessmentClassification;

@Repository
public interface DocumentAssessmentClassificationRepository
    extends JpaRepository<DocumentAssessmentClassification, Integer> {

    @Query("SELECT eac FROM DocumentAssessmentClassification eac WHERE " +
        "eac.document.id = :documentId ORDER BY eac.timestamp DESC")
    List<DocumentAssessmentClassification> findAssessmentClassificationsForDocument(
        Integer documentId);

    @Query("SELECT eac FROM DocumentAssessmentClassification eac " +
        "JOIN FETCH eac.assessmentClassification WHERE " +
        "eac.document.id = :documentId AND eac.commission.id = :commissionId")
    List<DocumentAssessmentClassification> findAssessmentClassificationsForDocumentAndCommission(
        Integer documentId, Integer commissionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DocumentAssessmentClassification dac " +
        "WHERE dac.document.id = :documentId AND dac.commission.id = :commissionId AND dac.manual = :isManual")
    void deleteByDocumentIdAndCommissionId(Integer documentId, Integer commissionId,
                                           Boolean isManual);

}
