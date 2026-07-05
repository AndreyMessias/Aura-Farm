package com.aurafarm.backend.entity;

import com.aurafarm.backend.enums.Cargo;
import jakarta.persistence.*;
import lombok.*;
import lombok.AccessLevel;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_cpf", columnNames = "cpf"),
                @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_usuario_cadastrou", columnList = "usuario_cadastrou_id")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "senha", length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    @ColumnTransformer(write = "CAST(? AS tipo_usuario)")
    private Cargo cargo;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "pais", length = 100)
    private String pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastrou_id")
    private Usuario usuarioCadastrou;

    @Builder.Default
    @OneToMany(mappedBy = "usuarioCadastrou", fetch = FetchType.LAZY)
    private List<Usuario> usuariosCadastrados = new ArrayList<>();

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

    public boolean isPrimeiroAcesso() {
        return senha == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return id != null && id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}