package patoes.bet.patoes.bet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "usuarios")
@Getter
@Setter
@Entity(name = "Usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Long ID;
    private String role = "USER";
    private String dataCadastro;
    private Boolean contaAtiva = true;
    private String nomeCompleto;
    private String usuario;
    private String senha;
    private String celular;
    private String cpf;
    private Double saldo = 0.0;
    private Double saldoEmRetirada = 0.0;
    private Double auditoria = 0.0;
    private Integer nivel = 1;
    private Integer pontosAdquiridos = 0;
    private Integer pontosNecessariosParaProximoNivel = 100;
    private  String senhaSaque = "";

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chavcs_id")
    private List<ChavePixModel> chaves = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "depositos_id")
    private List<DepositoModel> historicoDepositos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "saques_id")
    private List<SaqueModel> historicoSaques = new ArrayList<>();
}
