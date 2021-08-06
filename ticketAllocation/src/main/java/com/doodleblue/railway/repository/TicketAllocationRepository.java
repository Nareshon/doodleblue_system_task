package com.doodleblue.railway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doodleblue.railway.model.PassengerDetails;

@Repository
public interface TicketAllocationRepository extends JpaRepository<PassengerDetails,Integer>{
	public List<PassengerDetails> findPassengerDetailsByTicketStatus(String ticketStatus);
	public List<PassengerDetails> findPassengerDetailsByAllotedCoach(String allotedCoach);
	
}
