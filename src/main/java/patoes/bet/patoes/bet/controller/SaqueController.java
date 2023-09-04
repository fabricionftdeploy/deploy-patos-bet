package patoes.bet.patoes.bet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.dto.request.SaqueRequestDTO;
import patoes.bet.patoes.bet.model.SaqueModel;
import patoes.bet.patoes.bet.service.SaqueService;

@RestController
@RequestMapping("/saques")
public class SaqueController {

    @Autowired
    private SaqueService saqueService;

    @GetMapping
    public ResponseEntity<?> listarSaques(){
        return new ResponseEntity<>(saqueService.listarSaques(), HttpStatus.OK);
    }

    @GetMapping(path = "/usuario/{codigoUsuario}")
    public ResponseEntity<?> listarSaquesPorCodigoDeUsuario(@PathVariable Long codigoUsuario){
        return new ResponseEntity<>(saqueService.listarDaquesPorCodigoDeUsuario(codigoUsuario), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarSaquePorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(saqueService.buscarSaquePorCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> solicitarSaque(@RequestBody SaqueRequestDTO saqueRequest){
        return new ResponseEntity<>(saqueService.solicitarSaque(saqueRequest), HttpStatus.CREATED);
    }

    @PostMapping(path = "/autorizar")
    public ResponseEntity<?> autorizarSaque(@RequestBody SaqueModel saque){
        return new ResponseEntity<>(saqueService.autorizarSaque(saque), HttpStatus.OK);
    }

    @PostMapping(path = "/recusar")
    public ResponseEntity<?> recusarSaque(@RequestBody SaqueModel saque){
        return new ResponseEntity<>(saqueService.recusarSaque(saque), HttpStatus.OK);
    }
}
