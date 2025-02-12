package rs.teslaris.core.assessment.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.teslaris.core.util.exceptionhandling.exception.StorageException;

@Component
public class ClassificationMappingConfigurationLoader {

    private static ClassificationMappingConfigurationLoader.ClassificationMappingConfiguration
        classificationMappingConfiguration = null;

    @Scheduled(fixedRate = (1000 * 60 * 10)) // 10 minutes
    private static void reloadConfiguration() {
        try {
            loadConfiguration();
        } catch (IOException e) {
            throw new StorageException(
                "Failed to reload classification mapping configuration: " + e.getMessage());
        }
    }

    private static synchronized void loadConfiguration() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        classificationMappingConfiguration = objectMapper.readValue(
            new FileInputStream(
                "src/main/resources/assessment/classificationMappingConfiguration.json"),
            ClassificationMappingConfigurationLoader.ClassificationMappingConfiguration.class
        );
    }

    public static ClassificationMappingConfigurationLoader.ClassificationMapping fetchClassificationMappingConfiguration(
        String mappingName) {
        return classificationMappingConfiguration.mappings().getOrDefault(
            mappingName, null);
    }

    private record ClassificationMappingConfiguration(
        @JsonProperty(value = "mappings", required = true) Map<String, ClassificationMapping> mappings
    ) {
    }

    public record ClassificationMapping(
        @JsonProperty(value = "commissionId", required = true) Integer commissionId,
        @JsonProperty(value = "yearParseRegex", required = true) String yearParseRegex,
        @JsonProperty(value = "nameColumn", required = true) Integer nameColumn,
        @JsonProperty(value = "issnColumn", required = true) Integer issnColumn,
        @JsonProperty(value = "issnDelimiter", required = true) String issnDelimiter,
        @JsonProperty(value = "categoryColumn", required = true) Integer categoryColumn,
        @JsonProperty(value = "classificationColumn", required = true) Integer classificationColumn,
        @JsonProperty(value = "defaultLanguage", required = true) String defaultLanguage,
        @JsonProperty(value = "parallelize", required = true) Boolean parallelize
    ) {
    }
}
