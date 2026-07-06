package com.aurafarm.backend.repository;

import com.aurafarm.backend.entity.ItemVenda;
import com.aurafarm.backend.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    List<ItemVenda> findByVenda(Venda venda);
    List<ItemVenda> findByProdutoId(Long produtoId);
    boolean existsByProdutoId(Long produtoId);
}