package vendas.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vendas.domain.entity.Cliente;
import vendas.domain.entity.ItemPedido;
import vendas.domain.entity.Pedido;
import vendas.domain.entity.Produto;
import vendas.domain.enums.StatusPedido;
import vendas.domain.repository.Clientes;
import vendas.domain.repository.ItemsPedido;
import vendas.domain.repository.Pedidos;
import vendas.domain.repository.Produtos;
import vendas.exception.PedidoNaoEncontradoException;
import vendas.exception.RegraNegocioException;
import vendas.rest.dto.ItemPedidoDto;
import vendas.rest.dto.PedidoDto;
import vendas.service.PedidoService;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
	
	private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

	@Override
    @Transactional
    public Pedido salvar( PedidoDto dto ) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDto> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: "+ idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }

	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		// TODO Auto-generated method stub
		return repository.findByIdFetchItens(id);
	}

	@Override
	@Transactional
	public void atualizaStatus(Integer id, StatusPedido statusPedido) {

		repository
			.findById(id)
			.map( pedido -> {
				pedido.setStatus(statusPedido);
				return repository.save(pedido);
			}).orElseThrow(() -> new PedidoNaoEncontradoException());
		
	}

}
