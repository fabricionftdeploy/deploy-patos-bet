package patoes.bet.patoes.bet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import patoes.bet.patoes.bet.model.SaqueModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaqueRepository extends JpaRepository<SaqueModel, Long> {

    Optional<SaqueModel> findByCodigo(Long codigo);

    @Query(value = "select u.historicoSaques from Usuario u where u.codigo = :codigoUsuario")
    List<SaqueModel> listarSaquesPorCodigoDeUsuario(Long codigoUsuario);
}
