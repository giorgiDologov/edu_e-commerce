package com.education.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Question {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="exam_id")
	private Exam exam;
	
	public enum Type{
		MULTIPLE_CHOICE,
		SINGLE_CHOICE,
		CHOOSE_PIC,
		MAKE_TEXT
	}
	
	private Type type;
	
	private String questionText;
	
	@ElementCollection
	private Collection<String> correctAnswers;
	
	@ElementCollection
	private Collection<String> faultAnswers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		if(type==Type.MULTIPLE_CHOICE) {
			this.correctAnswers= new ArrayList<String>();
			this.faultAnswers= new ArrayList<String>();
		} else if(type==Type.SINGLE_CHOICE) {
			this.correctAnswers= new ArrayList<String>(1);
			this.faultAnswers= new ArrayList<String>();
		} else if(type==Type.MAKE_TEXT) {
			this.correctAnswers= new ArrayList<String>(1);
		} 
		this.type = type;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Collection<?> getCorrectAnswers() {
		return correctAnswers;
	}

	public Collection<?> getFaultAnswers() {
		return faultAnswers;
	}

	
	

}
