package com.doodleblue.railway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doodleblue.railway.model.PassengerDetails;
import com.doodleblue.railway.repository.TicketAllocationRepository;

@Service
public class TicketAllocationService {

	@Autowired
	TicketAllocationRepository ticketAllocationRepository;
	List<HashMap<String,Object>> seatDetails = new ArrayList<>();
	String noSeatsAvailable = "No seats available";
	String confirmedSeats = "confirmed";
	String racSeats = "rac";
	String waitingSeats = "waiting";
	String occupied = "occupied";
	String ticketIsFull = "ticket is full";
	String ticketWaiting = " Booked in Waiting";
	String noTicketNeeded = "no ticket needed for below 5 years";
	String bookedInCoach = " Booked in coach ";
	String in = " in ";
	String forNotAKeyWord = " for ";
    String ticketStatusInWords = "-----Ticket Status - ";

	
	@Transactional
	public String returnTicketStatus(PassengerDetails passengerDetails) {
		List<HashMap<String,Object>> allFreeSeats = getAllFreeSeats();
		String returnStatus = "";
		if(passengerDetails.getAge() > 60 || (passengerDetails.getGender().equalsIgnoreCase("F") && passengerDetails.getTravellingWithChild() != null && passengerDetails.getAge() >= 20 )) {
			returnStatus = ticketBookingForAbv60(passengerDetails,allFreeSeats);
		} else if(passengerDetails.getAge() >= 5) {
			returnStatus = ticketBookingForAbv5(passengerDetails,allFreeSeats);
		} else if(passengerDetails.getAge() < 5) {
			if(ticketAllocationRepository.count() > 36) {
				return ticketIsFull;
			}
			passengerDetails.setAllotedBerth("no alloted berth");
			passengerDetails.setAllotedCoach("no alloted coach");
			passengerDetails.setTicketStatus("no ticket");
			ticketAllocationRepository.save(passengerDetails);
			returnStatus = noTicketNeeded;
			
		}
		return returnStatus;
	}
	
	private String savePassenger(PassengerDetails passengerDetails,Map.Entry<String, Object> set,String ticketStatus) {
		set.setValue(occupied);
		passengerDetails.setAllotedBerth(set.getKey().toString().substring(2));
		passengerDetails.setAllotedCoach(set.getKey().toString().substring(0,2));
		passengerDetails.setTicketStatus(ticketStatus);
		ticketAllocationRepository.save(passengerDetails);
		return bookedInCoach+set.getKey().toString().substring(0,2) + in + set.getKey().toString().substring(2) +forNotAKeyWord+passengerDetails.getPassengerName() +ticketStatusInWords+ticketStatus;
	}
	
	private String ticketBookingForAbv5(PassengerDetails passengerDetails,List<HashMap<String,Object>> allFreeSeats) {
		String bookingStatus = "";
		if(getCountOfSeats(confirmedSeats) < 24) {
			for(HashMap<String,Object> map : allFreeSeats) {
					for (Map.Entry<String, Object> set : map.entrySet()) {
						if(!set.getKey().toString().contains("SideSeatLower") && set.getValue().toString().equalsIgnoreCase("available") && checkForLadiesIfCoachIsFulledWithGents(passengerDetails,set.getKey().toString().substring(0,2))){
							bookingStatus = savePassenger(passengerDetails, set, confirmedSeats);
							break;
						}
					}
					if(!bookingStatus.equalsIgnoreCase("")) {
						break;
					}
			}
		} else if(getCountOfSeats(racSeats) < 8) {
			bookingStatus = bookRacTicket(passengerDetails,allFreeSeats);
		} else if(getCountOfSeats(waitingSeats) < 5) {
			bookingStatus = bookWaitingTicket(passengerDetails,allFreeSeats);
		} else {
			bookingStatus = ticketIsFull;
		}
		return bookingStatus;
	}
	
	private String ticketBookingForAbv60(PassengerDetails passengerDetails,List<HashMap<String,Object>> allFreeSeats) {
		String bookingStatus = "";
		if(getCountOfSeats(confirmedSeats) < 24) {
			for(HashMap<String,Object> map : allFreeSeats) {
					for (Map.Entry<String, Object> set : map.entrySet()) {
						if(set.getKey().toString().contains("Lower") && !set.getKey().toString().contains("SideSeatLower") && set.getValue().toString().equalsIgnoreCase("available") && checkForLadiesIfCoachIsFulledWithGents(passengerDetails,set.getKey().toString().substring(0,2))){
							bookingStatus = savePassenger(passengerDetails, set, confirmedSeats);
							break;
						}
					}
					if(!bookingStatus.equalsIgnoreCase("")) {
						break;
					}
			}
		
			if(bookingStatus.equalsIgnoreCase("")) {
				for(HashMap<String,Object> map : allFreeSeats) {
					for (Map.Entry<String, Object> set : map.entrySet()) {
						if( (set.getKey().toString().contains("Middle") || set.getKey().toString().contains("Upper")) && !set.getKey().toString().contains("SideSeatLower") && set.getValue().toString().equalsIgnoreCase("available") && checkForLadiesIfCoachIsFulledWithGents(passengerDetails,set.getKey().toString().substring(0,2))){
							bookingStatus = savePassenger(passengerDetails, set, confirmedSeats);
							break;
						}
					}
					if(!bookingStatus.equalsIgnoreCase("")) {
						break;
					}
				}
			}
			
		} else if(getCountOfSeats(racSeats) < 8) {
			bookingStatus = bookRacTicket(passengerDetails,allFreeSeats);
		} else if(getCountOfSeats(waitingSeats) < 5) {
			bookingStatus = bookWaitingTicket(passengerDetails,allFreeSeats);
		} else {
			bookingStatus = ticketIsFull;
		}
		return bookingStatus;
	}
	
