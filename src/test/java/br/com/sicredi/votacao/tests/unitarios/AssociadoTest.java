package br.com.sicredi.votacao.tests.unitarios;

import br.com.sicredi.votacao.entity.Associado;
import br.com.sicredi.votacao.repository.IAssociadoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AssociadoTest {

    @Autowired
    private IAssociadoRepository associadoRepository;

    @Test
    public void devePersistirAssociado() {
        final Associado associado = new Associado(null, "81479012084", 1);
        this.associadoRepository.save(associado);
        assertThat(associado.getId()).isNotNull();
        assertThat(associado.getCpfAssociado()).isEqualTo("81479012084");
    }

    @Test
    public void deveRetornarUmAssociado() {
        final Associado associado = new Associado(null, "81479012084", 1);
        this.associadoRepository.save(associado);
        assertThat(this.associadoRepository.findById(1)).isNotNull();
    }

    @Test
    public void deveRetonarVerdadeiro() {
        final Associado associado = new Associado(null, "81479012084", 1);
        this.associadoRepository.save(associado);
        assertThat(this.associadoRepository.existsByCpfAssociadoAndIdPauta("81479012084", 1)).isTrue();
    }

    @Test
    public void deveRetonarFalso() {
        final Associado associado = new Associado(null, "81479012084", 12);
        this.associadoRepository.save(associado);
        assertThat(this.associadoRepository.existsByCpfAssociadoAndIdPauta("81479012084", 1)).isFalse();
    }
}