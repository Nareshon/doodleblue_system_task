package com.doodleblue.railway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import net.bytebuddy.implementation.bind.annotation.Default;

@Entity
@Table(name="ticket_details")
public class PassengerDetails {
	
	@Id
	@GeneratedValue
	@Column(name="sno")
	int sno;
	@Column(name="passenger_name")
	String passengerName;
	@Column(name="age")
	int age;
	@Column(name="gender")
	String gender;
	@Column(name="berth_perference")
	String berthPerference;
	@Column(name="alloted_berth")
	String allotedBerth;
	@Column(name="alloted_coach")
	String allotedCoach;
	@Column(name="ticket_status")
	String ticketStatus;
	@Column(name="travellingWithChild")
	String travellingWithChild;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getPassengerName() {
		return passengerName;
	}
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBerthPerference() {
		return berthPerference;
	}
	public void setBerthPerference(String berthPerference) {
		this.berthPerference = berthPerference;
	}
	public String getAllotedBerth() {
		return allotedBerth;
	}
	public void setAllotedBerth(String allotedBerth) {
		this.allotedBerth = allotedBerth;
	}
	public String getAllotedCoach() {
		return allotedCoach;
	}
	public void setAllotedCoach(String allotedCoach) {
		this.allotedCoach = allotedCoach;
	}
	public String getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public String getTravellingWithChild() {
		return travellingWithChild;
	}
	public void setTravellingWithChild(String travellingWithChild) {
		this.travellingWithChild = travellingWithChild;
	}
}
