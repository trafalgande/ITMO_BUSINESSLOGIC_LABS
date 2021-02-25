package se.ifmo.pepe.lab1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Table(name = "AUTHORS")
@Entity
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
