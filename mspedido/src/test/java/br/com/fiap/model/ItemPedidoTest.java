package br.com.fiap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemPedidoTest {

    @Test
    public void testConstrutorVazio() {
        ItemPedido itemPedido = new ItemPedido();
        assertNotNull(itemPedido);
    }

    @Test
    public void testConstrutor() {
        // Arrange
        Integer id = 1;
        Integer idProduto = 100;
        int quantidade = 5;

        // Act
        ItemPedido itemPedido = new ItemPedido(id, idProduto, quantidade);

        // Assert
        assertEquals(id, itemPedido.getId());
        assertEquals(idProduto, itemPedido.getIdProduto());
        assertEquals(quantidade, itemPedido.getQuantidade());
    }

    @Test
    public void testSetters() {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(1);
        itemPedido.setIdProduto(10);
        itemPedido.setQuantidade(5);

        assertEquals(1, itemPedido.getId());
        assertEquals(10, itemPedido.getIdProduto());
        assertEquals(5, itemPedido.getQuantidade());
    }
}
