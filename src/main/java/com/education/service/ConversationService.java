package com.education.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.education.domain.Conversation;
import com.education.domain.Message;
import com.education.domain.User;

public interface ConversationService {

	List<Conversation> findByOwner(int userId);
	
	public Conversation openConversation(User from, User to);
	
	public Conversation save(Conversation conversation);
	
	public Conversation findByOwners(int owner1ID, int owner2ID);
	
	public Message findLastMessage(Conversation conversation);
	
	public Conversation findActiveConversation(int userId);
	
	public Map<Date, Conversation> setConversationList(int userId);
	
	public Conversation findById(int conversationId);

	Map<Date, Conversation> searchedConversationList(int userId, String keyword);
}
