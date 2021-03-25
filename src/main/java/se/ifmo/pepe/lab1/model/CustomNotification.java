package se.ifmo.pepe.lab1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "NOTIFICATIONS")
public class CustomNotification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message")
    private String message;

    @Column(name = "is_new")
    private Boolean isNew;

    public CustomNotification setUser(User user) {
        this.user = user;
        return this;
    }

    public CustomNotification setMessage(String message) {
        this.message = message;
        return this;
    }

    public CustomNotification setIsNew(Boolean aNew) {
        isNew = aNew;
        return this;
    }
}
