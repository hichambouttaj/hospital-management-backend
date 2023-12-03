package com.ghopital.projet.controller;


import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDtoResponse> createRole(@Valid @RequestBody RoleDtoRequest roleDto) {
        RoleDtoResponse response = roleService.createRole(roleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDtoResponse> getRoleById(@PathVariable(name = "roleId") Long roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }
    @GetMapping(params = {"roleName"})
    public ResponseEntity<RoleDtoResponse> getRoleByName(@RequestParam String name) {
        return ResponseEntity.ok(roleService.getRoleByName(name));
    }
    @GetMapping
    public ResponseEntity<List<RoleDtoResponse>> getAll() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDtoResponse> updateRole(@PathVariable(name = "roleId") Long roleId, @Valid @RequestBody RoleDtoRequest roleDto) {
        return ResponseEntity.ok(roleService.updateRole(roleDto, roleId));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable(name = "roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }
}
