package br.com.sicredi.votacao.tests.unitarios;

import br.com.sicredi.votacao.entity.SessaoVotacao;
import br.com.sicredi.votacao.repository.ISessaoVotacaoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SessaoVotacaoTest {

    @Autowired
    private ISessaoVotacaoRepository sessaoVotacaoRepository;

    @Test
    public void devePersistirSessaoVotacao() {
        final SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        this.sessaoVotacaoRepository.save(sessaoVotacao);
        assertThat(sessaoVotacao.getId()).isNotNull();
    }

    @Test
    public void deveRetornarVerdadeiroParaListaDeSessoesVotacaoEmAndamento() {
        final SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        this.sessaoVotacaoRepository.save(sessaoVotacao);
        assertThat(this.sessaoVotacaoRepository.buscarTodasSessoesEmAndamento(Boolean.TRUE)).isNotEmpty();
    }

    @Test
    public void deveRetornarVerdadeiroParaListaVaziaDeSessoesVotacaoEmAndamento() {
        final SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.FALSE);
        this.sessaoVotacaoRepository.save(sessaoVotacao);
        assertThat(this.sessaoVotacaoRepository.buscarTodasSessoesEmAndamento(Boolean.TRUE)).isEmpty();
    }

    @Test
    public void deveRetornarVerdadeiroParaBuscaSessaoExistenteAtiva() {
        final SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.TRUE);
        this.sessaoVotacaoRepository.save(sessaoVotacao);
        assertThat(this.sessaoVotacaoRepository.existsByIdAndAtiva(sessaoVotacao.getId(), Boolean.TRUE)).isTrue();
    }

    @Test
    public void deveRetornarFalseParaBuscaSessaoExistenteAtiva() {
        this.sessaoVotacaoRepository.deleteAll();
        final SessaoVotacao sessaoVotacao = new SessaoVotacao(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), Boolean.FALSE);
        this.sessaoVotacaoRepository.save(sessaoVotacao);
        assertThat(this.sessaoVotacaoRepository.existsByIdAndAtiva(sessaoVotacao.getId(), Boolean.TRUE)).isFalse();
    }
}
