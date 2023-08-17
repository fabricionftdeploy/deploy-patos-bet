package patoes.bet.patoes.bet.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaqueRequestDTO {

    private Long codigoUsuario;
    private Double valorSaque;
    private String chaveDestinatario;
}
