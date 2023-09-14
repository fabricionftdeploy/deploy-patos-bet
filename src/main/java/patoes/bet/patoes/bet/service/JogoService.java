package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.exception.RequestException;
import patoes.bet.patoes.bet.model.JogoModel;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.repository.JogoRepository;
import patoes.bet.patoes.bet.repository.UsuarioRepository;

import java.util.List;

@Service
public class JogoService {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<JogoModel> listarJogos(){
        return jogoRepository.findAll();
    }

    public JogoModel buscarjogoPorCodigo(Long codigo){
        return  jogoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("jogo inexistente"));
    }

    public JogoModel salvarJogo(JogoModel jogo){
        return jogoRepository.save(jogo);
    }

    public String executarJogada(Long codigo, Double bet){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        if(bet < 1)
            throw new RequestException("Desculpe, a aposta mínima é de ao menos 1 real!");

        if(usuario.getSaldo() < bet)
            throw new RequestException("Desculpe, você não possui saldo suficiente para uma aposta deste valor!");

        usuario.setSaldo(usuario.getSaldo() - bet);
        if(bet >= usuario.getAuditoria()) usuario.setAuditoria(0.0);
        else usuario.setAuditoria(usuario.getAuditoria() - bet);

        adcionarPontosDeVip(codigo, bet);

        usuarioRepository.save(usuario);

        return "Apostou";
    }

    public String encerraeJogada(Long codigo, Double lucro){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);
        usuario.setSaldo(usuario.getSaldo() + lucro);

        usuarioRepository.save(usuario);

        return "Finalizado";
    }

    public String excluirjogos(){
        jogoRepository.deleteAll();
        return "jogos excluiídos com sucesso!";
    }

    public String excluirjogoPorCodigo(Long codigo){
        JogoModel jogo = buscarjogoPorCodigo(codigo);

        jogoRepository.delete(jogo);
        return "jogo excluiído com sucesso!";
    }


    //Métodos provados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return  usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private UsuarioModel adcionarPontosDeVip(Long codigo, Double valorApostado){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        Integer pontos = (int) (valorApostado.intValue() * 0.5);
        usuario.setPontosAdquiridos(usuario.getPontosAdquiridos() + pontos);

        while(usuario.getPontosAdquiridos() >= usuario.getPontosNecessariosParaProximoNivel() && usuario.getNivel() < 100) {
            usuario.setPontosAdquiridos(usuario.getPontosAdquiridos() - usuario.getPontosNecessariosParaProximoNivel());
            usuario.setPontosNecessariosParaProximoNivel(usuario.getPontosNecessariosParaProximoNivel() + 50);
            usuario.setNivel(usuario.getNivel() + 1);
        }

        return usuarioRepository.save(usuario);
    }
}
