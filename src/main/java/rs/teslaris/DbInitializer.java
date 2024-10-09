package rs.teslaris;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.assessment.model.AssessmentClassification;
import rs.teslaris.core.assessment.model.AssessmentMeasure;
import rs.teslaris.core.assessment.model.AssessmentRulebook;
import rs.teslaris.core.assessment.model.Commission;
import rs.teslaris.core.assessment.model.DocumentIndicator;
import rs.teslaris.core.assessment.model.EventAssessmentClassification;
import rs.teslaris.core.assessment.model.Indicator;
import rs.teslaris.core.assessment.model.PublicationSeriesAssessmentClassification;
import rs.teslaris.core.assessment.repository.AssessmentClassificationRepository;
import rs.teslaris.core.assessment.repository.AssessmentMeasureRepository;
import rs.teslaris.core.assessment.repository.AssessmentRulebookRepository;
import rs.teslaris.core.assessment.repository.CommissionRepository;
import rs.teslaris.core.assessment.repository.DocumentIndicatorRepository;
import rs.teslaris.core.assessment.repository.EventAssessmentClassificationRepository;
import rs.teslaris.core.assessment.repository.IndicatorRepository;
import rs.teslaris.core.assessment.repository.PublicationSeriesAssessmentClassificationRepository;
import rs.teslaris.core.model.commontypes.AccessLevel;
import rs.teslaris.core.model.commontypes.ApproveStatus;
import rs.teslaris.core.model.commontypes.Country;
import rs.teslaris.core.model.commontypes.GeoLocation;
import rs.teslaris.core.model.commontypes.Language;
import rs.teslaris.core.model.commontypes.LanguageTag;
import rs.teslaris.core.model.commontypes.MultiLingualContent;
import rs.teslaris.core.model.commontypes.ResearchArea;
import rs.teslaris.core.model.document.AffiliationStatement;
import rs.teslaris.core.model.document.BookSeries;
import rs.teslaris.core.model.document.Conference;
import rs.teslaris.core.model.document.Dataset;
import rs.teslaris.core.model.document.DocumentContributionType;
import rs.teslaris.core.model.document.DocumentFile;
import rs.teslaris.core.model.document.EventsRelation;
import rs.teslaris.core.model.document.EventsRelationType;
import rs.teslaris.core.model.document.Journal;
import rs.teslaris.core.model.document.License;
import rs.teslaris.core.model.document.Monograph;
import rs.teslaris.core.model.document.MonographPublication;
import rs.teslaris.core.model.document.MonographType;
import rs.teslaris.core.model.document.Patent;
import rs.teslaris.core.model.document.PersonDocumentContribution;
import rs.teslaris.core.model.document.Proceedings;
import rs.teslaris.core.model.document.Publisher;
import rs.teslaris.core.model.document.ResourceType;
import rs.teslaris.core.model.document.Software;
import rs.teslaris.core.model.document.Thesis;
import rs.teslaris.core.model.document.ThesisType;
import rs.teslaris.core.model.institution.OrganisationUnit;
import rs.teslaris.core.model.person.Contact;
import rs.teslaris.core.model.person.Education;
import rs.teslaris.core.model.person.Employment;
import rs.teslaris.core.model.person.EmploymentPosition;
import rs.teslaris.core.model.person.ExpertiseOrSkill;
import rs.teslaris.core.model.person.InvolvementType;
import rs.teslaris.core.model.person.Membership;
import rs.teslaris.core.model.person.Person;
import rs.teslaris.core.model.person.PersonName;
import rs.teslaris.core.model.person.PersonalInfo;
import rs.teslaris.core.model.person.PostalAddress;
import rs.teslaris.core.model.person.Prize;
import rs.teslaris.core.model.person.Sex;
import rs.teslaris.core.model.user.Authority;
import rs.teslaris.core.model.user.PasswordResetToken;
import rs.teslaris.core.model.user.Privilege;
import rs.teslaris.core.model.user.User;
import rs.teslaris.core.model.user.UserNotificationPeriod;
import rs.teslaris.core.model.user.UserRole;
import rs.teslaris.core.repository.commontypes.CountryRepository;
import rs.teslaris.core.repository.commontypes.LanguageRepository;
import rs.teslaris.core.repository.commontypes.LanguageTagRepository;
import rs.teslaris.core.repository.commontypes.ResearchAreaRepository;
import rs.teslaris.core.repository.document.BookSeriesRepository;
import rs.teslaris.core.repository.document.ConferenceRepository;
import rs.teslaris.core.repository.document.DatasetRepository;
import rs.teslaris.core.repository.document.EventsRelationRepository;
import rs.teslaris.core.repository.document.JournalRepository;
import rs.teslaris.core.repository.document.MonographPublicationRepository;
import rs.teslaris.core.repository.document.MonographRepository;
import rs.teslaris.core.repository.document.PatentRepository;
import rs.teslaris.core.repository.document.PersonContributionRepository;
import rs.teslaris.core.repository.document.ProceedingsRepository;
import rs.teslaris.core.repository.document.PublisherRepository;
import rs.teslaris.core.repository.document.SoftwareRepository;
import rs.teslaris.core.repository.document.ThesisRepository;
import rs.teslaris.core.repository.person.OrganisationUnitRepository;
import rs.teslaris.core.repository.person.PersonRepository;
import rs.teslaris.core.repository.user.AuthorityRepository;
import rs.teslaris.core.repository.user.PasswordResetTokenRepository;
import rs.teslaris.core.repository.user.PrivilegeRepository;
import rs.teslaris.core.repository.user.UserRepository;
import rs.teslaris.core.util.language.LanguageAbbreviations;

@Component
@RequiredArgsConstructor
public class DbInitializer implements ApplicationRunner {

    private final AuthorityRepository authorityRepository;

    private final PrivilegeRepository privilegeRepository;

    private final UserRepository userRepository;

    private final LanguageRepository languageRepository;

    private final LanguageTagRepository languageTagRepository;

