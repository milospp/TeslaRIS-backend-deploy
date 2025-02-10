package rs.teslaris.dbinitialization;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.assessment.model.ApplicableEntityType;
import rs.teslaris.core.assessment.model.AssessmentClassification;
import rs.teslaris.core.assessment.model.Commission;
import rs.teslaris.core.assessment.model.CommissionRelation;
import rs.teslaris.core.assessment.model.Indicator;
import rs.teslaris.core.assessment.model.IndicatorContentType;
import rs.teslaris.core.assessment.model.ResultCalculationMethod;
import rs.teslaris.core.assessment.repository.AssessmentClassificationRepository;
import rs.teslaris.core.assessment.repository.CommissionRelationRepository;
import rs.teslaris.core.assessment.repository.CommissionRepository;
import rs.teslaris.core.assessment.repository.IndicatorRepository;
import rs.teslaris.core.model.commontypes.AccessLevel;
import rs.teslaris.core.model.commontypes.LanguageTag;
import rs.teslaris.core.model.commontypes.MultiLingualContent;

@Component
@RequiredArgsConstructor
@Transactional
public class AssessmentDataInitializer {

    private final AssessmentClassificationRepository assessmentClassificationRepository;

    private final IndicatorRepository indicatorRepository;

    private final CommissionRepository commissionRepository;

    private final CommissionRelationRepository commissionRelationRepository;


