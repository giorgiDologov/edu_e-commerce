package com.education.service;

import java.util.List;

import com.education.domain.Course;

public interface CourseService {
	
	Course findById(int id);
	
	void save(Course course);

	Course openCourse(int id, String intro, List<String> goalOfCourse, String about, byte[] banner);

}
