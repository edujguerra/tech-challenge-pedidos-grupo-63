package br.com.fiap.service;

import br.com.fiap.model.Enum.StatusPedidoEnum;
import br.com.fiap.model.ItemPedido;
import br.com.fiap.model.Pedido;
import br.com.fiap.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String RETIRAR_ESTOQUE = "retirar";
    private static final String INSERIR_ESTOQUE = "inserir";

    public PedidoService(PedidoRepository pedidoRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido obterPedido(Integer pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido != null) {
            return pedido;
        } else {
            throw new NoSuchElementException("Pedido com código {} não encontrado" + pedidoId);
        }
    }

    public List<Pedido> obterPedidoPorStatus(String statusPedido) {
        StatusPedidoEnum statusPedidoEnum = StatusPedidoEnum.obterStatusPorNomeOuStringEnum(statusPedido);
        List<Pedido> listaPedidos = pedidoRepository.findByStatus(statusPedidoEnum);
        if (listaPedidos.isEmpty()) {
            throw new NoSuchElementException("Não existem pedidos com o status solicitado");
        }
        return listaPedidos;
    }

    public Pedido criarPedido(Pedido pedido) {

        boolean produtosDisponiveis = verificarDisponibilidadeProdutos(pedido.getItensPedido());

        if (!produtosDisponiveis) {
            throw new NoSuchElementException("Um ou mais produtos não estao disponiveis");
        }

        double valorTotal = calcularValorTotal(pedido.getItensPedido());
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(StatusPedidoEnum.PEDIDO_RECEBIDO);

        atualizarEstoqueProdutos(pedido.getItensPedido(), RETIRAR_ESTOQUE);

        return pedidoRepository.save(pedido);
    }

    public void cancelarPedido(Integer pedidoId) {
        Pedido pedidoExistente = pedidoRepository.findById(pedidoId).orElse(null);

        if (pedidoExistente != null) {
            pedidoRepository.delete(pedidoExistente);

            atualizarEstoqueProdutos(pedidoExistente.getItensPedido(), INSERIR_ESTOQUE);
        } else {
            throw new NoSuchElementException("Pedido com código {} não encontrado" + pedidoId);
        }
    }

    public Pedido atualizarStatus(Integer pedidoId, StatusPedidoEnum status) {

        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

        if (pedido != null) {
            pedido.setStatus(status);
            pedidoRepository.save(pedido);
        } else {
            throw new NoSuchElementException("Pedido com código {} não encontrado" + pedidoId);
        }

        return pedido;
    }

    public Pedido incluirEntregador(Integer pedidoId, Integer entregadorId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

        if (pedido != null) {
            pedido.setEntregadorId(entregadorId);
            pedidoRepository.save(pedido);
        } else {
            throw new NoSuchElementException("Pedido com código {} não encontrado" + pedidoId);
        }

        return pedido;
    }

    private boolean verificarDisponibilidadeProdutos(List<ItemPedido> itensPedidos) {
        for (ItemPedido itemPedido : itensPedidos) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://msprodutos:8082/api/produtos/{produtoId}",
//                    "http://localhost:8082/api/produtos/{produtoId}",
                    String.class,
                    idProduto
            );

            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoSuchElementException("Produto não encontrado");
            } else {
                try {
                    JsonNode produtoJson = objectMapper.readTree(response.getBody());
                    int quantidadeEstoque = produtoJson.get("quantidade_estoque").asInt();

                    if (quantidadeEstoque < quantidade) {
                        return false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Erro no metodo verificarDisponibilidadeProdutos");
                }
            }
        }
        return true;
    }

    private Double calcularValorTotal(List<ItemPedido> itemsPedido) {
        double valorTotal = 0.0;

        for (ItemPedido itemPedido : itemsPedido) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://msprodutos:8082/api/produtos/{idProduto}",
//                    "http://localhost:8082/api/produtos/{idProduto}",
                    String.class,
                    idProduto
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    JsonNode produtoJson = objectMapper.readTree(response.getBody());
                    double preco = produtoJson.get("preco").asDouble();
                    valorTotal += preco * quantidade;
                } catch (IOException e) {
                    throw new RuntimeException("Erro no metodo verificarDisponibilidadeProdutos");
                }
            }
        }
        return valorTotal;
    }

    private void atualizarEstoqueProdutos(List<ItemPedido> itensPedido, String entradaSaida) {

        for (ItemPedido itemPedido : itensPedido) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            restTemplate.put(
                    "http://msprodutos:8082/api/produtos/atualizar/estoque/{idProduto}/{quantidade}/{entradaSaida}",
//                    "http://localhost:8082/api/produtos/atualizar/estoque/{idProduto}/{quantidade}/{entradaSaida}",
                    null,
                    idProduto,
                    quantidade,
                    entradaSaida
            );
        }
    }

}
