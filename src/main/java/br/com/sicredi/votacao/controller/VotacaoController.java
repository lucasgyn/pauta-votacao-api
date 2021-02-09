package br.com.sicredi.votacao.controller;

import br.com.sicredi.votacao.dto.ResultadoDTO;
import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.service.IVotacaoService;
import br.com.sicredi.votacao.service.impl.VotacaoServiceImpl;
import br.com.sicredi.votacao.util.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/votacoes")
@Api(value = "Votacao", tags = "Votacao")
public class VotacaoController {

    private final IVotacaoService votacaoService;

    @Autowired
    public VotacaoController(final IVotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    @ApiOperation(value = "Votar em determinada pauta, enquanto a sessão de votação estiver aberta")
    @PostMapping(value = "/votar")
    public ResponseEntity<String> votar(@Valid @RequestBody VotoDTO dto) {
        LoggerUtils.debug("Associado votando associado = " + dto.getCpfAssociado());
        final String mensagem = votacaoService.votar(dto);
        LoggerUtils.debug("Voto associado finalizado associado = " + dto.getCpfAssociado());
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @ApiOperation(value = "Resultado da votacao, somente após finalização da sessão de votação")
    @GetMapping(value = "/resultado")
    public ResponseEntity<ResultadoDTO> resultadoVotacao(final @RequestParam("idPauta") Integer idPauta, final @RequestParam("idSessaoVotacao") Integer idSessaoVotacao) {
        LoggerUtils.debug(String.format("Buscando resultado da votacao idPauta = %s, idSessaoVotacao = %s ", idPauta, idSessaoVotacao));
        final ResultadoDTO dto = votacaoService.buscarDadosResultadoVotacao(idPauta, idSessaoVotacao);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}