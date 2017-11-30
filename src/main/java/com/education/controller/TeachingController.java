package com.education.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.education.domain.TeacherInstructor;
import com.education.domain.User;
import com.education.service.UserService;

@Controller
public class TeachingController {
	
	//WHEN U FINISH: COME BACK, STRUCTURE CODE, AND COMMENT!!!!!!!!!!!!!!!
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/teaching")
	public String teaching(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		
		User user = userService.findByUsername(principal.getName());
		
		//teaching profile magic coming soon
		
		return "account-teaching";
	}
	
	//helps passing data from jsp to controller via post
	
    /*@ModelAttribute("teacherInstructor")
    public TeacherInstructor getTeacherInstructor(HttpServletRequest request) 
    {
        return (TeacherInstructor) request.getAttribute("teacherInstructor");
    }*/
	
	
	@RequestMapping(value="/newInstructor/{id}/{onList}")
	public String newInstructor(@ModelAttribute("id") int id, @ModelAttribute("onList") String onList, Model model, Principal principal, HttpServletRequest request,  RedirectAttributes attr) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		
		User user = userService.findByUsername(principal.getName());
		
		//+boolean to enable JS scroll down to instr field!!!!
		
		User newInstructor = userService.findById(id);
		
		TeacherInstructor teacherInstructor = new TeacherInstructor();
		
		if(!onList.isEmpty()) {
			onList = onList + "," + id;
		} else {
			onList = String.valueOf(id);
		}
		
		attr.addFlashAttribute("onList", onList);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ONLIST: " + onList);
      
		for(User teacher : userService.findByTeacher(true)) {
			if(!Arrays.asList(onList.split(",")).contains(Integer.toString(teacher.getId()))) {
				if(teacher.getId() != user.getId()) {
					teacherInstructor.getTeachers().add(teacher);
				}
			} else {
				teacherInstructor.getInstructors().add(teacher);
			}
		}
        
        attr.addFlashAttribute("teacherInstructor",teacherInstructor);
       
		return "redirect:/createCourseBasic";

	}
	
	@RequestMapping(value="/deleteInstructor/{id}/{onList}")
	public String deleteInstructor(@ModelAttribute("id") int id, @ModelAttribute("onList") String onList, Model model, Principal principal, HttpServletRequest request,  RedirectAttributes attr) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		
		//NOT READY!!!!!!!!!!!!!!! NEEDS TO DELETE INSTR AND GIVE BACK TO TEACHERS
		//+boolean to enable JS scroll down to instr field!!!!
		
		User user = userService.findByUsername(principal.getName());
		
		User newInstructor = userService.findById(id);
		
		TeacherInstructor teacherInstructor = new TeacherInstructor();
		
		if(!onList.isEmpty()) {
			onList = onList + "," + id;
		} else {
			onList = String.valueOf(id);
		}
		
		for(String instrId: Arrays.asList(onList.split(","))){
			if(Integer.toString(id)!=instrId) {
				
			}
		}
		
		attr.addFlashAttribute("onList", onList);
      
		for(User teacher : userService.findByTeacher(true)) {
			if(!Arrays.asList(onList.split(",")).contains(Integer.toString(teacher.getId()))) {
				if(teacher.getId() != user.getId()) {
					teacherInstructor.getTeachers().add(teacher);
				}
			} else {
				teacherInstructor.getInstructors().add(teacher);
			}
		}
        
        attr.addFlashAttribute("teacherInstructor",teacherInstructor);
       
		return "redirect:/createCourseBasic";

	}
	
	@RequestMapping(value="/createCourseBasic", method=RequestMethod.GET)
	public String createCourse(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		User user = userService.findByUsername(principal.getName());
		
		
		if(!model.containsAttribute("teacherInstructor")) {
			TeacherInstructor teacherInstructor = new TeacherInstructor();
			List<User> teachersList = userService.findByTeacher(true);
			
			for(User teacher: teachersList) {
				if(teacher.getId() != user.getId()) {
					teacherInstructor.getTeachers().add(teacher);
				}
			}
     
		model.addAttribute("teacherInstructor",teacherInstructor);
		String onList = " ";
	    model.addAttribute("onList", onList);
		}

		return "create-basic-information";
	}
	
	@RequestMapping(value="/createCourseBasic", method=RequestMethod.POST)
	public String createCoursePOST(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		User user = userService.findByUsername(principal.getName());
		
		return "create-design-course";
	}
	
	//only jumps to page, doesn't do anything, TO BE FINISHED!!
	
	@RequestMapping("/createCourseDesign")
	public String createCourseDesign(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		User user = userService.findByUsername(principal.getName());
		
		return "create-design-course";
	}
	
	@RequestMapping("/createCoursePublish")
	public String createCoursePublish(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("presentUser", user);
			model.addAttribute("user", user);
			model.addAttribute("userPresent", true);
			if(user.getTeacher()) {
				model.addAttribute("teacherOwn", true);
			}
		} else {
			return "redirect:/logout";
		}
		User user = userService.findByUsername(principal.getName());
		
		return "create-publish-course";
	}
	
	

}
