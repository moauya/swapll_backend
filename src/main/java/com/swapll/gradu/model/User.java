package com.swapll.gradu.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    @NotNull
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "bio")
    private String bio;

    @Lob
    @Column(name = "profile_pic", columnDefinition = "LONGBLOB")
    private byte[] profilePic;

    @Column(name = "my_referral_code")
    private String myReferralCode;

    @Column(name = "balance")
    private int balance ;

    @Column(name = "referral_code")
    private String referralCode;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> sellerTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> buyerTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();



    public User() {

        this.myReferralCode = generateReferralCode();
       balance=10;

    }

    private String generateReferralCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


    public void addOffer(Offer offer) {
        offers.add(offer);
        offer.setOwner(this);
    }

    public void addBuyerTransaction(Transaction transaction) {
        buyerTransactions.add(transaction);
        transaction.setBuyer(this);
    }
    public void addSellerTransaction(Transaction transaction) {
        sellerTransactions.add(transaction);
        transaction.setSeller(this);
    }


}
