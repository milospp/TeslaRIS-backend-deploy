package rs.teslaris.core.repository.document;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.teslaris.core.model.document.Journal;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Integer> {

    @Query("select count(p) > 0 from JournalPublication p join p.journal j where j.id = :journalId")
    boolean hasPublication(Integer journalId);

    Optional<Journal> findJournalByOldId(Integer oldId);

    @Query(value = "SELECT * FROM journals j WHERE " +
        "j.last_modification >= CURRENT_TIMESTAMP - INTERVAL '1 DAY'", nativeQuery = true)
    Page<Journal> findAllModifiedInLast24Hours(Pageable pageable);
}
