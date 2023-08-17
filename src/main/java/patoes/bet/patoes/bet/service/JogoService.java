package patoes.bet.patoes.bet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patoes.bet.patoes.bet.model.JogoModel;
import patoes.bet.patoes.bet.repository.JogoRepository;

import java.util.List;

@Service
public class JogoService {

    @Autowired
    private JogoRepository jogoRepository;


    public List<JogoModel> listarJogos(){
        return jogoRepository.findAll();
    }

    public JogoModel salvarJogo(JogoModel jogo){
        return jogoRepository.save(jogo);
    }

    public String excluirjogos(){
        jogoRepository.deleteAll();
        return "jogos excluiídos com sucesso!";
    }
}
