package com.education.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.education.domain.Conversation;
import com.education.domain.User;
import com.education.domain.security.PasswordResetToken;
import com.education.domain.security.Role;
import com.education.domain.security.UserRole;
import com.education.service.ConversationService;
import com.education.service.MessageService;
import com.education.service.UserService;
import com.education.service.impl.UserSecurityService;
import com.education.utility.MailConstructor;
import com.education.utility.SecurityUtility;

@Controller
public class UserController {

	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ConversationService conversationService;
	
	@RequestMapping("/register")
	public String register(Principal principal, Model model) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
		} else {
			model.addAttribute("userPresent", false);
		}
		return "register";
	}
	
	@RequestMapping(value="/newUser", method = RequestMethod.POST)
	public String newUserPost(
			HttpServletRequest request,
			@ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username,
			@ModelAttribute("firstname") String firstName,
			@ModelAttribute("lastname") String lastName,
			Model model
			) throws Exception {
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		
		
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
		if (userService.findByEmail(userEmail) != null) {
				model.addAttribute("emailExists", true);
		}
		model.addAttribute("userPresent", false);
			return "register";
		}
		
		if (userService.findByUsername(username) == null && userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			model.addAttribute("userPresent", false);
			return "register";
		}
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setTeacher(false);
		user.setLocation("somewere around...");
		
		 File fnew=new File("src/main/resources/static/images/team-13.jpg");
		 BufferedImage originalImage=ImageIO.read(fnew);
		 ByteArrayOutputStream baos=new ByteArrayOutputStream();
		 ImageIO.write(originalImage, "jpg", baos );


		 user.setUserImage(baos.toByteArray());
		
		
		String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		
		mailSender.send(email);
		
		model.addAttribute("emailSent", "true");
		model.addAttribute("userPresent", false);
		return "register";
	}
	
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.GET)
	public String updateUserInfoGET(Principal principal,
									Model model) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
			model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
			model.addAttribute("userPresent", true);
			model.addAttribute("presentUser", user);
			if(user.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			model.addAttribute("userPresent", false);
		}
		
		
		return "account-profile-owner-view";
	}
	
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public String updateUserInfoPOST(
			@ModelAttribute("user") User user,
			@ModelAttribute("newPassword") String newPassword,
			@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName,
			@ModelAttribute("image") MultipartFile image,
			Model model,
			Principal principal,
			HttpServletRequest request
			) throws Exception {
		
		User currentUser = userService.findByUsername(principal.getName());
		
		if(userService.findByUsername(principal.getName())!=currentUser) {
			return "redirect:/logout";
		}
		
		if(user.getTeacher()) {
			model.addAttribute("teacherProfile", true);
		}
		
		if(currentUser == null) {
			throw new Exception ("User not found");
		} else {
			model.addAttribute("userPresent", true);
		}
		
		//check email already exists
		if (userService.findByEmail(user.getEmail())!=null) {
			if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				model.addAttribute("userPresent", true);
				model.addAttribute("presentUser", currentUser);
				return "account-profile-owner-view";
			}
		}
		
		//check username already exists
		if (userService.findByUsername(user.getUsername())!=null) {
			if(userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				model.addAttribute("userPresent", true);
				model.addAttribute("presentUser", currentUser);
				return "account-profile-owner-view";
			}
		} 
		
//		update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				model.addAttribute("userPresent", true);
				model.addAttribute("presentUser", currentUser);
				return "account-profile-owner-view";
			}
		}
		
		//process accepted data
		currentUser.setFirstName(firstName);
		currentUser.setLastName(lastName);
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());
		currentUser.setLocation(user.getLocation());
		currentUser.setDescription(user.getDescription());
		
		try {
			
		byte[] bytes = image.getBytes();
		
		if(bytes.length>0) {
		currentUser.setUserImage(bytes);
				}
		
		} catch (Exception e) {
			return "account-profile-owner-view";
		}
		
		userService.save(currentUser);
		
		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("presentUser", currentUser);
		model.addAttribute("ownerView", true);
		
		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
		
		
		return "redirect:/profile?id=" + user.getId();
	}
	
	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/?logout";
		} else {
			model.addAttribute("userPresent", true);
		}

		User user = passToken.getUser();
		String username = user.getUsername();
		
		if(user.getTeacher()) {
			model.addAttribute("teacherProfile", true);
		}

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Conversation conversation = conversationService.openConversation(userService.findById(1), user);
		messageService.openMessage(conversation,userService.findById(1),"Welcome message from me. Yo!");
		user.setNewMessages(1);
		userService.save(user);
		
		model.addAttribute("user", user);
		model.addAttribute("presentUser", user);
		model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
	
		return "account-profile-owner-view";
	}
	
	@RequestMapping(value="/forgetPassword", method=RequestMethod.POST)
	public String forgetPassword(
			HttpServletRequest request,
			@ModelAttribute("email") String email,
			Principal principal,
			Model model
			) {
		
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
			}
		
			if (userService.findByEmail(email) == null) {
			model.addAttribute("emailNotExist", true);
			return "forget-password";
			}
			
			User userToSend = userService.findByEmail(email);
			
			String password = SecurityUtility.randomPassword();
			
			String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
			userToSend.setPassword(encryptedPassword);
			
			userService.save(userToSend);
			
			String token = UUID.randomUUID().toString();
			userService.createPasswordResetTokenForUser(userToSend, token);
			
			String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			
			SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, userToSend, password);
			
			mailSender.send(newEmail);
			
			model.addAttribute("forgetPasswordEmailSent", "true");
			
			return "forget-password";
		
	}
	
	@RequestMapping("/login")
	public String login(Principal principal, Model model) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("userPresent", true);
		} else {
			model.addAttribute("userPresent", false);
		}
		return "login";
	}
	
	@RequestMapping("/inbox")
	public String inbox(Principal principal, Model model) {
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
		
		Map<Date, Conversation> conversationList = conversationService.setConversationList(user.getId());
		
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
		
		}
		
		return "account-inbox";
	}
	
	@Transactional
	@RequestMapping(value="/sendMsg")
	public String sendMessage(@ModelAttribute("id") int convId,
							 @ModelAttribute("body") String body,
							 Principal principal, Model model) {
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


