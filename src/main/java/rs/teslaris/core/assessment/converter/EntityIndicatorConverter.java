package rs.teslaris.core.assessment.converter;

import java.util.stream.Collectors;
import rs.teslaris.core.assessment.dto.EntityIndicatorResponseDTO;
import rs.teslaris.core.assessment.dto.PublicationSeriesIndicatorResponseDTO;
import rs.teslaris.core.assessment.model.EntityIndicator;
import rs.teslaris.core.assessment.model.PublicationSeriesIndicator;
import rs.teslaris.core.converter.document.DocumentFileConverter;

public class EntityIndicatorConverter {

    public static EntityIndicatorResponseDTO toDTO(EntityIndicator entityIndicator) {
        return new EntityIndicatorResponseDTO(entityIndicator.getId(),
            entityIndicator.getNumericValue(), entityIndicator.getBooleanValue(),
            entityIndicator.getTextualValue(), entityIndicator.getFromDate(),
            entityIndicator.getToDate(), IndicatorConverter.toDTO(entityIndicator.getIndicator()),
            entityIndicator.getSource(), entityIndicator.getProofs().stream().map(
            DocumentFileConverter::toDTO).collect(Collectors.toList()));
    }

    public static PublicationSeriesIndicatorResponseDTO toDTO(
        PublicationSeriesIndicator entityIndicator) {
        return new PublicationSeriesIndicatorResponseDTO(entityIndicator.getId(),
            entityIndicator.getNumericValue(), entityIndicator.getBooleanValue(),
            entityIndicator.getTextualValue(), entityIndicator.getFromDate(),
            entityIndicator.getToDate(), IndicatorConverter.toDTO(entityIndicator.getIndicator()),
            entityIndicator.getSource(), entityIndicator.getCategoryIdentifier());
    }
}
