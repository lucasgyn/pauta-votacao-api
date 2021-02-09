package br.com.sicredi.votacao.repository;

import br.com.sicredi.votacao.entity.Associado;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public interface IAssociadoRepository extends JpaRepository<Associado, Integer> {

    Boolean existsByCpfAssociadoAndIdPauta(final String cpfAssociado, final Integer idPauta);
}