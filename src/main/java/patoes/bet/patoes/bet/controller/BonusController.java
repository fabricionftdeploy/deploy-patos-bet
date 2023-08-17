package patoes.bet.patoes.bet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patoes.bet.patoes.bet.dto.request.BonusRequestDTO;
import patoes.bet.patoes.bet.service.BonusService;

@RestController
@RequestMapping("/bonus")
public class BonusController {

    @Autowired
    private BonusService bonusService;

    @GetMapping
    public ResponseEntity<?> listarBonus(){
        return  new ResponseEntity<>(bonusService.listarBonus(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> criarBonus(@RequestBody BonusRequestDTO bonusRequest){
        return new ResponseEntity<>(bonusService.criarBonus(bonusRequest), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirBonus(){
        return new ResponseEntity<>(bonusService.excluirTodosBonus(), HttpStatus.OK);
    }
}
