package com.swapll.gradu.model.dto;

import com.swapll.gradu.model.Enum.OfferType;
import com.swapll.gradu.model.Enum.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {

    private int id;
    private String title;
    private String description;
    private int price;
    private int deliveryTime;
    private PaymentMethod paymentMethod;
    private boolean allowSwap;
    private String status;
    private String image;
    private OfferType type;
    private int categoryId;
    private int OwnerId;
}
