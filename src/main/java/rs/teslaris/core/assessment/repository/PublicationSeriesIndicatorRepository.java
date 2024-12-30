package rs.teslaris.core.assessment.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.teslaris.core.assessment.model.EntityIndicatorSource;
import rs.teslaris.core.assessment.model.PublicationSeriesIndicator;
import rs.teslaris.core.model.commontypes.AccessLevel;

@Repository
public interface PublicationSeriesIndicatorRepository extends
    JpaRepository<PublicationSeriesIndicator, Integer> {

    @Query("SELECT psi FROM PublicationSeriesIndicator psi " +
        "WHERE psi.publicationSeries.id = :publicationSeriesId AND " +
        "psi.indicator.accessLevel <= :accessLevel")
    List<PublicationSeriesIndicator> findIndicatorsForPublicationSeriesAndIndicatorAccessLevel(
        Integer publicationSeriesId,
        AccessLevel accessLevel);

    @Query("SELECT ps " +
        "FROM PublicationSeriesIndicator ps " +
        "WHERE ps.publicationSeries.id = :publicationSeriesId " +
        "AND ps.indicator.code = :indicatorCode " +
        "AND ps.source = :source " +
        "AND ps.fromDate = :date " +
        "AND (ps.categoryIdentifier = :category OR (:category IS NULL AND ps.categoryIdentifier IS NULL))")
    Optional<PublicationSeriesIndicator> existsByPublicationSeriesIdAndSourceAndYearAndCategory(
        Integer publicationSeriesId,
        EntityIndicatorSource source,
        LocalDate date, String category,
        String indicatorCode);

}
