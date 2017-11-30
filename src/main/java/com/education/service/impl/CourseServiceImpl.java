package com.education.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.education.domain.Course;
import com.education.repository.CourseRepository;
import com.education.service.CourseService;

public class CourseServiceImpl implements CourseService  {
	
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public Course findById(int id) {
		return courseRepository.findById(id);
	}

	@Override
	public Course openCourse(int id, String intro, List<String> goalOfCourse, String about, byte[] banner) {
		if(findById(id)!=null) {
			return findById(id);
		}
		
		Course course = new Course();
		course.setIntroduction(intro);
		course.setGoalOfCourse(goalOfCourse);
		course.setAbout(about);
		course.setBanner(banner);
		
		save(course);
		
		return course;
	}

	@Override
	public void save(Course course) {
		courseRepository.save(course);
	}

}
