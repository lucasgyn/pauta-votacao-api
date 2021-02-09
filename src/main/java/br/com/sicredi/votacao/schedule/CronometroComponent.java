package br.com.sicredi.votacao.schedule;

import br.com.sicredi.votacao.dto.SessaoVotacaoDTO;
import br.com.sicredi.votacao.service.impl.SessaoVotacaoServiceImpl;
import br.com.sicredi.votacao.util.LoggerUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CronometroComponent {

    private final SessaoVotacaoServiceImpl sessaoVotacaoService;

    public CronometroComponent(final SessaoVotacaoServiceImpl sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Scheduled(cron = "15 * * * * *")
    private void teste() {
        LoggerUtils.debug("Contador de tempo sendo excutado...");
        List<SessaoVotacaoDTO> list = sessaoVotacaoService.buscarSessaoesEmAndamento();
        LoggerUtils.debug("Quantidade de sessoes abertas = " + list.size());
        list.forEach(dto -> {
            LoggerUtils.debug("Sessao encerrada = " + dto.getId());
            if (dto.getAtiva()) {
                sessaoVotacaoService.encerraoSessaoVotacao(dto);
            }
        });
    }
}