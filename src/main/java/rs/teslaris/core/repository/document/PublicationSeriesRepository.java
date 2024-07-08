package rs.teslaris.core.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.teslaris.core.model.document.PublicationSeries;

@Repository
public interface PublicationSeriesRepository extends JpaRepository<PublicationSeries, Integer> {

    @Query("select count(p) > 0 from Proceedings p join p.publicationSeries bs where bs.id = :publicationSeriesId")
    boolean hasProceedings(Integer publicationSeriesId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
        "FROM PublicationSeries p WHERE p.eISSN = :eISSN AND p.id <> :id")
    boolean existsByeISSN(String eISSN, Integer id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
        "FROM PublicationSeries p WHERE p.printISSN = :printISSN AND p.id <> :id")
    boolean existsByPrintISSN(String printISSN, Integer id);
}
