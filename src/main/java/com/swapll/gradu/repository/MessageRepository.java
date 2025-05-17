package com.swapll.gradu.repository;

import com.swapll.gradu.model.Message;
import com.swapll.gradu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findBySenderAndRecipient(User sender, User recipient);

    List<Message> findBySenderOrRecipient(User sender, User recipient);
}
