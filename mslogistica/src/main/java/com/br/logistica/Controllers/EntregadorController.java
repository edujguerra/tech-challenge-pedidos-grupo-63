package com.br.logistica.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.logistica.Entity.Entregador;
import com.br.logistica.Service.EntregadorService;
@RestController
@RequestMapping("/api/entregador")
public class EntregadorController {

    private final EntregadorService service;

    public EntregadorController(EntregadorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Entregador> buscarTodos() {

        return service.buscarTodos();
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody Entregador entregador){

        entregador = service.salvar(entregador);
        return new ResponseEntity<>(entregador, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarUm(@PathVariable Integer id) {

        return service.buscarUm(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer id, @RequestBody Entregador novo) {

        return service.atualizar(id,novo);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {

        service.excluir(id);
    }
}
