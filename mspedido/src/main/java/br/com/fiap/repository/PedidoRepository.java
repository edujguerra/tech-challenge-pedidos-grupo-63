package br.com.fiap.repository;

import br.com.fiap.model.enums.StatusPedidoEnum;
import br.com.fiap.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p FROM Pedido p WHERE p.status = :status")
    List<Pedido> findByStatus(StatusPedidoEnum status);
}
