package vendas.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vendas.domain.entity.Cliente;
import vendas.domain.entity.ItemPedido;
import vendas.domain.entity.Pedido;
import vendas.validation.NotEmptyList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoDto {

	@NotNull(message = "{campo.codigo-cliente.obrigatorio}")
	private Integer cliente;
	@NotNull(message = "{campo.total-pedido.obrigatorio}")
	private BigDecimal total;
	@NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
	private List<ItemPedidoDto> items;

}
