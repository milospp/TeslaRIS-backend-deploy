package rs.teslaris.core.assessment.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.json.JsonData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.TriConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.assessment.converter.EntityAssessmentClassificationConverter;
import rs.teslaris.core.assessment.dto.EntityAssessmentClassificationResponseDTO;
import rs.teslaris.core.assessment.model.AssessmentClassification;
import rs.teslaris.core.assessment.model.Commission;
import rs.teslaris.core.assessment.model.CommissionRelation;
import rs.teslaris.core.assessment.model.DocumentAssessmentClassification;
import rs.teslaris.core.assessment.model.EntityAssessmentClassification;
import rs.teslaris.core.assessment.model.ResultCalculationMethod;
import rs.teslaris.core.assessment.repository.DocumentAssessmentClassificationRepository;
import rs.teslaris.core.assessment.repository.EntityAssessmentClassificationRepository;
import rs.teslaris.core.assessment.repository.EventAssessmentClassificationRepository;
import rs.teslaris.core.assessment.repository.PublicationSeriesAssessmentClassificationRepository;
import rs.teslaris.core.assessment.service.interfaces.AssessmentClassificationService;
import rs.teslaris.core.assessment.service.interfaces.CommissionService;
import rs.teslaris.core.assessment.service.interfaces.DocumentAssessmentClassificationService;
import rs.teslaris.core.assessment.util.ClassificationPriorityMapping;
import rs.teslaris.core.indexmodel.DocumentPublicationIndex;
import rs.teslaris.core.indexmodel.DocumentPublicationType;
import rs.teslaris.core.indexrepository.DocumentPublicationIndexRepository;
import rs.teslaris.core.repository.document.DocumentRepository;
import rs.teslaris.core.repository.person.OrganisationUnitsRelationRepository;
import rs.teslaris.core.service.interfaces.commontypes.SearchService;
import rs.teslaris.core.service.interfaces.commontypes.TaskManagerService;
import rs.teslaris.core.service.interfaces.user.UserService;
import rs.teslaris.core.util.exceptionhandling.exception.NotFoundException;

