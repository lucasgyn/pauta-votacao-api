package br.com.sicredi.votacao.tests.integracao;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.entity.Pauta;
import br.com.sicredi.votacao.repository.IPautaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PautaControllerTest {

    private static final String PAUTAS_URI = "/api/v1/pautas";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IPautaRepository repository;

    @Test
    public void deveraCadastrarUmaNovaPauta() {
        final ResponseEntity<PautaDTO> responseEntity = restTemplate.postForEntity(PAUTAS_URI, new PautaDTO(null, "Teste Cadastro Pauta"), PautaDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void deveraConsultarUmaPautaJaCadastra() {
        final Pauta pauta = new Pauta(null, "Teste Pauta 1");
        repository.save(pauta);
        final ResponseEntity<PautaDTO> responseEntity = restTemplate.getForEntity(PAUTAS_URI + "?id={id}", PautaDTO.class, 1);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getDescricao()).isEqualTo(pauta.getDescricao());
    }

    @Test
    public void deveraCadastrarUmaPautaComValoresNULL_QuandoRetornaErro() {
        final ResponseEntity<PautaDTO> responseEntity = restTemplate.postForEntity(PAUTAS_URI, new PautaDTO(null, null), PautaDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deveraConsultarUmaPautaNaoExistente_QuandoRetornaErro() {
        final Pauta pauta = new Pauta(null, "Teste Pauta 1");
        repository.save(pauta);
        final ResponseEntity<PautaDTO> responseEntity = restTemplate.getForEntity(PAUTAS_URI + "?id={id}", PautaDTO.class, 2);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getDescricao()).isNotEqualTo(pauta.getDescricao());
    }
}