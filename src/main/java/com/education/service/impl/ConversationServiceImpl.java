package com.education.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.education.domain.Conversation;
import com.education.domain.Message;
import com.education.domain.User;
import com.education.repository.ConversationRepository;
import com.education.service.ConversationService;
import com.education.service.UserService;

@Service
public class ConversationServiceImpl implements ConversationService {
	
	@Autowired
	private ConversationRepository conversationRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<Conversation> findByOwner(int userId) {
		List<Conversation> listToReturn = new ArrayList<>();
		listToReturn.addAll(conversationRepository.findByOwner1(userId));
		listToReturn.addAll(conversationRepository.findByOwner2(userId));
		
		return listToReturn;
	}

	@Override
	public Conversation openConversation(User from, User to) {
		Conversation newConversation;
		Conversation conversation = null;
		if(findByOwners(from.getId(), to.getId())!=null) {
			conversation = findByOwners(from.getId(), to.getId());
			}
			if(findByOwners(to.getId(), from.getId())!=null) {
			conversation = findByOwners(to.getId(), from.getId());
			}
		
		
		if(conversation==null) {
		
		newConversation = new Conversation();
		
		newConversation.setOwner1(from.getId());
		newConversation.setOwner2(to.getId());
		
		save(newConversation);
		
		
		} else {
		newConversation = conversation;
		}

		return newConversation;
	}

	@Override
	public Conversation save(Conversation conversation) {
		
		return conversationRepository.save(conversation);
	}

	@Override
	public Conversation findByOwners(int owner1id, int owner2id) {
		
		return conversationRepository.findByOwner1InAndOwner2In(owner1id, owner2id);
	}

	@Override
	public synchronized Message findLastMessage(Conversation conversation) {
		Message lastMsg = null;
		List<Message> messageList = conversation.getMessageList();
		if(messageList.size()>0) {
		
		for(Message message: messageList) {
			message.setDate(message.getDate());
			
			if(message.getDate().equals(conversation.getLastDate())) {
				
				//SETTING LAST MSG
				conversation.setLastMessage(message);
				
				//SETTING CONVERSATIONS LAST DATE
				conversation.setLastDate(message.getDate());
				
				//SET POST ACTIVE TO BE NOTHING
				if(conversation.getActClass()=="active") {
					conversation.setActClass("");
				}
				
				//SETTING RED
				if(conversation.getLastMessage().getRed()==true) {
					conversation.setActClass("");
				} else {
					conversation.setActClass("ac-new");
				}
		
				save(conversation);
			}
		}

		
		return lastMsg;
		} else {
			//IF NO MESSAGES FOUND, SET CONVEERSATION DATE NOW (AS FAR AS IT IS A NEW CONVERSATION)
			conversation.setLastDate(new Date());
			save(conversation);
			return null;
		}
	}

	@Override
	public Conversation findActiveConversation(int userId) {
		Map<Date, Conversation> conversationList = setConversationList(userId);
		Object firstKey = conversationList.keySet().toArray()[0];
		Object lastConv = (Conversation) conversationList.get(firstKey);
		return (Conversation) lastConv;
	}
	
	@Override
	public synchronized Map<Date, Conversation> setConversationList(int userId){
		Map<Date, Conversation> conversationList = new TreeMap<Date, Conversation>(Collections.reverseOrder()); //findByOwner(userId);
		List<Conversation> convList = findByOwner(userId);
		userService.findById(userId).setNewMessages(0);
		if(convList!=null) {
			for(Conversation conversation: convList) {
				
				//SET PARTNER
				if(conversation.getOwner1()==userId) {
					conversation.setPartner(userService.findById(conversation.getOwner2()));
				} else {
					conversation.setPartner(userService.findById(conversation.getOwner1()));
				}
				
				//FINDING AND SETTING LAST MESSAGE
				findLastMessage(conversation);
				
				if(conversation.getLastMessage()!=null) {
					if(!conversation.getLastMessage().getRed()) {
						if(conversation.getLastMessage().getFrom().getId() != userService.findById(userId).getId()) {
							userService.findById(userId).setNewMessages(userService.findById(userId).getNewMessages()+1);
						}
					}
				}
				
				save(conversation);
				
					conversationList.put(conversation.getLastDate(), conversation);
				
			}
		}
		
		return conversationList;
		
	}
	
	@Override
	public synchronized Map<Date, Conversation> searchedConversationList(int userId, String keyword){
		Map<Date, Conversation> conversationList = new TreeMap<Date, Conversation>(Collections.reverseOrder()); //findByOwner(userId);
		List<Conversation> convList = new ArrayList<>();
		for(User user: userService.findByUsernameContaining(keyword)) {
			if(findByOwners(userId, user.getId())!=null) {
			convList.add(findByOwners(userId, user.getId()));
			}
			if(findByOwners(user.getId(), userId)!=null) {
			convList.add(findByOwners(user.getId(), userId));
			}
		}
		if(convList!=null) {
			for(Conversation conversation: convList) {
				findLastMessage(conversation);
				
				//SET PARTNER
				if(conversation.getOwner1()==userId) {
					conversation.setPartner(userService.findById(conversation.getOwner2()));
				} else {
					conversation.setPartner(userService.findById(conversation.getOwner1()));
				}
				
				//SET POST ACTIVE TO BE NOTHING
				if(conversation.getActClass()=="active") {
					conversation.setActClass("");
				}
				
				save(conversation);
				
					conversationList.put(conversation.getLastDate(), conversation);
				
			}
		}
		
		return conversationList;
		
	}

	@Override
	public Conversation findById(int conversationId) {
		
		return conversationRepository.findById(conversationId);
	}




}
