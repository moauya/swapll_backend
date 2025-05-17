package com.swapll.gradu.service;

import com.swapll.gradu.model.Offer;
import com.swapll.gradu.model.Review;
import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.ReviewDTO;
import com.swapll.gradu.repository.OfferRepository;
import com.swapll.gradu.repository.ReviewRepository;
import com.swapll.gradu.repository.UserRepository;
import com.swapll.gradu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {


    private ReviewRepository reviewRepository;
    private OfferRepository offerRepository;
    private UserRepository userRepository;

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository, OfferRepository offerRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;

    }

    public ReviewDTO addReview( ReviewDTO reviewDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User owner = userDetails.getUser();
        Offer offer = offerRepository.findById(reviewDto.getOfferId()).orElse(null);
        Review review = new Review();
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        review.setUser(owner);

            review.setOffer(offer);
            offer.addReview(review);

            reviewDto.setCreatedAt(review.getCreatedAt());
            reviewDto.setUserId(owner.getId());



        reviewRepository.save(review);

        return reviewDto ;
    }
    public List<ReviewDTO> reviewsByOffer(int offerId) {

        List<Review> reviews = reviewRepository.findAllByOffer_Id(offerId);

        if (reviews.isEmpty()) {
            return Collections.emptyList();
        }


        List<ReviewDTO> reviewDtos = reviews.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = new ReviewDTO();
                    reviewDTO.setOfferId(offerId);
                    reviewDTO.setRating(review.getRating());
                    reviewDTO.setComment(review.getComment());
                    reviewDTO.setCreatedAt(LocalDateTime.parse(review.getCreatedAt().toString()));
                    return reviewDTO;
                })
                .collect(Collectors.toList());

        return reviewDtos;
    }


    public boolean deleteReviewByOffer(int offerId, int reviewId) {
        boolean flag = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User owner = userDetails.getUser();

        Optional<Review> review = reviewRepository.findById(reviewId);
        Optional<Offer> offer = offerRepository.findById(offerId);

        if (review.isPresent() && offer.isPresent()) {
            owner.getReviews().remove(review.get());
            offer.get().getReviews().remove(review.get());

            if (review.get().getOffer().getId() == offerId && review.get().getUser().getId() == owner.getId()) {
                reviewRepository.delete(review.get());
                flag = true;
            }
        }

        return flag;
    }



}
