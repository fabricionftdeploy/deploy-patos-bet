package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.dto.request.BonusRequestDTO;
import patoes.bet.patoes.bet.exception.RequestException;
import patoes.bet.patoes.bet.model.BonusModel;
import patoes.bet.patoes.bet.repository.BonusRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BonusService {

    @Autowired
    private BonusRepository bonusRepository;

    public List<BonusModel> listarBonus(){
        return  bonusRepository.findAll();
    }

    public BonusModel criarBonus(BonusRequestDTO bonusRequest){
        Date data;

        try {
            data = new SimpleDateFormat("yyyy-MM-dd").parse(bonusRequest.getDataValidade());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        BonusModel bonus = new BonusModel(
            null,
            gerarCodigoBonusSemRepeticao(),
            bonusRequest.getTipo(),
            (bonusRequest.getTipo().equals("cadastro")) ? bonusRequest.getValorBonus() : 0,
            (bonusRequest.getTipo().equals("deposito")) ? bonusRequest.getPercentualBonus() : 0,
            bonusRequest.getMultiplicadorDeAuditoria(),
            data
        );

        return bonusRepository.save(bonus);
    }

    public String excluirTodosBonus(){
        bonusRepository.deleteAll();
        return "Todos bonûs foram excluídos!";
    }

    //Métodos privados
    private Integer gerarCodigoBonusSemRepeticao(){
        Integer numero = ((int)(Math.random() * 8999) + 1000);
        while(bonusRepository.findByCodigoBonus(numero).isPresent())
            numero = ((int)(Math.random() * 8999) + 1000);

        return numero;
    }

}
