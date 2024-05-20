package br.com.fiap.helper;

import br.com.fiap.model.enums.StatusPedidoEnum;
import br.com.fiap.model.ItemPedido;
import br.com.fiap.model.Pedido;
import br.com.fiap.model.dtos.ProdutoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class PedidoHelper {

    public static Pedido gerarPedido() {
        List<ItemPedido> listaItens = new ArrayList<>();
        ItemPedido itemPedido = new ItemPedido(1, 1, 2);
        listaItens.add(itemPedido);

        return new Pedido(1, "Julio Cesar", listaItens,
                359.99, StatusPedidoEnum.PEDIDO_PAGO, 1);
    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static ProdutoDTO gerarProduto() {
        ProdutoDTO produtoDTO = new ProdutoDTO(1, "Produto ficticio", "Descricao do produto ficticio",
                10, 235.50);
        return produtoDTO;
    }

}