package br.com.sicredi.votacao.client;

import br.com.sicredi.votacao.exception.NotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class UserClient {

    private static final String USERS_URL = "https://user-info.herokuapp.com/users/{cpf}";
    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    public static boolean isAssociadoHabilitadoVotacao(final String cpf) {
        try {
            final ResponseEntity<String> response = new RestTemplate().getForEntity(USERS_URL, String.class, cpf);
            final JsonNode status = new ObjectMapper().readTree(response.getBody()).path("status");
            return ABLE_TO_VOTE.equals(status.textValue());
        } catch (final HttpClientErrorException | IOException ex) {
            throw new NotFoundException("Não foi possivel localizar o CPF do associado");
        }
    }
}