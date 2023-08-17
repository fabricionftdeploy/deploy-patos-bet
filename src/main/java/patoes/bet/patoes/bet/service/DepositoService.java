package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.dto.request.DepositoRequestDTO;
import patoes.bet.patoes.bet.exception.RequestException;
import patoes.bet.patoes.bet.model.BonusModel;
import patoes.bet.patoes.bet.model.DepositoModel;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.repository.BonusRepository;
import patoes.bet.patoes.bet.repository.DepositoRepository;
import patoes.bet.patoes.bet.repository.UsuarioRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class DepositoService {

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BonusRepository bonusRepository;

    public DepositoModel buscarDepositoPorCodigo(Long codigo){
        return depositoRepository.findByCodigo(codigo)
               .orElseThrow(() -> new RequestException("Depósito inexistente!"));
    }

    public UsuarioModel depositar(DepositoRequestDTO depositoRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(depositoRequest.getCodigoUsuario());
        Double valorComBonus = 0.0;
        Double auditoria = 0.0;
        Integer multiplicadorAuditoria = 1;

        if(!depositoRequest.getCodigoBonus().equals(0) && depositoRequest.getCodigoBonus().toString().length() == 4) {
            BonusModel bonus = verificarValidadeDoCodigoBonus(depositoRequest.getCodigoBonus(), "deposito");
            valorComBonus = depositoRequest.getValorDeposito() + (depositoRequest.getValorDeposito() * bonus.getPercentualBonus() / 100);
            auditoria = valorComBonus * bonus.getMultiplicadorDeAuditoria();
            multiplicadorAuditoria = bonus.getMultiplicadorDeAuditoria();
        }else {
            valorComBonus = depositoRequest.getValorDeposito();
            auditoria = depositoRequest.getValorDeposito();
        }

        usuario.getHistoricoDepositos().add(new DepositoModel(
            null,
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()),
            "Solicitado",
            depositoRequest.getValorDeposito(),
            valorComBonus,
            auditoria,
            multiplicadorAuditoria,
            depositoRequest.getCodigoBonus()
        ));

        return usuarioRepository.save(usuario);
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return  usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private BonusModel buscarBonusPorCodigoDeBonus(Integer codigoBonus){
        return bonusRepository.findByCodigoBonus(codigoBonus)
                .orElseThrow(() -> new RequestException("Bônus inexistente!"));
    }

    private BonusModel verificarValidadeDoCodigoBonus(Integer codigoBonus, String acao){
        BonusModel bonus = buscarBonusPorCodigoDeBonus(codigoBonus);

        if(bonus.getDataValidade().compareTo(new Date()) == -1)
            throw new RequestException("Desculpe, este código de bônus expirou! Para prosseguir altere ou remova o mesmo.");

        if(!bonus.getTipo().equals(acao))
            throw new RequestException("Desculpe, este não é um bonûs válido para "+acao+"!");

        return bonus;
    }
}
