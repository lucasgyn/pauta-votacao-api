package br.com.sicredi.votacao.controller;


import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.service.IPautaService;
import br.com.sicredi.votacao.service.impl.PautaServiceImpl;
import br.com.sicredi.votacao.util.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/pautas")
@Api(value = "Pauta", tags = "Pauta")
public class PautaController {

    private final IPautaService pautaService;

    @Autowired
    public PautaController(final IPautaService pautaService) {
        this.pautaService = pautaService;
    }

    @ApiOperation(value = "Criar uma pauta para ser votada")
    @PostMapping
    public ResponseEntity<PautaDTO> salvar(@Valid @RequestBody PautaDTO dto) {
        LoggerUtils.debug("Salvando a pauta = " + dto.getDescricao());
        dto = this.pautaService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @ApiOperation(value = "Buscar a pauta utilizando ID")
    @GetMapping
    public ResponseEntity<PautaDTO> getById(final @RequestParam("id") Integer id) {
        LoggerUtils.debug("Buscando a pauta pelo ID = " + id);
        return ResponseEntity.ok(this.pautaService.findById(id));
    }
}