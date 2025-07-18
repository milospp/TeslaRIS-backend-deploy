package rs.teslaris.assessment.service.impl.classification;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import rs.teslaris.assessment.converter.AssessmentClassificationConverter;
import rs.teslaris.assessment.dto.classification.AssessmentClassificationDTO;
import rs.teslaris.assessment.model.classification.AssessmentClassification;
import rs.teslaris.assessment.model.indicator.ApplicableEntityType;
import rs.teslaris.assessment.repository.classification.AssessmentClassificationRepository;
import rs.teslaris.assessment.service.interfaces.classification.AssessmentClassificationService;
import rs.teslaris.core.annotation.Traceable;
import rs.teslaris.core.service.impl.JPAServiceImpl;
import rs.teslaris.core.service.interfaces.commontypes.MultilingualContentService;
import rs.teslaris.core.util.exceptionhandling.exception.AssessmentClassificationReferenceConstraintViolationException;
import rs.teslaris.core.util.exceptionhandling.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Traceable
public class AssessmentClassificationServiceImpl extends JPAServiceImpl<AssessmentClassification>
    implements AssessmentClassificationService {

    private final AssessmentClassificationRepository assessmentClassificationRepository;

    private final MultilingualContentService multilingualContentService;


    @Override
    public Page<AssessmentClassificationDTO> readAllAssessmentClassifications(Pageable pageable) {
        return assessmentClassificationRepository.findAll(pageable)
            .map(AssessmentClassificationConverter::toDTO);
    }

    @Override
    protected JpaRepository<AssessmentClassification, Integer> getEntityRepository() {
        return assessmentClassificationRepository;
    }

    @Override
    public AssessmentClassificationDTO readAssessmentClassification(
        Integer assessmentClassificationId) {
        return AssessmentClassificationConverter.toDTO(findOne(assessmentClassificationId));
    }

    @Override
    public List<AssessmentClassificationDTO> getAssessmentClassificationsApplicableToEntity(
        List<ApplicableEntityType> applicableEntityTypes) {
        if (!applicableEntityTypes.isEmpty() &&
            !applicableEntityTypes.contains(ApplicableEntityType.ALL)) {
            applicableEntityTypes.add(ApplicableEntityType.ALL);
        }

        return assessmentClassificationRepository.getAssessmentClassificationsApplicableToEntity(
                applicableEntityTypes).stream()
            .map(AssessmentClassificationConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    public AssessmentClassification createAssessmentClassification(
        AssessmentClassificationDTO assessmentClassification) {
        var newAssessmentClassification = new AssessmentClassification();

        setCommonFields(newAssessmentClassification, assessmentClassification);

        return save(newAssessmentClassification);
    }

    @Override
    public void updateAssessmentClassification(Integer assessmentClassificationId,
                                               AssessmentClassificationDTO assessmentClassification) {
        var assessmentClassificationToUpdate = findOne(assessmentClassificationId);

        setCommonFields(assessmentClassificationToUpdate, assessmentClassification);

        save(assessmentClassificationToUpdate);
    }

    private void setCommonFields(AssessmentClassification assessmentClassification,
                                 AssessmentClassificationDTO assessmentClassificationDTO) {
        assessmentClassification.setFormalDescriptionOfRule(
            assessmentClassificationDTO.formalDescriptionOfRule());
        assessmentClassification.setCode(assessmentClassificationDTO.code());
        assessmentClassification.setTitle(
            multilingualContentService.getMultilingualContent(assessmentClassificationDTO.title()));
        assessmentClassification.setApplicableTypes(
            new HashSet<>(assessmentClassificationDTO.applicableTypes()));
    }

    @Override
    public void deleteAssessmentClassification(Integer assessmentClassificationId) {
        if (assessmentClassificationRepository.isInUse(assessmentClassificationId)) {
            throw new AssessmentClassificationReferenceConstraintViolationException(
                "assessmentClassificationInUse.");
        }

        delete(assessmentClassificationId);
    }

    @Override
    public AssessmentClassification readAssessmentClassificationByCode(String code) {
        return assessmentClassificationRepository.findByCode(code).orElseThrow(
            () -> new NotFoundException(
                "Assessment Classification with given code does not exist - " + code + "."));
    }
}
