package br.com.sicredi.votacao.controller;

import br.com.sicredi.votacao.dto.SessaoVotacaoAbrirDTO;
import br.com.sicredi.votacao.dto.SessaoVotacaoDTO;
import br.com.sicredi.votacao.service.ISessaoVotacaoService;
import br.com.sicredi.votacao.util.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/sessoes-votacao")
@Api(value = "Sessao Votacao", tags = "Sessao Votacao")
public class SessaoVotacaoController {

    private final ISessaoVotacaoService sessaoVotacaoService;

    @Autowired
    public SessaoVotacaoController(final ISessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @ApiOperation(value = "Abrir uma sessão de votação, referente a determinada pauta")
    @PostMapping(value = "/abrir-sessao")
    public ResponseEntity<SessaoVotacaoDTO> abrirSessaoVotacao(final @Valid @RequestBody SessaoVotacaoAbrirDTO sessaoVotacaoAbrirDTO) {
        LoggerUtils.debug("Abrindo a sessao para votacao da pauta id = " + sessaoVotacaoAbrirDTO.getIdPauta());
        final SessaoVotacaoDTO dto = this.sessaoVotacaoService.abrirSessaoVotacao(sessaoVotacaoAbrirDTO);
        LoggerUtils.debug("Sessao para votacao da pauta id = " + sessaoVotacaoAbrirDTO.getIdPauta());
        LoggerUtils.debug("Hora de inicio sessao para votacao " + dto.getDataHoraInicio());
        LoggerUtils.debug("Hora de encerramento sessao para votacao " + dto.getDataHoraFim());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}