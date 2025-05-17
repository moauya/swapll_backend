package com.swapll.gradu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="title")
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Offer> offers = new ArrayList<>();

    public void addOffer(Offer offer) {
        offers.add(offer);
        offer.setCategory(this);
    }
}
