package com.education.repository;

import org.springframework.data.repository.CrudRepository;

import com.education.domain.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {

	Course findById(int id);

}
