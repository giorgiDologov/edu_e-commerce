package com.education;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.education.domain.Conversation;
import com.education.domain.User;
import com.education.domain.security.Role;
import com.education.domain.security.UserRole;
import com.education.service.ConversationService;
import com.education.service.MessageService;
import com.education.service.UserService;
import com.education.utility.SecurityUtility;

@SpringBootApplication
@EnableJpaRepositories 
public class BookstoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ConversationService conversationService;
	
	@Autowired
	private MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// SAM -> s (username, password)
		User user1 = new User();
		user1.setFirstName("Sam");
		user1.setLastName("Student");
		user1.setUsername("sam");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("s"));
		user1.setEmail("sam.student@gmail.com");
		user1.setTeacher(false);
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		 File fnew=new File("src/main/resources/static/images/team-13.jpg");
		 BufferedImage originalImage=ImageIO.read(fnew);
		 ByteArrayOutputStream baos=new ByteArrayOutputStream();
		 ImageIO.write(originalImage, "jpg", baos );


		 user1.setUserImage(baos.toByteArray());
		
		userService.createUser(user1, userRoles);
		
		User user2 = new User();
		user2.setFirstName("Daniel");
		user2.setLastName("Dr Teacher");
		user2.setUsername("teacher1");
		user2.setPassword(SecurityUtility.passwordEncoder().encode("t"));
		user2.setEmail("teacher1@gmail.com");
		user2.setTeacher(true);
		Set<UserRole> userRoles2 = new HashSet<>();
		Role role2= new Role();
		role2.setRoleId(1);
		role2.setName("ROLE_USER");
		userRoles2.add(new UserRole(user2, role2));
		

		 user2.setUserImage(baos.toByteArray());
		
		userService.createUser(user2, userRoles2);
		if(user2.getNewMessages()<1) {
		Conversation conversation = conversationService.openConversation(userService.findById(1), user2);
		messageService.openMessage(conversation,userService.findById(1),"Welcome message from me. Yo!");
		user2.setNewMessages(1);
			}
		
		User user3 = new User();
		user3.setFirstName("Tim");
		user3.setLastName("Teacher");
		user3.setUsername("teacher2");
		user3.setPassword(SecurityUtility.passwordEncoder().encode("t"));
		user3.setEmail("teacher2@gmail.com");
		user3.setTeacher(true);
		Set<UserRole> userRoles3 = new HashSet<>();
		Role role3= new Role();
		role3.setRoleId(1);
		role3.setName("ROLE_USER");
		userRoles3.add(new UserRole(user3, role3));
		

		 user3.setUserImage(baos.toByteArray());
		
		userService.createUser(user3, userRoles3);
		if(user3.getNewMessages()<1) {
		Conversation conversation = conversationService.openConversation(userService.findById(1), user3);
		messageService.openMessage(conversation,userService.findById(1),"Welcome message from me. Yo!");
		user3.setNewMessages(1);
		}
		
		User user4 = new User();
		user4.setFirstName("Tony");
		user4.setLastName("Teacher");
		user4.setUsername("teacher3'");
		user4.setPassword(SecurityUtility.passwordEncoder().encode("t"));
		user4.setEmail("teacher3@gmail.com");
		user4.setTeacher(true);
		Set<UserRole> userRoles4 = new HashSet<>();
		Role role4= new Role();
		role4.setRoleId(1);
		role4.setName("ROLE_USER");
		userRoles4.add(new UserRole(user4, role4));
		

		 user4.setUserImage(baos.toByteArray());
		
		userService.createUser(user4, userRoles4);
		if(user4.getNewMessages()<1) {
		Conversation conversation = conversationService.openConversation(userService.findById(1), user4);
		messageService.openMessage(conversation,userService.findById(1),"Welcome message from me. Yo!");
		user4.setNewMessages(1);
		}
		
		User user5 = new User();
		user5.setFirstName("Terry");
		user5.setLastName("Teacher");
		user5.setUsername("teacher4");
		user5.setPassword(SecurityUtility.passwordEncoder().encode("t"));
		user5.setEmail("teacher4@gmail.com");
		user5.setTeacher(true);
		Set<UserRole> userRoles5 = new HashSet<>();
		Role role5= new Role();
		role5.setRoleId(1);
		role5.setName("ROLE_USER");
		userRoles5.add(new UserRole(user5, role5));
		

		 user5.setUserImage(baos.toByteArray());
		
		userService.createUser(user5, userRoles5);
		if(user5.getNewMessages()<1) {
		Conversation conversation = conversationService.openConversation(userService.findById(1), user5);
		messageService.openMessage(conversation,userService.findById(1),"Welcome message from me. Yo!");
		user5.setNewMessages(1);
		}
	}
}
