package rs.teslaris.core.service;

import org.springframework.stereotype.Service;
import rs.teslaris.core.model.document.Journal;

@Service
public interface JournalService extends JPAService<Journal> {

    Journal findJournalById(Integer journalId);
}
