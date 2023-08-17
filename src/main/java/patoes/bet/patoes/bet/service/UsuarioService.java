package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.dto.request.AlterarSenhaRequestDTO;
import patoes.bet.patoes.bet.dto.request.DepositoRequestDTO;
import patoes.bet.patoes.bet.dto.request.LoginRequestDTO;
import patoes.bet.patoes.bet.dto.request.SaqueRequestDTO;
import patoes.bet.patoes.bet.exception.RequestException;
import patoes.bet.patoes.bet.model.BonusModel;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.repository.BonusRepository;
import patoes.bet.patoes.bet.repository.UsuarioRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${senha.sistema}")
    private String senhaSistema;


    public List<UsuarioModel> listarUsuarios(){
        return  usuarioRepository.findAll();
    }

    public UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return  usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    //Apagar dps
    public UsuarioModel addSaldo(Long codigo, Double valor){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        usuario.setSaldo(usuario.getSaldo() + valor);
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel salvarUsuario(UsuarioModel usuario, Integer codigoBonus){
       verificarSeOsDadosDoUsuarioNaoSeRepetem(usuario);
       if(!codigoBonus.equals(0) && codigoBonus.toString().length() == 4) {
           BonusModel bonus = verificarValidadeDoCodigoBonus(codigoBonus, "cadastro");
           usuario.setSaldo(bonus.getValorBonus());
           usuario.setAuditoria(bonus.getValorBonus() * bonus.getMultiplicadorDeAuditoria());
       }

       usuario.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
       usuario.setID(gerarIDSemRepeticao());
       usuario.setSenha(encoder.encode(usuario.getSenha()));

       return usuarioRepository.save(usuario);
    }

    public UsuarioModel fazerLogin(LoginRequestDTO loginRequest){
        if(verificarSenha(loginRequest.getUsuario(), loginRequest.getSenha())){
            return  buscarUsarioPorUsername(loginRequest.getUsuario());
        }else throw new RequestException("Senha incorreta");
    }

    public UsuarioModel alterarRoleUsuario(Long codigo, String senha){
        if(encoder.matches(senha, senhaSistema)){
            UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);
            usuario.setRole("ADMIN");
            return usuario;
        }
        throw new RequestException("Senha do sistema incorreta!");
    }

    public String alterarSenhaUsuario(AlterarSenhaRequestDTO alterarSenhaRequest){
        if(verificarSenha(alterarSenhaRequest.getUsuario(), alterarSenhaRequest.getSenha())){
            UsuarioModel usuario = buscarUsarioPorUsername(alterarSenhaRequest.getUsuario());
            usuario.setSenha(encoder.encode(alterarSenhaRequest.getNovaSenha()));
            usuarioRepository.save(usuario);
            return "Senha alterada com sucesso!";
        }else throw new RequestException("Senha atual incorreta!");
    }

    public UsuarioModel adcionarPontosDeVip(Long codigo, Double valorApostado){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        Integer pontos = (int) (valorApostado.intValue() * 0.5);

        usuario.setPontosAdquiridos(usuario.getPontosAdquiridos() + pontos);
        System.out.println("---- Inicio ---\nPontos: "+pontos+"\nNivel: "+usuario.getNivel()+"\nPontos adquiridos: "+usuario.getPontosAdquiridos()+"\nPontos necessariosa para passar: "+usuario.getPontosNecessariosParaProximoNivel());

        while(usuario.getPontosAdquiridos() >= usuario.getPontosNecessariosParaProximoNivel() && usuario.getNivel() < 100) {
            usuario.setPontosAdquiridos(usuario.getPontosAdquiridos() - usuario.getPontosNecessariosParaProximoNivel());
            usuario.setPontosNecessariosParaProximoNivel(usuario.getPontosNecessariosParaProximoNivel() + 50);
            usuario.setNivel(usuario.getNivel() + 1);

            System.out.println("\nPontos: "+pontos+"\nNivel: "+usuario.getNivel()+"\nPontos adquiridos: "+usuario.getPontosAdquiridos()+"\nPontos necessariosa para passar: "+usuario.getPontosNecessariosParaProximoNivel());
        }

        /*usuario.setNivel(1);
        usuario.setPontosAdquiridos(0);
        usuario.setPontosNecessariosParaProximoNivel(100);*/

        return usuarioRepository.save(usuario);
    }


    public String excluirUsuarios(){
        usuarioRepository.deleteAll();
        return "Usuários excluidos com sucesso!";
    }

    
    //Metódos privados
    private UsuarioModel buscarUsarioPorUsername(String usuario){
        return usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RequestException("usuário inexistente!"));
    }

    private BonusModel buscarBonusPorCodigoDeBonus(Integer codigoBonus){
        return bonusRepository.findByCodigoBonus(codigoBonus)
               .orElseThrow(() -> new RequestException("Bônus inexistente!"));
    }

    private Long gerarIDSemRepeticao(){
        Long numero = ((long)(Math.random() * 899999999) + 100000000);
        while(usuarioRepository.findByID(numero).isPresent())
            numero = ((long)(Math.random() * 899999999) + 100000000);

        return numero;
    }

    private BonusModel verificarValidadeDoCodigoBonus(Integer codigoBonus, String acao){
        BonusModel bonus = buscarBonusPorCodigoDeBonus(codigoBonus);

        if(bonus.getDataValidade().compareTo(new Date()) == -1)
            throw new RequestException("Desculpe, este código de bônus expirou! Para prosseguir altere ou remova o mesmo.");

        if(!bonus.getTipo().equals(acao))
            throw new RequestException("Desculpe, este não é um bonûs válido para "+acao+"!");

        return bonus;
    }

    private Boolean verificarSenha(String username, String senha){
        UsuarioModel usuario = buscarUsarioPorUsername(username);
        return encoder.matches(senha, usuario.getSenha());
    }

    private void verificarSeOsDadosDoUsuarioNaoSeRepetem(UsuarioModel usuario){
        if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            throw new RequestException("Descuple, este nome de usuárioo já está sendo utilizado!");

        if(usuarioRepository.findByCelular(usuario.getCelular()).isPresent())
            throw new RequestException("Descuple, este número de celular já está vinculado em outra conta!");

        if(usuarioRepository.findByCpf(usuario.getCpf()).isPresent())
            throw new RequestException("Descuple, este CPF já está vinculado em outra conta!");
    }
}