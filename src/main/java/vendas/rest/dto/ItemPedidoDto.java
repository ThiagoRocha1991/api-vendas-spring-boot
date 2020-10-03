package vendas.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vendas.domain.entity.Cliente;
import vendas.domain.entity.ItemPedido;
import vendas.domain.entity.Pedido;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPedidoDto {

	private Integer produto;
	private Integer quantidade;

}
