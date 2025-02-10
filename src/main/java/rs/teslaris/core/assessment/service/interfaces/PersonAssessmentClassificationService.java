package rs.teslaris.core.assessment.service.interfaces;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import rs.teslaris.core.assessment.dto.EntityAssessmentClassificationResponseDTO;

@Service
public interface PersonAssessmentClassificationService {

    List<EntityAssessmentClassificationResponseDTO> getAssessmentClassificationsForPerson(
        Integer personId);

    void assessResearchers(LocalDate fromDate, Integer commissionId, Integer rulebookId,
                           List<Integer> researcherIds, List<Integer> orgUnitIds);
}
