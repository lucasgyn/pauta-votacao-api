package br.com.sicredi.votacao.service;

import br.com.sicredi.votacao.dto.PautaDTO;

public interface IPautaService {

    PautaDTO salvar(final PautaDTO dto);

    PautaDTO findById(final Integer id);

    boolean isPautaValida(Integer id);
}