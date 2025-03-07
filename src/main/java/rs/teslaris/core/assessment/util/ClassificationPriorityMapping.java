package rs.teslaris.core.assessment.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.teslaris.core.assessment.model.AssessmentClassification;
import rs.teslaris.core.assessment.model.ResultCalculationMethod;
import rs.teslaris.core.model.document.JournalPublicationType;
import rs.teslaris.core.model.document.ProceedingsPublicationType;
import rs.teslaris.core.repository.document.JournalPublicationRepository;
import rs.teslaris.core.repository.document.ProceedingsPublicationRepository;
import rs.teslaris.core.util.exceptionhandling.exception.StorageException;

@Component
public class ClassificationPriorityMapping {

    private static ProceedingsPublicationRepository proceedingsPublicationRepository;

    private static JournalPublicationRepository journalPublicationRepository;

    private static AssessmentConfig assessmentConfig;

    @Autowired
    public ClassificationPriorityMapping(
        ProceedingsPublicationRepository proceedingsPublicationRepository,
        JournalPublicationRepository journalPublicationRepository) {
        ClassificationPriorityMapping.proceedingsPublicationRepository =
            proceedingsPublicationRepository;
        ClassificationPriorityMapping.journalPublicationRepository = journalPublicationRepository;
        reloadConfiguration();
    }

    @Scheduled(fixedRate = (1000 * 60 * 10)) // 10 minutes
    private static void reloadConfiguration() {
        try {
            loadConfiguration();
        } catch (IOException e) {
            throw new StorageException(
                "Failed to reload indicator mapping configuration: " + e.getMessage());
        }
    }

    private static synchronized void loadConfiguration() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        assessmentConfig = objectMapper.readValue(
            new FileInputStream(
                "src/main/resources/assessment/assessmentConfiguration.json"),
            AssessmentConfig.class
        );
    }

    public static Optional<AssessmentClassification> getClassificationBasedOnCriteria(
        ArrayList<AssessmentClassification> classifications,
        ResultCalculationMethod resultCalculationMethod) {
        return switch (resultCalculationMethod) {
            case BEST_VALUE -> classifications.stream()
                .min(Comparator.comparingInt(
                    assessmentClassification -> assessmentConfig.classificationPriorities.getOrDefault(
                        assessmentClassification.getCode(), Integer.MAX_VALUE)));
            case WORST_VALUE -> classifications.stream()
                .max(Comparator.comparingInt(
                    assessmentClassification -> assessmentConfig.classificationPriorities.getOrDefault(
                        assessmentClassification.getCode(), Integer.MIN_VALUE)));
        };
    }

    public static Optional<String> getDocClassificationCodeBasedOnCode(
        String classificationCode, Integer documentId) {

        String documentCode =
            assessmentConfig.classificationToAssessmentMapping.get(classificationCode);

        if (Objects.isNull(documentCode)) {
            return Optional.empty();
        }

        if (documentCode.equals("M30") || documentCode.equals("M60")) {
            return proceedingsPublicationRepository.findById(documentId)
                .flatMap(proceedingsPublication -> getMappedCode(documentCode,
                    proceedingsPublication.getProceedingsPublicationType()));
        }

        if (documentCode.equals("M24") ||
            assessmentConfig.classificationPriorities.get(classificationCode) <
                assessmentConfig.classificationPriorities.get("journalM24")) {
            return journalPublicationRepository.findById(documentId).flatMap(
                journalPublication -> getMappedCode(documentCode,
                    journalPublication.getJournalPublicationType()));
        }

        return Optional.of(documentCode);
    }

    private static Optional<String> getMappedCode(String baseCode,
                                                  ProceedingsPublicationType type) {
        Map<ProceedingsPublicationType, String> mappingM30 = Map.of(
            ProceedingsPublicationType.INVITED_FULL_ARTICLE, "M31",
            ProceedingsPublicationType.INVITED_ABSTRACT_ARTICLE, "M32",
            ProceedingsPublicationType.REGULAR_FULL_ARTICLE, "M33",
            ProceedingsPublicationType.REGULAR_ABSTRACT_ARTICLE, "M34"
        );

        Map<ProceedingsPublicationType, String> mappingM60 = Map.of(
            ProceedingsPublicationType.INVITED_FULL_ARTICLE, "M61",
            ProceedingsPublicationType.INVITED_ABSTRACT_ARTICLE, "M62",
            ProceedingsPublicationType.REGULAR_FULL_ARTICLE, "M63",
            ProceedingsPublicationType.REGULAR_ABSTRACT_ARTICLE, "M64",
            ProceedingsPublicationType.SCIENTIFIC_CRITIC, "M69"
        );

        return Optional.ofNullable(
            baseCode.equals("M30") ? mappingM30.get(type) : mappingM60.get(type)
        );
    }

    private static Optional<String> getMappedCode(String baseCode,
                                                  JournalPublicationType type) {
        Map<JournalPublicationType, String> mappingM26 = Map.of(
            JournalPublicationType.SCIENTIFIC_CRITIC, "M26"
        );

        Map<JournalPublicationType, String> mappingM27 = Map.of(
            JournalPublicationType.SCIENTIFIC_CRITIC, "M27"
        );

        return Optional.ofNullable(
            baseCode.equals("M24") ? mappingM26.getOrDefault(type, baseCode) :
                mappingM27.getOrDefault(type, baseCode)
        );
    }

    public static List<String> getAssessmentGroups() {
        return assessmentConfig.groupToClassificationsMapping.keySet().stream().sorted().toList();
    }

    public static boolean existsInGroup(String groupCode, String classificationCode) {
        return assessmentConfig.groupToClassificationsMapping.getOrDefault(groupCode, List.of())
            .contains(classificationCode);
    }

    public static String getCodeDisplayValue(String code) {
        return code.toLowerCase()
            .replace("m", "M")
            .replace("plus", "+");
    }

    private record AssessmentConfig(
        @JsonProperty("classificationPriorities") Map<String, Integer> classificationPriorities,
        @JsonProperty("classificationToAssessmentMapping") Map<String, String> classificationToAssessmentMapping,
        @JsonProperty("groupToClassificationsMapping") Map<String, List<String>> groupToClassificationsMapping
    ) {
    }
}
