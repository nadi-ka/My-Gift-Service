package com.epam.esm.rest;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	/**
	 * POST method which creates new user; In case of success, the
	 * method returns Status Code = 200 and the response body contains the user with 
	 * the new generated Id
	 */
	@PostMapping
	public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) {
		UserDTO newUser = userService.saveUser(userDTO);
		return newUser;
	}
	
	/**
	 * GET user by the long Id; In case when the user with the given Id is not found,
	 * the method returns Status Code = 404
	 */
	@GetMapping("{userId}")
	public UserDTO getUser(@PathVariable long userId) {
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO == null) {
			throw new NotFoundException("The user wasn't found, id - " + userId);
		}
		return userDTO;
	}

}