	private Boolean checkForLadiesIfCoachIsFulledWithGents(PassengerDetails passengerDetails,String coachName) {
		if(!passengerDetails.getGender().equalsIgnoreCase("F")){
			return true;
		}
		List<PassengerDetails> listOfPassengersInRespectiveCoach = ticketAllocationRepository.findPassengerDetailsByAllotedCoach(coachName);
		for(PassengerDetails passengerDetail : listOfPassengersInRespectiveCoach) {
			if(!passengerDetail.getGender().equalsIgnoreCase("M")) {
				return true;
			}
		}
		if(listOfPassengersInRespectiveCoach.size() > 4) {
			return false;
		} else {
			return true;
		}
	}
	
	private String bookRacTicket(PassengerDetails passengerDetails,List<HashMap<String,Object>> allFreeSeats) {
		String bookingStatus = "";
		for(HashMap<String,Object> map : allFreeSeats) {
			for(Map.Entry<String, Object> set : map.entrySet()) {
				if(set.getKey().toString().contains("SideSeatLower") && set.getValue().toString().equalsIgnoreCase("available") && checkForLadiesIfCoachIsFulledWithGents(passengerDetails,set.getKey().toString().substring(0,2) )) {
					bookingStatus = savePassenger(passengerDetails, set, racSeats);
					break;
				}
			}
			if(!bookingStatus.equalsIgnoreCase("")) {
				break;
			}
		}
		return bookingStatus;
	}
	
	private String bookWaitingTicket(PassengerDetails passengerDetails,List<HashMap<String,Object>> allFreeSeats) {
		passengerDetails.setAllotedBerth("no alloted berth");
		passengerDetails.setAllotedCoach("no alloted coach");
		passengerDetails.setTicketStatus(waitingSeats);
		ticketAllocationRepository.save(passengerDetails);
		return ticketWaiting;
	}
	
	private List<HashMap<String,Object>> getAllFreeSeats(){
		if(seatDetails.isEmpty()) {
			insertValuesIntoSeatDetails();
		}
		return seatDetails;
	}
	
	private void insertValuesIntoSeatDetails() {
		for(int i=1;i<=4;i++) {
			LinkedHashMap<String,Object> map = new LinkedHashMap<>();
			map.put("S"+i+"UpperSeat1","available" ); //UpperSeat1
			map.put("S"+i+"UpperSeat2","available" ); //UpperSeat2
			map.put("S"+i+"MiddleSeat1","available" ); //MiddleSeat1
			map.put("S"+i+"MiddleSeat2","available" ); //MiddleSeat2
			map.put("S"+i+"LowerSeat1","available" ); //LowerSeat1
			map.put("S"+i+"LowerSeat2","available" ); //LowerSeat2
			map.put("S"+i+"SideSeatUpper","available" ); //SideSeatUpper
			map.put("S"+i+"SideSeatLower1","available" ); //SideSeatLower
			map.put("S"+i+"SideSeatLower2","available" ); //SideSeatLower
			seatDetails.add(map);
		}
	}
	
	private int getCountOfSeats(String seatType) {
		List<PassengerDetails> listOfPassengerDetails = ticketAllocationRepository.findPassengerDetailsByTicketStatus(seatType);
		return listOfPassengerDetails.size();
	}
	
	@Transactional
	public List<PassengerDetails> getTicketDetails(){
		return ticketAllocationRepository.findAll();
	}
	
	public void updateForTravellingWithChildrenByIteratingTheList(List<PassengerDetails> passengerDetails) {
		for(PassengerDetails passengerDetail : passengerDetails) {
			if(passengerDetail.getAge() < 5) {
				updateTravellingWithChildFieldForLady(passengerDetails);	
			}
		}
	}
	
	public void updateTravellingWithChildFieldForLady(List<PassengerDetails> passengerDetails) {
		for(PassengerDetails passengerDetail : passengerDetails) {
			if(passengerDetail.getGender().equalsIgnoreCase("F") && passengerDetail.getTravellingWithChild() == null && passengerDetail.getAge() > 4) {
				passengerDetail.setTravellingWithChild("Y");
			}
		}
	}

}
