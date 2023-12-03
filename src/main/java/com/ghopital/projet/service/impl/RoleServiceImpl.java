package com.ghopital.projet.service.impl;

import com.ghopital.projet.auth.AuthenticationFacade;
import com.ghopital.projet.dto.ROLES;
import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.entity.Role;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.RoleMapper;
import com.ghopital.projet.repository.RoleRepository;
import com.ghopital.projet.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final AuthenticationFacade authenticationFacade;
    public RoleServiceImpl(RoleRepository roleRepository, AuthenticationFacade authenticationFacade) {
        this.roleRepository = roleRepository;
        this.authenticationFacade = authenticationFacade;
    }
    @Override
    @Transactional
    public RoleDtoResponse createRole(RoleDtoRequest roleDto) {
        // check if already exist in db
        if(roleRepository.existsByName(roleDto.name())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Role already exist");
        }

        // convert DTO to role entity
        Role role = RoleMapper.INSTANCE.roleDtoRequestToRole(roleDto);

        // save role in db
        Role savedRole = roleRepository.save(role);

        return RoleMapper.INSTANCE.roleToRoleDtoResponse(savedRole);
    }

    @Override
    public RoleDtoResponse getRoleById(Long roleId) {
        // get role from db
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleId.toString())
        );

        return RoleMapper.INSTANCE.roleToRoleDtoResponse(role);
    }

    @Override
    public RoleDtoResponse getRoleByName(String name) {
        // get role from db
        Role role = roleRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", name)
        );

        return RoleMapper.INSTANCE.roleToRoleDtoResponse(role);
    }

    @Override
    public List<RoleDtoResponse> getAllRoles() {
        boolean isAdmin = authenticationFacade.getAuthentication().getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(ROLES.ROLE_ADMIN.name()));

        Stream<Role> roles = roleRepository.findAll().stream();

        if(!isAdmin) {
            roles = roles.filter(role -> !role.getName().equals(ROLES.ROLE_ADMIN.name()));
        }

        return roles.map(RoleMapper.INSTANCE::roleToRoleDtoResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDtoResponse updateRole(RoleDtoRequest roleDto, Long roleId) {
        // get role from db
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleId.toString())
        );

        if(StringUtils.hasText(roleDto.name())) {
            role.setName(roleDto.name());
        }

        // save updated role in db
        Role updatedRole = roleRepository.save(role);

        return RoleMapper.INSTANCE.roleToRoleDtoResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        // get role from db
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleId.toString())
        );

        // delete role from db
        roleRepository.delete(role);
    }
}
