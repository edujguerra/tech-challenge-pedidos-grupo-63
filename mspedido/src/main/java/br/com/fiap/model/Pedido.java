package br.com.fiap.model;

import br.com.fiap.model.Enum.StatusPedidoEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeCliente;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> itensPedido;
    private double valorTotal;
    @Enumerated(EnumType.STRING)
    private StatusPedidoEnum status;
//    @OneToOne
//    @JoinColumn(name = "entregador_id")
//    private Entregador entregador;
    @Column(name = "entregador_id")
    private Integer entregadorId;
}
