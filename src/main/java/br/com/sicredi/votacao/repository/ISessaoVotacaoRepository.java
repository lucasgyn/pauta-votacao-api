package br.com.sicredi.votacao.repository;

import br.com.sicredi.votacao.entity.SessaoVotacao;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope("prototype")
public interface ISessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Integer> {

    @Query("select s from SessaoVotacao s where s.ativa=:ativo")
    List<SessaoVotacao> buscarTodasSessoesEmAndamento(final Boolean ativo);

    Boolean existsByIdAndAtiva(final Integer id, final Boolean ativa);
}