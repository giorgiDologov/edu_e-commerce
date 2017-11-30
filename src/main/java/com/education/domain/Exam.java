package com.education.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Exam {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ElementCollection
	private Set<CourseContent> courseContentList;
	
	private String title;
	private String description;
	
	@OneToMany(mappedBy="exam", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Question> questions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<CourseContent> getCourseContentList() {
		return courseContentList;
	}

	public void setCourseContentList(Set<CourseContent> courseContentList) {
		this.courseContentList = courseContentList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	

}
