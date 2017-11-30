package com.education.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.education.domain.Conversation;
import com.education.domain.Message;
import com.education.domain.User;
import com.education.repository.MessageRepository;
import com.education.service.ConversationService;
import com.education.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private ConversationService conversationService;
	
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public List<Message> findByConversation(Conversation conversation) {
		
		return messageRepository.findByConversation(conversation);
	}

	@Override
	public Message openMessage(Conversation conversation, User from, String message) {
		
		Message newMessage = new Message();
		newMessage.setConversation(conversation);
		newMessage.setFrom(from);
		newMessage.setDate(new Date());
		newMessage.setRed(false);
		newMessage.setTitle("title");
		newMessage.setBody(message);
		newMessage.setActClass("ac-new");
		save(newMessage);
		conversation.getMessageList().add(newMessage);
		conversation.setLastDate(newMessage.getDate());
		
		conversationService.save(conversation);
		
		return null;
	}

	@Override
	public Message save(Message message) {
		
		return messageRepository.save(message);
	}

}