    private final PersonRepository personRepository;

    private final OrganisationUnitRepository organisationUnitRepository;

    private final PasswordEncoder passwordEncoder;

    private final CountryRepository countryRepository;

    private final JournalRepository journalRepository;

    private final BookSeriesRepository bookSeriesRepository;

    private final ResearchAreaRepository researchAreaRepository;

    private final ProceedingsRepository proceedingsRepository;

    private final ConferenceRepository conferenceRepository;

    private final PublisherRepository publisherRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PatentRepository patentRepository;

    private final SoftwareRepository softwareRepository;

    private final DatasetRepository datasetRepository;

    private final PersonContributionRepository personContributionRepository;

    private final MonographRepository monographRepository;

    private final EventsRelationRepository eventsRelationRepository;

    private final MonographPublicationRepository monographPublicationRepository;

    private final ThesisRepository thesisRepository;

    private final AssessmentClassificationRepository assessmentClassificationRepository;

    private final AssessmentMeasureRepository assessmentMeasureRepository;

    private final IndicatorRepository indicatorRepository;

    private final CommissionRepository commissionRepository;

    private final AssessmentRulebookRepository assessmentRulebookRepository;

    private final DocumentIndicatorRepository documentIndicatorRepository;

    private final EventAssessmentClassificationRepository eventAssessmentClassificationRepository;

    private final PublicationSeriesAssessmentClassificationRepository
        publicationSeriesAssessmentClassificationRepository;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        ///////////////////// NECESSARY DATA /////////////////////

        // PRIVILEGES
        var allowAccountTakeover = new Privilege("ALLOW_ACCOUNT_TAKEOVER");
        var takeRoleOfUser = new Privilege("TAKE_ROLE");
        var deactivateUser = new Privilege("DEACTIVATE_USER");
        var updateProfile = new Privilege("UPDATE_PROFILE");
        var createUserBasic = new Privilege("REGISTER_PERSON");
        var editPersonalInfo = new Privilege("EDIT_PERSON_INFORMATION");
        var approvePerson = new Privilege("APPROVE_PERSON");
        var editDocumentFiles = new Privilege("EDIT_DOCUMENT_FILES");
        var approvePublication = new Privilege("APPROVE_PUBLICATION");
        var editResearchAreas = new Privilege("EDIT_RESEARCH_AREAS");
        var editOrganisationUnit = new Privilege("EDIT_ORGANISATION_UNITS");
        var editOURelations = new Privilege("EDIT_OU_RELATIONS");
        var editPublishers = new Privilege("EDIT_PUBLISHERS");
        var editPublicationSeries = new Privilege("EDIT_PUBLICATION_SERIES");
        var editConferences = new Privilege("EDIT_CONFERENCES");
        var editEventRelations = new Privilege("EDIT_EVENT_RELATIONS");
        var mergeJournalPublications = new Privilege("MERGE_JOURNAL_PUBLICATIONS");
        var mergePersonPublications = new Privilege("MERGE_PERSON_PUBLICATIONS");
        var mergePersonMetadata = new Privilege("MERGE_PERSON_METADATA");
        var mergeOUEmployments = new Privilege("MERGE_OU_EMPLOYMENTS");
        var mergeOUMetadata = new Privilege("MERGE_OU_METADATA");
        var mergeConferenceProceedings = new Privilege("MERGE_CONFERENCE_PROCEEDINGS");
        var mergeProceedingsPublications = new Privilege("MERGE_PROCEEDINGS_PUBLICATIONS");
        var startDeduplicationProcess = new Privilege("START_DEDUPLICATION_PROCESS");
        var performDeduplication = new Privilege("PERFORM_DEDUPLICATION");
        var mergeDocumentsMetadata = new Privilege("MERGE_DOCUMENTS_METADATA");
        var mergeEventMetadata = new Privilege("MERGE_EVENT_METADATA");
        var mergePublicationSeriesMetadata = new Privilege("MERGE_PUBLICATION_SERIES_METADATA");
        var mergeMonographPublications = new Privilege("MERGE_MONOGRAPH_PUBLICATIONS");
        var prepareExportData = new Privilege("PREPARE_EXPORT_DATA");
        var editIndicators = new Privilege("EDIT_INDICATORS");
        var editAssessmentClassifications = new Privilege("EDIT_ASSESSMENT_CLASSIFICATIONS");
        var editAssessmentMeasures = new Privilege("EDIT_ASSESSMENT_MEASURES");
        var editAssessmentRulebooks = new Privilege("EDIT_ASSESSMENT_RULEBOOKS");
        var editDocumentIndicators = new Privilege("EDIT_DOCUMENT_INDICATORS");
        var editCommissions = new Privilege("EDIT_COMMISSIONS");
        var editEntityIndicatorProofs = new Privilege("EDIT_ENTITY_INDICATOR_PROOFS");
        var editEntityIndicators = new Privilege("EDIT_ENTITY_INDICATOR");
        var listMyJournalPublications = new Privilege("LIST_MY_JOURNAL_PUBLICATIONS");
        var deletePerson = new Privilege("DELETE_PERSON");
        var registerEmployee = new Privilege("REGISTER_EMPLOYEE");
        var reindexPrivilege = new Privilege("REINDEX_DATABASE");
        var mergeBookSeriesPublications = new Privilege("MERGE_BOOK_SERIES_PUBLICATIONS");
        var editEntityAssessmentClassifications =
            new Privilege("EDIT_ENTITY_ASSESSMENT_CLASSIFICATION");

