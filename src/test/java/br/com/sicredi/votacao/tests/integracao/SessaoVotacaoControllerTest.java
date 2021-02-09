package br.com.sicredi.votacao.tests.integracao;

import br.com.sicredi.votacao.dto.SessaoVotacaoAbrirDTO;
import br.com.sicredi.votacao.dto.SessaoVotacaoDTO;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessaoVotacaoControllerTest {

    private static final String SESSOES_VOTACAO_URI = "/api/v1/sessoes-votacao";
    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IPautaRepository pautaRepository;

    @Test
    public void deveraCadastrarUmaSessaoVotacao_quandoRetornaSucesso() {
        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO = new SessaoVotacaoAbrirDTO(pauta.getId(), null);
        final ResponseEntity<SessaoVotacaoDTO> responseEntity = this.restTemplate.postForEntity(SESSOES_VOTACAO_URI + "/abrir-sessao", sessaoVotacaoAbrirDTO, SessaoVotacaoDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getAtiva()).isTrue();
        assertThat(responseEntity.getBody().getDataHoraInicio().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter));
        assertThat(responseEntity.getBody().getDataHoraFim().format(formatter)).isEqualTo(LocalDateTime.now().plusMinutes(1).format(formatter));
    }

    @Test
    public void deveraCadastrarUmaSessaoVotacao_quandoRetornaSucesso_e_tempoSessao10_minutos() {
        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO = new SessaoVotacaoAbrirDTO(pauta.getId(), 10);
        final ResponseEntity<SessaoVotacaoDTO> responseEntity = this.restTemplate.postForEntity(SESSOES_VOTACAO_URI + "/abrir-sessao", sessaoVotacaoAbrirDTO, SessaoVotacaoDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getAtiva()).isTrue();
        assertThat(responseEntity.getBody().getDataHoraInicio().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter));
        assertThat(responseEntity.getBody().getDataHoraFim().format(formatter)).isEqualTo(LocalDateTime.now().plusMinutes(10).format(formatter));
    }

    @Test
    public void deveraCadastrarUmaSessaoVotacao_quandoRetornaErro_404_quandoPauta_idNaoLocalizado() {
        final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO = new SessaoVotacaoAbrirDTO(10, null);
        final ResponseEntity<SessaoVotacaoDTO> responseEntity = this.restTemplate.postForEntity(SESSOES_VOTACAO_URI + "/abrir-sessao", sessaoVotacaoAbrirDTO, SessaoVotacaoDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deveraCadastrarUmaSessaoVotacao_quandoRetornaErro_400_quandoPauta_null() {
        final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO = new SessaoVotacaoAbrirDTO(null, null);
        final ResponseEntity<SessaoVotacaoDTO> responseEntity = this.restTemplate.postForEntity(SESSOES_VOTACAO_URI + "/abrir-sessao", sessaoVotacaoAbrirDTO, SessaoVotacaoDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}