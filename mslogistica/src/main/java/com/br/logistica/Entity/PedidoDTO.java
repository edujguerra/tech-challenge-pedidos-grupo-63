package com.br.logistica.Entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long pedido_id;
    private String pedido_statusEntrega;
    private LocalDateTime pedido_previsaoEntrega;
    private String nomeEntregador;
}