        privilegeRepository.saveAll(
            Arrays.asList(allowAccountTakeover, takeRoleOfUser, deactivateUser, updateProfile,
                createUserBasic, editPersonalInfo, approvePerson, editDocumentFiles,
                editOrganisationUnit, editResearchAreas, approvePublication, editOURelations,
                editPublishers, editPublicationSeries, editConferences, editEventRelations,
                mergeOUEmployments, mergeJournalPublications, mergePersonPublications,
                mergePersonMetadata, mergeConferenceProceedings, mergeProceedingsPublications,
                prepareExportData, editIndicators, editAssessmentClassifications, editCommissions,
                editAssessmentMeasures, editAssessmentRulebooks, editDocumentIndicators,
                editEntityIndicatorProofs, listMyJournalPublications, deletePerson,
                registerEmployee, reindexPrivilege, startDeduplicationProcess, performDeduplication,
                mergeDocumentsMetadata, mergeEventMetadata, mergePublicationSeriesMetadata,
                mergeMonographPublications, prepareExportData, mergeBookSeriesPublications,
                mergeOUMetadata, editEntityIndicators, editEntityAssessmentClassifications));

        // AUTHORITIES
        var adminAuthority = new Authority(UserRole.ADMIN.toString(), new HashSet<>(
            List.of(takeRoleOfUser, deactivateUser, updateProfile, editPersonalInfo,
                createUserBasic, approvePerson, editDocumentFiles, editOrganisationUnit,
                editResearchAreas, editOURelations, approvePublication, editPublishers,
                editPublicationSeries, editConferences, editEventRelations, editCommissions,
                mergeJournalPublications, mergePersonPublications, mergePersonMetadata,
                mergeOUEmployments, mergeConferenceProceedings, mergeProceedingsPublications,
                prepareExportData, editIndicators, editAssessmentClassifications,
                editAssessmentMeasures, editAssessmentRulebooks, editDocumentIndicators,
                editEntityIndicatorProofs, deletePerson, registerEmployee, reindexPrivilege,
                startDeduplicationProcess, performDeduplication, mergeDocumentsMetadata,
                mergeEventMetadata, mergePublicationSeriesMetadata, mergeMonographPublications,
                prepareExportData, mergeBookSeriesPublications, mergeOUMetadata,
                editEntityIndicators, editEntityAssessmentClassifications
            )));

        var researcherAuthority = new Authority(UserRole.RESEARCHER.toString(), new HashSet<>(
            List.of(new Privilege[] {allowAccountTakeover, updateProfile, editPersonalInfo,
                createUserBasic, editDocumentFiles, editDocumentIndicators,
                editEntityIndicatorProofs, listMyJournalPublications, editEntityIndicators})));

        var institutionalEditorAuthority =
            new Authority(UserRole.INSTITUTIONAL_EDITOR.toString(), new HashSet<>(
                List.of(new Privilege[] {updateProfile})));

        authorityRepository.saveAll(
            List.of(adminAuthority, researcherAuthority, institutionalEditorAuthority));

        // LANGUAGES
        var serbianLanguage = new Language();
        serbianLanguage.setLanguageCode(LanguageAbbreviations.SERBIAN);
        var englishLanguage = new Language();
        englishLanguage.setLanguageCode(LanguageAbbreviations.ENGLISH);
        // We will maybe need YU, I don't know
        var yuLanguage = new Language();
        yuLanguage.setLanguageCode(LanguageAbbreviations.YUGOSLAV);
        yuLanguage.setDeleted(true);
        var germanLanguage = new Language();
        germanLanguage.setLanguageCode(LanguageAbbreviations.GERMAN);
        var frenchLanguage = new Language();
        frenchLanguage.setLanguageCode(LanguageAbbreviations.FRENCH);
        var spanishLanguage = new Language();
        spanishLanguage.setLanguageCode(LanguageAbbreviations.SPANISH);
        var russianLanguage = new Language();
        russianLanguage.setLanguageCode(LanguageAbbreviations.RUSSIAN);
        var croatianLanguage = new Language();
        croatianLanguage.setLanguageCode(LanguageAbbreviations.CROATIAN);
        var italianLanguage = new Language();
        italianLanguage.setLanguageCode(LanguageAbbreviations.ITALIAN);
        var slovenianLanguage = new Language();
        slovenianLanguage.setLanguageCode(LanguageAbbreviations.SLOVENIAN);

        languageRepository.saveAll(
            List.of(serbianLanguage, englishLanguage, yuLanguage, germanLanguage, frenchLanguage,
                spanishLanguage, russianLanguage, croatianLanguage, italianLanguage,
                slovenianLanguage));

        // LANGUAGE TAGS
        var englishTag = new LanguageTag(LanguageAbbreviations.ENGLISH, "English");
        var serbianTag = new LanguageTag(LanguageAbbreviations.SERBIAN, "Srpski");
        var hungarianTag = new LanguageTag(LanguageAbbreviations.HUNGARIAN, "Magyar");
        var germanTag = new LanguageTag(LanguageAbbreviations.GERMAN, "Deutsch");
        var frenchTag = new LanguageTag(LanguageAbbreviations.FRENCH, "Français");
        var spanishTag = new LanguageTag(LanguageAbbreviations.SPANISH, "Español");
        var russianTag = new LanguageTag(LanguageAbbreviations.RUSSIAN, "Русский");
        var croatianTag = new LanguageTag(LanguageAbbreviations.CROATIAN, "Croatian");
        var italianTag = new LanguageTag(LanguageAbbreviations.ITALIAN, "Italian");
        var slovenianTag = new LanguageTag(LanguageAbbreviations.SLOVENIAN, "Slovenian");
        languageTagRepository.saveAll(
            List.of(englishTag, serbianTag, hungarianTag, germanTag, frenchTag, spanishTag,
                russianTag, croatianTag, italianTag, slovenianTag));

        // ADMIN USER
        var adminUser =
            new User("admin@admin.com", passwordEncoder.encode("admin"), "note", "Marko",
                "Markovic", false, false, serbianLanguage, adminAuthority, null, null,
                UserNotificationPeriod.DAILY);
        userRepository.save(adminUser);

