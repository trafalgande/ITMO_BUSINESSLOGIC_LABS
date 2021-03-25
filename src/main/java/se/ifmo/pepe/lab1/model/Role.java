package se.ifmo.pepe.lab1.model;


import org.springframework.security.core.GrantedAuthority;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Table(name = "ROLES")
public class Role implements Serializable, GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public String getAuthority() {
        return getName();
    }
}