    public void initializeIndicators(LanguageTag englishTag, LanguageTag serbianTag) {
        var totalViews = new Indicator();
        totalViews.setCode("viewsTotal");
        totalViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Total views", 1),
            new MultiLingualContent(serbianTag, "Ukupno pregleda", 2)));
        totalViews.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of views.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj pregleda.",
                    2)));
        totalViews.setAccessLevel(AccessLevel.OPEN);
        totalViews.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        totalViews.setContentType(IndicatorContentType.NUMBER);

        var dailyViews = new Indicator();
        dailyViews.setCode("viewsDay");
        dailyViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Today's views", 1),
            new MultiLingualContent(serbianTag, "Pregleda danas", 2)));
        dailyViews.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of views in the last 24h.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj pregleda u poslednjih 24h.",
                    2)));
        dailyViews.setAccessLevel(AccessLevel.OPEN);
        dailyViews.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        dailyViews.setContentType(IndicatorContentType.NUMBER);

        var weeklyViews = new Indicator();
        weeklyViews.setCode("viewsWeek");
        weeklyViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Week's views", 1),
            new MultiLingualContent(serbianTag, "Pregleda ove sedmice", 2)));
        weeklyViews.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of views in the last 7 days.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj pregleda u poslednjih 7 dana.",
                    2)));
        weeklyViews.setAccessLevel(AccessLevel.OPEN);
        weeklyViews.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        weeklyViews.setContentType(IndicatorContentType.NUMBER);

        var monthlyViews = new Indicator();
        monthlyViews.setCode("viewsMonth");
        monthlyViews.setTitle(Set.of(new MultiLingualContent(englishTag, "Month's views", 1),
            new MultiLingualContent(serbianTag, "Pregleda ovog meseca", 2)));
        monthlyViews.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of views in the last month.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj pregleda u poslednjih mesec dana.",
                    2)));
        monthlyViews.setAccessLevel(AccessLevel.OPEN);
        monthlyViews.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        monthlyViews.setContentType(IndicatorContentType.NUMBER);

        var totalDownloads = new Indicator();
        totalDownloads.setCode("downloadsTotal");
        totalDownloads.setTitle(Set.of(new MultiLingualContent(englishTag, "Total downloads", 1),
            new MultiLingualContent(serbianTag, "Ukupno preuzimanja", 2)));
        totalDownloads.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of downloads.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj preuzimanja.",
                    2)));
        totalDownloads.setAccessLevel(AccessLevel.OPEN);
        totalDownloads.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        totalDownloads.setContentType(IndicatorContentType.NUMBER);

        var dailyDownloads = new Indicator();
        dailyDownloads.setCode("downloadsDay");
        dailyDownloads.setTitle(Set.of(new MultiLingualContent(englishTag, "Today's downloads", 1),
            new MultiLingualContent(serbianTag, "Preuzimanja danas", 2)));
        dailyDownloads.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of downloads in the last 24h.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj preuzimanja u poslednjih 24h.",
                    2)));
        dailyDownloads.setAccessLevel(AccessLevel.OPEN);
        dailyDownloads.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        dailyDownloads.setContentType(IndicatorContentType.NUMBER);

        var weeklyDownloads = new Indicator();
        weeklyDownloads.setCode("downloadsWeek");
        weeklyDownloads.setTitle(Set.of(new MultiLingualContent(englishTag, "Week's downloads", 1),
            new MultiLingualContent(serbianTag, "Preuzimanja ove sedmice", 2)));
        weeklyDownloads.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of downloads in the last 7 days.",
                    1),
                new MultiLingualContent(serbianTag, "Ukupan broj preuzimanja u poslednjih 7 dana.",
                    2)));
        weeklyDownloads.setAccessLevel(AccessLevel.OPEN);
        weeklyDownloads.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        weeklyDownloads.setContentType(IndicatorContentType.NUMBER);

        var monthlyDownloads = new Indicator();
        monthlyDownloads.setCode("downloadsMonth");
        monthlyDownloads.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Month's downloads", 1),
                new MultiLingualContent(serbianTag, "Preuzimanja ovog meseca", 2)));
        monthlyDownloads.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of downloads in the last month.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Ukupan broj preuzimanja u poslednjih mesec dana.",
                    2)));
        monthlyDownloads.setAccessLevel(AccessLevel.OPEN);
        monthlyDownloads.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.DOCUMENT, ApplicableEntityType.PERSON,
                ApplicableEntityType.ORGANISATION_UNIT));
        monthlyDownloads.setContentType(IndicatorContentType.NUMBER);

        var numberOfPages = new Indicator();
        numberOfPages.setCode("pageNum");
        numberOfPages.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Number of pages", 1),
                new MultiLingualContent(serbianTag, "Broj stranica", 2)));
        numberOfPages.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of pages in a document.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Ukupan broj stranica u dokumentu.",
                    2)));
        numberOfPages.setAccessLevel(AccessLevel.CLOSED);
        numberOfPages.getApplicableTypes().add(ApplicableEntityType.MONOGRAPH);
        numberOfPages.setContentType(IndicatorContentType.NUMBER);

        var totalCitations = new Indicator();
        totalCitations.setCode("totalCitations");
        totalCitations.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Total citations", 1),
                new MultiLingualContent(serbianTag, "Broj citata", 2)));
        totalCitations.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "Total number of citations in this journal.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Ukupan broj citata radova u ovom časopisu.",
                    2)));
        totalCitations.setAccessLevel(AccessLevel.CLOSED);
        totalCitations.getApplicableTypes().addAll(
            List.of(ApplicableEntityType.PUBLICATION_SERIES, ApplicableEntityType.DOCUMENT));
        totalCitations.setContentType(IndicatorContentType.NUMBER);

        var fiveYearJIF = new Indicator();
        fiveYearJIF.setCode("fiveYearJIF");
        fiveYearJIF.setTitle(
            Set.of(new MultiLingualContent(englishTag, "5 Year JIF", 1),
                new MultiLingualContent(serbianTag, "Petogodišnji IF", 2)));
        fiveYearJIF.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "JIF in the last 5 years.",
                    1),
                new MultiLingualContent(serbianTag,
                    "IF u poslednjih 5 godina.",
                    2)));
        fiveYearJIF.setAccessLevel(AccessLevel.CLOSED);
        fiveYearJIF.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        fiveYearJIF.setContentType(IndicatorContentType.NUMBER);

        var currentJIF = new Indicator();
        currentJIF.setCode("currentJIF");
        currentJIF.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Journal Impact Factor", 1),
                new MultiLingualContent(serbianTag, "Impakt Faktor", 2)));
        currentJIF.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "JIF in the current year.",
                    1),
                new MultiLingualContent(serbianTag,
                    "IF ove godine.",
                    2)));
        currentJIF.setAccessLevel(AccessLevel.CLOSED);
        currentJIF.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        currentJIF.setContentType(IndicatorContentType.NUMBER);

        var fiveYearJIFRank = new Indicator();
        fiveYearJIFRank.setCode("fiveYearJIFRank");
        fiveYearJIFRank.setTitle(
            Set.of(new MultiLingualContent(englishTag, "5 Year JIF Rank", 1),
                new MultiLingualContent(serbianTag, "Petogodišnji IF Rank", 2)));
        fiveYearJIFRank.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "JIF Rank in the last 5 years.",
                    1),
                new MultiLingualContent(serbianTag,
                    "IF Rank u poslednjih 5 godina.",
                    2)));
        fiveYearJIFRank.setAccessLevel(AccessLevel.CLOSED);
        fiveYearJIFRank.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        fiveYearJIFRank.setContentType(IndicatorContentType.TEXT);

        var currentJIFRank = new Indicator();
        currentJIFRank.setCode("currentJIFRank");
        currentJIFRank.setTitle(
            Set.of(new MultiLingualContent(englishTag, "JIF Rank", 1),
                new MultiLingualContent(serbianTag, "IF Rank", 2)));
        currentJIFRank.setDescription(
            Set.of(
                new MultiLingualContent(englishTag, "JIF rank in the current year.",
                    1),
                new MultiLingualContent(serbianTag,
                    "IF rank ove godine.",
                    2)));
        currentJIFRank.setAccessLevel(AccessLevel.CLOSED);
        currentJIFRank.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        currentJIFRank.setContentType(IndicatorContentType.TEXT);

        var eigenFactorNorm = new Indicator();
        eigenFactorNorm.setCode("eigenFactorNorm");
        eigenFactorNorm.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Normalized Eigenfactor", 1),
                new MultiLingualContent(serbianTag, "Normalizovani Eigenfactor", 2)));
        eigenFactorNorm.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "A measure of the total influence of a journal over a 5-year period, considering both the quantity and quality of citations.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Mera ukupnog uticaja časopisa u periodu od 5 godina, uzevši u obzir i kvalitet i kvantitet citata.",
                    2)));
        eigenFactorNorm.setAccessLevel(AccessLevel.CLOSED);
        eigenFactorNorm.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        eigenFactorNorm.setContentType(IndicatorContentType.NUMBER);

        var ais = new Indicator();
        ais.setCode("ais");
        ais.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Article Influence Score", 1),
                new MultiLingualContent(serbianTag, "Article Influence Score", 2)));
        ais.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Metric used to measure the average influence of a journal's articles over the first five years after publication.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Metrika koja se koristi da prikaže srednju vrednost uticaja radova u časopisu kroz prvih pet godina nakon publikacije.",
                    2)));
        ais.setAccessLevel(AccessLevel.CLOSED);
        ais.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        ais.setContentType(IndicatorContentType.NUMBER);

        var citedHL = new Indicator();
        citedHL.setCode("citedHL");
        citedHL.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Cited Half-Life", 1),
                new MultiLingualContent(serbianTag, "Cited Half-Life", 2)));
        citedHL.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Calculates the median age of articles cited by a journal during a calendar year. Half of the citations reference articles published before this time, and half reference articles published afterwards.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Izračunava srednju starost članaka citiranih u časopisu tokom jedne kalendarske godine. Polovina citata se odnosi na članke objavljene pre ovog vremena, a polovina na članke objavljene kasnije.",
                    2)));
        citedHL.setAccessLevel(AccessLevel.CLOSED);
        citedHL.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        citedHL.setContentType(IndicatorContentType.NUMBER);

        var citingHL = new Indicator();
        citingHL.setCode("citingHL");
        citingHL.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Citing Half-Life", 1),
                new MultiLingualContent(serbianTag, "Citing Half-Life", 2)));
        citingHL.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Counts all the references made by the journal during one calendar year and calculates the median article publication date—half of the cited references were published before this time, half were published afterwards.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Broji sve reference koje je časopis naveo tokom jedne kalendarske godine i izračunava srednji datum objavljivanja članka – polovina citiranih referenci je objavljena pre ovog vremena, polovina je objavljena kasnije.",
                    2)));
        citingHL.setAccessLevel(AccessLevel.CLOSED);
        citingHL.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        citingHL.setContentType(IndicatorContentType.NUMBER);


        var sjr = new Indicator();
        sjr.setCode("sjr");
        sjr.setTitle(
            Set.of(new MultiLingualContent(englishTag, "SCImago Journal Rank", 1),
                new MultiLingualContent(serbianTag, "SCImago Journal Rank", 2)));
        sjr.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "The SCImago Journal Rank (SJR) indicator is a measure of the prestige of scholarly journals that accounts for both the number of citations received by a journal and the prestige of the journals where the citations come from.",
                    1),
                new MultiLingualContent(serbianTag,
                    "SCImago Journal Rank (SJR) je mera prestiža naučnog časopisa koja uzima u obzir i broj citata i prestiž časopisa iz kojeg ti citati dolaze.",
                    2)));
        sjr.setAccessLevel(AccessLevel.CLOSED);
        sjr.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        sjr.setContentType(IndicatorContentType.TEXT);

        var hIndex = new Indicator();
        hIndex.setCode("hIndex");
        hIndex.setTitle(
            Set.of(new MultiLingualContent(englishTag, "H Index", 1),
                new MultiLingualContent(serbianTag, "H Index", 2)));
        hIndex.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "The h-index is calculated by counting the number of publications for which an author has been cited by other authors at least that same number of times.",
                    1),
                new MultiLingualContent(serbianTag,
                    "H-indeks se izračunava kao ukupan broj publikacija koje su citirali drugi autori najmanje isti broj puta.",
                    2)));
        hIndex.setAccessLevel(AccessLevel.CLOSED);
        hIndex.getApplicableTypes()
            .addAll(List.of(ApplicableEntityType.PUBLICATION_SERIES, ApplicableEntityType.PERSON));
        hIndex.setContentType(IndicatorContentType.NUMBER);

        var sdg = new Indicator();
        sdg.setCode("sdg");
        sdg.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Sustainable Development Goals (SDG)", 1),
                new MultiLingualContent(serbianTag, "Sustainable Development Goals (SDG)", 2)));
        sdg.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "SDG represents the alignment of journals with the United Nations Sustainable Development Goals based on their research focus and contribution to global sustainability.",
                    1),
                new MultiLingualContent(serbianTag,
                    "SDG označava povezanost časopisa sa Ciljevima održivog razvoja Ujedinjenih nacija na osnovu njihovog istraživačkog fokusa i doprinosa globalnoj održivosti.",
                    2)));
        sdg.setAccessLevel(AccessLevel.CLOSED);
        sdg.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        sdg.setContentType(IndicatorContentType.NUMBER);

        var overton = new Indicator();
        overton.setCode("overton");
        overton.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Overton", 1),
                new MultiLingualContent(serbianTag, "Overton", 2)));
        overton.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Overton measures the influence of academic journals by tracking citations in policy documents, legal texts, patents, and other non-academic sources, reflecting their impact on public policy and practical applications.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Overton meri uticaj akademskih časopisa praćenjem citata u političkim dokumentima, pravnim tekstovima, patentima i drugim neakademskim izvorima, čime odražava njihov uticaj na javne politike i praktične primene.",
                    2)));
        overton.setAccessLevel(AccessLevel.CLOSED);
        overton.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        overton.setContentType(IndicatorContentType.NUMBER);

        var erihPlus = new Indicator();
        erihPlus.setCode("erihPlus");
        erihPlus.setTitle(
            Set.of(new MultiLingualContent(englishTag, "ERIH PLUS list", 1),
                new MultiLingualContent(serbianTag, "ERIH PLUS lista", 2)));
        erihPlus.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "The ERIH PLUS list is a European reference index for scientific journals in the humanities and social sciences, aimed at enhancing their visibility and quality.",
                    1),
                new MultiLingualContent(serbianTag,
                    "ERIH PLUS lista je evropski referentni indeks za naučne časopise iz oblasti humanističkih i društvenih nauka, sa ciljem povećanja njihove vidljivosti i kvaliteta.",
                    2)));
        erihPlus.setAccessLevel(AccessLevel.CLOSED);
        erihPlus.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        erihPlus.setContentType(IndicatorContentType.BOOL);

        var jci = new Indicator();
        jci.setCode("jci");
        jci.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Journal Citation Indicator (JCI)", 1),
                new MultiLingualContent(serbianTag, "Indikator citiranosti časopisa (JCI)", 2)));
        jci.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Normalized metric that measures the citation impact of a journal's publications over a three-year period, allowing for comparison across disciplines.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Normalizovana metrika koja meri uticaj citiranosti publikacija časopisa tokom trogodišnjeg perioda, omogućavajući poređenje među disciplinama.",
                    2)));
        jci.setAccessLevel(AccessLevel.CLOSED);
        jci.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        jci.setContentType(IndicatorContentType.NUMBER);

        var jciPercentile = new Indicator();
        jciPercentile.setCode("jciPercentile");
        jciPercentile.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Journal Citation Indicator (JCI) percentile",
                    1),
                new MultiLingualContent(serbianTag,
                    "Indikator citiranosti časopisa (JCI) percentil", 2)));
        jciPercentile.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "The Journal Citation Indicator (JCI) percentile shows a journal's relative position within its research field, where a higher percentile indicates stronger performance compared to other journals in the same category.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Percentil Journal Citation Indicator-a (JCI) prikazuje relativnu poziciju časopisa unutar svoje oblasti istraživanja, pri čemu veći procenat označava bolje performanse u odnosu na druge časopise u istoj kategoriji.",
                    2)));
        jciPercentile.setAccessLevel(AccessLevel.CLOSED);
        jciPercentile.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        jciPercentile.setContentType(IndicatorContentType.NUMBER);

        var jcr = new Indicator();
        jcr.setCode("jcr");
        jcr.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Journal Citation Rank (JCR) list", 1),
                new MultiLingualContent(serbianTag, "Journal Citation Rank (JCR) lista", 2)));
        jcr.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "The Journal Citation Reports (JCR) list provides metrics like Impact Factor to evaluate and rank scientific journals based on citation data.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Lista Journal Citation Reports (JCR) pruža metrike, poput faktora uticaja, za ocenjivanje i rangiranje naučnih časopisa na osnovu podataka o citatima.",
                    2)));
        jcr.setAccessLevel(AccessLevel.CLOSED);
        jcr.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        jcr.setContentType(IndicatorContentType.BOOL);

        var scimago = new Indicator();
        scimago.setCode("scimago");
        scimago.setTitle(
            Set.of(new MultiLingualContent(englishTag, "SciMAGO list", 1),
                new MultiLingualContent(serbianTag, "SciMago lista", 2)));
        scimago.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "The SciMAGO Journal Rank (SJR) list ranks journals using citation data from the Scopus database, emphasizing visibility and prestige.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Lista SciMAGO Journal Rank (SJR) rangira časopise koristeći podatke o citatima iz Scopus baze, naglašavajući vidljivost i prestiž.",
                    2)));
        scimago.setAccessLevel(AccessLevel.CLOSED);
        scimago.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        scimago.setContentType(IndicatorContentType.BOOL);

        var numParticipants = new Indicator();
        numParticipants.setCode("numParticipants");
        numParticipants.setTitle(
            Set.of(new MultiLingualContent(englishTag, "Number of Participants", 1),
                new MultiLingualContent(serbianTag, "Broj učesnika", 2)));
        numParticipants.setDescription(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Number of conference participants.",
                    1),
                new MultiLingualContent(serbianTag,
                    "Broj učesnika na konferenciji.",
                    2)));
        numParticipants.setAccessLevel(AccessLevel.CLOSED);
        numParticipants.getApplicableTypes().add(ApplicableEntityType.EVENT);
        numParticipants.setContentType(IndicatorContentType.NUMBER);

        var numPresentations = new Indicator();
        numPresentations.setCode("numPresentations");
        numPresentations.setTitle(Set.of(
            new MultiLingualContent(englishTag, "Number of Presentations", 1),
            new MultiLingualContent(serbianTag, "Broj saopštenja na skupu", 2)
        ));
        numPresentations.setDescription(Set.of(
            new MultiLingualContent(englishTag, "Number of presentations at the conference.", 1),
            new MultiLingualContent(serbianTag, "Broj saopštenja na skupu.", 2)
        ));
        numPresentations.setAccessLevel(AccessLevel.CLOSED);
        numPresentations.getApplicableTypes().add(ApplicableEntityType.EVENT);
        numPresentations.setContentType(IndicatorContentType.NUMBER);

        var numParticipantCountries = new Indicator();
        numParticipantCountries.setCode("numParticipantCountries");
        numParticipantCountries.setTitle(Set.of(
            new MultiLingualContent(englishTag, "Number of Participant Countries", 1),
            new MultiLingualContent(serbianTag, "Broj zemalja koji imaju učesnika na skupu", 2)
        ));
        numParticipantCountries.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "Number of countries represented by participants at the conference.", 1),
            new MultiLingualContent(serbianTag, "Broj zemalja koji imaju učesnika na skupu.", 2)
        ));
        numParticipantCountries.setAccessLevel(AccessLevel.CLOSED);
        numParticipantCountries.getApplicableTypes().add(ApplicableEntityType.EVENT);
        numParticipantCountries.setContentType(IndicatorContentType.NUMBER);

        var numCountriesInScientificCommittee = new Indicator();
        numCountriesInScientificCommittee.setCode("numCountriesInScientificCommittee");
        numCountriesInScientificCommittee.setTitle(Set.of(
            new MultiLingualContent(englishTag,
                "Number of Countries Represented in Scientific Committee", 1),
            new MultiLingualContent(serbianTag,
                "Broj zemalja koji imaju učesnika u naučnom odboru skupa", 2)
        ));
        numCountriesInScientificCommittee.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "Number of countries represented in the scientific committee.", 1),
            new MultiLingualContent(serbianTag,
                "Broj zemalja koji imaju učesnika u naučnom odboru skupa.", 2)
        ));
        numCountriesInScientificCommittee.setAccessLevel(AccessLevel.CLOSED);
        numCountriesInScientificCommittee.getApplicableTypes().add(ApplicableEntityType.EVENT);
        numCountriesInScientificCommittee.setContentType(IndicatorContentType.NUMBER);

        var organizedByScientificInstitution = new Indicator();
        organizedByScientificInstitution.setCode("organizedByScientificInstitution");
        organizedByScientificInstitution.setTitle(Set.of(
            new MultiLingualContent(englishTag, "Organized by Scientific or National Institution",
                1),
            new MultiLingualContent(serbianTag,
                "Skup organizuje naučno-istraživačka institucija, ili institucija od nacionalnog značaja",
                2)
        ));
        organizedByScientificInstitution.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "Indicates if the event is organized by a scientific research or national institution.",
                1),
            new MultiLingualContent(serbianTag,
                "Da li skup organizuje naučno-istraživačka institucija, ili institucija od nacionalnog značaja.",
                2)
        ));
        organizedByScientificInstitution.setAccessLevel(AccessLevel.CLOSED);
        organizedByScientificInstitution.getApplicableTypes().add(ApplicableEntityType.EVENT);
        organizedByScientificInstitution.setContentType(IndicatorContentType.BOOL);

        var slavistiCategory = new Indicator();
        slavistiCategory.setCode("slavistiCategory");
        slavistiCategory.setTitle(Set.of(
            new MultiLingualContent(englishTag, "MKS Slavists Category",
                1),
            new MultiLingualContent(serbianTag,
                "MKS Slavisti Kategorija",
                2)
        ));
        slavistiCategory.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "Reference list of slavistic magazines issued by international committee of Slavists.",
                1),
            new MultiLingualContent(serbianTag,
                "Referentna lista slavističkih časopisa međunarodnog komiteta slavista.",
                2)
        ));
        slavistiCategory.setAccessLevel(AccessLevel.CLOSED);
        slavistiCategory.getApplicableTypes().add(ApplicableEntityType.PUBLICATION_SERIES);
        slavistiCategory.setContentType(IndicatorContentType.TEXT);

        var lectureInvitation = new Indicator();
        lectureInvitation.setCode("lectureInvitation");
        lectureInvitation.setTitle(Set.of(
            new MultiLingualContent(englishTag, "There is an invitation to lecture",
                1),
            new MultiLingualContent(serbianTag,
                "Postoji poziv za predavanje",
                2)
        ));
        lectureInvitation.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "There is an invitation to lecture.",
                1),
            new MultiLingualContent(serbianTag,
                "Postoji poziv za predavanje.",
                2)
        ));
        lectureInvitation.setAccessLevel(AccessLevel.CLOSED);
        lectureInvitation.getApplicableTypes().add(ApplicableEntityType.DOCUMENT);
        lectureInvitation.setContentType(IndicatorContentType.BOOL);

        var isTheoretical = new Indicator();
        isTheoretical.setCode("isTheoretical");
        isTheoretical.setTitle(Set.of(
            new MultiLingualContent(englishTag, "Paper is thoretical",
                1),
            new MultiLingualContent(serbianTag,
                "Rad je teorijski",
                2)
        ));
        isTheoretical.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "A paper is theoretical when it focuses on developing models, concepts, or frameworks based on abstract reasoning or mathematics.",
                1),
            new MultiLingualContent(serbianTag,
                "Rad je teorijski kada se fokusira na razvoj modela, koncepata ili okvira zasnovanih na apstraktnom razmišljanju ili matematici.",
                2)
        ));
        isTheoretical.setAccessLevel(AccessLevel.CLOSED);
        isTheoretical.getApplicableTypes().add(ApplicableEntityType.DOCUMENT);
        isTheoretical.setContentType(IndicatorContentType.BOOL);

        var isExperimental = new Indicator();
        isExperimental.setCode("isExperimental");
        isExperimental.setTitle(Set.of(
            new MultiLingualContent(englishTag, "Paper is experimental",
                1),
            new MultiLingualContent(serbianTag,
                "Rad je eksperimentalni",
                2)
        ));
        isExperimental.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "A paper is experimental when it involves testing or observing phenomena through real-world experiments or practical setups.",
                1),
            new MultiLingualContent(serbianTag,
                "Rad je eksperimentalni kada uključuje testiranje ili posmatranje fenomena kroz eksperimente u stvarnom svetu ili praktične postavke.",
                2)
        ));
        isExperimental.setAccessLevel(AccessLevel.CLOSED);
        isExperimental.getApplicableTypes().add(ApplicableEntityType.DOCUMENT);
        isExperimental.setContentType(IndicatorContentType.BOOL);

        var isSimulation = new Indicator();
        isSimulation.setCode("isSimulation");
        isSimulation.setTitle(Set.of(
            new MultiLingualContent(englishTag, "Paper is a simulation or an analysis",
                1),
            new MultiLingualContent(serbianTag,
                "Rad je simulacija ili analiza",
                2)
        ));
        isSimulation.setDescription(Set.of(
            new MultiLingualContent(englishTag,
                "A paper falls into this category when it uses computational models to mimic real-world processes or systems, or when it examines data, results, or systems to derive insights or conclusions.",
                1),
            new MultiLingualContent(serbianTag,
                "Rad pada u ovu kategoriju kada koristi računarske modele za oponašanje realnih procesa ili sistema, ili kada ispituje podatke, rezultate ili sisteme kako bi se izvukli uvidi ili zaključci.",
                2)
        ));
        isSimulation.setAccessLevel(AccessLevel.CLOSED);
        isSimulation.getApplicableTypes().add(ApplicableEntityType.DOCUMENT);
        isSimulation.setContentType(IndicatorContentType.BOOL);

        indicatorRepository.saveAll(
            List.of(totalViews, dailyViews, weeklyViews, monthlyViews, totalDownloads, fiveYearJIF,
                dailyDownloads, weeklyDownloads, monthlyDownloads, numberOfPages, totalCitations,
                currentJIF, eigenFactorNorm, ais, citedHL, currentJIFRank, fiveYearJIFRank, sjr,
                hIndex, sdg, overton, citingHL, erihPlus, jci, jcr, scimago, jciPercentile,
                numParticipants, organizedByScientificInstitution, slavistiCategory,
                numCountriesInScientificCommittee, numParticipantCountries, numPresentations,
                lectureInvitation, isTheoretical, isExperimental, isSimulation));
    }

    public void initializeAssessmentClassifications(LanguageTag englishTag,
                                                    LanguageTag serbianTag) {
        var m21APlus = new AssessmentClassification();
        m21APlus.setFormalDescriptionOfRule("handleM21APlus");
        m21APlus.setCode("M21APlus");
        m21APlus.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Vodeći međunarodni časopis kategorije M21A+.",
                    1)));
        m21APlus.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m21A = new AssessmentClassification();
        m21A.setFormalDescriptionOfRule("handleM21A");
        m21A.setCode("M21A");
        m21A.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Vodeći međunarodni časopis kategorije M21A.",
                    1)));
        m21A.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m21 = new AssessmentClassification();
        m21.setFormalDescriptionOfRule("handleM21");
        m21.setCode("M21");
        m21.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Vodeći međunarodni časopis kategorije M21.",
                    1)));
        m21.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m22 = new AssessmentClassification();
        m22.setFormalDescriptionOfRule("handleM22");
        m22.setCode("M22");
        m22.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Međunarodni časopis kategorije M22.",
                    1)));
        m22.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m23 = new AssessmentClassification();
        m23.setFormalDescriptionOfRule("handleM23");
        m23.setCode("M23");
        m23.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Međunarodni časopis kategorije M23.",
                    1)));
        m23.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m23e = new AssessmentClassification();
        m23e.setFormalDescriptionOfRule("handleM23e");
        m23e.setCode("M23e");
        m23e.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Međunarodni časopis kategorije M23e.",
                    1)));
        m23e.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m24plus = new AssessmentClassification();
        m24plus.setFormalDescriptionOfRule("handleM24plus");
        m24plus.setCode("M24Plus");
        m24plus.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Međunarodni časopis kategorije M24+.",
                    1)));
        m24plus.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m24 = new AssessmentClassification();
        m24.setFormalDescriptionOfRule("handleM24");
        m24.setCode("M24");
        m24.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Vodeći nacionalni časopis kategorije M24.",
                    1)));
        m24.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m51 = new AssessmentClassification();
        m51.setFormalDescriptionOfRule("handleM51");
        m51.setCode("M51");
        m51.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Vodeći nacionalni časopis kategorije M51.",
                    1)));
        m51.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m52 = new AssessmentClassification();
        m52.setFormalDescriptionOfRule("handleM52");
        m52.setCode("M52");
        m52.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Nacionalni časopis kategorije M52.",
                    1)));
        m52.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m53 = new AssessmentClassification();
        m53.setFormalDescriptionOfRule("handleM53");
        m53.setCode("M53");
        m53.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "Nacionalni časopis kategorije M53.",
                    1)));
        m53.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var m54 = new AssessmentClassification();
        m54.setFormalDescriptionOfRule("handleM54");
        m54.setCode("M54");
        m54.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "Nacionalni naučni časopis koji se prvi put kategoriše.",
                    1)));
        m54.setApplicableTypes(Set.of(ApplicableEntityType.PUBLICATION_SERIES));

        var multinationalConf = new AssessmentClassification();
        multinationalConf.setFormalDescriptionOfRule("multinationalConference");
        multinationalConf.setCode("multinationalConf");
        multinationalConf.setTitle(
            Set.of(
                new MultiLingualContent(serbianTag, "Međunarodna konferencija.", 2),
                new MultiLingualContent(englishTag, "Multinational conference.", 1)));
        multinationalConf.setApplicableTypes(Set.of(ApplicableEntityType.EVENT));

        var nationalConf = new AssessmentClassification();
        nationalConf.setFormalDescriptionOfRule("nationalConference");
        nationalConf.setCode("nationalConf");
        nationalConf.setTitle(
            Set.of(
                new MultiLingualContent(serbianTag, "Domaća konferencija.", 2),
                new MultiLingualContent(englishTag, "National conference.", 1)));
        nationalConf.setApplicableTypes(Set.of(ApplicableEntityType.EVENT));

        var nonAcademicConf = new AssessmentClassification();
        nonAcademicConf.setFormalDescriptionOfRule("nonAcademicConference");
        nonAcademicConf.setCode("nonAcademicConf");
        nonAcademicConf.setTitle(
            Set.of(
                new MultiLingualContent(serbianTag, "Tehnička (ne-naučna) konferencija.", 2),
                new MultiLingualContent(englishTag, "Technical (non-academic) conference.", 1)));
        nonAcademicConf.setApplicableTypes(Set.of(ApplicableEntityType.EVENT));

        var docM21APlus = new AssessmentClassification();
        docM21APlus.setFormalDescriptionOfRule("handleDocM21APlus");
        docM21APlus.setCode("docM21APlus");
        docM21APlus.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M21a+",
                    1)));
        docM21APlus.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM21A = new AssessmentClassification();
        docM21A.setFormalDescriptionOfRule("handleDocM21A");
        docM21A.setCode("docM21A");
        docM21A.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M21a",
                    1)));
        docM21A.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM21 = new AssessmentClassification();
        docM21.setFormalDescriptionOfRule("handleDocM21");
        docM21.setCode("docM21");
        docM21.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M21",
                    1)));
        docM21.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM22 = new AssessmentClassification();
        docM22.setFormalDescriptionOfRule("handleDocM22");
        docM22.setCode("docM22");
        docM22.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M22",
                    1)));
        docM22.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM23 = new AssessmentClassification();
        docM23.setFormalDescriptionOfRule("handleDocM23");
        docM23.setCode("docM23");
        docM23.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M23",
                    1)));
        docM23.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM23e = new AssessmentClassification();
        docM23e.setFormalDescriptionOfRule("handleDocM23e");
        docM23e.setCode("docM23e");
        docM23e.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M23e",
                    1)));
        docM23e.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM24plus = new AssessmentClassification();
        docM24plus.setFormalDescriptionOfRule("handleDocM24plus");
        docM24plus.setCode("docM24Plus");
        docM24plus.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M24+",
                    1)));
        docM24plus.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM24 = new AssessmentClassification();
        docM24.setFormalDescriptionOfRule("handleDocM24");
        docM24.setCode("docM24");
        docM24.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M24",
                    1)));
        docM24.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM26 = new AssessmentClassification();
        docM26.setFormalDescriptionOfRule("handleDocM26");
        docM26.setCode("M26");
        docM26.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M26",
                    1)));
        docM26.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM27 = new AssessmentClassification();
        docM27.setFormalDescriptionOfRule("handleDocM27");
        docM27.setCode("M27");
        docM27.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M27",
                    1)));
        docM27.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM51 = new AssessmentClassification();
        docM51.setFormalDescriptionOfRule("handleDocM51");
        docM51.setCode("docM51");
        docM51.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M51",
                    1)));
        docM51.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM52 = new AssessmentClassification();
        docM52.setFormalDescriptionOfRule("handleDocM52");
        docM52.setCode("docM52");
        docM52.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M52",
                    1)));
        docM52.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM53 = new AssessmentClassification();
        docM53.setFormalDescriptionOfRule("handleDocM53");
        docM53.setCode("docM53");
        docM53.setTitle(
            Set.of(
                new MultiLingualContent(englishTag, "M53",
                    1)));
        docM53.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var docM54 = new AssessmentClassification();
        docM54.setFormalDescriptionOfRule("handleDocM54");
        docM54.setCode("docM54");
        docM54.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M54",
                    1)));
        docM54.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m31 = new AssessmentClassification();
        m31.setFormalDescriptionOfRule("handleM31");
        m31.setCode("M31");
        m31.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M31",
                    1)));
        m31.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m32 = new AssessmentClassification();
        m32.setFormalDescriptionOfRule("handleM31");
        m32.setCode("M32");
        m32.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M32",
                    1)));
        m32.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m33 = new AssessmentClassification();
        m33.setFormalDescriptionOfRule("handleM31");
        m33.setCode("M33");
        m33.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M33",
                    1)));
        m33.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m34 = new AssessmentClassification();
        m34.setFormalDescriptionOfRule("handleM31");
        m34.setCode("M34");
        m34.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M34",
                    1)));
        m34.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m61 = new AssessmentClassification();
        m61.setFormalDescriptionOfRule("handleM61");
        m61.setCode("M61");
        m61.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M61",
                    1)));
        m61.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m62 = new AssessmentClassification();
        m62.setFormalDescriptionOfRule("handleM62");
        m62.setCode("M62");
        m62.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M62",
                    1)));
        m62.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m63 = new AssessmentClassification();
        m63.setFormalDescriptionOfRule("handleM63");
        m63.setCode("M63");
        m63.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M63",
                    1)));
        m63.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m64 = new AssessmentClassification();
        m64.setFormalDescriptionOfRule("handleM64");
        m64.setCode("M64");
        m64.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M64",
                    1)));
        m64.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        var m69 = new AssessmentClassification();
        m69.setFormalDescriptionOfRule("handleM69");
        m69.setCode("M69");
        m69.setTitle(
            Set.of(
                new MultiLingualContent(englishTag,
                    "M69",
                    1)));
        m69.setApplicableTypes(Set.of(ApplicableEntityType.DOCUMENT));

        assessmentClassificationRepository.saveAll(
            List.of(m21APlus, m21A, m21, m22, m23, m23e, m24plus, multinationalConf, nationalConf,
                nonAcademicConf, m51, m52, m53, m54, m24, docM21APlus, docM21A, docM21, docM22,
                docM23, docM23e, docM24plus, docM24, docM51, docM52, docM53, docM54, docM26, docM27,
                m31, m32, m33,
                m34, m61, m62, m63, m64, m69));
    }

    public Commission initializeCommissions(LanguageTag englishTag, LanguageTag serbianTag) {
        var commission1 = new Commission();
        commission1.setDescription(Set.of(new MultiLingualContent(englishTag, "Web Of Science", 1),
            new MultiLingualContent(serbianTag, "Web Of Science", 2)));
        commission1.setFormalDescriptionOfRule("WOSJournalClassificationRuleEngine");

        var commission2 = new Commission();
        commission2.setDescription(Set.of(new MultiLingualContent(englishTag, "SciMAGO", 1),
            new MultiLingualContent(serbianTag, "SciMAGO", 2)));
        commission2.setFormalDescriptionOfRule("ScimagoJournalClassificationRuleEngine");
        commission2.setAssessmentDateFrom(LocalDate.of(2022, 2, 4));
        commission2.setAssessmentDateTo(LocalDate.of(2022, 5, 4));

        var commission3 = new Commission();
        commission3.setDescription(Set.of(new MultiLingualContent(englishTag, "Erih PLUS", 1),
            new MultiLingualContent(serbianTag, "Erih PLUS", 2)));
        commission3.setFormalDescriptionOfRule("ErihPlusJournalClassificationRuleEngine");

        var commission4 = new Commission();
        commission4.setDescription(Set.of(new MultiLingualContent(englishTag, "MKS Slavists", 1),
            new MultiLingualContent(serbianTag, "MKS Slavisti", 2)));
        commission4.setFormalDescriptionOfRule("MKSJournalClassificationRuleEngine");

        var commission5 = new Commission();
        commission5.setDescription(
            Set.of(new MultiLingualContent(englishTag, "MNO ALL", 1),
                new MultiLingualContent(serbianTag, "MNO SVE", 2)));
        commission5.setFormalDescriptionOfRule("load-mno");

        var commission6 = new Commission();
        commission6.setDescription(
            Set.of(new MultiLingualContent(englishTag, "MNO Physics & Chemistry", 1),
                new MultiLingualContent(serbianTag, "MNO Fizika i Hemija", 2)));
        commission6.setFormalDescriptionOfRule("load-mnoPhysChem");

        var commission7 = new Commission();
        commission7.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DMI-PMF", 1),
                new MultiLingualContent(serbianTag, "DMI-PMF", 2)));
        commission7.setFormalDescriptionOfRule("load-mno");

        var commission8 = new Commission();
        commission8.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DH-PMF", 1),
                new MultiLingualContent(serbianTag, "DH-PMF", 2)));
        commission8.setFormalDescriptionOfRule("load-mno");

        var commission9 = new Commission();
        commission9.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DGTH-NAT-PMF", 1),
                new MultiLingualContent(serbianTag, "DGTH-NAT-PMF", 2)));
        commission9.setFormalDescriptionOfRule("load-mno");

        var commission10 = new Commission();
        commission10.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DF-PMF", 1),
                new MultiLingualContent(serbianTag, "DF-PMF", 2)));
        commission10.setFormalDescriptionOfRule("load-mno");

        var commission11 = new Commission();
        commission11.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DBE-PMF", 1),
                new MultiLingualContent(serbianTag, "DBE-PMF", 2)));
        commission11.setFormalDescriptionOfRule("load-mno");

        var commission12 = new Commission();
        commission12.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DMI-SOC-PMF", 1),
                new MultiLingualContent(serbianTag, "DMI-SOC-PMF", 2)));
        commission12.setFormalDescriptionOfRule("load-mno");

        var commission13 = new Commission();
        commission13.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DH-SOC-PMF", 1),
                new MultiLingualContent(serbianTag, "DH-SOC-PMF", 2)));
        commission13.setFormalDescriptionOfRule("load-mno");

        var commission14 = new Commission();
        commission14.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DGTH-SOC-PMF", 1),
                new MultiLingualContent(serbianTag, "DGTH-SOC-PMF", 2)));
        commission14.setFormalDescriptionOfRule("load-mno");

        var commission15 = new Commission();
        commission15.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DF-SOC-PMF", 1),
                new MultiLingualContent(serbianTag, "DF-SOC-PMF", 2)));
        commission15.setFormalDescriptionOfRule("load-mno");

        var commission16 = new Commission();
        commission16.setDescription(
            Set.of(new MultiLingualContent(englishTag, "DBE-SOC-PMF", 1),
                new MultiLingualContent(serbianTag, "DBE-SOC-PMF", 2)));
        commission16.setFormalDescriptionOfRule("load-mno");

        commissionRepository.saveAll(
            List.of(commission1, commission2, commission3, commission4, commission5, commission6,
                commission7, commission8, commission9, commission10, commission11, commission12,
                commission13, commission14, commission15, commission16));

        var commissionRelation1 = new CommissionRelation();
        commissionRelation1.setSourceCommission(commission5);
        commissionRelation1.setTargetCommissions(new HashSet<>(List.of(commission2)));
        commissionRelation1.setPriority(2);
        commissionRelation1.setResultCalculationMethod(ResultCalculationMethod.BEST_VALUE);

        var commissionRelation2 = new CommissionRelation();
        commissionRelation2.setSourceCommission(commission5);
        commissionRelation2.setTargetCommissions(new HashSet<>(List.of(commission1, commission4)));
        commissionRelation2.setPriority(1);
        commissionRelation2.setResultCalculationMethod(ResultCalculationMethod.BEST_VALUE);

        commissionRelationRepository.saveAll(List.of(commissionRelation1, commissionRelation2));

        return commission5;
    }
}
