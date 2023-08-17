package patoes.bet.patoes.bet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import patoes.bet.patoes.bet.model.ChavePixModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePixModel, Long> {

    Optional<ChavePixModel> findByChave(String chave);

    @Query(value = "select u.chaves from Usuario u where u.codigo = :codigoUsuario")
    List<ChavePixModel> buscarChavesPorCodigoDeUsuario(Long codigoUsuario);
}
