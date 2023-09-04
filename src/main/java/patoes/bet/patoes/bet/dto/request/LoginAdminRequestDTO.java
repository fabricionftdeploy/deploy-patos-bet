package patoes.bet.patoes.bet.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAdminRequestDTO {

    private String usuario;
    private String senha;
    private String senhaSistema;
}
