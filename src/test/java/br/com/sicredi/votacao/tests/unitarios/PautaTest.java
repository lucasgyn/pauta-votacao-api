package br.com.sicredi.votacao.tests.unitarios;

import br.com.sicredi.votacao.entity.Pauta;
import br.com.sicredi.votacao.repository.IPautaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PautaTest {

    @Autowired
    private IPautaRepository pautaRepository;

    @Test
    public void devePersistirPauta() {
        final Pauta pauta = new Pauta(null, "Pauta Teste Descricao");
        this.pautaRepository.save(pauta);
        assertThat(pauta.getId()).isNotNull();
        assertThat(pauta.getDescricao()).isEqualTo("Pauta Teste Descricao");
    }

    @Test
    public void deveRetornarVerdadeiro() {
        final Pauta pauta = new Pauta(null, "Pauta Teste Descricao");
        this.pautaRepository.save(pauta);
        assertThat(this.pautaRepository.existsById(pauta.getId())).isTrue();
    }

    @Test
    public void deveRetornarFalso() {
        final Pauta pauta = new Pauta(null, "Pauta Teste Descricao");
        this.pautaRepository.save(pauta);
        assertThat(this.pautaRepository.existsById(3)).isFalse();
    }

}
