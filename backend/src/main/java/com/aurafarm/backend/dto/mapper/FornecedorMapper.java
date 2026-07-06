package com.aurafarm.backend.dto.mapper;

import com.aurafarm.backend.dto.request.FornecedorRequest;
import com.aurafarm.backend.dto.response.FornecedorListItemResponse;
import com.aurafarm.backend.dto.response.FornecedorResponse;
import com.aurafarm.backend.entity.Fornecedor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FornecedorMapper {

    FornecedorResponse toResponse(Fornecedor fornecedor);

    FornecedorListItemResponse toListItem(Fornecedor fornecedor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Fornecedor toEntity(FornecedorRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(FornecedorRequest request, @MappingTarget Fornecedor fornecedor);
}
