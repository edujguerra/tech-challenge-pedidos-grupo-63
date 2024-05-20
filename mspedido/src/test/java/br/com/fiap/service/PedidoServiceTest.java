package br.com.fiap.service;

import br.com.fiap.model.enums.StatusPedidoEnum;
import br.com.fiap.model.ItemPedido;
import br.com.fiap.model.Pedido;
import br.com.fiap.helper.PedidoHelper;
import br.com.fiap.repository.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testListarPedidos() {

        Pedido pedido = PedidoHelper.gerarPedido();
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(pedido);
        Mockito.when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listarPedidos();

        assertTrue(resultado.contains(pedidos.get(0)));
        assertEquals(pedidos, resultado);
    }

    @Test
    public void testListarPedidosNenhumPedidoEncontrado() {

        Mockito.when(pedidoRepository.findAll()).thenReturn(new ArrayList<>());

        List<Pedido> resultado = pedidoService.listarPedidos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testObterPedidoPorStatus() {

        String statusPedido = "PEDIDO_RECEBIDO";
        StatusPedidoEnum statusPedidoEnum = StatusPedidoEnum.PEDIDO_RECEBIDO;
        Pedido pedido = new Pedido(); // Pedido fictício
        List<Pedido> listaPedidos = new ArrayList<>();
        listaPedidos.add(pedido);

        // Simulação do método findByStatus do repositório
        Mockito.when(pedidoRepository.findByStatus(statusPedidoEnum)).thenReturn(listaPedidos);

        // Chama o método a ser testado
        List<Pedido> resultado = pedidoService.obterPedidoPorStatus(statusPedido);

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.contains(pedido));
    }

    @Test
    public void testObterPedidoPorStatusNenhumPedidoEncontrado() {

        String statusPedido = "PEDIDO_RECEBIDO";
        StatusPedidoEnum statusPedidoEnum = StatusPedidoEnum.PEDIDO_RECEBIDO;

        // Simulação do método findByStatus do repositório que retorna uma lista vazia
        Mockito.when(pedidoRepository.findByStatus(statusPedidoEnum)).thenReturn(new ArrayList<>());

        // Chama o método a ser testado e verifica se ele lança a exceção correta
        assertThrows(NoSuchElementException.class, () -> {
            pedidoService.obterPedidoPorStatus(statusPedido);
        });
    }

    @Test
    public void testObterPedido() {
        // Dados de exemplo para simular uma resposta do serviço externo
        String jsonResponse = "{\"id\": 1, \"nome_cliente\": \"João Silva\", \"valor_total\": 75.50, \"status\": \"PEDIDO_RECEBIDO\", \"entregador_id\": 101}";

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setNomeCliente("João Silva");
        pedido.setValorTotal(75.50);
        pedido.setStatus(StatusPedidoEnum.PEDIDO_RECEBIDO);
        pedido.setEntregadorId(101);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));

        Pedido pedidoEncontrado = pedidoService.obterPedido(1);

        assertNotNull(pedidoEncontrado);
        assertEquals(1, pedidoEncontrado.getId().intValue());
        assertEquals("João Silva", pedidoEncontrado.getNomeCliente());
        assertEquals(75.50, pedidoEncontrado.getValorTotal(), 0.01);
        assertEquals(StatusPedidoEnum.PEDIDO_RECEBIDO, pedidoEncontrado.getStatus());
        assertEquals(101, pedidoEncontrado.getEntregadorId().intValue());
    }

    @Test
    public void testCancelarPedido() {

        Integer pedidoId = 1;
        String entradaSaida = "inserir";


        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(pedidoId);
        List<ItemPedido> itensPedido = new ArrayList<>();
        ItemPedido itemPedido = new ItemPedido(2, 2, 3);
        itensPedido.add(itemPedido);
        pedidoExistente.setItensPedido(itensPedido);

        Mockito.when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoExistente));


        Mockito.doNothing().when(restTemplateMock).put(anyString(), any(), any(), any(), any());

        pedidoService.cancelarPedido(pedidoId);

        // Verifica se o pedido foi excluído e se o método delete foi chamado no repositório
        Mockito.verify(pedidoRepository).delete(pedidoExistente);
        // Verifica se o restTemplate foi chamado corretamente
        Mockito.verify(restTemplateMock).put(anyString(), any(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testCancelarPedidoPedidoNaoEncontrado() {

        Integer pedidoId = 1;

        Mockito.when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Chama o método a ser testado e verifica se ele lança a exceção correta
        assertThrows(NoSuchElementException.class, () -> {
            pedidoService.cancelarPedido(pedidoId);
        });
    }

    @Test
    public void testAtualizarStatus() {

        Integer pedidoId = 1;
        StatusPedidoEnum novoStatus = StatusPedidoEnum.EM_TRANSITO;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusPedidoEnum.PEDIDO_RECEBIDO); // Status original do pedido

        Mockito.when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        Pedido pedidoAtualizado = pedidoService.atualizarStatus(pedidoId, novoStatus);

        // Verifica se o status foi atualizado corretamente no pedido
        assertNotNull(pedidoAtualizado);
        assertEquals(novoStatus, pedidoAtualizado.getStatus());
        Mockito.verify(pedidoRepository).save(pedido); // Verifica se o método save foi chamado no repositório
    }

    @Test
    public void testAtualizarStatusPedidoNaoEncontrado() {

        Integer pedidoId = 1;
        StatusPedidoEnum novoStatus = StatusPedidoEnum.EM_TRANSITO;

        Mockito.when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Chama o método a ser testado e verifica se ele lança a exceção correta
        assertThrows(NoSuchElementException.class, () -> {
            pedidoService.atualizarStatus(pedidoId, novoStatus);
        });
    }

    @Test
    public void testIncluirEntregador() {

        Integer pedidoId = 1;
        Integer entregadorId = 101;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEntregadorId(null); // Simula um pedido sem entregador

        Mockito.when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        Pedido pedidoAtualizado = pedidoService.incluirEntregador(pedidoId, entregadorId);

        // Verifica se o entregador foi incluído corretamente no pedido
        assertNotNull(pedidoAtualizado);
        assertEquals(entregadorId, pedidoAtualizado.getEntregadorId());
        Mockito.verify(pedidoRepository).save(pedido); // Verifica se o método save foi chamado no repositório
    }

    @Test
    public void testIncluirEntregadorPedidoNaoEncontrado() {

        Integer pedidoId = 1;
        Integer entregadorId = 101;

        Mockito.when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Chama o método a ser testado e verifica se ele lança a exceção corretamente
        assertThrows(NoSuchElementException.class, () -> {
            pedidoService.incluirEntregador(pedidoId, entregadorId);
        });
    }

}

