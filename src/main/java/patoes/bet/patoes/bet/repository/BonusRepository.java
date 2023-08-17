package patoes.bet.patoes.bet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patoes.bet.patoes.bet.model.BonusModel;

import java.util.Optional;

@Repository
public interface BonusRepository extends JpaRepository<BonusModel, Long> {

    Optional<BonusModel> findByCodigo(Long codigo);
    Optional<BonusModel> findByCodigoBonus(Integer codigoBonus);
}
