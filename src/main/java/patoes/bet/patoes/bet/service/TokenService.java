package patoes.bet.patoes.bet.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String gerarToken(String usuario){
        return JWT.create()
                .withIssuer("Produtos")
                .withSubject(usuario)
                .withExpiresAt(LocalDateTime.now()
                        .plusDays(1)
                        .toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256("pretinha"));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256("pretinha"))
                .withIssuer("Produtos")
                .build().verify(token).getSubject();
    }
}
