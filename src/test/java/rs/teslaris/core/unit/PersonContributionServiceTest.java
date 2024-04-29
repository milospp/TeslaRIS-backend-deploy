package rs.teslaris.core.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import rs.teslaris.core.dto.document.DocumentDTO;
import rs.teslaris.core.dto.document.PersonDocumentContributionDTO;
import rs.teslaris.core.dto.person.PersonNameDTO;
import rs.teslaris.core.model.document.DocumentContributionType;
import rs.teslaris.core.model.document.JournalPublication;
import rs.teslaris.core.model.person.Contact;
import rs.teslaris.core.model.person.Person;
import rs.teslaris.core.model.person.PersonName;
import rs.teslaris.core.model.person.PersonalInfo;
import rs.teslaris.core.model.person.PostalAddress;
import rs.teslaris.core.service.impl.person.PersonContributionServiceImpl;
import rs.teslaris.core.service.interfaces.commontypes.CountryService;
import rs.teslaris.core.service.interfaces.commontypes.MultilingualContentService;
import rs.teslaris.core.service.interfaces.person.OrganisationUnitService;
import rs.teslaris.core.service.interfaces.person.PersonService;

@SpringBootTest
public class PersonContributionServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private CountryService countryService;

    @Mock
    private OrganisationUnitService organisationUnitService;

    @Mock
    private MultilingualContentService multilingualContentService;

    @InjectMocks
    private PersonContributionServiceImpl personContributionService;


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(personContributionService, "contributionApprovedByDefault",
            true);
    }

    @Test
    public void testSetPersonDocumentContributionsForDocument() {
        // Given
        var document = new JournalPublication();
        var documentDTO = new DocumentDTO();

        var contributor = new Person();
        contributor.setName(new PersonName());
        var personalInfo = new PersonalInfo();
        personalInfo.setPostalAddress(new PostalAddress(null, new HashSet<>(), new HashSet<>()));
        personalInfo.setContact(new Contact("email", "phone number"));
        contributor.setPersonalInfo(personalInfo);

        var contributionDTO1 = new PersonDocumentContributionDTO();
        contributionDTO1.setContributionType(DocumentContributionType.AUTHOR);
        contributionDTO1.setIsMainContributor(true);
        contributionDTO1.setIsCorrespondingContributor(false);
        contributionDTO1.setContributionDescription(new ArrayList<>());
        contributionDTO1.setOrderNumber(1);
        contributionDTO1.setPersonId(1);
        contributionDTO1.setPersonName(
            new PersonNameDTO(null, "Ime", "Srednje ime", "Prezime", null, null));

        var contributionsDTO = new ArrayList<PersonDocumentContributionDTO>();
        contributionsDTO.add(contributionDTO1);
        documentDTO.setContributions(contributionsDTO);

        when(personService.findOne(1)).thenReturn(contributor);

        // When
        personContributionService.setPersonDocumentContributionsForDocument(document, documentDTO);

        // Then
        var contributions = document.getContributors();
        assertTrue(contributions.size() > 0);
    }
}
