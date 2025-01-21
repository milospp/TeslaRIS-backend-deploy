package rs.teslaris.core.assessment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.teslaris.core.annotation.Idempotent;
import rs.teslaris.core.assessment.converter.CommissionConverter;
import rs.teslaris.core.assessment.dto.CommissionDTO;
import rs.teslaris.core.assessment.dto.CommissionResponseDTO;
import rs.teslaris.core.assessment.service.interfaces.CommissionService;

@RestController
@RequestMapping("/api/assessment/commission")
@RequiredArgsConstructor
public class CommissionController {

    private final CommissionService commissionService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('EDIT_ENTITY_ASSESSMENT_CLASSIFICATION')")
    public Page<CommissionResponseDTO> readCommissions(Pageable pageable,
                                                       @RequestParam(required = false)
                                                       String searchExpression,
                                                       @RequestParam("lang")
                                                       String language) {
        return commissionService.readAllCommissions(pageable, searchExpression, language);
    }

    @GetMapping("/rule-engines")
    @PreAuthorize("hasAuthority('EDIT_COMMISSIONS')")
    public List<String> readApplicableRuleEnginesForCommissions() {
        return commissionService.readAllApplicableRuleEngines();
    }


    @GetMapping("/{commissionId}")
    @PreAuthorize("hasAuthority('EDIT_COMMISSIONS')")
    public CommissionResponseDTO readCommission(@PathVariable Integer commissionId) {
        return commissionService.readCommissionById(commissionId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EDIT_COMMISSIONS')")
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public CommissionResponseDTO createCommission(@RequestBody CommissionDTO commissionDTO) {
        var createdCommission = commissionService.createCommission(commissionDTO);

        return CommissionConverter.toDTO(createdCommission);
    }

    @PutMapping("/{commissionId}")
    @PreAuthorize("hasAuthority('EDIT_COMMISSIONS')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCommission(@RequestBody CommissionDTO commissionDTO,
                                 @PathVariable Integer commissionId) {
        commissionService.updateCommission(commissionId,
            commissionDTO);
    }

    @DeleteMapping("/{commissionId}")
    @PreAuthorize("hasAuthority('EDIT_COMMISSIONS')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommission(@PathVariable Integer commissionId) {
        commissionService.deleteCommission(commissionId);
    }
}
