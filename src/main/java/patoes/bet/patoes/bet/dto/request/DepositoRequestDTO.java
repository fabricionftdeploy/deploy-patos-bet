package patoes.bet.patoes.bet.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositoRequestDTO {

    private Long codigoUsuario;
    private Double valorDeposito;
    private Integer codigoBonus;
}
