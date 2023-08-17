package patoes.bet.patoes.bet.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BonusRequestDTO {

    private String tipo;
    private Double valorBonus;
    private Integer percentualBonus;
    private Integer multiplicadorDeAuditoria;
    private String dataValidade;
}
