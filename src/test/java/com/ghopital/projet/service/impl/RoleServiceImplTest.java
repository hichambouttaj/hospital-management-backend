package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.auth.AuthenticationFacade;
import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.entity.Role;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RoleServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class RoleServiceImplTest {
    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    /**
     * Method under test:  {@link RoleServiceImpl#createRole(RoleDtoRequest)}
     */
    @Test
    void testCreateRole() {
        when(roleRepository.existsByName(Mockito.<String>any())).thenReturn(true);
        assertThrows(AppException.class, () -> roleServiceImpl.createRole(new RoleDtoRequest("Name")));
        verify(roleRepository).existsByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#createRole(RoleDtoRequest)}
     */
    @Test
    void testCreateRole2() {
        Role role = new Role();
        role.setName("Name");
        when(roleRepository.existsByName(Mockito.<String>any())).thenReturn(false);
        when(roleRepository.save(Mockito.<Role>any())).thenReturn(role);
        RoleDtoResponse actualCreateRoleResult = roleServiceImpl.createRole(new RoleDtoRequest("Name"));
        verify(roleRepository).existsByName(Mockito.<String>any());
        verify(roleRepository).save(Mockito.<Role>any());
        assertEquals("Name", actualCreateRoleResult.name());
        assertNull(actualCreateRoleResult.id());
    }

    /**
     * Method under test:  {@link RoleServiceImpl#createRole(RoleDtoRequest)}
     */
    @Test
    void testCreateRole3() {
        when(roleRepository.existsByName(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> roleServiceImpl.createRole(new RoleDtoRequest("Name")));
        verify(roleRepository).existsByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#getRoleById(Long)}
     */
    @Test
    void testGetRoleById() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        RoleDtoResponse actualRoleById = roleServiceImpl.getRoleById(1L);
        verify(roleRepository).findById(Mockito.<Long>any());
        assertEquals("Name", actualRoleById.name());
        assertNull(actualRoleById.id());
    }

    /**
     * Method under test: {@link RoleServiceImpl#getRoleById(Long)}
     */
    @Test
    void testGetRoleById2() {
        Optional<Role> emptyResult = Optional.empty();
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> roleServiceImpl.getRoleById(1L));
        verify(roleRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link RoleServiceImpl#getRoleById(Long)}
     */
    @Test
    void testGetRoleById3() {
        when(roleRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> roleServiceImpl.getRoleById(1L));
        verify(roleRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#getRoleByName(String)}
     */
    @Test
    void testGetRoleByName() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);
        when(roleRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        RoleDtoResponse actualRoleByName = roleServiceImpl.getRoleByName("Name");
        verify(roleRepository).findByName(Mockito.<String>any());
        assertEquals("Name", actualRoleByName.name());
        assertNull(actualRoleByName.id());
    }

    /**
     * Method under test: {@link RoleServiceImpl#getRoleByName(String)}
     */
    @Test
    void testGetRoleByName2() {
        Optional<Role> emptyResult = Optional.empty();
        when(roleRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> roleServiceImpl.getRoleByName("Name"));
        verify(roleRepository).findByName(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link RoleServiceImpl#getRoleByName(String)}
     */
    @Test
    void testGetRoleByName3() {
        when(roleRepository.findByName(Mockito.<String>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> roleServiceImpl.getRoleByName("Name"));
        verify(roleRepository).findByName(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link RoleServiceImpl#getAllRoles()}
     */
    @Test
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        List<RoleDtoResponse> actualAllRoles = roleServiceImpl.getAllRoles();
        verify(authenticationFacade).getAuthentication();
        verify(roleRepository).findAll();
        assertTrue(actualAllRoles.isEmpty());
    }

    /**
     * Method under test:  {@link RoleServiceImpl#getAllRoles()}
     */
    @Test
    void testGetAllRoles2() {
        when(roleRepository.findAll()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(AppException.class, () -> roleServiceImpl.getAllRoles());
        verify(authenticationFacade).getAuthentication();
        verify(roleRepository).findAll();
    }

    /**
     * Method under test: {@link RoleServiceImpl#getAllRoles()}
     */
    @Test
    void testGetAllRoles3() {
        Role role = new Role();
        role.setName("Name");

        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(role);
        when(roleRepository.findAll()).thenReturn(roleList);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        List<RoleDtoResponse> actualAllRoles = roleServiceImpl.getAllRoles();
        verify(authenticationFacade).getAuthentication();
        verify(roleRepository).findAll();
        RoleDtoResponse getResult = actualAllRoles.get(0);
        assertEquals("Name", getResult.name());
        assertNull(getResult.id());
        assertEquals(1, actualAllRoles.size());
    }

    /**
     * Method under test: {@link RoleServiceImpl#getAllRoles()}
     */
    @Test
    void testGetAllRoles4() {
        Role role = new Role();
        role.setName("Name");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");

        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(role2);
        roleList.add(role);
        when(roleRepository.findAll()).thenReturn(roleList);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        List<RoleDtoResponse> actualAllRoles = roleServiceImpl.getAllRoles();
        verify(authenticationFacade).getAuthentication();
        verify(roleRepository).findAll();
        RoleDtoResponse getResult = actualAllRoles.get(0);
        assertEquals("Name", getResult.name());
        assertNull(getResult.id());
        assertEquals(1, actualAllRoles.size());
    }

    /**
     * Method under test:  {@link RoleServiceImpl#getAllRoles()}
     */
    @Test
    void testGetAllRoles5() {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials", "JaneDoe"));
        List<RoleDtoResponse> actualAllRoles = roleServiceImpl.getAllRoles();
        verify(authenticationFacade).getAuthentication();
        verify(roleRepository).findAll();
        assertTrue(actualAllRoles.isEmpty());
    }

    /**
     * Method under test: {@link RoleServiceImpl#getAllRoles()}
     */
    @Test
    void testGetAllRoles6() {
        Role role = mock(Role.class);
        when(role.getId()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(role.getName()).thenReturn("Name");
        doNothing().when(role).setName(Mockito.<String>any());
        role.setName("Name");

        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(role);
        when(roleRepository.findAll()).thenReturn(roleList);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(AppException.class, () -> roleServiceImpl.getAllRoles());
        verify(authenticationFacade).getAuthentication();
        verify(role).getId();
        verify(role).getName();
        verify(role).setName(Mockito.<String>any());
        verify(roleRepository).findAll();
    }

    /**
     * Method under test: {@link RoleServiceImpl#updateRole(RoleDtoRequest, Long)}
     */
    @Test
    void testUpdateRole() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);

        Role role2 = new Role();
        role2.setName("Name");
        when(roleRepository.save(Mockito.<Role>any())).thenReturn(role2);
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        RoleDtoResponse actualUpdateRoleResult = roleServiceImpl.updateRole(new RoleDtoRequest("Name"), 1L);
        verify(roleRepository).findById(Mockito.<Long>any());
        verify(roleRepository).save(Mockito.<Role>any());
        assertEquals("Name", actualUpdateRoleResult.name());
        assertNull(actualUpdateRoleResult.id());
    }

    /**
     * Method under test: {@link RoleServiceImpl#updateRole(RoleDtoRequest, Long)}
     */
    @Test
    void testUpdateRole2() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);
        when(roleRepository.save(Mockito.<Role>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class, () -> roleServiceImpl.updateRole(new RoleDtoRequest("Name"), 1L));
        verify(roleRepository).findById(Mockito.<Long>any());
        verify(roleRepository).save(Mockito.<Role>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#updateRole(RoleDtoRequest, Long)}
     */
    @Test
    void testUpdateRole3() {
        Optional<Role> emptyResult = Optional.empty();
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> roleServiceImpl.updateRole(new RoleDtoRequest("Name"), 1L));
        verify(roleRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#updateRole(RoleDtoRequest, Long)}
     */
    @Test
    void testUpdateRole4() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);

        Role role2 = new Role();
        role2.setName("Name");
        when(roleRepository.save(Mockito.<Role>any())).thenReturn(role2);
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        RoleDtoResponse actualUpdateRoleResult = roleServiceImpl.updateRole(new RoleDtoRequest(""), 1L);
        verify(roleRepository).findById(Mockito.<Long>any());
        verify(roleRepository).save(Mockito.<Role>any());
        assertEquals("Name", actualUpdateRoleResult.name());
        assertNull(actualUpdateRoleResult.id());
    }

    /**
     * Method under test: {@link RoleServiceImpl#deleteRole(Long)}
     */
    @Test
    void testDeleteRole() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);
        doNothing().when(roleRepository).delete(Mockito.<Role>any());
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        roleServiceImpl.deleteRole(1L);
        verify(roleRepository).delete(Mockito.<Role>any());
        verify(roleRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#deleteRole(Long)}
     */
    @Test
    void testDeleteRole2() {
        Role role = new Role();
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);
        doThrow(new AppException(HttpStatus.CONTINUE, "An error occurred")).when(roleRepository)
                .delete(Mockito.<Role>any());
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class, () -> roleServiceImpl.deleteRole(1L));
        verify(roleRepository).delete(Mockito.<Role>any());
        verify(roleRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link RoleServiceImpl#deleteRole(Long)}
     */
    @Test
    void testDeleteRole3() {
        Optional<Role> emptyResult = Optional.empty();
        when(roleRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> roleServiceImpl.deleteRole(1L));
        verify(roleRepository).findById(Mockito.<Long>any());
    }
}
