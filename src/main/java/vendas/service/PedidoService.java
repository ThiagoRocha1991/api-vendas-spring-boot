package vendas.service;

import java.util.Optional;

import vendas.domain.entity.Pedido;
import vendas.domain.enums.StatusPedido;
import vendas.rest.dto.PedidoDto;

public interface PedidoService {

	Pedido salvar(PedidoDto dto);
	
	Optional<Pedido> obterPedidoCompleto(Integer id);
	
	void atualizaStatus(Integer id, StatusPedido statusPedido); 

}
