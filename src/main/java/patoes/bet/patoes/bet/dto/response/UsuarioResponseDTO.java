package patoes.bet.patoes.bet.dto.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import patoes.bet.patoes.bet.model.ChavePixModel;
import patoes.bet.patoes.bet.model.DepositoModel;
import patoes.bet.patoes.bet.model.SaqueModel;

import java.util.List;

@Getter
@Setter
public class UsuarioResponseDTO {

    private Long codigo;
    private Long ID;
    private String role;
    private String dataCadastro;
    private Boolean contaAtiva;
    private String nomeCompleto;
    private String usuario;
    private String celular;
    private String cpf;
    private Double saldo;
    private Double saldoEmRetirada;
    private Double auditoria;
    private Integer nivel;
    private Integer pontosAdquiridos = 0;
    private Integer pontosNecessariosParaProximoNivel = 100;
    private List<ChavePixModel> chaves;
    private List<DepositoModel> historicoDepositos;
    private List<SaqueModel> historicoSaques;
}
