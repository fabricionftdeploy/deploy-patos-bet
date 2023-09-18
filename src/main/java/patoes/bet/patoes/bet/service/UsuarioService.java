package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.dto.request.AlterarSenhaRequestDTO;
import patoes.bet.patoes.bet.dto.request.LoginAdminRequestDTO;
import patoes.bet.patoes.bet.dto.request.LoginRequestDTO;
import patoes.bet.patoes.bet.dto.response.LoginAdminResponsetDTO;
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

    @Autowired
    private TokenService tokenService;

    @Value("${senha.sistema}")
    private String senhaSistema;

    @Value("${senha.login.admin}")
    private String senhaLoginAdmin;


    public List<UsuarioModel> listarUsuarios(){
        return  usuarioRepository.findAll();
    }

    public UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return  usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    public UsuarioModel buscarUsuarioPorID(Long id){
        return  usuarioRepository.findByID(id)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    public Double buscarSaldoDeUmUsuarioPorCodigo(Long codigo){
        return buscarUsuarioPorCodigo(codigo).getSaldo();
    }

    public UsuarioModel salvarUsuario(UsuarioModel usuario, Integer codigoBonus){
       verificarSeOsDadosDoUsuarioNaoSeRepetem(usuario);

       if(!codigoBonus.equals(0) && codigoBonus.toString().length() == 4) {
           BonusModel bonus = verificarValidadeDoCodigoBonus(codigoBonus, "cadastro");
           usuario.setSaldo(bonus.getValorBonus());
           usuario.setAuditoria(bonus.getValorBonus() * bonus.getMultiplicadorDeAuditoria());
           usuario.getBonusUsados().add(bonus);
       }

       if(!usuario.getConvite().equals(0L) && usuario.getConvite().toString().length() == 9){
           if(usuarioRepository.findByID(usuario.getConvite()).isEmpty())
               throw new RequestException("Não existe nenhum usuário com este código de convie! Altere ou apague o conteúdo deste campo para prosseguir");
       } else usuario.setConvite(0L);

       usuario.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
       usuario.setID(gerarIDSemRepeticao());
       usuario.setSenha(encoder.encode(usuario.getSenha()));

       return usuarioRepository.save(usuario);
    }

    public UsuarioModel fazerLogin(LoginRequestDTO loginRequest){
        if(verificarSenha(loginRequest.getUsuario(), loginRequest.getSenha())){
            UsuarioModel usuario = buscarUsarioPorUsername(loginRequest.getUsuario());

            if(usuario.getContaAtiva().equals(false))
                throw new RequestException("Desculpe, você não pode fazer login, sua conta foi desativada por um administrador");

            return usuario;
        }else throw new RequestException("Senha incorreta");
    }

    public LoginAdminResponsetDTO fazerLoginComoAdmin(LoginAdminRequestDTO loginAdminRequest){
        if(!encoder.matches(loginAdminRequest.getSenhaSistema(), senhaSistema))
            throw new RequestException("Senha do sistema incorreta!");

        if(verificarSenha(loginAdminRequest.getUsuario(), loginAdminRequest.getSenha())){
            UsuarioModel usuario = buscarUsarioPorUsername(loginAdminRequest.getUsuario());
            if(!usuario.getRole().equals("ROLE_ADMIN"))
                throw new RequestException("Desculpe, sua conta não possui autorização ADMIN!");
            return new LoginAdminResponsetDTO(
                usuario.getRole(),
                tokenService.gerarToken(usuario.getUsuario())
            );
        }else throw new RequestException("Senha incorreta");
    }

    public UsuarioModel alterarRoleUsuario(Long codigo, String senha){
        if(encoder.matches(senha, senhaSistema)){
            UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);
            usuario.setRole("ROLE_ADMIN");
            return usuarioRepository.save(usuario);
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

    public UsuarioModel alterarStatusUsuario(Long codigo, String acao){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        usuario.setContaAtiva((acao.equals("desativar")) ? false : true);

        return usuarioRepository.save(usuario);
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
