package rs.teslaris.core.unit;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import rs.teslaris.core.indexmodel.EntityType;
import rs.teslaris.core.service.impl.commontypes.ReindexServiceImpl;
import rs.teslaris.core.service.interfaces.document.BookSeriesService;
import rs.teslaris.core.service.interfaces.document.ConferenceService;
import rs.teslaris.core.service.interfaces.document.DatasetService;
import rs.teslaris.core.service.interfaces.document.DocumentFileService;
import rs.teslaris.core.service.interfaces.document.DocumentPublicationService;
import rs.teslaris.core.service.interfaces.document.JournalPublicationService;
import rs.teslaris.core.service.interfaces.document.JournalService;
import rs.teslaris.core.service.interfaces.document.MonographPublicationService;
import rs.teslaris.core.service.interfaces.document.MonographService;
import rs.teslaris.core.service.interfaces.document.PatentService;
import rs.teslaris.core.service.interfaces.document.ProceedingsPublicationService;
import rs.teslaris.core.service.interfaces.document.ProceedingsService;
import rs.teslaris.core.service.interfaces.document.PublisherService;
import rs.teslaris.core.service.interfaces.document.SoftwareService;
import rs.teslaris.core.service.interfaces.document.ThesisService;
import rs.teslaris.core.service.interfaces.person.OrganisationUnitService;
import rs.teslaris.core.service.interfaces.person.PersonService;
import rs.teslaris.core.service.interfaces.user.UserService;

@SpringBootTest
public class ReindexServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private PersonService personService;

    @Mock
    private OrganisationUnitService organisationUnitService;

    @Mock
    private JournalService journalService;

    @Mock
    private BookSeriesService bookSeriesService;

    @Mock
    private ConferenceService conferenceService;

    @Mock
    private DocumentPublicationService documentPublicationService;

    @Mock
    private DocumentFileService documentFileService;

    @Mock
    private JournalPublicationService journalPublicationService;

    @Mock
    private ProceedingsService proceedingsService;

    @Mock
    private ProceedingsPublicationService proceedingsPublicationService;

    @Mock
    private PatentService patentService;

    @Mock
    private SoftwareService softwareService;

    @Mock
    private DatasetService datasetService;

    @Mock
    private MonographService monographService;

    @Mock
    private MonographPublicationService monographPublicationService;

    @Mock
    private ThesisService thesisService;

    @InjectMocks
    private ReindexServiceImpl reindexService;


    static Stream<EntityType> indexTypeProvider() {
        return Stream.of(
            EntityType.USER_ACCOUNT,
            EntityType.JOURNAL,
            EntityType.PUBLISHER,
            EntityType.PERSON,
            EntityType.ORGANISATION_UNIT,
            EntityType.BOOK_SERIES,
            EntityType.EVENT,
            EntityType.PUBLICATION
        );
    }

    @ParameterizedTest
    @MethodSource("indexTypeProvider")
    void testReindexDatabase(EntityType indexType) {
        // Given
        var indexesToRepopulate = List.of(indexType);

        // When
        reindexService.reindexDatabase(indexesToRepopulate);

        // Then
        verify(userService,
            indexType.equals(EntityType.USER_ACCOUNT) ? times(1) : never()).reindexUsers();
        verify(publisherService,
            indexType.equals(EntityType.PUBLISHER) ? times(1) : never()).reindexPublishers();
        verify(personService,
            indexType.equals(EntityType.PERSON) ? times(1) : never()).reindexPersons();
        verify(organisationUnitService, indexType.equals(EntityType.ORGANISATION_UNIT) ? times(1) :
            never()).reindexOrganisationUnits();
        verify(journalService,
            indexType.equals(EntityType.JOURNAL) ? times(1) : never()).reindexJournals();
        verify(bookSeriesService,
            indexType.equals(EntityType.BOOK_SERIES) ? times(1) : never()).reindexBookSeries();
        verify(conferenceService,
            indexType.equals(EntityType.EVENT) ? times(1) : never()).reindexConferences();
        verify(documentFileService,
            indexType.equals(EntityType.PUBLICATION) ? times(1) : never()).deleteIndexes();
        verify(documentPublicationService,
            indexType.equals(EntityType.PUBLICATION) ? times(1) : never()).deleteIndexes();
        verify(journalPublicationService, indexType.equals(EntityType.PUBLICATION) ? times(1) :
            never()).reindexJournalPublications();
        verify(proceedingsPublicationService, indexType.equals(EntityType.PUBLICATION) ? times(1) :
            never()).reindexProceedingsPublications();
    }

}
