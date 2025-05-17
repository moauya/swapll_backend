package com.swapll.gradu.controller;

import com.swapll.gradu.model.Review;
import com.swapll.gradu.model.dto.ReviewDTO;
import com.swapll.gradu.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public ReviewDTO addReview(@RequestBody ReviewDTO review) {
       return reviewService.addReview(review);

    }
    @GetMapping("/reviews/offer/{offerId}")
    public List<ReviewDTO> getReviewsByOffer(@PathVariable int offerId){
        return reviewService.reviewsByOffer(offerId);
    }


    @DeleteMapping("review/{offerId}/{reviewId}")
    public boolean deleteReview(@PathVariable int offerId,@PathVariable int reviewId){
      return  reviewService.deleteReviewByOffer(offerId,reviewId);
    }

}
