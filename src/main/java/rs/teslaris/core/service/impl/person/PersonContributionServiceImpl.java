package rs.teslaris.core.service.impl.person;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.dto.document.BookSeriesDTO;
import rs.teslaris.core.dto.document.DocumentDTO;
import rs.teslaris.core.dto.document.EventDTO;
import rs.teslaris.core.dto.document.PersonContributionDTO;
import rs.teslaris.core.dto.document.PublicationSeriesDTO;
import rs.teslaris.core.model.commontypes.ApproveStatus;
import rs.teslaris.core.model.document.AffiliationStatement;
import rs.teslaris.core.model.document.Document;
import rs.teslaris.core.model.document.Event;
import rs.teslaris.core.model.document.PersonContribution;
import rs.teslaris.core.model.document.PersonDocumentContribution;
import rs.teslaris.core.model.document.PersonEventContribution;
import rs.teslaris.core.model.document.PersonPublicationSeriesContribution;
import rs.teslaris.core.model.document.PublicationSeries;
import rs.teslaris.core.model.person.Contact;
import rs.teslaris.core.model.person.Person;
import rs.teslaris.core.model.person.PersonName;
import rs.teslaris.core.model.person.PostalAddress;
import rs.teslaris.core.service.interfaces.commontypes.CountryService;
import rs.teslaris.core.service.interfaces.commontypes.MultilingualContentService;
import rs.teslaris.core.service.interfaces.person.OrganisationUnitService;
import rs.teslaris.core.service.interfaces.person.PersonContributionService;
import rs.teslaris.core.service.interfaces.person.PersonService;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonContributionServiceImpl implements PersonContributionService {

    private final PersonService personService;

    private final CountryService countryService;

    private final OrganisationUnitService organisationUnitService;

    private final MultilingualContentService multilingualContentService;


    @Value("${contribution.approved_by_default}")
    private Boolean contributionApprovedByDefault;


    @Override
    public void setPersonDocumentContributionsForDocument(Document document,
                                                          DocumentDTO documentDTO) {
        documentDTO.getContributions().forEach(contributionDTO -> {
            var contribution = new PersonDocumentContribution();
            setPersonContributionCommonFields(contribution, contributionDTO);

            contribution.setContributionType(contributionDTO.getContributionType());
            contribution.setIsMainContributor(contributionDTO.getIsMainContributor());
            contribution.setIsCorrespondingContributor(
                contributionDTO.getIsCorrespondingContributor());

            document.addDocumentContribution(contribution);
        });
    }

    @Override
    public void setPersonPublicationSeriesContributionsForJournal(
        PublicationSeries publicationSeries,
        PublicationSeriesDTO journalDTO) {
        publicationSeries.getContributions().clear();

        journalDTO.getContributions().forEach(contributionDTO -> {
            var contribution = new PersonPublicationSeriesContribution();
            setPersonContributionCommonFields(contribution, contributionDTO);

            contribution.setContributionType(contributionDTO.getContributionType());
            contribution.setDateFrom(contributionDTO.getDateFrom());
            contribution.setDateTo(contributionDTO.getDateTo());

            publicationSeries.addContribution(contribution);
        });
    }

    @Override
    public void setPersonPublicationSeriesContributionsForBookSeries(
        PublicationSeries publicationSeries,
        BookSeriesDTO bookSeriesDTO) {
        publicationSeries.getContributions().clear();

        bookSeriesDTO.getContributions().forEach(contributionDTO -> {
            var contribution = new PersonPublicationSeriesContribution();
            setPersonContributionCommonFields(contribution, contributionDTO);

            contribution.setContributionType(contributionDTO.getContributionType());
            contribution.setDateFrom(contributionDTO.getDateFrom());
            contribution.setDateTo(contributionDTO.getDateTo());

            publicationSeries.addContribution(contribution);
        });
    }

    @Override
    public void setPersonEventContributionForEvent(Event event, EventDTO eventDTO) {
        eventDTO.getContributions().forEach(contributionDTO -> {
            var contribution = new PersonEventContribution();
            setPersonContributionCommonFields(contribution, contributionDTO);

            contribution.setContributionType(contributionDTO.getEventContributionType());

            event.addContribution(contribution);
        });
    }

    private void setAffiliationStatement(PersonContribution contribution,
                                         PersonContributionDTO contributionDTO,
                                         Person contributor) {
        var personName = getPersonName(contributionDTO, contributor);

        Contact contact = null;
        if (Objects.nonNull(contributor.getPersonalInfo().getContact())) {
            contact = new Contact(contributor.getPersonalInfo().getContact().getContactEmail(),
                contributor.getPersonalInfo().getContact().getPhoneNumber());
        }

        contribution.setAffiliationStatement(new AffiliationStatement(
            multilingualContentService.getMultilingualContent(
                contributionDTO.getDisplayAffiliationStatement()), personName,
            new PostalAddress(contributor.getPersonalInfo().getPostalAddress().getCountry(),
                multilingualContentService.deepCopy(
                    contributor.getPersonalInfo().getPostalAddress().getStreetAndNumber()),
                multilingualContentService.deepCopy(
                    contributor.getPersonalInfo().getPostalAddress().getCity())),
            contact));
    }

    private PersonName getPersonName(PersonContributionDTO contributionDTO,
                                     Person contributor) {
        var personName = new PersonName(contributionDTO.getPersonName().getFirstname(),
            contributionDTO.getPersonName().getOtherName(),
            contributionDTO.getPersonName().getLastname(),
            contributionDTO.getPersonName().getDateFrom(),
            contributionDTO.getPersonName().getDateTo());
        if (personName.getFirstname().isEmpty() && personName.getLastname().isEmpty()) {
            personName = new PersonName(contributor.getName().getFirstname(),
                contributor.getName().getOtherName(),
                contributor.getName().getLastname(),
                contributor.getName().getDateFrom(),
                contributor.getName().getDateTo());
        }
        return personName;
    }

    private void setPersonContributionCommonFields(PersonContribution contribution,
                                                   PersonContributionDTO contributionDTO) {
        var contributor = personService.findOne(contributionDTO.getPersonId());
        contribution.setPerson(contributor);

        contribution.setContributionDescription(multilingualContentService.getMultilingualContent(
            contributionDTO.getContributionDescription()));

        setAffiliationStatement(contribution, contributionDTO, contributor);
        // TODO: Moze li ovo ovako?
        contribution.getInstitutions()
            .add(personService.getLatestResearcherInvolvement(contributor));


        contribution.setOrderNumber(contributionDTO.getOrderNumber());
        contribution.setApproveStatus(
            contributionApprovedByDefault ? ApproveStatus.APPROVED : ApproveStatus.REQUESTED);
    }
}
