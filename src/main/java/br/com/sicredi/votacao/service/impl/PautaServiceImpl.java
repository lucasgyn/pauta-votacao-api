package br.com.sicredi.votacao.service.impl;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.entity.Pauta;
import br.com.sicredi.votacao.exception.NotFoundException;
import br.com.sicredi.votacao.repository.IPautaRepository;
import br.com.sicredi.votacao.service.IPautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(value = "prototype")
public class PautaServiceImpl implements IPautaService {

    private final IPautaRepository pautaRepository;

    @Autowired
    public PautaServiceImpl(final IPautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public PautaDTO salvar(final PautaDTO dto) {
        return PautaDTO.toDTO(this.pautaRepository.save(PautaDTO.toEntity(dto)));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public PautaDTO findById(final Integer id) {
        final Pauta pauta = this.pautaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pauta não localizada para o id " + id));
        return PautaDTO.toDTO(pauta);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public boolean isPautaValida(final Integer id) {
        if (!this.pautaRepository.existsById(id)) {
            throw new NotFoundException("Pauta não localizada para o id = " + id);
        }
        // else
        return true;
    }
}