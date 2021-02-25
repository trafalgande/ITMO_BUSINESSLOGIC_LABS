package se.ifmo.pepe.lab1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "SKINS")
public class Skin implements Serializable {
    /*
    * AUTO GENERATED ID
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*
    * EXPLICIT FROM REQUEST
    * */
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    /*
    * IMPLICIT FROM SkinService.class
    * */
    @Column(name = "dl_url")
    private String dlUrl;
    @Column(name = "approved")
    private Boolean approved;

    /*
    * BUILDER SETTERS
    * */
    public Skin setId(Long id) {
        this.id = id;
        return this;
    }

    public Skin setTitle(String title) {
        this.title = title;
        return this;
    }

    public Skin setDescription(String description) {
        this.description = description;
        return this;
    }

    public Skin setDlUrl(String dlUrl) {
        this.dlUrl = dlUrl;
        return this;
    }

    public Skin setApproved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public Skin setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Skin setPrice(Double price) {
        this.price = price;
        return this;
    }
}
