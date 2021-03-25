package se.ifmo.pepe.lab1.model;

import lombok.Data;
import se.ifmo.pepe.lab1.exception.PaymentException;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "wallets")
public class Wallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rub")
    private Double rub = 0.d;

    public void withdraw(Double amount, Wallet target) throws PaymentException {
        if (this.rub >= amount) {
            this.rub -= amount;
            target.deposit(amount);
        } else
            throw new PaymentException("Owner doesn't have ehough money to proceed withdrawing");
    }

    public void deposit(Double amount) {
        this.rub += amount;
    }
}

