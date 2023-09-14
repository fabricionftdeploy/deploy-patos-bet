package patoes.bet.patoes.bet.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.dto.request.DepositoRequestDTO;
import patoes.bet.patoes.bet.dto.response.UsuarioResponseDTO;
import patoes.bet.patoes.bet.model.DepositoModel;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.service.DepositoService;

@RestController
@RequestMapping("/depositos")
public class DepositoController {

    @Autowired
    private DepositoService depositoService;

    @Autowired
    private ModelMapper modelMapper;



    //Teste
    @GetMapping(path = "/teste/{codigo}")
    public ResponseEntity<?> teste(@PathVariable Long codigo){
        return new ResponseEntity<>(depositoService.teste(codigo), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarDepositos(){
        return new ResponseEntity<>(depositoService.listarDepositos(), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarDepositoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(depositoService.buscarDepositoPorCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> solicitarDeposito(@RequestBody DepositoRequestDTO depositoRequest){
        return new ResponseEntity<>(converterEmUsuarioResponse(depositoService.solicitarDeposito(depositoRequest)), HttpStatus.CREATED);
    }

    @PostMapping(path = "/autorizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> autorizarDeposito(@RequestBody DepositoModel deposito){
        return new ResponseEntity<>(depositoService.autorizarDeposito(deposito), HttpStatus.OK);
    }

    @PostMapping(path = "/recusar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> recusarDeposito(@RequestBody DepositoModel deposito){
        return new ResponseEntity<>(depositoService.recusarDeposito(deposito), HttpStatus.OK);
    }

    //Metódos privados
    private UsuarioResponseDTO converterEmUsuarioResponse(UsuarioModel usuario){
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }
}
