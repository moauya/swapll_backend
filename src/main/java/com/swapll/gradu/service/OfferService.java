package com.swapll.gradu.service;

import com.swapll.gradu.model.Category;
import com.swapll.gradu.model.Offer;
import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.OfferDTO;
import com.swapll.gradu.model.dto.mappers.OfferMapper;
import com.swapll.gradu.repository.CategoryRepository;
import com.swapll.gradu.repository.OfferRepository;
import com.swapll.gradu.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OfferRepository offerRepository;


    @Autowired
    public OfferService(OfferRepository repo) {

        offerRepository = repo;
    }


    public OfferDTO addOffer(OfferDTO offer){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User owner = userDetails.getUser();
        Category category = categoryRepository.findById(offer.getCategoryId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));



        Offer newOffer=OfferMapper.toEntity(offer,category);
        newOffer.setOwner(owner);
        owner.addOffer(newOffer);
        category.addOffer(newOffer);

       return OfferMapper.toDTO(offerRepository.save(newOffer));

    }
    public List<OfferDTO> getAllOffersByCategoryId(int categoryId) {
        List<Offer> offers = offerRepository.findByCategoryId(categoryId);
        if (offers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return offers.stream()
                .map(OfferMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<OfferDTO> getAllOffersByUserId(int ownerId) {
        List<Offer> offers= offerRepository.findByOwnerId(ownerId);
        if (offers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
        }
        return offers.stream()
                .map(OfferMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OfferDTO updateOffer(OfferDTO offerDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User owner = userDetails.getUser();
        if(offerRepository.existsByOwner_IdAndId(owner.getId(),offerDTO.getId())){
            Category category=categoryRepository.findById(offerDTO.getCategoryId()).orElse(null);
            Offer offer=OfferMapper.toEntity(offerDTO,category);
            offerRepository.save(offer);
            return OfferMapper.toDTO(offer);
        }
        return offerDTO;


    }


    public OfferDTO getOfferById(int id) {
        Offer offer=offerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("there is no offer with this id"));
        OfferDTO dto=new OfferDTO();
        dto.setId(offer.getId());
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
        dto.setOwnerId(offer.getOwner().getId());

        return dto;

    }

    @Transactional
    public void deleteOffer(int offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User currentUser = userDetails.getUser();

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("Offer not found"));

        if (offer.getOwner() == null || offer.getOwner().getId() != currentUser.getId()) {
            throw new SecurityException("You are not authorized to delete this offer");
        }

        offerRepository.delete(offer);
    }

    public List<OfferDTO> searchOffers(String keyword, Integer categoryId, Double minPrice, Double maxPrice, Boolean allowSwap) {
        List<Offer> offers = offerRepository.search(keyword, categoryId, minPrice, maxPrice, allowSwap);

        List<OfferDTO> dtos = new ArrayList<>();
        for (Offer offer : offers) {
            OfferDTO dto = new OfferDTO();
            dto.setId(offer.getId());
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
            dto.setOwnerId(offer.getOwner().getId());
            dtos.add(dto);
        }

        return dtos;
    }


}
