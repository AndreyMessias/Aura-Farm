package com.aurafarm.backend.repository;

import com.aurafarm.backend.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByCnpj(String cnpj);
    Optional<Fornecedor> findByEmail(String email);
    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);
}