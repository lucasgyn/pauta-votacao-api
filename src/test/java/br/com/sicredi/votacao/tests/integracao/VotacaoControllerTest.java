package br.com.sicredi.votacao.tests.integracao;

import br.com.sicredi.votacao.dto.ResultadoDTO;
import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.entity.Associado;
import br.com.sicredi.votacao.entity.Pauta;
import br.com.sicredi.votacao.entity.SessaoVotacao;
import br.com.sicredi.votacao.entity.Votacao;
import br.com.sicredi.votacao.repository.IAssociadoRepository;
import br.com.sicredi.votacao.repository.IPautaRepository;
import br.com.sicredi.votacao.repository.ISessaoVotacaoRepository;
import br.com.sicredi.votacao.repository.IVotacaoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VotacaoControllerTest {

    private static final String VOTACOES_URI = "/api/v1/votacoes";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IVotacaoRepository votacaoRepository;

    @Autowired
    private ISessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private IAssociadoRepository associadoRepository;

    @Autowired
    private IPautaRepository pautaRepository;

    @Test
    public void deveraRealizarVoto_quandoRetornaSucesso() {
        this.pautaRepository.deleteAll();
        this.sessaoVotacaoRepository.deleteAll();
        this.associadoRepository.deleteAll();

        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        sessaoVotacao = this.sessaoVotacaoRepository.save(sessaoVotacao);

        final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(VOTACOES_URI + "/votar",
                new VotoDTO(pauta.getId(),
                        sessaoVotacao.getId(),
                        Boolean.TRUE, "37597931000"),
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo("Voto validado.");
    }

    @Test
    public void deveraRetornarResultadoVotacao_quandoRetornaSucesso() {
        this.pautaRepository.deleteAll();
        this.sessaoVotacaoRepository.deleteAll();

        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        sessaoVotacao = this.sessaoVotacaoRepository.save(sessaoVotacao);

        this.votacaoRepository.save(new Votacao(null, Boolean.TRUE, pauta.getId(), sessaoVotacao.getId()));
        this.votacaoRepository.save(new Votacao(null, Boolean.TRUE, pauta.getId(), sessaoVotacao.getId()));
        this.votacaoRepository.save(new Votacao(null, Boolean.TRUE, pauta.getId(), sessaoVotacao.getId()));
        this.votacaoRepository.save(new Votacao(null, Boolean.TRUE, pauta.getId(), sessaoVotacao.getId()));
        this.votacaoRepository.save(new Votacao(null, Boolean.FALSE, pauta.getId(), sessaoVotacao.getId()));
        this.votacaoRepository.save(new Votacao(null, Boolean.FALSE, pauta.getId(), sessaoVotacao.getId()));
        this.votacaoRepository.save(new Votacao(null, Boolean.FALSE, pauta.getId(), sessaoVotacao.getId()));

        sessaoVotacao = new SessaoVotacao(sessaoVotacao.getId(), sessaoVotacao.getDataHoraInicio(), sessaoVotacao.getDataHoraFim(), Boolean.FALSE);
        this.sessaoVotacaoRepository.save(sessaoVotacao);

        final ResponseEntity<ResultadoDTO> responseEntity = this.restTemplate.getForEntity(VOTACOES_URI + "/resultado?idPauta={idPauta}&idSessaoVotacao={idSessaoVotacao}",
                ResultadoDTO.class,
                pauta.getId(),
                sessaoVotacao.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getVotacaoDTO().getIdSessaoVotacao()).isEqualTo(sessaoVotacao.getId());
        assertThat(responseEntity.getBody().getVotacaoDTO().getQuantidadeVotosSim()).isEqualTo(4);
        assertThat(responseEntity.getBody().getVotacaoDTO().getQuantidadeVotosNao()).isEqualTo(3);
    }

    @Test
    public void deveraRealizarVoto_quandoRetornaErro_cpfAssociadoInvalido() {
        this.pautaRepository.deleteAll();
        this.sessaoVotacaoRepository.deleteAll();
        this.associadoRepository.deleteAll();

        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        sessaoVotacao = this.sessaoVotacaoRepository.save(sessaoVotacao);

        final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(VOTACOES_URI + "/votar",
                new VotoDTO(pauta.getId(),
                        sessaoVotacao.getId(),
                        Boolean.TRUE, "123"),
                String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deveraRealizarVoto_quandoRetornaErro_sessaoEncerrada() {
        this.pautaRepository.deleteAll();
        this.sessaoVotacaoRepository.deleteAll();
        this.associadoRepository.deleteAll();

        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now(), Boolean.FALSE);
        sessaoVotacao = this.sessaoVotacaoRepository.save(sessaoVotacao);

        final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(VOTACOES_URI + "/votar",
                new VotoDTO(pauta.getId(),
                        sessaoVotacao.getId(),
                        Boolean.TRUE,
                        "37597931000"),
                String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    }

    @Test
    public void deveraRealizarVoto_quandoRetornaErro_400_quandoAssociado_votouNovamente() {
        this.pautaRepository.deleteAll();
        this.sessaoVotacaoRepository.deleteAll();
        this.associadoRepository.deleteAll();

        Pauta pauta = new Pauta(null, "Teste Pauta 1");
        pauta = this.pautaRepository.save(pauta);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        sessaoVotacao = this.sessaoVotacaoRepository.save(sessaoVotacao);

        final Associado associado = new Associado(null, "37597931000", pauta.getId());
        this.associadoRepository.save(associado);

        final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(VOTACOES_URI + "/votar",
                new VotoDTO(pauta.getId(),
                        sessaoVotacao.getId(),
                        Boolean.TRUE,
                        "37597931000"),
                String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}