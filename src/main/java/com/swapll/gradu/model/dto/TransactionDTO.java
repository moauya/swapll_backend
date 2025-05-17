package com.swapll.gradu.model.dto;

import com.swapll.gradu.model.Enum.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionDTO {
    private int id ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int offerId;
    private int sellerId;
    private int buyerId;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private boolean buyerConfirmed;
    private boolean sellerConfirmed;


}
