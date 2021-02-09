package br.com.sicredi.votacao.dto;

import br.com.sicredi.votacao.entity.Pauta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "PautaDTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautaDTO {

    @ApiModelProperty(value = "ID Pauta", required = true)
    private Integer id;

    @ApiModelProperty(value = "Descrição referente o que será votado")
    @NotBlank(message = "Descricao deve ser preenchida")
    private String descricao;

    public static Pauta toEntity(final PautaDTO dto) {
        return Pauta.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .build();
    }

    public static PautaDTO toDTO(final Pauta pauta) {
        return PautaDTO.builder()
                .id(pauta.getId())
                .descricao(pauta.getDescricao())
                .build();
    }
}