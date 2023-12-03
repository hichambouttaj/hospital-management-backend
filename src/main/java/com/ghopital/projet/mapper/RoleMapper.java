package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    Role roleDtoRequestToRole(RoleDtoRequest roleDtoRequest);
    RoleDtoResponse roleToRoleDtoResponse(Role role);
}
