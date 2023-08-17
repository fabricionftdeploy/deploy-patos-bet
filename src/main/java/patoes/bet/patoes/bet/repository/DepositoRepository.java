package patoes.bet.patoes.bet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patoes.bet.patoes.bet.model.DepositoModel;

import java.util.Optional;

@Repository
public interface DepositoRepository extends JpaRepository<DepositoModel, Long> {

    Optional<DepositoModel> findByCodigo(Long codigo);
}
