package com.swapll.gradu.model.dto.mappers;



import com.swapll.gradu.model.Offer;

import com.swapll.gradu.model.Category;
import com.swapll.gradu.model.dto.OfferDTO;

public class OfferMapper {

    public static OfferDTO toDTO(Offer offer) {
        OfferDTO dto = new OfferDTO();
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        dto.setPrice(offer.getPrice());
        dto.setDeliveryTime(offer.getDeliveryTime());
        dto.setPaymentMethod(offer.getPaymentMethod());
        dto.setAllowSwap(offer.isAllowSwap());
        dto.setStatus(offer.getStatus());
        dto.setImage(offer.getImage());
        dto.setType(offer.getType());
        dto.setCategoryId(offer.getCategory().getId());
        dto.setId(offer.getId());
        dto.setOwnerId(offer.getOwner().getId());
        return dto;
    }

    public static Offer toEntity(OfferDTO dto, Category category) {
        Offer offer = new Offer();
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setPrice(dto.getPrice()*5);
        offer.setDeliveryTime(dto.getDeliveryTime());
        offer.setPaymentMethod(dto.getPaymentMethod());
        offer.setAllowSwap(dto.isAllowSwap());
        offer.setStatus(dto.getStatus());
        offer.setImage(dto.getImage());
        offer.setType(dto.getType());
        offer.setCategory(category);
        return offer;
    }
}
