package br.com.fiap.model;

import br.com.fiap.model.enums.StatusPedidoEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PedidoTest {

    @Test
    public void testConstrutorVazio() {
        Pedido pedido = new Pedido();
        assertNotNull(pedido);
    }

    @Test
    public void testConstrutorComArgumentos() {
        List<ItemPedido> itensPedido = new ArrayList<>();
        itensPedido.add(new ItemPedido());
        Pedido pedido = new Pedido(1, "Cliente Teste", itensPedido, 100.0,
                StatusPedidoEnum.PEDIDO_RECEBIDO, 1);
        assertEquals(1, pedido.getId());
        assertEquals("Cliente Teste", pedido.getNomeCliente());
        assertEquals(itensPedido, pedido.getItensPedido());
        assertEquals(100.0, pedido.getValorTotal());
        assertEquals(StatusPedidoEnum.PEDIDO_RECEBIDO, pedido.getStatus());
        assertEquals(1, pedido.getEntregadorId());
    }

    @Test
    public void testSetId() {
        Pedido pedido = new Pedido();
        pedido.setId(1);
        assertEquals(1, pedido.getId());
    }

    @Test
    public void testSetNomeCliente() {
        Pedido pedido = new Pedido();
        pedido.setNomeCliente("Novo Cliente");
        assertEquals("Novo Cliente", pedido.getNomeCliente());
    }

    @Test
    public void testSetItensPedido() {
        Pedido pedido = new Pedido();
        List<ItemPedido> itensPedido = new ArrayList<>();
        itensPedido.add(new ItemPedido());
        pedido.setItensPedido(itensPedido);
        assertEquals(itensPedido, pedido.getItensPedido());
    }

    @Test
    public void testSetValorTotal() {
        Pedido pedido = new Pedido();
        pedido.setValorTotal(200.0);
        assertEquals(200.0, pedido.getValorTotal());
    }

    @Test
    public void testSetStatus() {
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedidoEnum.PEDIDO_PAGO);
        assertEquals(StatusPedidoEnum.PEDIDO_PAGO, pedido.getStatus());
    }

    @Test
    public void testSetEntregadorId() {
        Pedido pedido = new Pedido();
        pedido.setEntregadorId(2);
        assertEquals(2, pedido.getEntregadorId());
    }
}
