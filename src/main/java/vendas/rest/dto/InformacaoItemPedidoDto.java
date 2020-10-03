package vendas.rest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoItemPedidoDto {

	private String descricaoProduto;
	private BigDecimal precoUnitario;
	private Integer quantidade;

}
