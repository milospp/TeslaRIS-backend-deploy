package rs.teslaris.core.assessment.service.impl.statistics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.assessment.model.DocumentIndicator;
import rs.teslaris.core.assessment.model.EntityIndicator;
import rs.teslaris.core.assessment.model.OrganisationUnitIndicator;
import rs.teslaris.core.assessment.model.PersonIndicator;
import rs.teslaris.core.assessment.repository.DocumentIndicatorRepository;
import rs.teslaris.core.assessment.repository.OrganisationUnitIndicatorRepository;
import rs.teslaris.core.assessment.repository.PersonIndicatorRepository;
import rs.teslaris.core.assessment.service.interfaces.IndicatorService;
import rs.teslaris.core.assessment.service.interfaces.statistics.StatisticsService;
import rs.teslaris.core.assessment.util.IndicatorMappingConfigurationLoader;
import rs.teslaris.core.indexmodel.DocumentPublicationIndex;
import rs.teslaris.core.indexmodel.OrganisationUnitIndex;
import rs.teslaris.core.indexmodel.PersonIndex;
import rs.teslaris.core.indexmodel.statistics.StatisticsIndex;
import rs.teslaris.core.indexmodel.statistics.StatisticsType;
import rs.teslaris.core.indexrepository.DocumentPublicationIndexRepository;
import rs.teslaris.core.indexrepository.OrganisationUnitIndexRepository;
import rs.teslaris.core.indexrepository.PersonIndexRepository;
import rs.teslaris.core.indexrepository.statistics.StatisticsIndexRepository;
import rs.teslaris.core.model.document.Document;
import rs.teslaris.core.model.institution.OrganisationUnit;
import rs.teslaris.core.model.person.Person;
import rs.teslaris.core.repository.document.DocumentRepository;
import rs.teslaris.core.repository.person.OrganisationUnitRepository;
import rs.teslaris.core.repository.person.PersonRepository;
import rs.teslaris.core.util.FunctionalUtil;
import rs.teslaris.core.util.exceptionhandling.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsIndexRepository statisticsIndexRepository;

    private final IndicatorService indicatorService;

    private final DocumentPublicationIndexRepository documentPublicationIndexRepository;

    private final PersonIndexRepository personIndexRepository;

    private final OrganisationUnitIndexRepository organisationUnitIndexRepository;

    private final DocumentIndicatorRepository documentIndicatorRepository;

    private final PersonIndicatorRepository personIndicatorRepository;

    private final OrganisationUnitIndicatorRepository organisationUnitIndicatorRepository;

    private final DocumentRepository documentRepository;

    private final PersonRepository personRepository;

    private final OrganisationUnitRepository organisationUnitRepository;


    @Override
    public void savePersonView(Integer personId) {
        var statisticsEntry = new StatisticsIndex();
        statisticsEntry.setPersonId(personId);
        saveView(statisticsEntry);
    }

    @Override
    public void saveDocumentView(Integer documentId) {
        var statisticsEntry = new StatisticsIndex();
        statisticsEntry.setDocumentId(documentId);
        saveView(statisticsEntry);
    }

    @Override
    public void saveOrganisationUnitView(Integer organisationUnitId) {
        var statisticsEntry = new StatisticsIndex();
        statisticsEntry.setOrganisationUnitId(organisationUnitId);
        saveView(statisticsEntry);
    }

    @Override
    public void saveDocumentDownload(Integer documentId) {
        var statisticsEntry = new StatisticsIndex();
        statisticsEntry.setDocumentId(documentId);
        saveDownload(statisticsEntry);
    }

    private void saveView(StatisticsIndex index) {
        index.setType(StatisticsType.VIEW.name());
        save(index);
    }

    private void saveDownload(StatisticsIndex index) {
        index.setType(StatisticsType.DOWNLOAD.name());
        save(index);
    }

    private void save(StatisticsIndex index) {
        index.setTimestamp(LocalDateTime.now());

        updateTotalCount(index);

        statisticsIndexRepository.save(index);
    }

    protected void updateTotalCount(StatisticsIndex index) {
        var indicatorCodes =
            IndicatorMappingConfigurationLoader.getIndicatorNameForLoaderMethodName(
                index.getType().equals(StatisticsType.VIEW.name()) ? "updateTotalViews" :
                    "updateTotalDownloads");
        var indicator = indicatorService.getIndicatorByCode(indicatorCodes.getFirst());

        if (Objects.isNull(indicator)) {
            log.error("Indicator not configured for loader: {}", "updateDailyViews");
            return;
        }

        if (Objects.nonNull(index.getDocumentId())) {
            updateIndicator(index.getDocumentId(),
                indicator.getCode(),
                documentRepository::findById,
                documentIndicatorRepository::findIndicatorForCodeAndDocumentId,
                (id) -> new DocumentIndicator(),
                entityIndicator -> {
                    entityIndicator.setNumericValue(entityIndicator.getNumericValue() + 1);
                    entityIndicator.setTimestamp(LocalDateTime.now());
                    documentIndicatorRepository.save(entityIndicator);
                });
        } else if (Objects.nonNull(index.getPersonId())) {
            updateIndicator(index.getPersonId(),
                indicator.getCode(),
                personRepository::findById,
                personIndicatorRepository::findIndicatorForCodeAndPersonId,
                (id) -> new PersonIndicator(),
                entityIndicator -> {
                    entityIndicator.setNumericValue(entityIndicator.getNumericValue() + 1);
                    entityIndicator.setTimestamp(LocalDateTime.now());
                    personIndicatorRepository.save(entityIndicator);
                });
        } else if (Objects.nonNull(index.getOrganisationUnitId())) {
            updateIndicator(index.getOrganisationUnitId(),
                indicator.getCode(),
                organisationUnitRepository::findById,
                organisationUnitIndicatorRepository::findIndicatorForCodeAndOrganisationUnitId,
                (id) -> new OrganisationUnitIndicator(),
                entityIndicator -> {
                    entityIndicator.setNumericValue(entityIndicator.getNumericValue() + 1);
                    entityIndicator.setTimestamp(LocalDateTime.now());
                    organisationUnitIndicatorRepository.save(entityIndicator);
                });
        }
    }

    @Scheduled(cron = "${statistics.schedule.views}")
    protected void updatePeriodViews() {
        var indicatorCodes =
            IndicatorMappingConfigurationLoader.getIndicatorNameForLoaderMethodName(
                "updatePeriodViews");

        var startPeriods = loadStartPeriodsForIndicators(indicatorCodes);

        updateStatisticsFromPeriod(startPeriods, indicatorCodes,
            StatisticsType.VIEW);
    }

    @Scheduled(cron = "${statistics.schedule.downloads}")
    protected void updatePeriodDownloads() {
        var indicatorCodes =
            IndicatorMappingConfigurationLoader.getIndicatorNameForLoaderMethodName(
                "updatePeriodDownloads");

        var startPeriods = loadStartPeriodsForIndicators(indicatorCodes);

        updateStatisticsFromPeriod(startPeriods, indicatorCodes,
            StatisticsType.DOWNLOAD);
    }

    private List<LocalDateTime> loadStartPeriodsForIndicators(List<String> indicatorCodes) {
        List<LocalDateTime> startPeriods = new ArrayList<>();
        indicatorCodes.forEach(code -> {
            var indicator = indicatorService.getIndicatorByCode(code);

            if (Objects.isNull(indicator)) {
                log.error("Indicator not configured for loader: {}", "updateWeeklyViews");
                throw new NotFoundException("Missing indicator for code: " + code);
            }

            setStartPeriodBasedOnIndicatorCode(code, startPeriods);
        });

        return startPeriods;
    }

    private void setStartPeriodBasedOnIndicatorCode(String code, List<LocalDateTime> startPeriods) {
        if (code.contains("Day")) {
            startPeriods.add(LocalDateTime.now().minusHours(24));
        } else if (code.contains("Week")) {
            startPeriods.add(LocalDateTime.now().minusDays(7));
        } else if (code.contains("Month")) {
            startPeriods.add(LocalDateTime.now().minusDays(30));
        }
    }

    private void updateStatisticsFromPeriod(List<LocalDateTime> startPeriods,
                                            List<String> indicatorCodes,
                                            StatisticsType statisticsType) {
        CompletableFuture<Void> documentTask = CompletableFuture.runAsync(() -> {
            updateEntityStatisticInPeriod(
                startPeriods,
                indicatorCodes,
                documentPublicationIndexRepository,
                documentRepository,
                (start, document) -> statisticsIndexRepository.countByTimestampBetweenAndTypeAndDocumentId(
                    start, LocalDateTime.now(), statisticsType.name(),
                    document.getDatabaseId()),
                DocumentPublicationIndex::getDatabaseId,
                documentIndicatorRepository::findIndicatorForCodeAndDocumentId,
                id -> new DocumentIndicator(),
                (entityIndicator, document) -> entityIndicator.setDocument(
                    documentRepository.findById(document.getDatabaseId()).orElseThrow()),
                documentIndicatorRepository
            );
        });

        CompletableFuture<Void> personTask = CompletableFuture.runAsync(() -> {
            updateEntityStatisticInPeriod(
                startPeriods,
                indicatorCodes,
                personIndexRepository,
                personRepository,
                (start, person) -> statisticsIndexRepository.countByTimestampBetweenAndTypeAndPersonId(
                    start, LocalDateTime.now(), statisticsType.name(), person.getDatabaseId()),
                PersonIndex::getDatabaseId,
                personIndicatorRepository::findIndicatorForCodeAndPersonId,
                id -> new PersonIndicator(),
                (entityIndicator, person) -> entityIndicator.setPerson(
                    personRepository.findById(person.getDatabaseId()).orElseThrow()),
                personIndicatorRepository
            );
        });

        CompletableFuture<Void> organisationTask = CompletableFuture.runAsync(() -> {
            updateEntityStatisticInPeriod(
                startPeriods,
                indicatorCodes,
                organisationUnitIndexRepository,
                organisationUnitRepository,
                (start, ou) -> statisticsIndexRepository.countByTimestampBetweenAndTypeAndOrganisationUnitId(
                    start, LocalDateTime.now(), statisticsType.name(), ou.getDatabaseId()),
                OrganisationUnitIndex::getDatabaseId,
                organisationUnitIndicatorRepository::findIndicatorForCodeAndOrganisationUnitId,
                id -> new OrganisationUnitIndicator(),
                (entityIndicator, ou) -> entityIndicator.setOrganisationUnit(
                    organisationUnitRepository.findById(ou.getDatabaseId()).orElseThrow()),
                organisationUnitIndicatorRepository
            );
        });

        var allTasks = CompletableFuture.allOf(documentTask, personTask, organisationTask);

        try {
            allTasks.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while updating statistics for " + statisticsType.name(), e);
            Thread.currentThread().interrupt();
        }
    }

    private <T, D, I extends EntityIndicator> void updateEntityStatisticInPeriod(
        List<LocalDateTime> startPeriods,
        List<String> indicatorCodes,
        ElasticsearchRepository<T, String> indexRepository,
        JpaRepository<D, Integer> entityRepository,
        BiFunction<LocalDateTime, T, Integer> countFunction,
        Function<T, Integer> getEntityId,
        BiFunction<String, Integer, Optional<I>> findIndicatorByEntityId,
        Function<Integer, I> createNewIndicator,
        BiConsumer<I, T> setEntity,
        JpaRepository<I, Integer> entityIndicatorRepository) {

        int pageNumber = 0;
        int chunkSize = 50;
        boolean hasNextPage = true;

        while (hasNextPage) {
            List<T> chunk =
                indexRepository.findAll(PageRequest.of(pageNumber, chunkSize)).getContent();

            chunk.forEach(entity -> {
                Integer entityId = getEntityId.apply(entity);

                FunctionalUtil.forEachWithCounter(indicatorCodes, (i, indicatorCode) -> {
                    Integer statisticsCount = countFunction.apply(startPeriods.get(i), entity);

                    updateIndicator(entityId,
                        indicatorCode,
                        entityRepository::findById,
                        findIndicatorByEntityId,
                        id -> {
                            I indicator = createNewIndicator.apply(id);
                            setEntity.accept(indicator, entity);
                            return indicator;
                        },
                        entityIndicator -> {
                            entityIndicator.setNumericValue(Double.valueOf(statisticsCount));
                            entityIndicator.setTimestamp(LocalDateTime.now());
                            entityIndicatorRepository.save(entityIndicator);
                        });
                });

            });

            pageNumber++;
            hasNextPage = chunk.size() == chunkSize;
        }
    }

    private <T, R> void updateIndicator(Integer id,
                                        String indicatorCode,
                                        Function<Integer, Optional<T>> findEntityById,
                                        BiFunction<String, Integer, Optional<R>> findIndicator,
                                        Function<Integer, R> createIndicator,
                                        Consumer<R> updateAndSaveIndicatorValue) {

        var optionalIndicator = findIndicator.apply(indicatorCode, id);
        R indicatorEntity;
        if (optionalIndicator.isEmpty()) {
            indicatorEntity = createIndicator.apply(id);
            if (indicatorEntity instanceof DocumentIndicator) {
                ((DocumentIndicator) indicatorEntity).setDocument(
                    (Document) findEntityById.apply(id).orElseThrow());
            } else if (indicatorEntity instanceof PersonIndicator) {
                ((PersonIndicator) indicatorEntity).setPerson(
                    (Person) findEntityById.apply(id).orElseThrow());
            } else if (indicatorEntity instanceof OrganisationUnitIndicator) {
                ((OrganisationUnitIndicator) indicatorEntity).setOrganisationUnit(
                    (OrganisationUnit) findEntityById.apply(id).orElseThrow());
            }
            ((EntityIndicator) indicatorEntity).setIndicator(
                indicatorService.getIndicatorByCode(indicatorCode));
            ((EntityIndicator) indicatorEntity).setNumericValue(0.0);
        } else {
            indicatorEntity = optionalIndicator.get();
        }

        updateAndSaveIndicatorValue.accept(indicatorEntity);
    }
}
