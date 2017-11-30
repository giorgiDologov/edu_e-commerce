package com.education.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Education {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ElementCollection
	private Set<CourseContent> courseContentList;
	
	private String title;
	
	private String description;
	
	private byte[] mediaFile;

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

	public byte[] getMediaFile() {
		return mediaFile;
	}

	public void setMediaFile(byte[] mediaFile) {
		this.mediaFile = mediaFile;
	}

}
