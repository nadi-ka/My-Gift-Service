package com.epam.esm.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.PurchaseDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
	
	private PurchaseService purchaseService;
	private UserService userService;
	
	@Autowired
	public PurchaseController(PurchaseService purchaseService, UserService userService) {
		this.purchaseService = purchaseService;
		this.userService = userService;
	}
	
	private static final Logger log = LogManager.getLogger(PurchaseController.class);
	
	/**
	 * GET all user's orders by the long Id; In case when the user 
	 * with the given Id is not found, the method returns Status Code = 404
	 */
	@GetMapping("{userId}")
	public List<PurchaseDTO> getPurchases(@PathVariable long userId) {
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO == null) {
			throw new NotFoundException("The user wasn't found, id - " + userId);
		}
		List<PurchaseDTO> purchases = purchaseService.getPurchasesByUserId(userId);
		return purchases;
	}
	
	/**
	 * POST method which creates new order; In case of success, the
	 * method returns Status Code = 200 and the response body contains the order with 
	 * the new generated Id
	 */
	@PostMapping("{userId}")
	public PurchaseDTO addPurchase(@PathVariable long userId, 
			@RequestBody long[] certificateId) {
		PurchaseDTO newPurchase = purchaseService.savePurchase(userId, certificateId);
		return newPurchase;
	}

}
