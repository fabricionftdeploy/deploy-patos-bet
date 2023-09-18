package patoes.bet.patoes.bet.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarUsuarios(){
        return new ResponseEntity<>(converterEmListaResponseUSuario(usuarioService.listarUsuarios()), HttpStatus.OK);
    }

    @GetMapping(path = "/porCodigo/{codigo}")
    public ResponseEntity<?> buscarUsuarioPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.buscarUsuarioPorCodigo(codigo)), HttpStatus.OK);
    }

    @GetMapping(path = "/porID/{id}")
    public ResponseEntity<?> buscarUsuarioPorID(@PathVariable Long id){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.buscarUsuarioPorID(id)), HttpStatus.OK);
    }

    @GetMapping("/saldo/{codigo}")
    public ResponseEntity<?> buscarSaldoDeUmUsuarioPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(usuarioService.buscarSaldoDeUmUsuarioPorCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> salvarUsuario(@RequestBody CadastroRequestDTO cadastroRequest){
        UsuarioModel usuario = modelMapper.map(cadastroRequest, UsuarioModel.class);
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.salvarUsuario(usuario, cadastroRequest.getCodigoBonus())), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> fazerLogin(@RequestBody LoginRequestDTO loginRequest){
        return new ResponseEntity<>(converterEmUsuarioResponse(usuarioService.fazerLogin(loginRequest)), HttpStatus.OK);
    }

    @PostMapping(path = "/loginAdmin")
    public ResponseEntity<?> fazerLoginComoAdmin(@RequestBody LoginAdminRequestDTO loginAdminRequest){
        return new ResponseEntity<>(usuarioService.fazerLoginComoAdmin(loginAdminRequest), HttpStatus.OK);
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

    @PutMapping(path = "/bloquear/{codigo}/{acao}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> alterarStatusUsuario(@PathVariable Long codigo,
                                             @PathVariable String acao){
        return new ResponseEntity<>(usuarioService.alterarStatusUsuario(codigo, acao), HttpStatus.OK);
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
