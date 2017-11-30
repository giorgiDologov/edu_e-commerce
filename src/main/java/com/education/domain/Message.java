package com.education.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyTemporal;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TemporalType;

@Entity
@Table(name="user_message")
public class Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private Date date;
	@Column(columnDefinition = "LONGBLOB")
	private User from_user; //userID
	
	@ManyToOne
	private Conversation conversation;
	
	private Boolean red;
	
	private String title;
	@Column(columnDefinition="LONGTEXT")
	private String body;
	
	private String actClass; // "ac-new", "active", ""
	
	private String returnDate;

	public String getActClass() {
		return actClass;
	}

	public void setActClass(String actClass) {
		this.actClass = actClass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		setReturnDate(date);
		this.date = date;
	}

	public User getFrom() {
		return from_user;
	}

	public void setFrom(User from) {
		this.from_user = from;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Boolean getRed() {
		return red;
	}

	public void setRed(Boolean red) {
		this.red = red;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getReturnDate() {
		if(this.returnDate==null) {
			return " ";
		} else {
			return returnDate;
		}
	}

	public void setReturnDate(Date date) {
		Date now = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String stringDate;
		
		stringDate= new SimpleDateFormat("YYYY", Locale.ENGLISH).format(date);
		
		//THIS YEAR
		if(localDate.getYear()==localNow.getYear()) {
			stringDate= new SimpleDateFormat("MMM", Locale.ENGLISH).format(date);
		}
		
		//THIS MONTH
		if(localDate.getYear()==localNow.getYear() && localDate.getMonth()==localNow.getMonth()) {
			stringDate = new SimpleDateFormat("d MMM", Locale.ENGLISH).format(date);
		}
		
		//THIS WEEK
		if(localDate.getYear()==localNow.getYear() && localDate.getMonth()==localNow.getMonth() && localNow.getDayOfMonth()-7<localDate.getDayOfMonth()) {
			stringDate = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date);
		}
		
		//TODAY
		if(localDate.getYear()==localNow.getYear() && localDate.getMonth()==localNow.getMonth() && localDate.getDayOfMonth()==localNow.getDayOfMonth()) {
			stringDate = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date);
		}
		
		
		
		this.returnDate = stringDate;
	}
	
	

}
