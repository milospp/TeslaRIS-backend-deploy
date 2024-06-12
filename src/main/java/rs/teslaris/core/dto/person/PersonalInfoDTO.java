package rs.teslaris.core.dto.person;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.teslaris.core.model.person.Sex;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoDTO {

    @NotNull(message = "You have to provide a local bitrth date.")
    private LocalDate localBirthDate;

    private String placeOfBirth;

    @NotNull(message = "You must provide a person sex.")
    private Sex sex;

    @Valid
    private PostalAddressDTO postalAddress;

    @NotNull(message = "You have to provide a contact info.")
    @Valid
    private ContactDTO contact;

    private String apvnt;

    private String mnid;

    private String orcid;

    private String scopusAuthorId;
}
