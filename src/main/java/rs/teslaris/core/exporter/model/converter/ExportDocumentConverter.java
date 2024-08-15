package rs.teslaris.core.exporter.model.converter;

import com.google.common.base.Functions;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import rs.teslaris.core.exporter.model.common.ExportContribution;
import rs.teslaris.core.exporter.model.common.ExportDocument;
import rs.teslaris.core.exporter.model.common.ExportMultilingualContent;
import rs.teslaris.core.exporter.model.common.ExportPublicationType;
import rs.teslaris.core.exporter.model.common.ExportPublisher;
import rs.teslaris.core.importer.model.oaipmh.common.DC;
import rs.teslaris.core.importer.model.oaipmh.common.PersonAttributes;
import rs.teslaris.core.importer.model.oaipmh.publication.PartOf;
import rs.teslaris.core.importer.model.oaipmh.publication.Publication;
import rs.teslaris.core.importer.model.oaipmh.publication.PublishedIn;
import rs.teslaris.core.importer.model.oaipmh.publication.Publisher;
import rs.teslaris.core.model.commontypes.MultiLingualContent;
import rs.teslaris.core.model.document.Dataset;
import rs.teslaris.core.model.document.Document;
import rs.teslaris.core.model.document.JournalPublication;
import rs.teslaris.core.model.document.License;
import rs.teslaris.core.model.document.Monograph;
import rs.teslaris.core.model.document.MonographPublication;
import rs.teslaris.core.model.document.Patent;
import rs.teslaris.core.model.document.PersonDocumentContribution;
import rs.teslaris.core.model.document.Proceedings;
import rs.teslaris.core.model.document.ProceedingsPublication;
import rs.teslaris.core.model.document.Software;

public class ExportDocumentConverter extends ExportConverterBase {

