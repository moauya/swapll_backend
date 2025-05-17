package com.swapll.gradu.controller;

import com.swapll.gradu.model.Offer;
import com.swapll.gradu.model.dto.OfferDTO;
import com.swapll.gradu.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {

    @Autowired
    private OfferService offerService;



    @PostMapping("/offer")
    public OfferDTO addOffer(@RequestBody OfferDTO offerDTO) {

        return offerService.addOffer(offerDTO);
    }

    @GetMapping("/offers/category/{categoryId}")
    public List<OfferDTO> getOffersByCategoryId(@PathVariable int categoryId) {
        return offerService.getAllOffersByCategoryId(categoryId);
    }
    @GetMapping("/offers/user/{userId}")
    public List<OfferDTO> getOffersByUserId(@PathVariable int userId) {
       return offerService.getAllOffersByUserId(userId);
    }
    @PutMapping("/offer")
    public OfferDTO updateOffer(@RequestBody OfferDTO offerDTO){

        return offerService.updateOffer(offerDTO);

    }
    @GetMapping("/offer/{id}")
    public OfferDTO getOfferById(@PathVariable int id) {
        return offerService.getOfferById(id);
    }

    @DeleteMapping("/offer/{id}")
    public void deleteOffer(@PathVariable int id) {
        offerService.deleteOffer(id);
    }

    @GetMapping("/offers/search")
    public List<OfferDTO> searchOffers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean allowSwap
    ) {
        return offerService.searchOffers(keyword, categoryId, minPrice, maxPrice, allowSwap);
    }





}
