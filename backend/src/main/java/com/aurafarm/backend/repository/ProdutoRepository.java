package com.aurafarm.backend.repository;

import com.aurafarm.backend.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Produto> findByFornecedorId(Long fornecedorId);
    List<Produto> findByUsuarioId(Long usuarioId);
    List<Produto> findByStatus(com.aurafarm.backend.enums.StatusProduto status);
}