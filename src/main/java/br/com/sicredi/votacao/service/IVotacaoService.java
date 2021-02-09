package br.com.sicredi.votacao.service;

import br.com.sicredi.votacao.dto.ResultadoDTO;
import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.dto.VotoDTO;

public interface IVotacaoService {

    void validarVoto(final VotoDTO dto);

    String votar(final VotoDTO dto);

    void registrarAssociadoVotou(final VotoDTO dto);

    void registrarVoto(final VotacaoDTO dto);

    VotacaoDTO buscarResultadoVotacao(final Integer idPauta, final Integer idSessaoVotacao);

    ResultadoDTO buscarDadosResultadoVotacao(final Integer idPauta, final Integer idSessaoVotacao);

    boolean isDadosExistem(final Integer idPauta, final Integer idSessaoVotacao);
}