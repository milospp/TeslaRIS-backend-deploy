package rs.teslaris.core.assessment.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalPublicationAssessmentRequestDTO {

    private Integer commissionId;

    @NotNull(message = "You have to provide author IDs.")
    private List<Integer> authorIds;

    @NotNull(message = "You have to provide organisation unit IDs.")
    private List<Integer> organisationUnitIds;

    @NotNull(message = "You have to provide journal IDs.")
    private List<Integer> journalIds;
}
