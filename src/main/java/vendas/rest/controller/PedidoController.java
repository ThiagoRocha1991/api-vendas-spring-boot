package vendas.rest.controller;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import vendas.domain.entity.ItemPedido;
import vendas.domain.entity.Pedido;
import vendas.domain.enums.StatusPedido;
import vendas.rest.dto.AtualizacaoStatusPedidoDto;
import vendas.rest.dto.InformacaoItemPedidoDto;
import vendas.rest.dto.InformacoesPedidoDto;
import vendas.rest.dto.PedidoDto;
import vendas.service.PedidoService;

import static org.springframework.http.HttpStatus.*;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

	private PedidoService service;

	public PedidoController(PedidoService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(CREATED)
	public Integer save(@RequestBody @Valid PedidoDto dto) {

		Pedido pedido = service.salvar(dto);
		return pedido.getId();

	}

	@GetMapping("{id}")
    public InformacoesPedidoDto getById( @PathVariable Integer id ){
        return service
                .obterPedidoCompleto(id)
                .map( p -> converter(p) )
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }
	
	private InformacoesPedidoDto converter(Pedido pedido){
        return InformacoesPedidoDto
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();
    }

	private List<InformacaoItemPedidoDto> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacaoItemPedidoDto
                            .builder().descricaoProduto(item.getProduto().getDescricao())
                            .precoUnitario(item.getProduto().getPreco())
                            .quantidade(item.getQuantidade())
                            .build()
        ).collect(Collectors.toList());
    }
	
	@PatchMapping("{id}")
	@ResponseStatus(NO_CONTENT)
	public void updateStatus(@PathVariable Integer id, 
							@RequestBody AtualizacaoStatusPedidoDto dto) {
		
		String novoStatus = dto.getNovoStatus();
		service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
				
	}

}
