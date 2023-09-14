package patoes.bet.patoes.bet.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroRequestDTO {

    private String nomeCompleto;
    private String usuario;
    private String senha;
    private String celular;
    private String cpf;
    private Integer codigoBonus;
    private Long convite;
}
