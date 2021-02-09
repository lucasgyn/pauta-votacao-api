package br.com.sicredi.votacao.service;

import br.com.sicredi.votacao.dto.AssociadoDTO;

public interface IAssociadoService {

    boolean isParticipacaoAssociadoVotacao(final String cpfAssociado, final Integer idPauta);

    void salvar(final AssociadoDTO dto);

    boolean isAssociadoPodeVotar(final String cpf);
}