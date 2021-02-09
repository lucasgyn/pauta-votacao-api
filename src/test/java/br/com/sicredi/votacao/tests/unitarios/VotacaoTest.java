package br.com.sicredi.votacao.tests.unitarios;

import br.com.sicredi.votacao.entity.Votacao;
import br.com.sicredi.votacao.repository.IVotacaoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VotacaoTest {

    @Autowired
    IVotacaoRepository votacaoRepository;

    @Test
    public void devePersistirVotacao() {
        final Votacao votacao = new Votacao(null, Boolean.TRUE, 1, 1);
        this.votacaoRepository.save(votacao);
        assertThat(votacao.getId()).isNotNull();
    }

    @Test
    public void deveRetornarValorIgualUmParaConsultaTotaldeVotos() {
        final Votacao votacao = new Votacao(null, Boolean.TRUE, 1, 1);
        this.votacaoRepository.save(votacao);
        assertThat(this.votacaoRepository.countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(1, 1, Boolean.TRUE)).isEqualTo(1);
    }

    @Test
    public void deveRetornarValorIgualZeroParaConsultaTotaldeVotos() {
        final Votacao votacao = new Votacao(null, Boolean.TRUE, 1, 1);
        this.votacaoRepository.save(votacao);
        assertThat(this.votacaoRepository.countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(1, 1, Boolean.FALSE)).isEqualTo(0);
    }
}