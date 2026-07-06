package com.aurafarm.backend.dto.mapper;

import com.aurafarm.backend.dto.request.ProdutoRequest;
import com.aurafarm.backend.dto.response.ProdutoListItemResponse;
import com.aurafarm.backend.dto.response.ProdutoResponse;
import com.aurafarm.backend.entity.Produto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {

    @Mapping(target = "fornecedorId", source = "fornecedor.id")
    @Mapping(target = "fornecedorNome", source = "fornecedor.nome")
    ProdutoResponse toResponse(Produto produto);

    ProdutoListItemResponse toListItem(Produto produto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "itensVenda", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Produto toEntity(ProdutoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fornecedor", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "itensVenda", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ProdutoRequest request, @MappingTarget Produto produto);
}
