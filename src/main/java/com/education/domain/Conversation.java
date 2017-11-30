package com.education.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.OneToMany;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.education.service.ConversationService;

@Entity
public class Conversation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@OneToMany(mappedBy="conversation", cascade=CascadeType.ALL)
	private List<Message> messageList = new ArrayList<>();
	
	@ManyToOne
	private User user;
	
	private int owner1;
	
	private int owner2;
	
	private Date lastDate;
	
	private String returnDate;
	
	@Transient
	private String actClass; // "ac-new", "active", ""
	
	@Transient
	private User partner;
	
	@Transient
	private Message lastMessage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		setReturnDate(lastDate);
		this.lastDate = lastDate;
	}

	public int getOwner1() {
		return owner1;
	}

	public void setOwner1(int owner1) {
		this.owner1 = owner1;
	}

	public int getOwner2() {
		return owner2;
	}

	public void setOwner2(int owner2) {
		this.owner2 = owner2;
	}

	public Message getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}

	public User getPartner() {
		return partner;
	}

	public void setPartner(User partner) {
		this.partner = partner;
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

	public String getActClass() {
		return actClass;
	}

	public void setActClass(String actClass) {
		this.actClass = actClass;
	}

}