@Service
@Transactional
@Slf4j
public class DocumentAssessmentClassificationServiceImpl
    extends EntityAssessmentClassificationServiceImpl implements
    DocumentAssessmentClassificationService {

    private final DocumentAssessmentClassificationRepository
        documentAssessmentClassificationRepository;

    private final DocumentPublicationIndexRepository documentPublicationIndexRepository;

    private final UserService userService;

    private final OrganisationUnitsRelationRepository organisationUnitsRelationRepository;

    private final PublicationSeriesAssessmentClassificationRepository
        publicationSeriesAssessmentClassificationRepository;

    private final DocumentRepository documentRepository;

    private final TaskManagerService taskManagerService;

    private final SearchService<DocumentPublicationIndex> searchService;

    private final EventAssessmentClassificationRepository eventAssessmentClassificationRepository;


    @Autowired
    public DocumentAssessmentClassificationServiceImpl(
        AssessmentClassificationService assessmentClassificationService,
        EntityAssessmentClassificationRepository entityAssessmentClassificationRepository,
        CommissionService commissionService,
        DocumentAssessmentClassificationRepository documentAssessmentClassificationRepository,
        DocumentPublicationIndexRepository documentPublicationIndexRepository,
        UserService userService,
        OrganisationUnitsRelationRepository organisationUnitsRelationRepository,
        PublicationSeriesAssessmentClassificationRepository publicationSeriesAssessmentClassificationRepository,
        DocumentRepository documentRepository, TaskManagerService taskManagerService,
        SearchService<DocumentPublicationIndex> searchService,
        EventAssessmentClassificationRepository eventAssessmentClassificationRepository) {
        super(assessmentClassificationService, commissionService,
            entityAssessmentClassificationRepository);
        this.documentAssessmentClassificationRepository =
            documentAssessmentClassificationRepository;
        this.documentPublicationIndexRepository = documentPublicationIndexRepository;
        this.userService = userService;
        this.organisationUnitsRelationRepository = organisationUnitsRelationRepository;
        this.publicationSeriesAssessmentClassificationRepository =
            publicationSeriesAssessmentClassificationRepository;
        this.documentRepository = documentRepository;
        this.taskManagerService = taskManagerService;
        this.searchService = searchService;
        this.eventAssessmentClassificationRepository = eventAssessmentClassificationRepository;
    }

    @Override
    public List<EntityAssessmentClassificationResponseDTO> getAssessmentClassificationsForDocument(
        Integer documentId) {
        return documentAssessmentClassificationRepository.findAssessmentClassificationsForDocument(
                documentId).stream().map(EntityAssessmentClassificationConverter::toDTO)
            .sorted((a, b) -> b.year().compareTo(a.year()))
            .collect(Collectors.toList());
    }

    @Override
    public void schedulePublicationClassification(LocalDateTime timeToRun,
                                                  Integer userId, LocalDate fromDate,
                                                  DocumentPublicationType documentPublicationType,
                                                  Integer commissionId,
                                                  List<Integer> authorIds,
                                                  List<Integer> orgUnitIds,
                                                  List<Integer> publishedInIds) {
        taskManagerService.scheduleTask(
            documentPublicationType.name() + "_Assessment-From-" + fromDate + "-" +
                UUID.randomUUID(), timeToRun,
            () -> {
                switch (documentPublicationType) {
                    case JOURNAL_PUBLICATION ->
                        classifyJournalPublications(fromDate, commissionId, authorIds, orgUnitIds,
                            publishedInIds);
                    case PROCEEDINGS_PUBLICATION ->
                        classifyProceedingsPublications(fromDate, commissionId, authorIds,
                            orgUnitIds,
                            publishedInIds);
                }
            }, userId);
    }

    @Override
    public void classifyJournalPublication(Integer journalPublicationId) {
        var journalPublicationIndex =
            documentPublicationIndexRepository.findDocumentPublicationIndexByDatabaseId(
                journalPublicationId);

        if (journalPublicationIndex.isEmpty()) {
            throw new NotFoundException(
                "Journal publication with ID " + journalPublicationId + " does not exist");
        }

        documentAssessmentClassificationRepository.deleteByDocumentId(
            journalPublicationIndex.get().getDatabaseId());
        journalPublicationIndex.get().getOrganisationUnitIds().forEach(organisationUnitId ->
            assessJournalPublication(journalPublicationIndex.get(), organisationUnitId, null));
    }

    @Override
    public void classifyProceedingsPublication(Integer proceedingsPublicationId) {
        var proceedingsPublicationIndex =
            documentPublicationIndexRepository.findDocumentPublicationIndexByDatabaseId(
                proceedingsPublicationId);

        if (proceedingsPublicationIndex.isEmpty()) {
            throw new NotFoundException(
                "Journal publication with ID " + proceedingsPublicationId + " does not exist");
        }

        documentAssessmentClassificationRepository.deleteByDocumentId(
            proceedingsPublicationIndex.get().getDatabaseId());
        proceedingsPublicationIndex.get().getOrganisationUnitIds().forEach(organisationUnitId ->
            assessProceedingsPublication(proceedingsPublicationIndex.get(), organisationUnitId,
                null));
    }

    private void classifyJournalPublications(LocalDate fromDate, Integer commissionId,
                                             List<Integer> authorIds, List<Integer> orgUnitIds,
                                             List<Integer> journalIds) {
        classifyPublications(fromDate, commissionId, authorIds, orgUnitIds, journalIds,
            DocumentPublicationType.JOURNAL_PUBLICATION, this::assessJournalPublication);
    }

    private void classifyProceedingsPublications(LocalDate fromDate, Integer commissionId,
                                                 List<Integer> authorIds, List<Integer> orgUnitIds,
                                                 List<Integer> eventIds) {
        classifyPublications(fromDate, commissionId, authorIds, orgUnitIds, eventIds,
            DocumentPublicationType.PROCEEDINGS_PUBLICATION, this::assessProceedingsPublication);
    }

    private void classifyPublications(LocalDate fromDate, Integer commissionId,
                                      List<Integer> authorIds, List<Integer> orgUnitIds,
                                      List<Integer> entityIds,
                                      DocumentPublicationType publicationType,
                                      TriConsumer<DocumentPublicationIndex, Integer, Commission> assessFunction) {
        int pageNumber = 0;
        int chunkSize = 10;
        boolean hasNextPage = true;

        Commission presetCommission = Objects.nonNull(commissionId)
            ? commissionService.findOneWithFetchedRelations(commissionId)
            : null;

        while (hasNextPage) {
            List<DocumentPublicationIndex> chunk = searchService
                .runQuery(findAllDocumentPublicationsByFilters(fromDate.toString(),
                        publicationType.name(),
                        authorIds, orgUnitIds, entityIds),
                    PageRequest.of(pageNumber, chunkSize), DocumentPublicationIndex.class,
                    "document_publication").getContent();

            chunk.forEach(publicationIndex -> {
                documentAssessmentClassificationRepository.deleteByDocumentId(
                    publicationIndex.getDatabaseId());

                if (Objects.nonNull(presetCommission)) {
                    assessFunction.accept(publicationIndex, null, presetCommission);
                } else {
                    publicationIndex.getOrganisationUnitIds().forEach(organisationUnitId ->
                        assessFunction.accept(publicationIndex, organisationUnitId, null));
                }
            });

            pageNumber++;
            hasNextPage = chunk.size() == chunkSize;
        }
    }

    private void assessJournalPublication(DocumentPublicationIndex journalPublicationIndex,
                                          Integer organisationUnitId,
                                          Commission presetCommission) {
        Commission commission = (presetCommission != null)
            ? presetCommission
            : findCommissionInHierarchy(organisationUnitId).orElse(null);

        if (commission == null) {
            log.info("No commission found for organisation unit {} or its hierarchy.",
                organisationUnitId);
            return;
        }

        performPublicationAssessment((year, classifications, commissionObj) -> {
                var classification = publicationSeriesAssessmentClassificationRepository
                    .findAssessmentClassificationsForPublicationSeriesAndCommissionAndYear(
                        journalPublicationIndex.getJournalId(), commission.getId(), year);

                if (classification.isPresent()) {
                    classifications.add(classification.get().getAssessmentClassification());
                } else {
                    handleRelationAssessments(commission,
                        (targetCommissionId) -> publicationSeriesAssessmentClassificationRepository
                            .findAssessmentClassificationsForPublicationSeriesAndCommissionAndYear(
                                journalPublicationIndex.getJournalId(), targetCommissionId, year)
                    ).ifPresent(classifications::add);
                }
            },
            journalPublicationIndex.getYear(), journalPublicationIndex.getDatabaseId(), commission);
    }

    private void assessProceedingsPublication(DocumentPublicationIndex proceedingsPublicationIndex,
                                              Integer organisationUnitId,
                                              Commission presetCommission) {
        Commission commission = (presetCommission != null)
            ? presetCommission
            : findCommissionInHierarchy(organisationUnitId).orElse(null);

        if (commission == null) {
            log.info("No commission found for organisation unit {} or its hierarchy.",
                organisationUnitId);
            return;
        }

        performPublicationAssessment((year, classifications, commissionObj) ->
            {
                var classification = eventAssessmentClassificationRepository
                    .findAssessmentClassificationsForEventAndCommissionAndYear(
                        proceedingsPublicationIndex.getEventId(), commission.getId(), year);

                if (classification.isPresent()) {
                    classifications.add(classification.get().getAssessmentClassification());
                } else {
                    handleRelationAssessments(commission,
                        (targetCommissionId) -> {
                            var assessmentClassification = eventAssessmentClassificationRepository
                                .findAssessmentClassificationsForEventAndCommissionAndYear(
                                    proceedingsPublicationIndex.getEventId(), targetCommissionId, year)
                                .orElse(null);
                            return Objects.nonNull(assessmentClassification) ? Optional.of(
                                assessmentClassification) : Optional.empty();
                        }
                    ).ifPresent(classifications::add);
                }
            },
            proceedingsPublicationIndex.getYear(), proceedingsPublicationIndex.getDatabaseId(),
            commission);
    }

    private void performPublicationAssessment(
        TriConsumer<Integer, List<AssessmentClassification>, Commission> yearHandler,
        Integer classificationYear, Integer documentId,
        Commission commission) {
        var yearsToConsider =
            List.of(classificationYear, classificationYear - 1, classificationYear - 2);
        var classifications = new ArrayList<AssessmentClassification>();

        yearsToConsider.forEach(year -> {
            yearHandler.accept(year, classifications, commission);
        });

        if (!classifications.isEmpty()) {
            var bestClassification =
                ClassificationPriorityMapping.getClassificationBasedOnCriteria(classifications,
                    ResultCalculationMethod.BEST_VALUE);
            bestClassification.ifPresent((documentClassification) -> {
                handleClassification(documentClassification,
                    commission, documentId, classificationYear);
            });
        }
    }

    private Optional<Commission> findCommissionInHierarchy(Integer organisationUnitId) {
        Optional<Commission> commission;
        do {
            commission = userService.findCommissionForOrganisationUnitId(organisationUnitId);
            if (commission.isEmpty()) {
                var superOU = organisationUnitsRelationRepository.getSuperOU(organisationUnitId);
                if (superOU.isPresent()) {
                    organisationUnitId = superOU.get().getId();
                } else {
                    break;
                }
            }
        } while (commission.isEmpty());
        return commission;
    }

    private void handleClassification(AssessmentClassification classification,
                                      Commission commission,
                                      Integer documentId, Integer classificationYear) {
        var mappedCode = ClassificationPriorityMapping.getDocClassificationCodeBasedOnCode(
            classification.getCode(), documentId);
        if (mappedCode.isEmpty()) {
            return;
        }

        var documentClassification = assessmentClassificationService
            .readAssessmentClassificationByCode(mappedCode.get());
        saveDocumentClassification(documentClassification, commission, documentId,
            classificationYear);
    }

    private Optional<AssessmentClassification> handleRelationAssessments(
        Commission commission,
        Function<Integer, Optional<EntityAssessmentClassification>> classificationFinder) {
        var sortedRelations = commission.getRelations().stream()
            .sorted(Comparator.comparingInt(CommissionRelation::getPriority))
            .toList();

        for (var relation : sortedRelations) {
            var respectedClassification =
                respectRelationAssessment(relation, classificationFinder);
            if (respectedClassification.isPresent()) {
                return respectedClassification;
            }
        }

        return Optional.empty();
    }

    private Optional<AssessmentClassification> respectRelationAssessment(
        CommissionRelation commissionRelation,
        Function<Integer, Optional<EntityAssessmentClassification>> classificationFinder) {
        var classifications = new ArrayList<AssessmentClassification>();
        commissionRelation.getTargetCommissions().forEach(targetCommission -> {
            var classification = classificationFinder.apply(targetCommission.getId());
            classification.ifPresent(journalClassification -> classifications.add(
                journalClassification.getAssessmentClassification()));
        });

        if (classifications.isEmpty()) {
            return Optional.empty();
        }

        return ClassificationPriorityMapping.getClassificationBasedOnCriteria(classifications,
            commissionRelation.getResultCalculationMethod());
    }

    public Query findAllDocumentPublicationsByFilters(
        String date, String type,
        List<Integer> authorIds,
        List<Integer> organisationUnitIds,
        List<Integer> publishedInIds) {

        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.must(sb -> sb.range(r -> r.field("last_edited").gt(JsonData.of(date))));
            b.must(sb -> sb.term(t -> t.field("type").value(type)));

            if (!authorIds.isEmpty()) {
                var authorIdTerms = new TermsQueryField.Builder()
                    .value(authorIds.stream().map(FieldValue::of).toList())
                    .build();
                b.must(sb -> sb.terms(t -> t.field("author_ids").terms(authorIdTerms)));
            }

            if (!organisationUnitIds.isEmpty()) {
                var orgUnitIdTerms = new TermsQueryField.Builder()
                    .value(organisationUnitIds.stream().map(FieldValue::of).toList())
                    .build();
                b.must(sb -> sb.terms(t -> t.field("organisation_unit_ids").terms(orgUnitIdTerms)));
            }

            if (!publishedInIds.isEmpty()) {
                var publishedInIdTerms = new TermsQueryField.Builder()
                    .value(publishedInIds.stream().map(FieldValue::of).toList())
                    .build();
                b.must(sb -> sb.terms(t -> t.field(
                    type.equals(DocumentPublicationType.PROCEEDINGS_PUBLICATION.name()) ?
                        "event_id" : "journal_id").terms(publishedInIdTerms)));
            }

            return b;
        })))._toQuery();
    }

    private void saveDocumentClassification(AssessmentClassification assessmentClassification,
                                            Commission commission, Integer documentId,
                                            Integer classificationYear) {
        var documentClassification = new DocumentAssessmentClassification();
        documentClassification.setTimestamp(LocalDateTime.now());
        documentClassification.setCommission(commission);
        documentClassification.setAssessmentClassification(assessmentClassification);
        documentClassification.setClassificationYear(classificationYear);
        documentClassification.setDocument(documentRepository.getReferenceById(documentId));

        documentAssessmentClassificationRepository.save(documentClassification);
    }
}
