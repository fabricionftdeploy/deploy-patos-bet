package patoes.bet.patoes.bet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.dto.request.ChavePixRequestDTO;
import patoes.bet.patoes.bet.service.ChavePixService;

@RestController
@RequestMapping("/chaves")
public class ChavePixController {

    @Autowired
    private ChavePixService chavePixService;


    @GetMapping(path = "/{codigoUsuario}")
    public ResponseEntity<?> buscarChavesDeUmUsuario(@PathVariable Long codigoUsuario){
        return new ResponseEntity<>(chavePixService.buscarChavesDeUmUsuário(codigoUsuario), HttpStatus.OK);
    }

   @PostMapping
    public ResponseEntity<?> salvarChaveDeUmUsuario(@RequestBody ChavePixRequestDTO chavePixRequest){
        return new ResponseEntity<>(chavePixService.salvarChaveParaUmUSuario(chavePixRequest), HttpStatus.CREATED);
    }
}
