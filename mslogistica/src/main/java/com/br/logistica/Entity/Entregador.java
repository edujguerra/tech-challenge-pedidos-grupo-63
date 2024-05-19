package com.br.logistica.Entity;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entregador {
    private Long id;
    private String nome;
    private String cpf;
}
