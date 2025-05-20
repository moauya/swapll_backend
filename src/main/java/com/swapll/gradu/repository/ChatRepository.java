package com.swapll.gradu.repository;

import com.swapll.gradu.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);

    Optional<Chat> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
    Optional<Chat> findByReceiverIdAndSenderId(Integer receiverId, Integer senderId);
}
