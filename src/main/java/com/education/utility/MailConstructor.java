package com.education.utility;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;


import com.education.domain.User;

@Component
public class MailConstructor {
	
	@Autowired
	private Environment env;
	
	//for later use
	/*
	@Autowired 
	private TemplateEngine templateEngine;
	*/
	
	public SimpleMailMessage constructResetTokenEmail(
			String contextPath, Locale locale, String token, User user, String password
			) {
		
		String url = contextPath + "/newUser?token="+token;
		String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"+password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("Ohr Sameach Academy - New User");
		email.setText(url+message);
		email.setFrom(env.getProperty("support.email"));
		return email;
		
	}
	
}
