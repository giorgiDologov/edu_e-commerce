package com.education.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.education.domain.Conversation;
import com.education.domain.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {

	List<Message> findByConversation(Conversation conversation);
}
