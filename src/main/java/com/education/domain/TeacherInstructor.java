package com.education.domain;

import java.util.ArrayList;
import java.util.List;

public class TeacherInstructor {
	
	private List<User> teachers;
	
	private List<User> instructors;
	
    public TeacherInstructor() {
        this.teachers = new ArrayList<User>();
        this.instructors = new ArrayList<User>();
   }

	public List<User> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<User> teachers) {
		this.teachers = teachers;
	}

	public List<User> getInstructors() {
		return instructors;
	}

	public void setInstructors(List<User> instructors) {
		this.instructors = instructors;
	}
	
	

}
