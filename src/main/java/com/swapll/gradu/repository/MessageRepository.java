package com.swapll.gradu.repository;

import com.swapll.gradu.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChatIdOrderByTimestamp(Integer chatId);
    Message findTopByChatIdOrderByTimestampDesc(Integer chatId);

}
