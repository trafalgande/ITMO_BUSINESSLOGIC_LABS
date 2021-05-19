package se.ifmo.pepe.lab1.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Role;
import se.ifmo.pepe.lab1.repository.RoleRepository;
import se.ifmo.pepe.lab1.service.RoleService;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> fetchRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
