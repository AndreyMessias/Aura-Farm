package com.aurafarm.backend.dto.mapper;

import com.aurafarm.backend.dto.request.UsuarioRequest;
import com.aurafarm.backend.dto.response.UsuarioListItemResponse;
import com.aurafarm.backend.dto.response.UsuarioResponse;
import com.aurafarm.backend.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario usuario);

    UsuarioListItemResponse toListItem(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "usuariosCadastrados", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "usuariosCadastrados", ignore = true)
    void updateEntity(UsuarioRequest request, @MappingTarget Usuario usuario);
}