        // RESEARCH AREAS - NOT COMPLETE
        var researchArea1 = new ResearchArea(new HashSet<>(Set.of(
            new MultiLingualContent(serbianTag, "Elektrotehnicko i racunarsko inzenjerstvo", 2))),
            new HashSet<>(), null);
        var researchArea2 = new ResearchArea(new HashSet<>(Set.of(
            new MultiLingualContent(serbianTag, "Softversko inzenjerstvo", 2))),
            new HashSet<>(), researchArea1);
        var researchArea3 = new ResearchArea(new HashSet<>(Set.of(
            new MultiLingualContent(serbianTag, "Cybersecurity", 2))),
            new HashSet<>(), researchArea2);

        researchAreaRepository.saveAll(List.of(researchArea1, researchArea2, researchArea3));

        ///////////////////// TESTING DATA /////////////////////
        initializeIntegrationTestingData(serbianTag, serbianLanguage, englishTag, germanLanguage,
            researchArea3, researcherAuthority);

        ///////////////////// ASSESSMENTS DATA /////////////////////
        initializeIndicators(englishTag);
    }

    private void initializeIntegrationTestingData(LanguageTag serbianTag, Language serbianLanguage,
                                                  LanguageTag englishTag, Language germanLanguage,
                                                  ResearchArea researchArea3,
                                                  Authority researcherAuthority) {
        var country = new Country("RS", new HashSet<>());
        country = countryRepository.save(country);

        var postalAddress = new PostalAddress(country, new HashSet<>(),
            new HashSet<>());
        var personalInfo =
            new PersonalInfo(LocalDate.of(2000, 1, 25), "Serbia", Sex.MALE, postalAddress,
                new Contact("john@ftn.uns.ac.com", "021555666"));
        var person1 = new Person();
        person1.setName(
            new PersonName("Ivan", "Radomir", "Mrsulja", LocalDate.of(2000, 1, 25), null));
        person1.setApproveStatus(ApproveStatus.APPROVED);
        person1.setPersonalInfo(personalInfo);
        person1.setOrcid("0000-0002-1825-0097");
        person1.setScopusAuthorId("35795419600");
        personRepository.save(person1);

        var researcherUser =
            new User("author@author.com", passwordEncoder.encode("author"), "note note note",
                "Janko", "Jankovic", false, false, serbianLanguage, researcherAuthority, person1,
                null, UserNotificationPeriod.DAILY);
        userRepository.save(researcherUser);

        var dummyOU = new OrganisationUnit();
        dummyOU.setNameAbbreviation("FTN");
        dummyOU.setName(new HashSet<>(List.of(new MultiLingualContent[] {
            new MultiLingualContent(englishTag, "Faculty of Technical Sciences", 1),
            new MultiLingualContent(serbianTag, "Fakultet Tehničkih Nauka", 2)})));
        dummyOU.setApproveStatus(ApproveStatus.APPROVED);
        dummyOU.setScopusAfid("60068801");
        dummyOU.setLocation(new GeoLocation(19.850885, 45.245688, "NOWHERE"));
        dummyOU.setContact(new Contact("office@ftn.uns.ac.com", "021555666"));
        organisationUnitRepository.save(dummyOU);
        researcherUser.setOrganisationUnit(dummyOU);
        userRepository.save(researcherUser);

        var dummyJournal = new Journal();
        dummyJournal.setTitle(Set.of(new MultiLingualContent(englishTag, "Title1", 1)));
        dummyJournal.setNameAbbreviation(Set.of(new MultiLingualContent(englishTag, "ABR1", 1)));
        journalRepository.save(dummyJournal);

        var dummyOU2 = new OrganisationUnit();
        dummyOU2.setNameAbbreviation("PMF");
        dummyOU2.setName(new HashSet<>(List.of(new MultiLingualContent[] {
            new MultiLingualContent(englishTag, "Faculty of Sciences", 1),
            new MultiLingualContent(serbianTag, "Prirodno matematicki fakultet", 2)})));
        dummyOU2.setApproveStatus(ApproveStatus.APPROVED);
        dummyOU2.setLocation(new GeoLocation(19.8502021, 45.2454147, "NOWHERE"));
        dummyOU2.setContact(new Contact("office@pmf.uns.ac.com", "021555667"));
        organisationUnitRepository.save(dummyOU2);

        var conferenceEvent1 = new Conference();
        conferenceEvent1.setName(Set.of(new MultiLingualContent(serbianTag, "Konferencija", 1)));
        conferenceEvent1.setDateFrom(LocalDate.of(2021, 3, 6));
        conferenceEvent1.setDateTo(LocalDate.of(2021, 3, 10));
        conferenceEvent1.setSerialEvent(false);
        conferenceRepository.save(conferenceEvent1);

        var proceedings1 = new Proceedings();
        proceedings1.getTitle().add(new MultiLingualContent(englishTag, "Proceedings 1", 1));
        proceedings1.setApproveStatus(ApproveStatus.APPROVED);
        proceedings1.setEISBN("978-3-16-148410-0");
        proceedings1.setDocumentDate("2021");
        proceedings1.setEvent(conferenceEvent1);
        proceedingsRepository.save(proceedings1);

        var proceedings2 = new Proceedings();
        proceedings2.getTitle().add(new MultiLingualContent(englishTag, "Proceedings 2", 1));
        proceedings2.setApproveStatus(ApproveStatus.REQUESTED);
        proceedings2.setEISBN("978-3-16-145410-0");
        proceedings2.setEvent(conferenceEvent1);
        proceedingsRepository.save(proceedings2);

        var publisher1 = new Publisher();
        publisher1.setName(Set.of(new MultiLingualContent(englishTag, "Name1", 1)));
        publisher1.setPlace(Set.of(new MultiLingualContent(englishTag, "Place1", 1)));
        publisher1.setState(Set.of(new MultiLingualContent(englishTag, "Serbia", 1)));
        publisherRepository.save(publisher1);

        var conferenceEvent2 = new Conference();
        conferenceEvent2.setName(Set.of(new MultiLingualContent(serbianTag, "Konferencija2", 1)));
        conferenceEvent2.setDateFrom(LocalDate.of(2020, 6, 13));
        conferenceEvent2.setDateTo(LocalDate.of(2020, 6, 19));
        conferenceEvent2.setSerialEvent(false);
        conferenceRepository.save(conferenceEvent2);

        var journal2 = new Journal();
        journal2.setTitle(Set.of(new MultiLingualContent(englishTag, "Title2", 1)));
        journal2.setNameAbbreviation(Set.of(new MultiLingualContent(englishTag, "ABR2", 1)));
        journalRepository.save(journal2);

        var bookSeries1 = new BookSeries();
        bookSeries1.setTitle(Set.of(new MultiLingualContent(englishTag, "BookSeries1", 1)));
        bookSeries1.setNameAbbreviation(Set.of(new MultiLingualContent(englishTag, "ABR1", 1)));
        bookSeriesRepository.save(bookSeries1);

        var bookSeries2 = new BookSeries();
        bookSeries2.setTitle(Set.of(new MultiLingualContent(englishTag, "BookSeries2", 1)));
        bookSeries2.setNameAbbreviation(Set.of(new MultiLingualContent(englishTag, "ABR2", 1)));
        bookSeriesRepository.save(bookSeries2);

        var passwordResetRequest = new PasswordResetToken("TOKEN", researcherUser);
        passwordResetTokenRepository.save(passwordResetRequest);

        var person2 = new Person();
        var postalAddress2 = new PostalAddress(country, new HashSet<>(),
            new HashSet<>());
        var personalInfo2 =
            new PersonalInfo(LocalDate.of(2000, 1, 31), "Germany", Sex.MALE, postalAddress2,
                new Contact("joakim@email.com", "021555769"));
        person2.setApproveStatus(ApproveStatus.APPROVED);
        person2.setPersonalInfo(personalInfo2);
        person2.setName(
            new PersonName("Schöpfel", "", "Joachim", LocalDate.of(2000, 1, 31), null));
        person2.setScopusAuthorId("14619562900");
        personRepository.save(person2);

        var software = new Software();
        software.setTitle(Set.of(new MultiLingualContent(englishTag,
            "TeslaRIS - Reengineering of CRIS at the University of Novi Sad", 1)));
        software.setInternalNumber("123456");
        software.setApproveStatus(ApproveStatus.APPROVED);

        var patent = new Patent();
        patent.setTitle(Set.of(new MultiLingualContent(englishTag, "Dummy Patent", 1)));
        patent.setApproveStatus(ApproveStatus.APPROVED);
        patentRepository.save(patent);

        var dataset = new Dataset();
        dataset.setTitle(Set.of(new MultiLingualContent(englishTag, "Dummy Dataset", 1)));
        dataset.setApproveStatus(ApproveStatus.APPROVED);
        dataset.setDocumentDate("2020-01-01");

        var datasetContribution = new PersonDocumentContribution();
        datasetContribution.setPerson(person1);
        datasetContribution.setContributionType(DocumentContributionType.AUTHOR);
        datasetContribution.setIsMainContributor(true);
        datasetContribution.setIsCorrespondingContributor(false);
        datasetContribution.setOrderNumber(1);
        datasetContribution.setDocument(dataset);
        datasetContribution.setApproveStatus(ApproveStatus.APPROVED);
        datasetContribution.setAffiliationStatement(
            new AffiliationStatement(new HashSet<>(), new PersonName(),
                new PostalAddress(country, new HashSet<>(), new HashSet<>()), new Contact("", "")));

        dataset.setContributors(Set.of(datasetContribution));
        datasetRepository.save(dataset);

        var sci2zero = new OrganisationUnit();
        sci2zero.setNameAbbreviation("Sci2zero");
        sci2zero.setName(new HashSet<>(List.of(new MultiLingualContent[] {
            new MultiLingualContent(serbianTag, "Science 2.0 Alliance", 1)})));
        sci2zero.setApproveStatus(ApproveStatus.APPROVED);
        sci2zero.setContact(new Contact("info@sci2zero.com", "021555666"));
        organisationUnitRepository.save(sci2zero);

        person1.getBiography()
            .add(new MultiLingualContent(englishTag, "Lorem ipsum dolor sit amet.", 1));
        person1.getBiography().add(new MultiLingualContent(serbianTag, "Srpska biografija.", 2));
        person1.getKeyword().add(new MultiLingualContent(englishTag,
            "Machine Learning\nCybersecurity\nReverse Engineering\nWeb Security", 1));
        person1.addInvolvement(
            new Employment(LocalDate.of(2021, 10, 3), null, ApproveStatus.APPROVED,
                new HashSet<>(), InvolvementType.EMPLOYED_AT, new HashSet<>(), null, dummyOU,
                EmploymentPosition.TEACHING_ASSISTANT, Set.of(new MultiLingualContent(englishTag,
                "Courses: Digital Documents Management, Secure Software Development", 1))));
        person1.addInvolvement(
            new Employment(LocalDate.of(2021, 10, 3), null, ApproveStatus.APPROVED,
                new HashSet<>(), InvolvementType.HIRED_BY, new HashSet<>(), null, sci2zero,
                EmploymentPosition.COLLABORATOR, Set.of(new MultiLingualContent(englishTag,
                "TeslaRIS - reingeneering of CRIS at the university of Novi Sad.", 1))));
        person1.addInvolvement(
            new Membership(LocalDate.of(2021, 10, 3), null, ApproveStatus.APPROVED,
                new HashSet<>(), InvolvementType.MEMBER_OF, new HashSet<>(), null, sci2zero,
                Set.of(new MultiLingualContent(englishTag,
                    "I just wanted to be around cool kids...", 1)),
                Set.of(new MultiLingualContent(englishTag,
                    "Gold member", 1))));
        person1.addInvolvement(
            new Education(LocalDate.of(2018, 10, 1), LocalDate.of(2023, 9, 1),
                ApproveStatus.APPROVED,
                new HashSet<>(), InvolvementType.STUDIED_AT, new HashSet<>(), null, dummyOU,
                Set.of(new MultiLingualContent(englishTag, "Reverse Image Search System", 1),
                    new MultiLingualContent(englishTag, "Sistem za reverznu pretragu slika", 1)),
                Set.of(new MultiLingualContent(englishTag, "Master in Software", 1),
                    new MultiLingualContent(englishTag, "Master inženjer softvera", 1)),
                Set.of(new MultiLingualContent(englishTag, "Msc", 1))));
        person1.getExpertisesAndSkills().add(new ExpertiseOrSkill(
            Set.of(new MultiLingualContent(englishTag, "Cybersecurity", 1)),
            Set.of(new MultiLingualContent(englishTag,
                "Proficiency in web exploitation and reverse engineering.", 1)),
            Set.of(
                new DocumentFile("ISACA Cybersecurity Fundamentals - Certificate.pdf", "1111.pdf",
                    new HashSet<>(), "appllication/pdf", 200L, ResourceType.SUPPLEMENT,
                    License.CREATIVE_COMMONS, ApproveStatus.APPROVED))));
        person1.getExpertisesAndSkills().add(new ExpertiseOrSkill(
            Set.of(new MultiLingualContent(englishTag, "CERIF-based systems", 1)),
            Set.of(new MultiLingualContent(englishTag,
                "Contributing to VIVO, Vitro and TeslaRIS current research information systems.",
                1)),
            new HashSet<>()));
        person1.getPrizes().add(new Prize(
            Set.of(
                new MultiLingualContent(englishTag, "Serbian Cybersecurity Challenge - 1st place",
                    1)),
            Set.of(new MultiLingualContent(englishTag,
                "1st place on a national cybersecurity competition finals. The competition is conducted in 5-man teams.",
                1)),
            Set.of(new DocumentFile("1st place certificate.pdf", "2222.pdf",
                new HashSet<>(), "appllication/pdf", 127L, ResourceType.SUPPLEMENT,
                License.OPEN_ACCESS, ApproveStatus.APPROVED)), LocalDate.of(2023, 4, 17)));
        personRepository.save(person1);

        country.getName().add(new MultiLingualContent(serbianTag, "Srbija", 1));
        countryRepository.save(country);

        datasetContribution.getAffiliationStatement().setDisplayPersonName(
            new PersonName("Ivan", "R.", "M.", LocalDate.of(2000, 1, 31), null));
        personContributionRepository.save(datasetContribution);

        var softwareContribution = new PersonDocumentContribution();
        softwareContribution.setPerson(person1);
        softwareContribution.setContributionType(DocumentContributionType.AUTHOR);
        softwareContribution.setIsMainContributor(true);
        softwareContribution.setIsCorrespondingContributor(false);
        softwareContribution.setOrderNumber(1);
        softwareContribution.setDocument(dataset);
        softwareContribution.setApproveStatus(ApproveStatus.APPROVED);
        softwareContribution.setAffiliationStatement(
            new AffiliationStatement(new HashSet<>(), new PersonName(),
                new PostalAddress(country, new HashSet<>(), new HashSet<>()), new Contact("", "")));
        softwareContribution.getAffiliationStatement().setDisplayPersonName(
            new PersonName("Ivan", "", "M.", LocalDate.of(2000, 1, 31), null));
        personContributionRepository.save(softwareContribution);

        dummyOU.getResearchAreas().add(researchArea3);
        organisationUnitRepository.save(dummyOU);

        var monograph1 = new Monograph();
        monograph1.setApproveStatus(ApproveStatus.APPROVED);
        monograph1.setTitle(
            Set.of(new MultiLingualContent(serbianTag, "Monografija 1", 1)));
        monograph1.setMonographType(MonographType.BIBLIOGRAPHY);
        monographRepository.save(monograph1);

        var monograph2 = new Monograph();
        monograph2.setApproveStatus(ApproveStatus.APPROVED);
        monograph2.setTitle(
            Set.of(new MultiLingualContent(serbianTag, "Monografija 2", 1)));
        monograph2.setMonographType(MonographType.RESEARCH_MONOGRAPH);
        monograph2.setDocumentDate("2024");
        monographRepository.save(monograph2);

        var researcherUser2 =
            new User("author2@author.com", passwordEncoder.encode("author2"), "note note note",
                "Schöpfel", "Joachim", false, false, germanLanguage, researcherAuthority, person2,
                null, UserNotificationPeriod.WEEKLY);
        userRepository.save(researcherUser2);

        var person3 = new Person();
        var postalAddress3 = new PostalAddress(country, new HashSet<>(),
            new HashSet<>());
        var personalInfo3 =
            new PersonalInfo(LocalDate.of(2000, 1, 31), "Serbia", Sex.MALE, postalAddress3,
                new Contact("test@email.com", "021555769"));
        person3.setApproveStatus(ApproveStatus.APPROVED);
        person3.setPersonalInfo(personalInfo3);
        person3.setName(
            new PersonName("Dusan", "", "Nikolic", LocalDate.of(1976, 7, 16), null));
        person3.setScopusAuthorId("14419566900");
        personRepository.save(person3);

        var softwareContribution2 = new PersonDocumentContribution();
        softwareContribution2.setPerson(person3);
        softwareContribution2.setContributionType(DocumentContributionType.AUTHOR);
        softwareContribution2.setIsMainContributor(false);
        softwareContribution2.setIsCorrespondingContributor(false);
        softwareContribution2.setOrderNumber(2);
        softwareContribution2.setDocument(dataset);
        softwareContribution2.setApproveStatus(ApproveStatus.APPROVED);
        softwareContribution2.setAffiliationStatement(
            new AffiliationStatement(new HashSet<>(), new PersonName(),
                new PostalAddress(country, new HashSet<>(), new HashSet<>()), new Contact("", "")));
        softwareContribution2.getAffiliationStatement().setDisplayPersonName(
            new PersonName("Dušan", "", "N.", null, null));
        personContributionRepository.save(softwareContribution2);

        software.addDocumentContribution(softwareContribution);
        software.addDocumentContribution(softwareContribution2);
        softwareRepository.save(software);

        var person4 = new Person();
        var postalAddress4 = new PostalAddress(country, new HashSet<>(),
            new HashSet<>());
        var personalInfo4 =
            new PersonalInfo(LocalDate.of(1976, 6, 24), "Serbia", Sex.FEMALE, postalAddress4,
                new Contact("test1@email.com", "021555769"));
        person4.setApproveStatus(ApproveStatus.APPROVED);
        person4.setPersonalInfo(personalInfo4);
        person4.setName(
            new PersonName("Jovana", "", "Stankovic", LocalDate.of(1976, 7, 16), null));
        person4.setScopusAuthorId("14419566900");
        personRepository.save(person4);

        var conferenceEvent3 = new Conference();
        conferenceEvent3.setName(Set.of(new MultiLingualContent(serbianTag, "Konferencija3", 1)));
        conferenceEvent3.setDateFrom(LocalDate.of(2024, 6, 29));
        conferenceEvent3.setDateTo(LocalDate.of(2024, 7, 3));
        conferenceEvent3.setSerialEvent(true);
        conferenceRepository.save(conferenceEvent3);

        var eventsRelation1 = new EventsRelation();
        eventsRelation1.setSource(conferenceEvent1);
        eventsRelation1.setTarget(conferenceEvent2);
        eventsRelation1.setEventsRelationType(EventsRelationType.PART_OF);
        eventsRelationRepository.save(eventsRelation1);

        var eventsRelation2 = new EventsRelation();
        eventsRelation2.setSource(conferenceEvent1);
        eventsRelation2.setTarget(conferenceEvent3);
        eventsRelation2.setEventsRelationType(EventsRelationType.BELONGS_TO_SERIES);
        eventsRelationRepository.save(eventsRelation2);

        var monographPublication1 = new MonographPublication();
        monographPublication1.setApproveStatus(ApproveStatus.APPROVED);
        monographPublication1.setDocumentDate("2024");
        monographPublication1.setMonograph(monograph1);
        monographPublication1.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Monograph Publication 1", 1)));
        monographPublicationRepository.save(monographPublication1);

        var monographPublication2 = new MonographPublication();
        monographPublication2.setApproveStatus(ApproveStatus.APPROVED);
        monographPublication2.setDocumentDate("2024");
        monographPublication2.setMonograph(monograph1);
        monographPublication2.setTitle(
            Set.of(new MultiLingualContent(serbianTag, "Rad u monografiji 2", 1)));
        monographPublicationRepository.save(monographPublication2);

        var thesis1 = new Thesis();
        thesis1.setApproveStatus(ApproveStatus.APPROVED);
        thesis1.setThesisType(ThesisType.PHD);
        thesis1.setDocumentDate("2021");
        thesis1.setOrganisationUnit(dummyOU2);
        thesis1.setTitle(
            Set.of(new MultiLingualContent(serbianTag, "Doktorska disertacija", 1)));
        thesis1.getLanguages().addAll(List.of(serbianTag, englishTag));
        thesisRepository.save(thesis1);

        var thesis2 = new Thesis();
        thesis2.setApproveStatus(ApproveStatus.APPROVED);
        thesis2.setThesisType(ThesisType.MASTER);
        thesis2.setDocumentDate("2023");
        thesis2.setOrganisationUnit(dummyOU2);
        thesis2.setTitle(
            Set.of(new MultiLingualContent(serbianTag, "Master rad", 1)));
        thesisRepository.save(thesis2);

        var conferenceEvent4 = new Conference();
        conferenceEvent4.setName(Set.of(new MultiLingualContent(englishTag, "EURO CRIS", 1)));
        conferenceEvent4.setDateFrom(LocalDate.of(2023, 5, 12));
        conferenceEvent4.setDateTo(LocalDate.of(2023, 5, 17));
        conferenceEvent4.setSerialEvent(false);
        conferenceRepository.save(conferenceEvent4);

        var monograph3 = new Monograph();
        monograph3.setApproveStatus(ApproveStatus.APPROVED);
        monograph3.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Dummy Translation", 1)));
        monograph3.setMonographType(MonographType.TRANSLATION);
        monographRepository.save(monograph3);

        var indicator1 = new Indicator();
        indicator1.setCode("Code 1");
        indicator1.setTitle(Set.of(new MultiLingualContent(englishTag, "Indicator 1", 1)));
        indicator1.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Indicator 1 description", 1)));
        indicator1.setAccessLevel(AccessLevel.OPEN);

        var indicator2 = new Indicator();
        indicator2.setCode("Code 2");
        indicator2.setTitle(Set.of(new MultiLingualContent(englishTag, "Indicator 2", 1)));
        indicator2.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Indicator 2 description", 1)));

        var indicator3 = new Indicator();
        indicator3.setCode("Code 3");
        indicator3.setTitle(Set.of(new MultiLingualContent(englishTag, "Indicator 3", 1)));
        indicator3.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Indicator 3 description", 1)));
        indicator3.setAccessLevel(AccessLevel.CLOSED);

        var indicator4 = new Indicator();
        indicator4.setCode("Code 4");
        indicator4.setTitle(Set.of(new MultiLingualContent(englishTag, "Indicator 4", 1)));
        indicator4.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Indicator 4 description", 1)));
        indicator4.setAccessLevel(AccessLevel.ADMIN_ONLY);

        indicatorRepository.saveAll(List.of(indicator1, indicator2, indicator3, indicator4));

        var assessmentClassification1 = new AssessmentClassification();
        assessmentClassification1.setFormalDescriptionOfRule("Rule 1");
        assessmentClassification1.setCode("Code 1");
        assessmentClassification1.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Assessment Classification 1", 1)));

        var assessmentClassification2 = new AssessmentClassification();
        assessmentClassification2.setFormalDescriptionOfRule("Rule 2");
        assessmentClassification2.setCode("Code 2");
        assessmentClassification2.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Assessment Classification 2", 1)));

        assessmentClassificationRepository.saveAll(
            List.of(assessmentClassification1, assessmentClassification2));

        var assessmentMeasure1 = new AssessmentMeasure();
        assessmentMeasure1.setFormalDescriptionOfRule("Rule 1");
        assessmentMeasure1.setCode("Code 1");
        assessmentMeasure1.setValue(2d);
        assessmentMeasure1.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Assessment Measure 1", 1)));

        var assessmentMeasure2 = new AssessmentMeasure();
        assessmentMeasure2.setFormalDescriptionOfRule("Rule 2");
        assessmentMeasure2.setCode("Code 2");
        assessmentMeasure2.setValue(4d);
        assessmentMeasure2.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Assessment Measure 2", 1)));

        assessmentMeasureRepository.saveAll(List.of(assessmentMeasure1, assessmentMeasure2));

        var commission1 = new Commission();
        commission1.setFormalDescriptionOfRule("Rule 1");

        var commission2 = new Commission();
        commission2.setFormalDescriptionOfRule("Rule 2");
        commission2.setSuperComission(commission1);

        var commission3 = new Commission();
        commission3.setFormalDescriptionOfRule("Rule 3");

        commissionRepository.saveAll(List.of(commission1, commission2, commission3));

        var assessmentRulebook1 = new AssessmentRulebook();
        assessmentRulebook1.setName(
            Set.of(new MultiLingualContent(englishTag, "Assessment Rulebook 1", 1)));
        assessmentRulebook1.setIssueDate(LocalDate.of(2023, 10, 1));

        var assessmentRulebook2 = new AssessmentRulebook();
        assessmentRulebook2.setName(
            Set.of(new MultiLingualContent(englishTag, "Assessment Rulebook 2", 1)));
        assessmentRulebook2.setIssueDate(LocalDate.of(2023, 10, 1));
        assessmentRulebook2.setPublisher(publisher1);
        assessmentRulebook2.setAssessmentMeasure(assessmentMeasure1);

        assessmentRulebookRepository.saveAll(List.of(assessmentRulebook1, assessmentRulebook2));

        var documentIndicator1 = new DocumentIndicator();
        documentIndicator1.setTextualValue("ADMIN ACCESS INDICATOR");
        documentIndicator1.setIndicator(indicator4);
        documentIndicator1.setDocument(dataset);

        var documentIndicator2 = new DocumentIndicator();
        documentIndicator2.setTextualValue("CLOSED ACCESS INDICATOR");
        documentIndicator2.setIndicator(indicator3);
        documentIndicator2.setDocument(dataset);

        var documentIndicator3 = new DocumentIndicator();
        documentIndicator3.setTextualValue("OPEN ACCESS INDICATOR");
        documentIndicator3.setIndicator(indicator1);
        documentIndicator3.setDocument(dataset);

        var documentIndicatorToDelete = new DocumentIndicator();
        documentIndicatorToDelete.setTextualValue("OPEN ACCESS INDICATOR");
        documentIndicatorToDelete.setIndicator(indicator1);
        documentIndicatorToDelete.setDocument(dataset);

        documentIndicatorRepository.saveAll(
            List.of(documentIndicator1, documentIndicator2, documentIndicator3,
                documentIndicatorToDelete));

        documentIndicator1.getProofs().add(new DocumentFile("Proof 1", "3333.pdf",
            new HashSet<>(), "appllication/pdf", 127L, ResourceType.SUPPLEMENT,
            License.OPEN_ACCESS, ApproveStatus.APPROVED));
        documentIndicatorRepository.save(documentIndicator1);

        var eventAssessmentClassification1 = new EventAssessmentClassification();
        eventAssessmentClassification1.setEvent(conferenceEvent1);
        eventAssessmentClassification1.setAssessmentClassification(assessmentClassification1);

        var eventAssessmentClassification2 = new EventAssessmentClassification();
        eventAssessmentClassification2.setEvent(conferenceEvent1);
        eventAssessmentClassification2.setAssessmentClassification(assessmentClassification1);

        eventAssessmentClassificationRepository.saveAll(
            List.of(eventAssessmentClassification1, eventAssessmentClassification2));

        var publicationSeriesAssessmentClassification =
            new PublicationSeriesAssessmentClassification();
        publicationSeriesAssessmentClassification.setPublicationSeries(dummyJournal);
        publicationSeriesAssessmentClassification.setAssessmentClassification(
            assessmentClassification1);

        publicationSeriesAssessmentClassificationRepository.saveAll(
            List.of(publicationSeriesAssessmentClassification));
    }

    void initializeIndicators(LanguageTag englishTag) {
        var totalViews = new Indicator();
        totalViews.setCode("viewsTotal");
        totalViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Total views", 1)));
        totalViews.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Total number of views.", 1)));
        totalViews.setAccessLevel(AccessLevel.OPEN);

        var dailyViews = new Indicator();
        dailyViews.setCode("viewsDay");
        dailyViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Daily views", 1)));
        dailyViews.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of views in the last 24h.", 1)));
        dailyViews.setAccessLevel(AccessLevel.OPEN);

        var weeklyViews = new Indicator();
        weeklyViews.setCode("viewsWeek");
        weeklyViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Total views", 1)));
        weeklyViews.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Total number of views in the last 7 days.",
                1)));
        weeklyViews.setAccessLevel(AccessLevel.OPEN);

        var monthlyViews = new Indicator();
        monthlyViews.setCode("viewsMonth");
        monthlyViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Total views", 1)));
        monthlyViews.setDescription(
            Set.of(new MultiLingualContent(englishTag, "Total number of views in the last month.",
                1)));
        monthlyViews.setAccessLevel(AccessLevel.OPEN);

        indicatorRepository.saveAll(List.of(totalViews, dailyViews, weeklyViews, monthlyViews));
    }
}
