package br.com.fiap.controller;

import br.com.fiap.model.enums.StatusPedidoEnum;
import br.com.fiap.model.Pedido;
import br.com.fiap.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Requisição para registro de pedidos",
            description = "Ids do pedido e do itensPedido são gerados automaticamente, assim como status e valorTotal"
    )
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(pedido);
            return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>("Um ou mais produtos não estao disponiveis", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Requisição para buscar todos os pedidos",
            description = "Requisição que faz a busca de todos os pedidos ativos"
    )
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @Operation(
            summary = "Requisição para buscar pedido por Id",
            description = "Requisição que faz a busca de um pedidos de acordo com seu id"
    )
    @GetMapping("/{pedidoId}")
    public Pedido obterPedidoPorId(@PathVariable Integer pedidoId) {
        return pedidoService.obterPedido(pedidoId);
    }

    @Operation(
            summary = "Requisição para buscar pedidos filtrado por status",
            description = "Requisição que faz a busca de todos os pedidos com o mesmo status, passado na url"
    )
    @GetMapping("/{statusPedido}")
    public ResponseEntity<?> obterPedidoPorStatus(@PathVariable String statusPedido) {
        try {
            List<Pedido> listaPedidos = pedidoService.obterPedidoPorStatus(statusPedido);
            return new ResponseEntity<>(listaPedidos, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum pedido encontrado para o status fornecido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar a solicitação.");
        }
    }

    @Operation(
            summary = "Requisição para cancelar um pedido, de acordo com o seu Id",
            description = "Requisição que faz o cancelamento de um pedido e devolve o produto ao estoque."
    )
    @DeleteMapping("/{pedidoId}")
    public void cancelarPedido(@PathVariable Integer pedidoId) {
        pedidoService.cancelarPedido(pedidoId);
    }

    @Operation(
            summary = "Requisição para atualizar o status do pedido",
            description = "Requisição que faz a atualizacao de status do pedido, vindo do microsservico de logistica"
    )
    @PutMapping("/{pedidoId}")
    public Pedido atualizarStatus(@PathVariable Integer pedidoId, @RequestBody StatusPedidoEnum status) {
        return pedidoService.atualizarStatus(pedidoId, status);
    }

    @Operation(
            summary = "Requisição para vincular um entregador ao pedido",
            description = "Requisição que vincula um entregador ao pedido, vindo do microsservico de logistica"
    )
    @PutMapping("/entregador/{pedidoId}/{entregadorId}")
    public Pedido incluirEntregador(@PathVariable Integer pedidoId, @PathVariable Integer entregadorId) {
        return pedidoService.incluirEntregador(pedidoId, entregadorId);
    }

}
