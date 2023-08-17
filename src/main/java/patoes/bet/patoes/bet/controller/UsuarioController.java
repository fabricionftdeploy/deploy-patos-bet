package patoes.bet.patoes.bet.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.dto.request.*;
import patoes.bet.patoes.bet.dto.response.UsuarioResponseDTO;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    //Metódos de teste
    @PostMapping(path = "/teste/{codigo}/{valor}")
    public ResponseEntity<?> addSaldo(@PathVariable Long codigo,
                                      @PathVariable Double valor){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.addSaldo(codigo, valor)), HttpStatus.CREATED);
    }

    @PostMapping(path = "/pontos/{codigo}/{valor}")
    public ResponseEntity<?> adcionarPontos(@PathVariable Long codigo,
                                            @PathVariable Double valor){
        return new ResponseEntity<>(usuarioService.adcionarPontosDeVip(codigo, valor), HttpStatus.OK);
    }
    /*----------------------------------------------*/


    @GetMapping
    public ResponseEntity<?> listarUsuarios(){
        return new ResponseEntity<>(converterEmListaResponseUSuario(usuarioService.listarUsuarios()), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarUsuarioPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.buscarUsuarioPorCodigo(codigo)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> salvarUsuario(@RequestBody CadastroRequestDTO cadastroRequest){
        UsuarioModel usuario = modelMapper.map(cadastroRequest, UsuarioModel.class);
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.salvarUsuario(usuario, cadastroRequest.getCodigoBonus())), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> salvarUsuario(@RequestBody LoginRequestDTO loginRequest){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.fazerLogin(loginRequest)), HttpStatus.OK);
    }

    @PutMapping(path = "/alterarRole/{codigo}/{senha}")
    public ResponseEntity<?> alterarRoleUsuario(@PathVariable Long codigo,
                                                @PathVariable String senha){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.alterarRoleUsuario(codigo, senha)), HttpStatus.OK);
    }

    @PutMapping(path = "/alterarSenha")
    public ResponseEntity<?> alterarSenhaUsuario(@RequestBody AlterarSenhaRequestDTO alterarSenhaRequest){
        return new ResponseEntity<>(usuarioService.alterarSenhaUsuario(alterarSenhaRequest), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirUsuarios(){
        return new ResponseEntity<>(usuarioService.excluirUsuarios(), HttpStatus.OK);
    }

    //Metódos privados
    private UsuarioResponseDTO converterEmUsuarioResponse(UsuarioModel usuario){
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    private List<UsuarioResponseDTO> converterEmListaResponseUSuario(List<UsuarioModel> usuarios){
        List<UsuarioResponseDTO> usuariosResonse = new ArrayList<>();
        for(UsuarioModel usuario: usuarioService.listarUsuarios()){
            usuariosResonse.add(modelMapper.map(usuario, UsuarioResponseDTO.class));
        }
        return  usuariosResonse;
    }
}