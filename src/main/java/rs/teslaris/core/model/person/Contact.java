package rs.teslaris.core.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Contact {

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

}
