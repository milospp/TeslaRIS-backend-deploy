package rs.teslaris.core.dto.deduplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.teslaris.core.dto.document.PatentDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MergedPatentsDTO {

    private PatentDTO leftPatent;

    private PatentDTO rightPatent;
}
