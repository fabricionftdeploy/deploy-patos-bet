package patoes.bet.patoes.bet.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.dto.request.DepositoRequestDTO;
import patoes.bet.patoes.bet.dto.response.UsuarioResponseDTO;
import patoes.bet.patoes.bet.model.UsuarioModel;
import patoes.bet.patoes.bet.service.DepositoService;

@RestController
@RequestMapping("/depositos")
public class DepositoController {

    @Autowired
    private DepositoService depositoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarDepositoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(depositoService.buscarDepositoPorCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> solicitarDeposito(@RequestBody DepositoRequestDTO depositoRequest){
        return new ResponseEntity<>(converterEmUsuarioResponse(depositoService.depositar(depositoRequest)), HttpStatus.CREATED);
    }

    //Metódos privados
    private UsuarioResponseDTO converterEmUsuarioResponse(UsuarioModel usuario){
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }
}
