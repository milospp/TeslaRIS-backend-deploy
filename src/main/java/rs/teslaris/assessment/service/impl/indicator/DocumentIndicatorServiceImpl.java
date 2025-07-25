package rs.teslaris.assessment.service.impl.indicator;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.assessment.converter.EntityIndicatorConverter;
import rs.teslaris.assessment.dto.indicator.DocumentIndicatorDTO;
import rs.teslaris.assessment.dto.indicator.EntityIndicatorResponseDTO;
import rs.teslaris.assessment.model.indicator.DocumentIndicator;
import rs.teslaris.assessment.repository.indicator.DocumentIndicatorRepository;
import rs.teslaris.assessment.repository.indicator.EntityIndicatorRepository;
import rs.teslaris.assessment.service.impl.cruddelegate.DocumentIndicatorJPAServiceImpl;
import rs.teslaris.assessment.service.interfaces.indicator.DocumentIndicatorService;
import rs.teslaris.assessment.service.interfaces.indicator.IndicatorService;
import rs.teslaris.core.annotation.Traceable;
import rs.teslaris.core.model.commontypes.AccessLevel;
import rs.teslaris.core.model.document.Document;
import rs.teslaris.core.model.document.Thesis;
import rs.teslaris.core.service.interfaces.document.DocumentFileService;
import rs.teslaris.core.service.interfaces.document.DocumentPublicationService;
import rs.teslaris.core.service.interfaces.user.UserService;
import rs.teslaris.core.util.exceptionhandling.exception.ThesisException;

@Service
@Transactional
@Traceable
public class DocumentIndicatorServiceImpl extends EntityIndicatorServiceImpl
    implements DocumentIndicatorService {

    private final DocumentIndicatorJPAServiceImpl documentIndicatorJPAService;

    private final DocumentIndicatorRepository documentIndicatorRepository;

    private final DocumentPublicationService documentPublicationService;

    private final UserService userService;


    @Autowired
    public DocumentIndicatorServiceImpl(
        EntityIndicatorRepository entityIndicatorRepository,
        DocumentFileService documentFileService,
        IndicatorService indicatorService,
        DocumentIndicatorJPAServiceImpl documentIndicatorJPAService,
        DocumentIndicatorRepository documentIndicatorRepository,
        DocumentPublicationService documentPublicationService, UserService userService) {
        super(indicatorService, entityIndicatorRepository, documentFileService);
        this.documentIndicatorJPAService = documentIndicatorJPAService;
        this.documentIndicatorRepository = documentIndicatorRepository;
        this.documentPublicationService = documentPublicationService;
        this.userService = userService;
    }

    @Override
    public List<EntityIndicatorResponseDTO> getIndicatorsForDocument(Integer documentId,
                                                                     AccessLevel accessLevel) {
        return documentIndicatorRepository.findIndicatorsForDocumentAndIndicatorAccessLevel(
            documentId, accessLevel).stream().map(
            EntityIndicatorConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    public DocumentIndicator createDocumentIndicator(DocumentIndicatorDTO documentIndicatorDTO,
                                                     Integer userId) {
        var newDocumentIndicator = new DocumentIndicator();

        setCommonFields(newDocumentIndicator, documentIndicatorDTO);
        newDocumentIndicator.setUser(userService.findOne(userId));

        var document = documentPublicationService.findOne(documentIndicatorDTO.getDocumentId());
        checkIfDocumentIsAThesis(document);

        newDocumentIndicator.setDocument(document);

        return documentIndicatorJPAService.save(newDocumentIndicator);
    }

    private void checkIfDocumentIsAThesis(Document document) {
        if (document instanceof Thesis && ((Thesis) document).getIsOnPublicReview()) {
            throw new ThesisException("Thesis is on public review, can't add indicators.");
        }
    }

    @Override
    public void updateDocumentIndicator(Integer documentIndicatorId,
                                        DocumentIndicatorDTO documentIndicatorDTO) {
        var documentIndicatorToUpdate = documentIndicatorJPAService.findOne(documentIndicatorId);

        setCommonFields(documentIndicatorToUpdate, documentIndicatorDTO);

        var document = documentPublicationService.findOne(documentIndicatorDTO.getDocumentId());
        checkIfDocumentIsAThesis(document);

        documentIndicatorToUpdate.setDocument(
            documentPublicationService.findOne(documentIndicatorDTO.getDocumentId()));

        documentIndicatorJPAService.save(documentIndicatorToUpdate);
    }
}
