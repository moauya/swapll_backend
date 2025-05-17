package com.swapll.gradu.controller;

import com.swapll.gradu.model.Transaction;
import com.swapll.gradu.model.dto.TransactionDTO;
import com.swapll.gradu.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction/{sellerId}/{offerId}")
    public TransactionDTO startTransaction(@PathVariable int sellerId,@PathVariable int offerId){

        return transactionService.startTransaction(sellerId,offerId) ;
    }
    @PutMapping("/transactions/accept/{transactionId}")
    public TransactionDTO acceptTransaction(@PathVariable int transactionId) {
        return transactionService.acceptTransaction(transactionId);
    }
    @PutMapping("/transactions/reject/{transactionId}")
    public TransactionDTO rejectTransaction(@PathVariable int transactionId) {
        return transactionService.rejectTransaction(transactionId);
    }
    @PutMapping("/transactions/complete/{transactionId}")
    public TransactionDTO completeTransaction(@PathVariable int transactionId) {
        return transactionService.confirmTransaction(transactionId);
    }
    @GetMapping("/transactions/incoming")
    public List<TransactionDTO> getIncomingTransactions() {

        return transactionService.getIncomingTransactionsForSeller();
    }

    @GetMapping("/transaction/{id}")
    public TransactionDTO getTransactionById(@PathVariable int id){
        return transactionService.getTransactionById(id);
    }


}
