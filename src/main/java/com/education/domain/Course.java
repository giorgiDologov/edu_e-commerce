package com.education.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Course {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@OneToOne(mappedBy="course")
	private CourseContent courseContent;
	
	@ManyToMany	
	@JoinColumn(name="user_id")
	private List<User> instructors;
	
	public String introduction;
	
	@ElementCollection
	public Collection<String> goalOfCourse;
	
	public String about;
	
	public byte[] banner;
	
	public String duration;
	
	public int price;
	
	@OneToMany
	public List<Category> categories;
	
	public String tools;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CourseContent getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(CourseContent courseContent) {
		this.courseContent = courseContent;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Collection<String> getGoalOfCourse() {
		return goalOfCourse;
	}

	public void setGoalOfCourse(Collection<String> goalOfCourse) {
		this.goalOfCourse = goalOfCourse;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public byte[] getBanner() {
		return banner;
	}

	public void setBanner(byte[] banner) {
		this.banner = banner;
	}

	public List<User> getInstructors() {
		return instructors;
	}

	public void setInstructors(List<User> instructors) {
		this.instructors = instructors;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
}
