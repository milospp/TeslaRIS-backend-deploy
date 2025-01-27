package rs.teslaris.core.assessment.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import rs.teslaris.core.assessment.model.AssessmentClassification;
import rs.teslaris.core.assessment.model.ResultCalculationMethod;

public class ClassificationPriorityMapping {

    private static final Map<String, Integer> CLASSIFICATION_PRIORITIES = Map.ofEntries(
        Map.entry("M21APlus", 1),
        Map.entry("M21A", 2),
        Map.entry("M21", 3),
        Map.entry("M22", 4),
        Map.entry("M23", 5),
        Map.entry("M23e", 6),
        Map.entry("M24Plus", 7),
        Map.entry("M24", 8),
        Map.entry("M51", 9),
        Map.entry("M52", 10),
        Map.entry("M53", 11),
        Map.entry("M54", 12)
    );

    public static Optional<AssessmentClassification> getClassificationBasedOnCriteria(
        ArrayList<AssessmentClassification> classifications,
        ResultCalculationMethod resultCalculationMethod) {
        return switch (resultCalculationMethod) {
            case BEST_VALUE -> classifications.stream()
                .min(Comparator.comparingInt(
                    assessmentClassification -> CLASSIFICATION_PRIORITIES.getOrDefault(
                        assessmentClassification.getCode(), Integer.MAX_VALUE)));
            case WORST_VALUE -> classifications.stream()
                .max(Comparator.comparingInt(
                    assessmentClassification -> CLASSIFICATION_PRIORITIES.getOrDefault(
                        assessmentClassification.getCode(), Integer.MIN_VALUE)));

            case null -> Optional.empty();
        };
    }
}
