package com.aurafarm.backend.entity;

import com.aurafarm.backend.enums.StatusPedido;
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
@Table(name = "venda",
        indexes = {
                @Index(name = "idx_venda_usuario", columnList = "usuario_id"),
                @Index(name = "idx_venda_data_pedido", columnList = "data_pedido"),
                @Index(name = "idx_venda_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    private Long id;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "status_pedido")
    @Builder.Default
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(name = "valor_total", precision = 12, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "quantidade_total", nullable = false)
    @Builder.Default
    private Integer quantidadeTotal = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", nullable = false, unique = true)
    private Endereco endereco;

    @OneToMany(mappedBy = "venda", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemVenda> itens = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dataPedido == null) dataPedido = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        item.setVenda(this);
        recalcularTotais();
    }

    public void removerItem(ItemVenda item) {
        itens.remove(item);
        item.setVenda(null);
        recalcularTotais();
    }

    private void recalcularTotais() {
        this.valorTotal = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.quantidadeTotal = itens.stream()
                .mapToInt(ItemVenda::getQuantidade)
                .sum();
    }
}