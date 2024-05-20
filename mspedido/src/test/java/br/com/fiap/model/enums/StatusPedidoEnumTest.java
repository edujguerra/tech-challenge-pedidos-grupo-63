package br.com.fiap.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusPedidoEnumTest {

    @Test
    public void testParametros() {
        StatusPedidoEnum statusPedidoEnum = StatusPedidoEnum.PEDIDO_RECEBIDO;

        assertEquals("Recebido", statusPedidoEnum.getNome());
        assertEquals("Seu pedido foi recebido e está sendo processado.", statusPedidoEnum.getDescricao());
    }

    @Test
    public void testObterStatusPorNome() {
        StatusPedidoEnum status = StatusPedidoEnum.obterStatusPorNome("Recebido");
        assertEquals(StatusPedidoEnum.PEDIDO_RECEBIDO, status);
    }

    @Test
    public void testObterStatusPorNome_Ok() {
        StatusPedidoEnum status = StatusPedidoEnum.obterStatusPorNomeOuStringEnum("Pedido em preparação");
        assertEquals(StatusPedidoEnum.PREPARANDO_PARA_ENVIO, status);
    }

    @Test
    public void testObterStatusPorNome_StringEnum() {
        StatusPedidoEnum status = StatusPedidoEnum.obterStatusPorNomeOuStringEnum("EM_TRANSITO");
        assertEquals(StatusPedidoEnum.EM_TRANSITO, status);
    }

    @Test
    public void testObterStatusPorNome_StringEnumCaseInsensitive() {
        StatusPedidoEnum status = StatusPedidoEnum.obterStatusPorNomeOuStringEnum("pAgAmenTo aPrOVaDO!");
        assertEquals(StatusPedidoEnum.PEDIDO_PAGO, status);
    }

    @Test
    public void testObterStatusPorNome_NaoEncontrado() {
        assertThrows(IllegalArgumentException.class, () -> StatusPedidoEnum.obterStatusPorNome("Status Inexistente"));
    }

    @Test
    public void testObterStatusPorNomeOuStringEnum_NaoEncontrado() {
        assertThrows(IllegalArgumentException.class, () -> StatusPedidoEnum.obterStatusPorNomeOuStringEnum("Status Inexistente"));
    }
}

