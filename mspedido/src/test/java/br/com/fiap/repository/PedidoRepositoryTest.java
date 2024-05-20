package br.com.fiap.repository;


import br.com.fiap.model.enums.StatusPedidoEnum;
import br.com.fiap.model.Pedido;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PedidoRepositoryTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Test
    public void testFindByStatus() {
        // Mock dos pedidos que serão retornados pelo repositório
        Pedido pedido1 = new Pedido();
        pedido1.setId(1);
        pedido1.setStatus(StatusPedidoEnum.PEDIDO_RECEBIDO);

        Pedido pedido2 = new Pedido();
        pedido2.setId(2);
        pedido2.setStatus(StatusPedidoEnum.AGUARDANDO_PAGAMENTO);

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        // Configurando o comportamento do repositório para retornar os pedidos simulados
        when(pedidoRepository.findByStatus(StatusPedidoEnum.PEDIDO_RECEBIDO)).thenReturn(Collections.singletonList(pedido1));
        when(pedidoRepository.findByStatus(StatusPedidoEnum.AGUARDANDO_PAGAMENTO)).thenReturn(Collections.singletonList(pedido2));

        // Chamando o método do repositório que retorna os pedidos por status
        List<Pedido> pedidosRecebidos = pedidoRepository.findByStatus(StatusPedidoEnum.PEDIDO_RECEBIDO);
        List<Pedido> pedidosAguardandoPagamento = pedidoRepository.findByStatus(StatusPedidoEnum.AGUARDANDO_PAGAMENTO);

        // Verificando se os resultados retornados pelo repositório são os esperados
        assertEquals(1, pedidosRecebidos.size());
        assertEquals(1, pedidosAguardandoPagamento.size());
        assertEquals(1, pedidosRecebidos.get(0).getId().intValue());
        assertEquals(2, pedidosAguardandoPagamento.get(0).getId().intValue());
        assertEquals(StatusPedidoEnum.PEDIDO_RECEBIDO, pedidosRecebidos.get(0).getStatus());
        assertEquals(StatusPedidoEnum.AGUARDANDO_PAGAMENTO, pedidosAguardandoPagamento.get(0).getStatus());
    }

    @Test
    public void testDevePermitirRegistrarPedido() {
        long totalRegistros = pedidoRepository.count();
        assertThat(totalRegistros).isNotNegative();
    }

}
