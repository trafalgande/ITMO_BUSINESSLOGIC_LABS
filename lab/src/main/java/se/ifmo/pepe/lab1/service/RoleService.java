package se.ifmo.pepe.lab1.service;

import se.ifmo.pepe.lab1.model.Role;

import java.util.Optional;

public interface RoleService {
    Role saveRole(Role role);

    Optional<Role> fetchRoleByName(String name);
}
