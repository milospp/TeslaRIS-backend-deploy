package rs.teslaris.core.repository.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.teslaris.core.model.user.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {

    Optional<JwtToken> findByJti(String jti);

    List<JwtToken> findByUserIdAndRevokedFalse(Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM JwtToken t WHERE t.user.id = :userId")
    void deleteForUser(Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM JwtToken t WHERE t.revoked = true OR t.expiresAt < CURRENT_TIMESTAMP")
    void deleteRevokedAndExpiredTokens();

}
