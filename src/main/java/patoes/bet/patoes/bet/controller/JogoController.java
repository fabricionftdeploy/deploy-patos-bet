package patoes.bet.patoes.bet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> buscarjogoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(jogoService.buscarjogoPorCodigo(codigo), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> salvarJogo(@RequestBody JogoModel jogo){
        return new ResponseEntity<>(jogoService.salvarJogo(jogo), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirJogos(){
        return new ResponseEntity<>(jogoService.excluirjogos(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{codigo}")
    public ResponseEntity<?> excluirJogoPorCodigo(@PathVariable Long codigo){
        return new ResponseEntity<>(jogoService.excluirjogoPorCodigo(codigo), HttpStatus.OK);
    }
}
