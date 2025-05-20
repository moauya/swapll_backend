package com.swapll.gradu.controller;

import com.swapll.gradu.model.Offer;
import com.swapll.gradu.model.dto.OfferDTO;
import com.swapll.gradu.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {

    @Autowired
    private OfferService offerService;



    @PostMapping("/add")
    public OfferDTO addOffer(@RequestPart("offer") OfferDTO offerDTO,
                             @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return offerService.addOffer(offerDTO, image);
    }


    @GetMapping("/offers/category/{categoryId}")
    public List<OfferDTO> getOffersByCategoryId(@PathVariable int categoryId) {
        return offerService.getAllOffersByCategoryId(categoryId);
    }
    @GetMapping("/offers/user/{userId}")
    public List<OfferDTO> getOffersByUserId(@PathVariable int userId) {
       return offerService.getAllOffersByUserId(userId);
    }
    @PutMapping("/update")
    public OfferDTO updateOffer(@RequestPart("offer") OfferDTO offerDTO,
                                @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return offerService.updateOffer(offerDTO, image);
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
