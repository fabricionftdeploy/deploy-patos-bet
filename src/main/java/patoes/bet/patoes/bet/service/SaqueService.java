package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.dto.request.SaqueRequestDTO;
import patoes.bet.patoes.bet.exception.RequestException;
import patoes.bet.patoes.bet.model.ChavePixModel;
import patoes.bet.patoes.bet.model.SaqueModel;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.repository.ChavePixRepository;
import patoes.bet.patoes.bet.repository.SaqueRepository;
import patoes.bet.patoes.bet.repository.UsuarioRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class SaqueService {

    @Autowired
    private SaqueRepository saqueRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ChavePixRepository chavePixRepository;


    public List<SaqueModel> listarSaques(){
        return saqueRepository.findAll();
    }

    /*public List<SaqueModel> listarDaquesPorCodigoDeUsuario(Long codigoUsuario){
        return saqueRepository.listarSaquesPorCodigoDeUsuario(codigoUsuario);
    }*/

    public SaqueModel buscarSaquePorCodigo(Long codigo){
        return  saqueRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Saque inexistente"));
    }

    public UsuarioModel solicitarSaque(SaqueRequestDTO saqueRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(saqueRequest.getCodigoUsuario());

        if(usuario.getAuditoria() > 0)
            throw new RequestException("Desculpe, você ainda não completou a auditoria!");

        if(saqueRequest.getValorSaque() > usuario.getSaldo())
            throw new RequestException("Desculpe, você não possui saldo suficiente para um saque deste valor!");

        if(saqueRequest.getValorSaque() < 60)
            throw new RequestException("Desculpe, você só pode solicitar um saque com valor igual ou superior à 60 reais!");

        ChavePixModel chavePix = buscarChavePixPorChave(saqueRequest.getChaveDestinatario());

        usuario.getHistoricoSaques().add(new SaqueModel(
            null,
            usuario.getUsuario(),
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()),
            "Solicitado",
            saqueRequest.getValorSaque(),
            chavePix.getNomeTitular(),
            chavePix.getTipoChave(),
            chavePix.getChave(),
            chavePix.getCpfTitular(),
            ""
        ));

        usuario.setSaldo(usuario.getSaldo() - saqueRequest.getValorSaque());
        usuario.setSaldoEmRetirada(usuario.getSaldoEmRetirada() + saqueRequest.getValorSaque());

        return usuarioRepository.save(usuario);
    }

    public SaqueModel autorizarSaque(SaqueModel saque){
        UsuarioModel usuario = buscarUsuarioPorUsername(saque.getUsuario());

        usuario.setSaldoEmRetirada(usuario.getSaldoEmRetirada() - saque.getValorSaque());
        saque.setStatusSaque("Concluído");

        return  saqueRepository.save(saque);
    }

    public SaqueModel recusarSaque(SaqueModel saque){
        UsuarioModel usuario = buscarUsuarioPorUsername(saque.getUsuario());

        usuario.setSaldoEmRetirada(usuario.getSaldoEmRetirada() - saque.getValorSaque());
        usuario.setSaldo(usuario.getSaldo() + saque.getValorSaque());

        saque.setStatusSaque("Recusado");
        saque.setMotivoRejeicao(saque.getMotivoRejeicao());

        return  saqueRepository.save(saque);
    }


    //Metódos privados
    private ChavePixModel buscarChavePixPorChave(String chave){
        return  chavePixRepository.findByChave(chave)
                .orElseThrow(() -> new RequestException("Chave pix inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return  usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorUsername(String username){
        return  usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }
}
