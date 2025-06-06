package rs.teslaris.core.model.oaipmh.dspaceinternal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import rs.teslaris.core.model.oaipmh.publication.PublicationConvertable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dim", namespace = "http://www.dspace.org/xmlns/dspace/dim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dim implements PublicationConvertable {

    @XmlElement(name = "field", namespace = "http://www.dspace.org/xmlns/dspace/dim")
    private List<DimField> fields = new ArrayList<>();
}
