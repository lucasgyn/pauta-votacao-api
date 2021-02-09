package br.com.sicredi.votacao.repository;

import br.com.sicredi.votacao.entity.Votacao;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public interface IVotacaoRepository extends JpaRepository<Votacao, Integer> {

    Integer countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(final Integer idPauta, final Integer idSessaoVotacao, final Boolean voto);
}