package com.aurafarm.backend.repository;

import com.aurafarm.backend.entity.Venda;
import com.aurafarm.backend.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByUsuarioId(Long usuarioId);
    List<Venda> findByStatus(StatusPedido status);
    List<Venda> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
    Page<Venda> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Venda> findByStatus(StatusPedido status, Pageable pageable);
}