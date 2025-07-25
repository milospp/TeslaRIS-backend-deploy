package rs.teslaris.assessment.service.interfaces.indicator;

import rs.teslaris.assessment.model.indicator.EntityIndicator;
import rs.teslaris.core.dto.document.DocumentFileDTO;
import rs.teslaris.core.dto.document.DocumentFileResponseDTO;
import rs.teslaris.core.service.interfaces.JPAService;

public interface EntityIndicatorService extends JPAService<EntityIndicator> {

    EntityIndicator findByUserId(Integer userId);

    boolean isUserTheOwnerOfEntityIndicator(Integer userId, Integer entityIndicatorId);

    DocumentFileResponseDTO addEntityIndicatorProof(DocumentFileDTO documentFile,
                                                    Integer entityIndicatorId);

    DocumentFileResponseDTO updateEntityIndicatorProof(DocumentFileDTO updatedProof);

    void deleteEntityIndicatorProof(Integer entityIndicatorId, Integer proofId);

    void deleteEntityIndicator(Integer entityIndicatorId);
}
