package patoes.bet.patoes.bet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.model.JogoModel;
import patoes.bet.patoes.bet.service.JogoService;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    @Autowired
    private JogoService jogoService;

    @GetMapping
    public ResponseEntity<?> listarJogos(){
        return new ResponseEntity<>(jogoService.listarJogos(), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarjogoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(jogoService.buscarjogoPorCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> salvarJogo(@RequestBody JogoModel jogo){
        return new ResponseEntity<>(jogoService.salvarJogo(jogo), HttpStatus.CREATED);
    }

    @PostMapping(path = "/solicitar/usuario/{codigo}/bet/{bet}")
    public ResponseEntity<?> executarJogada(@PathVariable Long codigo,
                                             @PathVariable Double bet){
        return new ResponseEntity<>(jogoService.executarJogada(codigo, bet), HttpStatus.OK);
    }

    @PostMapping(path = "/encerrar/usuario/{codigo}/lucro/{lucro}")
    public ResponseEntity<?> encerraeJogada(@PathVariable Long codigo,
                                            @PathVariable Double lucro){
        return new ResponseEntity<>(jogoService.encerraeJogada(codigo, lucro), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluirJogos(){
        return new ResponseEntity<>(jogoService.excluirjogos(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluirJogoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(jogoService.excluirjogoPorCodigo(codigo), HttpStatus.OK);
    }
}
