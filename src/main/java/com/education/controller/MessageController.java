package com.education.controller;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

import javax.transaction.Transactional;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.education.domain.Conversation;
import com.education.domain.User;
import com.education.service.ConversationService;
import com.education.service.MessageService;
import com.education.service.UserService;

public class MessageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ConversationService conversationService;
	
	@RequestMapping("/inbox")
	public String inbox(Principal principal, Model model) {
		//user present?, user is a teacher?
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			return "redirect:/logout";
		}
		
		User user = userService.findByUsername(principal.getName());
		
		//opening, or creating conversation list
		Map<Date, Conversation> conversationList = conversationService.setConversationList(user.getId());
		
		model.addAttribute("conversationList", conversationList);
		
		//getting last conversation to show as default
		if(conversationList.size()>0) {
		Object firstKey = conversationList.keySet().toArray()[0];
		Conversation lastConversation = (Conversation) conversationList.get(firstKey);
		
		
		if(lastConversation.getLastMessage()!=null) {
			//sender of last msg
			User fromUser = lastConversation.getLastMessage().getFrom();
			//conversation partner
			User partner = lastConversation.getPartner();
			//setting red for To user
			if(fromUser.getId()==partner.getId()) {
				if(!lastConversation.getLastMessage().getRed()) {
					lastConversation.getLastMessage().setRed(true);
					user.setNewMessages(user.getNewMessages()-1);
				}
			}
		} else {
			model.addAttribute("newConversation");
		}
		
		lastConversation.setActClass("active");
		
		conversationService.save(lastConversation);
		
		model.addAttribute("activeConversation", lastConversation);
		
		}
		
		return "account-inbox";
	}
	
	@Transactional
	@RequestMapping(value="/sendMsg")
	public String sendMessage(@ModelAttribute("id") int convId,
							 @ModelAttribute("body") String body,
							 Principal principal, Model model) {
		//user present?, user is a teacher?
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			return "redirect:/logout";
		}
		
		if(body==null || body.trim().isEmpty()) {
			return "redirect:/inbox";
		}
		
		User user = userService.findByUsername(principal.getName());
		//create and save new message
		messageService.openMessage(conversationService.findById(convId), user, body);
		
		return "redirect:/inbox";
	}
	
	@Transactional
	@RequestMapping(value="/newMessage")
	public String newMessage(@PathParam("id") int id,Principal principal, Model model) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			return "redirect:/logout";
		}
		User user = userService.findByUsername(principal.getName());
		
		Conversation lastConversation = conversationService.openConversation(userService.findById(id), user);
		
		Map<Date, Conversation> conversationList = conversationService.setConversationList(user.getId());
		
		model.addAttribute("conversationList", conversationList);
		System.out.println("OUR OPPONENTS: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " +userService.findById(id).getUsername() + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + user.getUsername());
		
		if(lastConversation.getLastMessage()!=null) {
			User fromUser = lastConversation.getLastMessage().getFrom();
			User partner = lastConversation.getPartner();
			if(fromUser.getId()==partner.getId()) {
				if(!lastConversation.getLastMessage().getRed()) {
					lastConversation.getLastMessage().setRed(true);
					user.setNewMessages(user.getNewMessages()-1);
				}
			}
		} else {
			model.addAttribute("newConversation");
		}
		lastConversation.setActClass("active");
		
			conversationService.save(lastConversation);
			
			model.addAttribute("activeConversation", lastConversation);
			
			return "account-inbox";
		
	}
	
	
	//opening chosen conversation
	@Transactional
	@RequestMapping("/message")
	public String openMessage(@ModelAttribute("id") int id, Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			System.out.println(userService.findByUsername(username));
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			return "redirect:/logout";
		}
		User user = userService.findByUsername(principal.getName());

			Map<Date, Conversation> conversationList = conversationService.setConversationList(user.getId());
			
			model.addAttribute("conversationList", conversationList);
			
			Conversation lastConversation = conversationService.findById(id);
		
			if(lastConversation.getLastMessage()!=null) {
				User fromUser = lastConversation.getLastMessage().getFrom();
				User partner = lastConversation.getPartner();
				if(fromUser.getId()==partner.getId()) {
					if(!lastConversation.getLastMessage().getRed()) {
						lastConversation.getLastMessage().setRed(true);
						user.setNewMessages(user.getNewMessages()-1);
					}
				}
			} else {
				model.addAttribute("newConversation");
			}
			lastConversation.setActClass("active");
				conversationService.save(lastConversation);
				
				
				model.addAttribute("activeConversation", lastConversation);
				
				return "account-inbox";
			
		
	}
	
	@Transactional
	@RequestMapping("/search")
	public String searchMessage(@ModelAttribute("username") String username, Model model, Principal principal) {
		if(principal != null) {
			User user = userService.findByUsername(principal.getName());
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			return "redirect:/logout";
		}
		
		User user = userService.findByUsername(principal.getName());

			Map<Date, Conversation> conversationList = conversationService.searchedConversationList(user.getId(), username);
			
			model.addAttribute("conversationList", conversationList);
			
			if(conversationList.size()>0) {
			Object firstKey = conversationList.keySet().toArray()[0];
			Conversation lastConversation = (Conversation) conversationList.get(firstKey);
		
			if(lastConversation.getLastMessage()!=null) {
				User fromUser = lastConversation.getLastMessage().getFrom();
				User partner = lastConversation.getPartner();
				if(fromUser.getId()==partner.getId()) {
					if(!lastConversation.getLastMessage().getRed()) {
						lastConversation.getLastMessage().setRed(true);
						user.setNewMessages(user.getNewMessages()-1);
					}
				}
			} else {
				model.addAttribute("newConversation");
			}
			lastConversation.setActClass("active");
				conversationService.save(lastConversation);
				
				
				model.addAttribute("activeConversation", lastConversation);
				
			} else {
				model.addAttribute("activeConversation", null);
			}
				
				return "account-inbox";
			
		
	}
}
