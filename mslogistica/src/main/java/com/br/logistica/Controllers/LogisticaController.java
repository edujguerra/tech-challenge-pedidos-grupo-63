package com.br.logistica.Controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.logistica.Entity.EntregadorDTO;
import com.br.logistica.Entity.Enum.StatusPedidoEnum;
import com.br.logistica.Service.LogisticaService;
import com.fasterxml.jackson.databind.JsonNode;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/api/logistica")
public class LogisticaController {
    
    @Autowired
    LogisticaService service;

    @PostMapping
    public ResponseEntity<String> adicionarEntregadorPedido(@RequestBody EntregadorDTO body) {

        return ResponseEntity.ok(service.adicionarEntregador(body));
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<String> alterarStatusPedido(@PathVariable Long idPedido,@RequestBody JsonNode body){

        return ResponseEntity.ok(service.alterarStatus(StatusPedidoEnum.obterStatusPorNomeOuStringEnum(body.findValue("status").asText()),idPedido));

    }

    //Processo que busca todos os pedidos pagos, gera os relatorios para entrega e altera o status dos pedidos.
    @GetMapping("/relatorio")
    public ResponseEntity<String> processarRelatorio() throws JRException, IOException {

        return ResponseEntity.ok(service.processoRelatorio());
    }

}
