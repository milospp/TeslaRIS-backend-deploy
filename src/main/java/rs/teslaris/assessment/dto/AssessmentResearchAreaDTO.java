package rs.teslaris.assessment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.teslaris.core.dto.commontypes.MultilingualContentDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResearchAreaDTO {

    private List<MultilingualContentDTO> name;

    private String code;
}
