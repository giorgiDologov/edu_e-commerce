package com.education.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.education.domain.User;
import com.education.service.UserService;


@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public String index(Principal principal, Model model) {
		//user details
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
		} else {
			model.addAttribute("userPresent", false);
		}
		
		//front page attributes... coming soon
		
		return "index";
	}

	
	//returning pics
	@RequestMapping(value = "/imageController/{id}")
	@ResponseBody
	public byte[] userImage(HttpServletRequest request, @ModelAttribute("id") int id, Model model)  {
	  return userService.findById(id).getUserImage();
	}
	
	//opening profiles (decide finally, it comes here or UserController!!!!!!!)
	@RequestMapping("/profile")
	public String myProfile(@PathParam("id") int id, Model model, Principal principal) {
		User checkedUser = userService.findById(id);
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			model.addAttribute("presentUser", user);
			if(checkedUser==user) {
				model.addAttribute("ownerView", true);
			}
			if(checkedUser.getTeacher()) {
				model.addAttribute("teacherProfile", true);
			}
		} else {
			model.addAttribute("userPresent", false);
		}
		model.addAttribute("user", checkedUser);
		model.addAttribute("name", checkedUser.getFirstName() + " " + checkedUser.getLastName());
		return "account-profile-guest-view";
		
		
	}
	

	
}
