package com.education.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.education.domain.Conversation;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {

	public List<Conversation> findByOwner1(int userId);
	
	public List<Conversation> findByOwner2(int userId);

	public Conversation findByOwner1InAndOwner2In(int owner1id, int owner2id);

	public Conversation findById(int conversationId);

}
