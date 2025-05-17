package com.swapll.gradu.model;

import com.swapll.gradu.model.Enum.OfferType;
import com.swapll.gradu.model.Enum.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="offer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OfferType type;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "price")
    @NotNull
    private int price;

    @Column(name="offer_img")
    private String image;

    @Column(name = "delivery_time")


    private int deliveryTime;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentMethod paymentMethod;


    @NotBlank
    private String status = "active";

    @Column(name = "allow_swap")
    private boolean allowSwap = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy = "offer", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();



    public void addReview(Review review) {
        reviews.add(review);
        review.setOffer(this);
    }
}
