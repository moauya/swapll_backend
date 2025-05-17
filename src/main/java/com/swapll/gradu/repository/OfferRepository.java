package com.swapll.gradu.repository;

import com.swapll.gradu.model.Offer;

import com.swapll.gradu.model.dto.OfferDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    List<Offer> findByCategoryId(int categoryId);
    List<Offer> findByOwnerId(int ownerId);
    boolean existsByOwner_IdAndId(int userId, int offerId);


    @Query("""
    SELECT o FROM Offer o
    WHERE (:keyword IS NULL OR
           LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(o.type) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:categoryId IS NULL OR o.category.id = :categoryId)
      AND (:minPrice IS NULL OR o.price >= :minPrice)
      AND (:maxPrice IS NULL OR o.price <= :maxPrice)
      AND (:allowSwap IS NULL OR o.allowSwap = :allowSwap)
""")
    List<Offer> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("allowSwap") Boolean allowSwap
    );
}
