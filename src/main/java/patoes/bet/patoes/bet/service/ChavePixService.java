package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.dto.request.ChavePixRequestDTO;
import patoes.bet.patoes.bet.exception.RequestException;
import patoes.bet.patoes.bet.model.ChavePixModel;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.repository.ChavePixRepository;
import patoes.bet.patoes.bet.repository.UsuarioRepository;

import java.util.List;

@Service
public class ChavePixService {

    @Autowired
    private ChavePixRepository chavePixRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<ChavePixModel> buscarChavesDeUmUsuário(Long codigoUsuario){
        return  chavePixRepository.buscarChavesPorCodigoDeUsuario(codigoUsuario);
    }

    public UsuarioModel salvarChaveParaUmUSuario(ChavePixRequestDTO chavePixRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(chavePixRequest.getCodigoUsuario());

        if(chavePixRepository.findByChave(chavePixRequest.getChave()).isPresent())
            throw new RequestException("Desculpe, outro usuário já vinculou esta chave");

        if(usuarioRepository.buscarUsuarioPorCPFDeChavePix(chavePixRequest.getCpfTitular()).isPresent())
            throw new RequestException("Desculpe, outro usuário já vinculou uma chave deste mesmo titular!");

        if(usuario.getChaves().size() == 3)
            throw new RequestException("Desculpe, você só pode cadastrar 3 chaves!");

        usuario.getChaves().add(new ChavePixModel(
            null,
            chavePixRequest.getNomeTitular(),
            chavePixRequest.getTipoChave(),
            chavePixRequest.getChave(),
            chavePixRequest.getCpfTitular()
        ));

        return usuarioRepository.save(usuario);
    }

    //Metódos privados
    public UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return  usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente"));
    }
}
