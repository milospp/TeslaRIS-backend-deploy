package rs.teslaris.core.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.teslaris.core.annotation.Idempotent;
import rs.teslaris.core.annotation.PersonEditCheck;
import rs.teslaris.core.converter.person.InvolvementConverter;
import rs.teslaris.core.dto.document.DocumentFileDTO;
import rs.teslaris.core.dto.person.involvement.EducationDTO;
import rs.teslaris.core.dto.person.involvement.EmploymentDTO;
import rs.teslaris.core.dto.person.involvement.MembershipDTO;
import rs.teslaris.core.model.person.Education;
import rs.teslaris.core.model.person.Employment;
import rs.teslaris.core.model.person.Membership;
import rs.teslaris.core.service.interfaces.person.InvolvementService;

@Validated
@RestController
@RequestMapping("api/involvement")
@RequiredArgsConstructor
public class InvolvementController {

    private final InvolvementService involvementService;

    @GetMapping("/education/{educationId}")
    public EducationDTO getEducation(@PathVariable Integer educationId) {
        return involvementService.getInvolvement(educationId, Education.class);
    }

    @GetMapping("/membership/{membershipId}")
    public MembershipDTO getMembership(@PathVariable Integer membershipId) {
        return involvementService.getInvolvement(membershipId, Membership.class);
    }

    @GetMapping("/employment/{employmentId}")
    public EmploymentDTO getEmployment(@PathVariable Integer employmentId) {
        return involvementService.getInvolvement(employmentId, Employment.class);
    }

    @PostMapping("/education/{personId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    @Idempotent
    public EducationDTO addEducation(@RequestBody @Valid EducationDTO education,
                                     @PathVariable Integer personId) {
        var newEducation = involvementService.addEducation(personId, education);
        return InvolvementConverter.toDTO(newEducation);
    }

    @PostMapping("/membership/{personId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    @Idempotent
    public MembershipDTO addMembership(@RequestBody @Valid MembershipDTO membership,
                                       @PathVariable Integer personId) {
        var newMembership = involvementService.addMembership(personId, membership);
        return InvolvementConverter.toDTO(newMembership);
    }

    @PostMapping("/employment/{personId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    @Idempotent
    public EmploymentDTO addEmployment(@RequestBody @Valid EmploymentDTO employment,
                                       @PathVariable Integer personId) {
        var newEmployment = involvementService.addEmployment(personId, employment);
        return InvolvementConverter.toDTO(newEmployment);
    }

    @PatchMapping(value = "/{involvementId}/{personId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    @Idempotent
    public void addInvolvementProofs(@ModelAttribute @Valid DocumentFileDTO proof,
                                     @PathVariable Integer involvementId) {
        involvementService.addInvolvementProofs(List.of(proof), involvementId);
    }

    @DeleteMapping("/{involvementId}/{personId}/{proofId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    public void deleteInvolvementProof(@PathVariable Integer involvementId,
                                       @PathVariable Integer proofId) {
        involvementService.deleteProof(proofId, involvementId);
    }

    @PutMapping("/education/{involvementId}/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    public void updateEducation(@RequestBody @Valid EducationDTO education,
                                @PathVariable Integer involvementId) {
        involvementService.updateEducation(involvementId, education);
    }

    @PutMapping("/membership/{involvementId}/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    public void updateMembership(@RequestBody @Valid MembershipDTO membership,
                                 @PathVariable Integer involvementId) {
        involvementService.updateMembership(involvementId, membership);
    }

    @PutMapping("/employment/{involvementId}/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    public void updateEmployment(@RequestBody @Valid EmploymentDTO employment,
                                 @PathVariable Integer involvementId) {
        involvementService.updateEmployment(involvementId, employment);
    }

    @DeleteMapping("/{involvementId}/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EDIT_PERSON_INFORMATION')")
    @PersonEditCheck
    public void deleteInvolvement(@PathVariable Integer involvementId) {
        involvementService.deleteInvolvement(involvementId);
    }
}
