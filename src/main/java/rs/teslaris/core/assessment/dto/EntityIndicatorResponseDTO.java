package rs.teslaris.core.assessment.dto;

import java.time.LocalDate;
import java.util.List;
import rs.teslaris.core.assessment.model.EntityIndicatorSource;
import rs.teslaris.core.dto.document.DocumentFileResponseDTO;

public record EntityIndicatorResponseDTO(
    Integer id,
    Double numericValue,

    Boolean booleanValue,

    String textualValue,

    LocalDate fromDate,

    LocalDate toDate,

    IndicatorResponseDTO indicatorResponse,

    EntityIndicatorSource source,

    List<DocumentFileResponseDTO> proofs
) {
}
