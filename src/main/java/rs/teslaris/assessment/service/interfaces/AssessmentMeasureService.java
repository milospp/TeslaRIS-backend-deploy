package rs.teslaris.assessment.service.interfaces;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.teslaris.assessment.dto.AssessmentMeasureDTO;
import rs.teslaris.assessment.model.AssessmentMeasure;
import rs.teslaris.core.service.interfaces.JPAService;

@Service
public interface AssessmentMeasureService extends JPAService<AssessmentMeasure> {

    Page<AssessmentMeasureDTO> searchAssessmentMeasures(Pageable pageable, String searchExpression);

    AssessmentMeasureDTO readAssessmentMeasureById(Integer assessmentMeasureId);

    AssessmentMeasure createAssessmentMeasure(AssessmentMeasureDTO assessmentMeasureDTO);

    void updateAssessmentMeasure(Integer assessmentMeasureId,
                                 AssessmentMeasureDTO assessmentMeasureDTO);

    void deleteAssessmentMeasure(Integer assessmentMeasureId);

    List<String> listAllPointRules();

    List<String> listAllScalingRules();

    List<String> listAllGroupCodes();
}
