package com.education.domain;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CourseContent {
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ManyToMany
	@JoinColumn(name="education_id")
	private Map<Integer, Education> educationList;
	
	@ManyToMany
	@JoinColumn(name="exam_id")
	private Map<Integer, Exam> examList;
	
	@OneToOne
	@JoinColumn(name="course_id")
	private Course course;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, Education> getEducationList() {
		return educationList;
	}

	public void setEducationList(Map<Integer, Education> educationList) {
		this.educationList = educationList;
	}

	public Map<Integer, Exam> getExamList() {
		return examList;
	}

	public void setExamList(Map<Integer, Exam> examList) {
		this.examList = examList;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	

}
