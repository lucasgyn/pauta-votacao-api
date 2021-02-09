package br.com.sicredi.votacao.service.impl;

import br.com.sicredi.votacao.client.UserClient;
import br.com.sicredi.votacao.dto.AssociadoDTO;
import br.com.sicredi.votacao.repository.IAssociadoRepository;
import br.com.sicredi.votacao.service.IAssociadoService;
import br.com.sicredi.votacao.util.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(value = "prototype")
public class AssociadoServiceImpl implements IAssociadoService {

    private final IAssociadoRepository associadoRepository;

    @Autowired
    public AssociadoServiceImpl(final IAssociadoRepository associadoRepository) {
        this.associadoRepository = associadoRepository;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public boolean isParticipacaoAssociadoVotacao(final String cpfAssociado, final Integer idPauta) {
        LoggerUtils.debug("Validando participacao do associado na votacao da pauta id = " + idPauta);
        return this.associadoRepository.existsByCpfAssociadoAndIdPauta(cpfAssociado, idPauta);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public void salvar(final AssociadoDTO dto) {
        LoggerUtils.debug(String.format("Registrando participacao do associado na votacao idAssociado = %s, idPauta = %s", dto.getCpfAssociado(), dto.getIdPauta()));
        this.associadoRepository.save(AssociadoDTO.toEntity(dto));
    }

    @Override
    public boolean isAssociadoPodeVotar(final String cpf) {
        return UserClient.isAssociadoHabilitadoVotacao(cpf);
    }
}