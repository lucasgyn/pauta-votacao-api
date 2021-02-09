package br.com.sicredi.votacao.service.impl;

import br.com.sicredi.votacao.dto.*;
import br.com.sicredi.votacao.exception.NotFoundException;
import br.com.sicredi.votacao.exception.SessoEncerradaException;
import br.com.sicredi.votacao.exception.VotoInvalidoException;
import br.com.sicredi.votacao.repository.IVotacaoRepository;
import br.com.sicredi.votacao.service.IAssociadoService;
import br.com.sicredi.votacao.service.IPautaService;
import br.com.sicredi.votacao.service.ISessaoVotacaoService;
import br.com.sicredi.votacao.service.IVotacaoService;
import br.com.sicredi.votacao.util.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(value = "prototype")
public class VotacaoServiceImpl implements IVotacaoService {

    private final IVotacaoRepository repository;
    private final IPautaService pautaService;
    private final ISessaoVotacaoService sessaoVotacaoService;
    private final IAssociadoService associadoService;

    @Autowired
    public VotacaoServiceImpl(final IVotacaoRepository repository,
                              final IPautaService pautaService,
                              final ISessaoVotacaoService sessaoVotacaoService,
                              final IAssociadoService associadoService) {
        this.repository = repository;
        this.pautaService = pautaService;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.associadoService = associadoService;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public void validarVoto(final VotoDTO dto) {
        LoggerUtils.debug(String.format("Validando os dados para voto idSessao = %s, idPauta = %s, idAssiciado = %s", dto.getIdSessaoVotacao(), dto.getIdPauta(), dto.getCpfAssociado()));

        if (!pautaService.isPautaValida(dto.getIdPauta())) {
            LoggerUtils.error("Pauta nao localizada para votacao idPauta " + dto.getCpfAssociado());
            throw new NotFoundException("Pauta não localizada id " + dto.getIdPauta());
        }

        if (!sessaoVotacaoService.isSessaoVotacaoValida(dto.getIdSessaoVotacao())) {
            LoggerUtils.error("Tentativa de voto para sessao encerrada idSessaoVotacao " + dto.getIdSessaoVotacao());
            throw new SessoEncerradaException("Sessão de votação já encerrada");
        }

        if (!associadoService.isAssociadoPodeVotar(dto.getCpfAssociado())) {
            LoggerUtils.error("Associado nao esta habilitado para votar " + dto.getCpfAssociado());
            throw new VotoInvalidoException("Não é possível votar mais de 1 vez na mesma pauta");
        }

        if (!associadoService.isParticipacaoAssociadoVotacao(dto.getCpfAssociado(), dto.getIdPauta())) {
            LoggerUtils.error("Associado tentou votar mais de 1 vez oidAssociado " + dto.getCpfAssociado());
            throw new VotoInvalidoException("Não é possível votar mais de 1 vez na mesma pauta");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public String votar(final VotoDTO dto) {
        this.validarVoto(dto);
        LoggerUtils.debug(String.format("Dados validos para voto idSessao = %s, idPauta = %s, idAssiciado = %s", dto.getIdSessaoVotacao(), dto.getIdPauta(), dto.getCpfAssociado()));
        final VotacaoDTO votacaoDTO = new VotacaoDTO(null,
                dto.getIdPauta(),
                dto.getIdSessaoVotacao(),
                dto.getVoto(),
                null,
                null);
        this.registrarVoto(votacaoDTO);
        this.registrarAssociadoVotou(dto);
        return "Voto validado.";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public void registrarAssociadoVotou(final VotoDTO dto) {
        final AssociadoDTO associadoDTO = new AssociadoDTO(null, dto.getCpfAssociado(), dto.getIdPauta());
        this.associadoService.salvar(associadoDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public void registrarVoto(final VotacaoDTO dto) {
        LoggerUtils.debug("Salvando o voto para oidPauta " + dto.getIdPauta());
        repository.save(VotacaoDTO.toEntity(dto));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public VotacaoDTO buscarResultadoVotacao(final Integer idPauta, final Integer idSessaoVotacao) {
        LoggerUtils.debug(String.format("Contabilizando os votos para oidPauta = %s, oidSessaoVotacao = %s", idPauta, idSessaoVotacao));
        VotacaoDTO dto = new VotacaoDTO();
        dto.setIdPauta(idPauta);
        dto.setIdSessaoVotacao(idSessaoVotacao);
        dto.setQuantidadeVotosSim(repository.countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(idPauta, idSessaoVotacao, Boolean.TRUE));
        dto.setQuantidadeVotosNao(repository.countVotacaoByIdPautaAndIdSessaoVotacaoAndVoto(idPauta, idSessaoVotacao, Boolean.FALSE));
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public ResultadoDTO buscarDadosResultadoVotacao(final Integer idPauta, final Integer idSessaoVotacao) {
        if (isDadosExistem(idPauta, idSessaoVotacao) && sessaoVotacaoService.isSessaoValidaParaContagem(idSessaoVotacao)) {
            LoggerUtils.debug(String.format("Construindo o objeto de retorno do resultado para idPauta = %s, idSessaoVotacao = %s", idPauta, idSessaoVotacao));
            PautaDTO pautaDTO = pautaService.findById(idPauta);
            VotacaoDTO votacaoDTO = buscarResultadoVotacao(idPauta, idSessaoVotacao);
            return new ResultadoDTO(pautaDTO, votacaoDTO);
        }
        throw new NotFoundException("Sessão de votação ainda está aberta, não é possível obter a contagem do resultado.");
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public boolean isDadosExistem(final Integer idPauta, final Integer idSessaoVotacao) {
        return this.sessaoVotacaoService.isSessaoVotacaoExiste(idPauta) && pautaService.isPautaValida(idSessaoVotacao);
    }
}