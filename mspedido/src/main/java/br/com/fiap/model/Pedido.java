package br.com.fiap.model;

import br.com.fiap.model.Enum.StatusPedidoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeCliente;

    @Column(name = "cpf_cliente")
    private String cpfCliente;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> itensPedido;

    private double valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusPedidoEnum status;

    @Column(name = "entregador_id")
    private Integer entregadorId;

    @Column(name = "pedido_cep")
    private String cep;
}