    public static ExportDocument toCommonExportModel(Dataset dataset) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.DATASET);

        setBaseFields(commonExportDocument, dataset);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, dataset);

        commonExportDocument.setNumber(dataset.getInternalNumber());
        if (Objects.nonNull(dataset.getPublisher())) {
            commonExportDocument.getPublishers().add(new ExportPublisher(
                ExportMultilingualContentConverter.toCommonExportModel(
                    dataset.getPublisher().getName())));
        }

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(Software software) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.SOFTWARE);

        setBaseFields(commonExportDocument, software);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, software);

        commonExportDocument.setNumber(software.getInternalNumber());
        if (Objects.nonNull(software.getPublisher())) {
            commonExportDocument.getPublishers().add(new ExportPublisher(
                ExportMultilingualContentConverter.toCommonExportModel(
                    software.getPublisher().getName())));
        }

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(Patent patent) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.PATENT);

        setBaseFields(commonExportDocument, patent);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, patent);

        commonExportDocument.setNumber(patent.getNumber());
        if (Objects.nonNull(patent.getPublisher())) {
            commonExportDocument.getPublishers().add(new ExportPublisher(
                ExportMultilingualContentConverter.toCommonExportModel(
                    patent.getPublisher().getName())));
        }

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(JournalPublication journalPublication) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.JOURNAL_PUBLICATION);

        setBaseFields(commonExportDocument, journalPublication);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, journalPublication);

        commonExportDocument.setJournalPublicationType(
            journalPublication.getJournalPublicationType());
        commonExportDocument.setStartPage(journalPublication.getStartPage());
        commonExportDocument.setEndPage(journalPublication.getEndPage());
        commonExportDocument.setNumber(journalPublication.getArticleNumber());
        commonExportDocument.setVolume(journalPublication.getVolume());
        commonExportDocument.setIssue(journalPublication.getIssue());
        if (Objects.nonNull(journalPublication.getJournal())) {
            commonExportDocument.setJournal(ExportPublicationSeriesConverter.toCommonExportModel(
                journalPublication.getJournal()));
        }

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(Proceedings proceedings) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.PROCEEDINGS);

        setBaseFields(commonExportDocument, proceedings);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, proceedings);

        commonExportDocument.setEIsbn(proceedings.getEISBN());
        commonExportDocument.setPrintIsbn(proceedings.getPrintISBN());
        commonExportDocument.setVolume(proceedings.getPublicationSeriesVolume());
        commonExportDocument.setIssue(proceedings.getPublicationSeriesIssue());
        proceedings.getLanguages().forEach(languageTag -> {
            commonExportDocument.getLanguageTags().add(languageTag.getLanguageTag());
        });

        if (Objects.nonNull(proceedings.getPublicationSeries())) {
            commonExportDocument.setJournal(ExportPublicationSeriesConverter.toCommonExportModel(
                proceedings.getPublicationSeries()));
            commonExportDocument.setEdition(proceedings.getPublicationSeries().getTitle().stream()
                .max(Comparator.comparingInt(MultiLingualContent::getPriority)).get().getContent());
        }
        if (Objects.nonNull(proceedings.getPublisher())) {
            commonExportDocument.getPublishers().add(new ExportPublisher(
                ExportMultilingualContentConverter.toCommonExportModel(
                    proceedings.getPublisher().getName())));
        }

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(
        ProceedingsPublication proceedingsPublication) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.PROCEEDINGS_PUBLICATION);

        setBaseFields(commonExportDocument, proceedingsPublication);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, proceedingsPublication);

        commonExportDocument.setProceedingsPublicationType(
            proceedingsPublication.getProceedingsPublicationType());
        commonExportDocument.setStartPage(proceedingsPublication.getStartPage());
        commonExportDocument.setEndPage(proceedingsPublication.getEndPage());
        commonExportDocument.setNumber(proceedingsPublication.getArticleNumber());
        if (Objects.nonNull(proceedingsPublication.getProceedings())) {
            commonExportDocument.setProceedings(ExportDocumentConverter.toCommonExportModel(
                proceedingsPublication.getProceedings()));
        }

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(Monograph monograph) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.MONOGRAPH);

        setBaseFields(commonExportDocument, monograph);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, monograph);

        commonExportDocument.setMonographType(monograph.getMonographType());
        commonExportDocument.setEIsbn(monograph.getEISBN());
        commonExportDocument.setPrintIsbn(monograph.getPrintISBN());
        commonExportDocument.setVolume(monograph.getVolume());
        commonExportDocument.setNumber(monograph.getNumber());

        if (Objects.nonNull(monograph.getPublicationSeries())) {
            commonExportDocument.setJournal(ExportPublicationSeriesConverter.toCommonExportModel(
                monograph.getPublicationSeries()));
            commonExportDocument.setEdition(monograph.getPublicationSeries().getTitle().stream()
                .max(Comparator.comparingInt(MultiLingualContent::getPriority)).get().getContent());
        }

        monograph.getLanguages().forEach(languageTag -> {
            commonExportDocument.getLanguageTags().add(languageTag.getLanguageTag());
        });

        return commonExportDocument;
    }

    public static ExportDocument toCommonExportModel(MonographPublication monographPublication) {
        var commonExportDocument = new ExportDocument();
        commonExportDocument.setType(ExportPublicationType.MONOGRAPH_PUBLICATION);

        setBaseFields(commonExportDocument, monographPublication);
        if (commonExportDocument.getDeleted()) {
            return commonExportDocument;
        }

        setCommonFields(commonExportDocument, monographPublication);

        commonExportDocument.setMonographPublicationType(
            monographPublication.getMonographPublicationType());
        commonExportDocument.setStartPage(monographPublication.getStartPage());
        commonExportDocument.setEndPage(monographPublication.getEndPage());
        commonExportDocument.setNumber(monographPublication.getArticleNumber());

        if (Objects.nonNull(monographPublication.getMonograph())) {
            commonExportDocument.setMonograph(
                ExportDocumentConverter.toCommonExportModel(monographPublication.getMonograph()));
        }

        return commonExportDocument;
    }

    private static void setCommonFields(ExportDocument commonExportDocument, Document document) {
        commonExportDocument.setTitle(
            ExportMultilingualContentConverter.toCommonExportModel(document.getTitle()));
        commonExportDocument.setSubtitle(
            ExportMultilingualContentConverter.toCommonExportModel(document.getSubTitle()));
        commonExportDocument.setDescription(
            ExportMultilingualContentConverter.toCommonExportModel(document.getDescription()));
        commonExportDocument.setKeywords(
            ExportMultilingualContentConverter.toCommonExportModel(document.getKeywords()));

        document.getContributors().forEach(contributor -> {
            switch (contributor.getContributionType()) {
                case AUTHOR:
                    commonExportDocument.getAuthors().add(createExportContribution(contributor));
                    break;
                case EDITOR:
                    commonExportDocument.getEditors().add(createExportContribution(contributor));
                    break;
            }
        });

        commonExportDocument.setUris(document.getUris().stream().toList());
        commonExportDocument.setDocumentDate(document.getDocumentDate());
        commonExportDocument.setDoi(document.getDoi());
        commonExportDocument.setScopus(document.getScopusId());
        commonExportDocument.setOldId(document.getOldId());

        if (Objects.nonNull(document.getEvent())) {
            commonExportDocument.setEvent(
                ExportEventConverter.toCommonExportModel(document.getEvent()));
        }

        commonExportDocument.getRelatedInstitutionIds()
            .addAll(getRelatedInstitutions(document, false));
        commonExportDocument.getActivelyRelatedInstitutionIds()
            .addAll(getRelatedInstitutions(document, true));

        commonExportDocument.setOpenAccess(false);
        document.getFileItems().forEach(file -> {
            commonExportDocument.getFileFormats().add(file.getMimeType());
            if (file.getLicense().equals(License.OPEN_ACCESS)) {
                commonExportDocument.setOpenAccess(true);
            }
        });
    }

    private static ExportContribution createExportContribution(
        PersonDocumentContribution contribution) {
        var exportContribution = new ExportContribution();
        exportContribution.setDisplayName(
            contribution.getAffiliationStatement().getDisplayPersonName().toString());
        if (Objects.nonNull(contribution.getPerson())) {
            exportContribution.setPerson(
                ExportPersonConverter.toCommonExportModel(contribution.getPerson()));
        }

        return exportContribution;
    }

    private static Set<Integer> getRelatedInstitutions(Document document, boolean onlyActive) {
        var relations = new HashSet<Integer>();
        document.getContributors().forEach(contribution -> {
            if (Objects.nonNull(contribution.getPerson())) {
                relations.addAll(ExportPersonConverter.getRelatedInstitutions(
                    contribution.getPerson(), onlyActive));
            }
        });
        return relations;
    }

    public static Publication toOpenaireModel(ExportDocument exportDocument) {
        var openairePublication = new Publication();
        openairePublication.setOldId("TESLARIS(" + exportDocument.getDatabaseId() + ")");
        openairePublication.setTitle(
            ExportMultilingualContentConverter.toOpenaireModel(exportDocument.getTitle()));
        openairePublication.setSubtitle(
            ExportMultilingualContentConverter.toOpenaireModel(exportDocument.getSubtitle()));

        openairePublication.setType(inferPublicationCOARType(exportDocument.getType()));

        if (!exportDocument.getLanguageTags().isEmpty()) {
            openairePublication.setLanguage(
                exportDocument.getLanguageTags().getFirst());
        }

        setDocumentDate(exportDocument.getDocumentDate(), openairePublication::setPublicationDate);

        openairePublication.setNumber(exportDocument.getNumber());
        openairePublication.setVolume(exportDocument.getVolume());
        openairePublication.setIssue(exportDocument.getIssue());
        openairePublication.setStartPage(exportDocument.getStartPage());
        openairePublication.setEndPage(exportDocument.getEndPage());
        openairePublication.setUrl(exportDocument.getUris());
        openairePublication.setDoi(exportDocument.getDoi());
        openairePublication.setScpNumber(exportDocument.getScopus());
        openairePublication.setIssn(
            Objects.nonNull(exportDocument.getEIssn()) && !exportDocument.getEIssn().isBlank() ?
                exportDocument.getEIssn() : exportDocument.getPrintIssn());
        openairePublication.setIsbn(
            Objects.nonNull(exportDocument.getEIsbn()) && !exportDocument.getEIsbn().isBlank() ?
                exportDocument.getEIsbn() : exportDocument.getPrintIsbn());
        openairePublication.setEdition(exportDocument.getEdition());

        openairePublication.setAccess(
            exportDocument.getOpenAccess() ? "http://purl.org/coar/access_right/c_abf2" :
                "http://purl.org/coar/access_right/c_14cb");

        if (Objects.nonNull(exportDocument.getJournal())) {
            openairePublication.setPublishedIn(new PublishedIn(
                ExportDocumentConverter.toOpenaireModel(exportDocument.getJournal())));
        }

        if (Objects.nonNull(exportDocument.getProceedings())) {
            var partOf = new PartOf();
            ExportMultilingualContentConverter.setFieldFromPriorityContent(
                exportDocument.getProceedings().getTitle().stream(),
                Function.identity(),
                partOf::setDisplayName
            );
            partOf.setPublication(
                ExportDocumentConverter.toOpenaireModel(exportDocument.getProceedings()));
            openairePublication.setPartOf(partOf);
        } else if (Objects.nonNull(exportDocument.getMonograph())) {
            var partOf = new PartOf();
            ExportMultilingualContentConverter.setFieldFromPriorityContent(
                exportDocument.getMonograph().getTitle().stream(),
                Function.identity(),
                partOf::setDisplayName
            );
            partOf.setPublication(
                ExportDocumentConverter.toOpenaireModel(exportDocument.getMonograph()));
            openairePublication.setPartOf(partOf);
        }

        ExportMultilingualContentConverter.setFieldFromPriorityContent(
            exportDocument.getDescription().stream(),
            Function.identity(),
            openairePublication::set_abstract
        );

        ExportMultilingualContentConverter.setFieldFromPriorityContent(
            exportDocument.getKeywords().stream(),
            content -> List.of(content.split("\n")),
            openairePublication::setKeywords
        );

        openairePublication.setAuthors(new ArrayList<>());
        exportDocument.getAuthors().forEach(contribution -> {
            openairePublication.getAuthors().add(new PersonAttributes(contribution.getDisplayName(),
                ExportPersonConverter.toOpenaireModel(contribution.getPerson())));
        });

        openairePublication.setEditors(new ArrayList<>());
        exportDocument.getEditors().forEach(contribution -> {
            openairePublication.getEditors().add(new PersonAttributes(contribution.getDisplayName(),
                ExportPersonConverter.toOpenaireModel(contribution.getPerson())));
        });

        openairePublication.setPublishers(new ArrayList<>());
        exportDocument.getPublishers().forEach(publisher -> {
            var openairePublisher = new Publisher();
            ExportMultilingualContentConverter.setFieldFromPriorityContent(
                publisher.getName().stream(),
                Function.identity(),
                openairePublisher::setDisplayName
            );
            openairePublication.getPublishers().add(openairePublisher);
        });

        return openairePublication;
    }

    public static DC toDCModel(ExportDocument exportDocument) {
        var dcPublication = new DC();
        dcPublication.getDate().add(exportDocument.getDocumentDate());
        dcPublication.getSource().add(repositoryName);

        dcPublication.getIdentifier().add("TESLARIS(" + exportDocument.getDatabaseId() + ")");
        // TODO: support other identifiers (if applicable)

        dcPublication.getType().add("text"); // TODO: support PHD dissertations when we add them

        clientLanguages.forEach(lang -> {
            dcPublication.getIdentifier()
                .add(baseFrontendUrl + lang + "/scientific-results/" +
                    getConcreteEntityPath(exportDocument.getType()) +
                    exportDocument.getDatabaseId());
        });

        addContentToList(
            exportDocument.getTitle(),
            ExportMultilingualContent::getContent,
            content -> dcPublication.getTitle().add(content)
        );

        addContentToList(
            exportDocument.getAuthors(),
            ExportContribution::getDisplayName,
            content -> dcPublication.getCreator().add(content)
        );

        addContentToList(
            exportDocument.getEditors(),
            ExportContribution::getDisplayName,
            content -> dcPublication.getContributor().add(content)
        );

        addContentToList(
            exportDocument.getDescription(),
            ExportMultilingualContent::getContent,
            content -> dcPublication.getDescription().add(content)
        );

        addContentToList(
            exportDocument.getKeywords(),
            ExportMultilingualContent::getContent,
            content -> dcPublication.getSubject().add(content.replace("\n", "; "))
        );

        addContentToList(
            exportDocument.getLanguageTags(),
            Function.identity(),
            content -> dcPublication.getLanguage().add(content)
        );

        exportDocument.getPublishers().forEach(publisher -> {
            publisher.getName().forEach(name -> {
                dcPublication.getPublisher().add(name.getContent());
            });
        });

        addContentToList(
            exportDocument.getFileFormats(),
            Functions.identity(),
            content -> dcPublication.getFormat().add(content)
        );

        dcPublication.getRights().add(
            exportDocument.getOpenAccess() ? "info:eu-repo/semantics/openAccess" :
                "info:eu-repo/semantics/metadataOnlyAccess");
        dcPublication.getRights().add("http://creativecommons.org/publicdomain/zero/1.0/");

        return dcPublication;
    }

    private static String getConcreteEntityPath(ExportPublicationType type) {
        return switch (type) {
            case JOURNAL_PUBLICATION -> "journal-publication";
            case PROCEEDINGS -> "proceedings";
            case PROCEEDINGS_PUBLICATION -> "proceedings-publication";
            case MONOGRAPH -> "monograph";
            case PATENT -> "patent";
            case SOFTWARE -> "software";
            case DATASET -> "dataset";
            case JOURNAL -> "journal";
            case MONOGRAPH_PUBLICATION -> "monograph-publication";
        };
    }
}
