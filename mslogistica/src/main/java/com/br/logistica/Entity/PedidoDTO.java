package com.br.logistica.Entity;

import com.br.logistica.Entity.Enum.StatusPedidoEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long pedido_id;
    private StatusPedidoEnum pedido_statusEntrega;
    private String cpfCliente;
    private String nomeCliente;
    private String cep;
}
