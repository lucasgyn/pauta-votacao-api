package br.com.sicredi.votacao.service;

import br.com.sicredi.votacao.dto.SessaoVotacaoAbrirDTO;
import br.com.sicredi.votacao.dto.SessaoVotacaoDTO;

import java.util.List;

public interface ISessaoVotacaoService {

    SessaoVotacaoDTO abrirSessaoVotacao(final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO);

    void isValidaAbrirSessao(final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO);

    List<SessaoVotacaoDTO> buscarSessaoesEmAndamento();

    void encerraoSessaoVotacao(final SessaoVotacaoDTO dto);

    SessaoVotacaoDTO buscarSessaoVotacaoPeloOID(final Integer id);

    boolean isSessaoVotacaoValida(final Integer id);

    boolean isSessaoVotacaoExiste(final Integer id);

    boolean isSessaoValidaParaContagem(final Integer id);

    SessaoVotacaoDTO salvar(final SessaoVotacaoDTO dto);
}