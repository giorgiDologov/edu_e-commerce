package com.education.service;

import java.util.List;
import java.util.Set;

import com.education.domain.Conversation;
import com.education.domain.Message;
import com.education.domain.User;

public interface MessageService {
	
	public List<Message> findByConversation(Conversation conversation);
	
	public Message openMessage(Conversation conversation, User from, String message);
	
	public Message save(Message message);

}
