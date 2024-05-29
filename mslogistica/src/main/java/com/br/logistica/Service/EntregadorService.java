package com.br.logistica.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.logistica.Entity.Entregador;
import com.br.logistica.Repository.EntregadorRepository;

@Service
public class EntregadorService {

    private final EntregadorRepository entregadorRepository;

    public EntregadorService(EntregadorRepository repository) {
        this.entregadorRepository = repository;
    }

    public List<Entregador> buscarTodos() {
        return entregadorRepository.findAll();
    }

    public Entregador salvar(Entregador entregador) {

        ResponseEntity<Object> response = validaCampos(entregador);
        if (!response.getStatusCode().equals(HttpStatus.OK)  ){
            throw new NoSuchElementException("Entregador com problemas..." + response);
        }

        entregador = entregadorRepository.save(entregador);
        return entregador;
    }

    private ResponseEntity<Object> validaCampos(Entregador entregador) {

        if (entregador.getNome() == null
                || entregador.getNome().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome não pode ser vazio.");
        }
        if (entregador.getCpf() == null ||
                entregador.getCpf().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF não pode ser vazio.");
        }

        return ResponseEntity.ok(entregador);
    }

    public ResponseEntity<Object> buscarUm(Integer id ) {

        Entregador entregador = entregadorRepository.findById(id).orElse(null);

        if (entregador != null) {
            return ResponseEntity.ok(entregador);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Entregador não encontrado.");
        }
    }

    public ResponseEntity<Object> atualizar(Integer id, Entregador novo) {

        Entregador existente = entregadorRepository.findById(id).orElse(null);

        if (existente != null) {
            ResponseEntity<Object> response = validaCampos(novo);
            if (!response.getStatusCode().equals(HttpStatus.OK)  ){
                return response;
            }

            existente.setNome(novo.getNome());
            existente.setCpf(novo.getCpf());

            return ResponseEntity.ok(entregadorRepository.save(existente));
        } else {
            throw new NoSuchElementException("Entregador não Encontrado.");
        }
    }

    public boolean excluir(Integer id) {

        Entregador existente = entregadorRepository.findById(id).orElse(null);

        if (existente != null) {
            entregadorRepository.delete(existente);
        } else {
            throw new NoSuchElementException("Entregador não encontrado");
        }
        return true;
    }
    
}
