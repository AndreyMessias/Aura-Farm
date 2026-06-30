package com.aurafarm.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.AccessLevel;

import java.time.LocalDateTime;

@Entity
@Table(name = "fornecedor",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_fornecedor_cnpj", columnNames = "cnpj"),
                @UniqueConstraint(name = "uk_fornecedor_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_fornecedor_usuario", columnList = "usuario_id")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Long id;

    @Column(name = "cnpj", length = 14, nullable = false)
    private String cnpj;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "pais", length = 100)
    private String pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
    private java.util.List<Produto> produtos;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (pais == null) pais = "Brasil";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}