package se.ifmo.pepe.lab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.ifmo.pepe.lab1.model.Role;
import se.ifmo.pepe.lab1.model.Skin;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>, JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}