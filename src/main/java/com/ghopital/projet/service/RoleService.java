package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;

import java.util.List;

public interface RoleService {
    RoleDtoResponse createRole(RoleDtoRequest roleDto);
    RoleDtoResponse getRoleByName(String name);
    RoleDtoResponse getRoleById(Long roleId);
    List<RoleDtoResponse> getAllRoles();
    RoleDtoResponse updateRole(RoleDtoRequest roleDto, Long roleId);
    void deleteRole(Long roleId);
}
