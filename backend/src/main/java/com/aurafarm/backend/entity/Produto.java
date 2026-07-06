package com.aurafarm.backend.entity;

import com.aurafarm.backend.enums.StatusProduto;
import com.aurafarm.backend.enums.Tamanho;
import jakarta.persistence.*;
import lombok.*;
import lombok.AccessLevel;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_produto_codigo", columnNames = "codigo")
        },
        indexes = {
                @Index(name = "idx_produto_fornecedor", columnList = "fornecedor_id"),
                @Index(name = "idx_produto_usuario", columnList = "usuario_id"),
                @Index(name = "idx_produto_codigo", columnList = "codigo")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tamanho", nullable = false, columnDefinition = "tamanho_produto")
    private Tamanho tamanho;

    @Column(name = "preco", precision = 12, scale = 2, nullable = false)
    private BigDecimal preco;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    @Column(name = "codigo", length = 100, nullable = false)
    private String codigo;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "descricao", length = 100)
    private String descricao;

    @Column(name = "cor", length = 100)
    private String cor;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "status_produto")
    @Builder.Default
    private StatusProduto status = StatusProduto.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Builder.Default
    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    private List<ItemVenda> itensVenda = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (quantidadeEstoque == null) quantidadeEstoque = 0;
        if (status == null) status = StatusProduto.ATIVO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}