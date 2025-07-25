package rs.teslaris.assessment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.teslaris.assessment.converter.AssessmentClassificationConverter;
import rs.teslaris.assessment.dto.classification.AssessmentClassificationDTO;
import rs.teslaris.assessment.model.indicator.ApplicableEntityType;
import rs.teslaris.assessment.service.interfaces.classification.AssessmentClassificationService;
import rs.teslaris.core.annotation.Idempotent;
import rs.teslaris.core.annotation.Traceable;

@RestController
@RequestMapping("/api/assessment/assessment-classification")
@RequiredArgsConstructor
@Traceable
public class AssessmentClassificationController {

    private final AssessmentClassificationService assessmentClassificationService;


    @GetMapping
    public Page<AssessmentClassificationDTO> readAssessmentClassifications(Pageable pageable) {
        return assessmentClassificationService.readAllAssessmentClassifications(pageable);
    }

    @GetMapping("/{assessmentClassificationId}")
    public AssessmentClassificationDTO readAssessmentClassification(
        @PathVariable Integer assessmentClassificationId) {
        return assessmentClassificationService.readAssessmentClassification(
            assessmentClassificationId);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('EDIT_ASSESSMENT_CLASSIFICATIONS', 'LIST_ASSESSMENT_CLASSIFICATIONS')")
    public List<AssessmentClassificationDTO> getClassificationsApplicableToEntity(
        @RequestParam("applicableType") List<ApplicableEntityType> applicableEntityTypes) {
        return assessmentClassificationService.getAssessmentClassificationsApplicableToEntity(
            applicableEntityTypes);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EDIT_ASSESSMENT_CLASSIFICATIONS')")
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public AssessmentClassificationDTO createAssessmentClassification(
        @RequestBody AssessmentClassificationDTO assessmentClassificationDTO) {
        var createdAssessmentClassification =
            assessmentClassificationService.createAssessmentClassification(
                assessmentClassificationDTO);

        return AssessmentClassificationConverter.toDTO(createdAssessmentClassification);
    }

    @PutMapping("/{assessmentClassificationId}")
    @PreAuthorize("hasAuthority('EDIT_ASSESSMENT_CLASSIFICATIONS')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAssessmentClassification(
        @RequestBody AssessmentClassificationDTO assessmentClassificationDTO,
        @PathVariable Integer assessmentClassificationId) {
        assessmentClassificationService.updateAssessmentClassification(assessmentClassificationId,
            assessmentClassificationDTO);
    }

    @DeleteMapping("/{assessmentClassificationId}")
    @PreAuthorize("hasAuthority('EDIT_ASSESSMENT_CLASSIFICATIONS')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssessmentClassification(@PathVariable Integer assessmentClassificationId) {
        assessmentClassificationService.deleteAssessmentClassification(assessmentClassificationId);
    }
}
