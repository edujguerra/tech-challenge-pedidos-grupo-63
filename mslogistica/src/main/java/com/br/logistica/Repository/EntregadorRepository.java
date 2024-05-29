package com.br.logistica.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.logistica.Entity.Entregador;
@Repository
public interface EntregadorRepository extends JpaRepository<Entregador,Integer>{
    
}
