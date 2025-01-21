package rs.teslaris.core.assessment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.teslaris.core.assessment.model.CommissionRelation;

@Repository
public interface CommissionRelationRepository extends JpaRepository<CommissionRelation, Integer> {

    @Query("SELECT cr FROM CommissionRelation cr JOIN FETCH cr.targetCommissions WHERE cr.sourceCommission.id = :sourceCommissionId")
    List<CommissionRelation> getRelationsForSourceCommission(Integer sourceCommissionId);
}
