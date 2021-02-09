package br.com.sicredi.votacao.repository;

import br.com.sicredi.votacao.entity.Pauta;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public interface IPautaRepository extends JpaRepository<Pauta, Integer> {}