package com.doodleblue.railway.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doodleblue.railway.model.PassengerDetails;
import com.doodleblue.railway.service.TicketAllocationService;

@RestController
@RequestMapping("/reservation")
public class TicketAllocation {
	
	@Autowired
	TicketAllocationService ticketAllocationService;

	@PostMapping("/bookTickets")
	public ResponseEntity<Object> returnTicketStatus(@RequestBody List<PassengerDetails> passengerDetails) {
		List<JSONObject> listOfTicketStatus = new ArrayList<>();
		ticketAllocationService.updateForTravellingWithChildrenByIteratingTheList(passengerDetails);
		for(PassengerDetails passengerDetail : passengerDetails) {
			JSONObject entity = new JSONObject();
			entity.put("ticketStatus", ticketAllocationService.returnTicketStatus(passengerDetail));
			listOfTicketStatus.add(entity);
		}
		return new ResponseEntity<Object>(listOfTicketStatus, HttpStatus.OK);
	}
	
	@GetMapping("/getAllTicketDetails")
	public List<PassengerDetails> getTicketDetails(){
		return ticketAllocationService.getTicketDetails();
	}
	
}
