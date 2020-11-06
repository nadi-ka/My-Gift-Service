package com.epam.esm.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	private static final Logger log = LogManager.getLogger(OrderController.class);
	
	/**
	 * GET all user's orders by the long Id; In case when the user 
	 * with the given Id is not found, the method returns Status Code = 404
	 */
	@GetMapping("/orders/{userId}")
	public UserDTO getOrders(@PathVariable long userId) {

		UserDTO userDTO = userService.getUser(userId);

		if (userDTO == null) {
			throw new NotFoundException("The user wasn't found, id - " + userId);
		}
		
		UserDTO userWithOrders = orderService.getUserOrdersById(userId);
		
		return userWithOrders;
	}

}
