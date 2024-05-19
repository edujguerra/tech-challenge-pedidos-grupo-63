package br.com.fiap.controller;

import br.com.fiap.model.Enum.StatusPedidoEnum;
import br.com.fiap.model.Pedido;
import br.com.fiap.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(pedido);
            return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>("Um ou mais produtos não estao disponiveis", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{pedidoId}")
    public Pedido obterPedidoPorId(@PathVariable Integer pedidoId) {
        return pedidoService.obterPedido(pedidoId);
    }

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

    @DeleteMapping("/{pedidoId}")
    public void cancelarPedido(@PathVariable Integer pedidoId) {
        pedidoService.cancelarPedido(pedidoId);
    }

    @PutMapping("/{pedidoId}")
    public Pedido atualizarStatus(@PathVariable Integer pedidoId, @RequestBody StatusPedidoEnum status) {
        return pedidoService.atualizarStatus(pedidoId, status);
    }

    @PutMapping("/entregador/{pedidoId}/{entregadorId}")
    public Pedido incluirEntregador(@PathVariable Integer pedidoId, @PathVariable Integer entregadorId) {
        return pedidoService.incluirEntregador(pedidoId, entregadorId);
    }

}
