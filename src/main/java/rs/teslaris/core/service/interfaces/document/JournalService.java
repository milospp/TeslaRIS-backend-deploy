package rs.teslaris.core.service.interfaces.document;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.teslaris.core.dto.document.JournalBasicAdditionDTO;
import rs.teslaris.core.dto.document.JournalResponseDTO;
import rs.teslaris.core.dto.document.PublicationSeriesDTO;
import rs.teslaris.core.indexmodel.JournalIndex;
import rs.teslaris.core.model.commontypes.LanguageTag;
import rs.teslaris.core.model.document.Journal;
import rs.teslaris.core.model.document.PublicationSeries;

@Service
public interface JournalService {

    Page<JournalResponseDTO> readAllJournals(Pageable pageable);

    Page<JournalIndex> searchJournals(List<String> tokens, Pageable pageable);

    Journal findJournalByJournalName(String journalName, LanguageTag defaultLanguage,
                                     String eIssn, String printIssn);

    PublicationSeries findOrCreatePublicationSeries(String[] line,
                                                    String defaultLanguageTag,
                                                    String journalName,
                                                    String eIssn, String printIssn,
                                                    boolean issnSpecified);

    JournalResponseDTO readJournal(Integer journalId);

    JournalIndex readJournalByIssn(String eIssn, String printIssn);

    Journal findJournalById(Integer journalId);

    Optional<Journal> tryToFindById(Integer journalId);

    Journal findJournalByOldId(Integer journalId);

    Journal createJournal(PublicationSeriesDTO journalDTO, Boolean index);

    Journal createJournal(JournalBasicAdditionDTO journalDTO);

    void updateJournal(Integer journalId, PublicationSeriesDTO journalDTO);

    void deleteJournal(Integer journalId);

    void forceDeleteJournal(Integer journalId);

    void reindexJournals();

    void indexJournal(Journal journal, JournalIndex index);
}
