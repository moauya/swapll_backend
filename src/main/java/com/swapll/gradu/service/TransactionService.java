package com.swapll.gradu.service;

import com.swapll.gradu.model.Enum.TransactionStatus;
import com.swapll.gradu.model.Offer;
import com.swapll.gradu.model.Transaction;
import com.swapll.gradu.model.User;
import com.swapll.gradu.model.dto.TransactionDTO;
import com.swapll.gradu.repository.OfferRepository;
import com.swapll.gradu.repository.TransactionRepository;
import com.swapll.gradu.repository.UserRepository;
import com.swapll.gradu.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Transactional
    public TransactionDTO startTransaction(int sellerId, int offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        User buyer = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));



        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if(buyer.getBalance()<offer.getPrice()){
            throw new IllegalStateException("the balance is not enough ");
        }


        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setOffer(offer);
        transaction.setStatus(TransactionStatus.PENDING);

        buyer.addBuyerTransaction(transaction);
        seller.addSellerTransaction(transaction);

        Transaction savedTransaction = transactionRepository.save(transaction);


        Hibernate.initialize(savedTransaction.getBuyer());
        Hibernate.initialize(savedTransaction.getSeller());
        Hibernate.initialize(savedTransaction.getOffer());

        TransactionDTO dto = new TransactionDTO();
        dto.setId(savedTransaction.getId());
        dto.setCreatedAt(savedTransaction.getCreatedAt());
        dto.setUpdatedAt(savedTransaction.getUpdatedAt());
        dto.setOfferId(savedTransaction.getOffer().getId());
        dto.setSellerId(savedTransaction.getSeller().getId());
        dto.setBuyerId(savedTransaction.getBuyer().getId());
        dto.setStatus(savedTransaction.getStatus());

        return dto;
    }
    @Transactional
    public TransactionDTO acceptTransaction(int transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only pending transactions can be accepted");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User authenticatedUser = userDetails.getUser();

        if (transaction.getSeller().getId() != authenticatedUser.getId()) {
            throw new IllegalStateException("Only the seller can accept the transaction");
        }

        User buyer=transaction.getBuyer();
        Offer offer=transaction.getOffer();

        if(buyer.getBalance()<offer.getPrice()){
            throw new IllegalStateException("the balance is not enough ");
        }

        transaction.setStatus(TransactionStatus.ACTIVE);


        Transaction updatedTransaction = transactionRepository.save(transaction);

        TransactionDTO savedTransactionDTO=new TransactionDTO();
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setOfferId(transaction.getOffer().getId());
        dto.setSellerId(transaction.getSeller().getId());
        dto.setBuyerId(transaction.getBuyer().getId());
        dto.setStatus(transaction.getStatus());
        return dto;
    }




    @Transactional
    public TransactionDTO rejectTransaction(int transactionId) {
        // Retrieve the transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User authenticatedUser = userDetails.getUser();


        if (transaction.getSeller().getId() != authenticatedUser.getId()) {
            throw new IllegalStateException("Only the seller can accept the transaction");
        }


        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only pending transactions can be rejected");
        }


        transaction.setStatus(TransactionStatus.DECLINED);


        Transaction updatedTransaction = transactionRepository.save(transaction);

        TransactionDTO savedTransactionDTO=new TransactionDTO();
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setOfferId(transaction.getOffer().getId());
        dto.setSellerId(transaction.getSeller().getId());
        dto.setBuyerId(transaction.getBuyer().getId());
        dto.setStatus(transaction.getStatus());

        return dto;
    }






    @Transactional
    public TransactionDTO confirmTransaction(int transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.ACTIVE) {
            throw new IllegalStateException("Only accepted transactions can be confirmed");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User currentUser = userDetails.getUser();

        boolean isBuyer;
        isBuyer = transaction.getBuyer().getId().equals(currentUser.getId());
        boolean isSeller = transaction.getSeller().getId().equals(currentUser.getId());

        if (!isBuyer && !isSeller) {
            throw new IllegalStateException("Only the buyer or seller can confirm the transaction");
        }

        if (isBuyer) {
            if (transaction.isBuyerConfirmed()) {
                throw new IllegalStateException("Buyer already confirmed");
            }
            transaction.setBuyerConfirmed(true);
        }

        if (isSeller) {
            if (transaction.isSellerConfirmed()) {
                throw new IllegalStateException("Seller already confirmed");
            }
            transaction.setSellerConfirmed(true);
        }

        // If both confirmed, transfer balance and complete the transaction
        if (transaction.isBuyerConfirmed() && transaction.isSellerConfirmed()) {
            Offer offer = transaction.getOffer();
            int price = offer.getPrice();
            User buyer = transaction.getBuyer();
            User seller = transaction.getSeller();

            if (buyer.getBalance() < price) {
                throw new IllegalStateException("Buyer no longer has enough balance");
            }

            buyer.setBalance(buyer.getBalance() - price);
            seller.setBalance(seller.getBalance() + price);
            transaction.setStatus(TransactionStatus.COMPLETED);

            userRepository.save(buyer);
            userRepository.save(seller);
        }

        transactionRepository.save(transaction);
        TransactionDTO savedTransactionDTO=new TransactionDTO();
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setOfferId(transaction.getOffer().getId());
        dto.setSellerId(transaction.getSeller().getId());
        dto.setBuyerId(transaction.getBuyer().getId());
        dto.setStatus(transaction.getStatus());
        return dto;
    }


    public List<TransactionDTO> getIncomingTransactionsForSeller() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User seller = userDetails.getUser();

        List<Transaction> transactions = transactionRepository.findBySellerAndStatus(seller, TransactionStatus.PENDING);
        List<TransactionDTO> dtos = new ArrayList<>();

        for (Transaction transaction : transactions) {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(transaction.getId());
            dto.setCreatedAt(transaction.getCreatedAt());
            dto.setUpdatedAt(transaction.getUpdatedAt());
            dto.setStatus(transaction.getStatus());
            dto.setBuyerId(transaction.getBuyer().getId());
            dto.setSellerId(transaction.getSeller().getId());
            dto.setOfferId(transaction.getOffer().getId());

            dtos.add(dto);
        }

        return dtos;
    }


    public TransactionDTO getTransactionById(int id) {
        Transaction transaction=transactionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("no transaction with this id"));

        TransactionDTO dto=new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setStatus(transaction.getStatus());
        dto.setBuyerId(transaction.getBuyer().getId());
        dto.setSellerId(transaction.getSeller().getId());
        dto.setOfferId(transaction.getOffer().getId());
        dto.setBuyerConfirmed(transaction.isBuyerConfirmed());
        dto.setSellerConfirmed(transaction.isSellerConfirmed());

        return dto;

    }
}
