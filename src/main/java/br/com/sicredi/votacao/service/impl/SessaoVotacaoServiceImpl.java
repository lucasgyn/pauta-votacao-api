package br.com.sicredi.votacao.service.impl;

import br.com.sicredi.votacao.dto.SessaoVotacaoAbrirDTO;
import br.com.sicredi.votacao.dto.SessaoVotacaoDTO;
import br.com.sicredi.votacao.entity.SessaoVotacao;
import br.com.sicredi.votacao.exception.NotFoundException;
import br.com.sicredi.votacao.repository.ISessaoVotacaoRepository;
import br.com.sicredi.votacao.service.IPautaService;
import br.com.sicredi.votacao.service.ISessaoVotacaoService;
import br.com.sicredi.votacao.util.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Scope(value = "prototype")
public class SessaoVotacaoServiceImpl implements ISessaoVotacaoService {

    private static final Integer TEMPO_DEFAULT = 1;

    private final ISessaoVotacaoRepository sessaoVotacaoRepository;
    private final IPautaService pautaService;

    @Autowired
    public SessaoVotacaoServiceImpl(final ISessaoVotacaoRepository sessaoVotacaoRepository, IPautaService pautaService) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.pautaService = pautaService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public SessaoVotacaoDTO abrirSessaoVotacao(final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO) {
        LoggerUtils.debug("Abrindo a sessao de votacao para a pauta " + sessaoVotacaoAbrirDTO.getIdPauta());
        this.isValidaAbrirSessao(sessaoVotacaoAbrirDTO);
        final SessaoVotacaoDTO dto = new SessaoVotacaoDTO(
                null,
                LocalDateTime.now(),
                calcularTempo(sessaoVotacaoAbrirDTO.getTempo()),
                Boolean.TRUE);
        return this.salvar(dto);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public void isValidaAbrirSessao(final SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO) {
        if (pautaService.isPautaValida(sessaoVotacaoAbrirDTO.getIdPauta())) {
            throw new NotFoundException("Pauta não localizada oidPauta" + sessaoVotacaoAbrirDTO.getIdPauta());
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public List<SessaoVotacaoDTO> buscarSessaoesEmAndamento() {
        LoggerUtils.debug("Buscando sessoes em andamento");
        List<SessaoVotacaoDTO> list = this.sessaoVotacaoRepository.buscarTodasSessoesEmAndamento(Boolean.TRUE)
                .stream()
                .map(SessaoVotacaoDTO::toDTO)
                .collect(Collectors.toList());

        return list
                .stream()
                .filter(dto -> dto.getDataHoraFim().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public void encerraoSessaoVotacao(final SessaoVotacaoDTO dto) {
        LoggerUtils.debug("Encerrando sessao com tempo de duracao expirado " + dto.getId());
        dto.setAtiva(Boolean.FALSE);
        this.salvar(buscarSessaoVotacaoPeloOID(dto.getId()));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public SessaoVotacaoDTO buscarSessaoVotacaoPeloOID(final Integer id) {
        Optional<SessaoVotacao> optionalSessaoVotacao = this.sessaoVotacaoRepository.findById(id);
        if (!optionalSessaoVotacao.isPresent()) {
            LoggerUtils.error("Sessao de votacao nao localizada para o id " + id);
            throw new NotFoundException("Sessão de votação não localizada para o id " + id);
        }
        return SessaoVotacaoDTO.toDTO(optionalSessaoVotacao.get());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public boolean isSessaoVotacaoValida(final Integer id) {
        return this.sessaoVotacaoRepository.existsByIdAndAtiva(id, Boolean.TRUE);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public boolean isSessaoVotacaoExiste(final Integer id) {
        if (!this.sessaoVotacaoRepository.existsById(id)) {
            LoggerUtils.error("Sessao de votacao nao localizada para o id " + id);
            throw new NotFoundException("Sessão de votação não localizada para o id " + id);
        }
        // else
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, timeout = 150, readOnly = true)
    public boolean isSessaoValidaParaContagem(final Integer id) {
        return this.sessaoVotacaoRepository.existsByIdAndAtiva(id, Boolean.FALSE);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, timeout = 150, rollbackFor = Exception.class)
    public SessaoVotacaoDTO salvar(final SessaoVotacaoDTO dto) {
        LoggerUtils.debug("Salvando a sessao de votacao");
        if (Optional.ofNullable(dto).isPresent()) {
            return SessaoVotacaoDTO.toDTO(this.sessaoVotacaoRepository.save(SessaoVotacaoDTO.toEntity(dto)));
        }
        return null;
    }

    private static LocalDateTime calcularTempo(final Integer tempo) {
        return null != tempo && 0 != tempo
                ? LocalDateTime.now().plusMinutes(tempo)
                : LocalDateTime.now().plusMinutes(TEMPO_DEFAULT);
    }
}