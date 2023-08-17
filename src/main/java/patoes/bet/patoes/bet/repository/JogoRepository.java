package patoes.bet.patoes.bet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patoes.bet.patoes.bet.model.JogoModel;

import java.util.Optional;

@Repository
public interface JogoRepository extends JpaRepository<JogoModel, Long> {

    Optional<JogoModel> findByCodigo(Long codigo);
}
