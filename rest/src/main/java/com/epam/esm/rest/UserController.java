package com.epam.esm.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;
	private MessageSource messageSource;

	@Autowired
	public UserController(UserService userService, MessageSource messageSource) {
		this.userService = userService;
		this.messageSource = messageSource;
	}

	/**
	 * POST method which creates new user;
	 * 
	 * @param userDTO
	 * @return {@link UserDTO} (in case of success, the method returns Status Code =
	 *         200 and the response body contains new user)
	 */
	@PostMapping
	public EntityModel<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
		UserDTO createdUser = userService.saveUser(userDTO);
		EntityModel<UserDTO> entityModel = new EntityModel<>(createdUser);
		return entityModel.add(linkTo(methodOn(UserController.class).getUser(createdUser.getId())).withSelfRel());
	}

	/**
	 * GET user by the long Id;
	 * 
	 * @param userId
	 * @return {@link UserDTO}
	 * @throws NotFoundException
	 */
	@GetMapping("{userId}")
	public EntityModel<UserDTO> getUser(@PathVariable long userId) {
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO.getId() == 0) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.USER_NOT_FOUND_KEY),
					new Object[] { userId }, LocaleContextHolder.getLocale()));
		}
		EntityModel<UserDTO> entityModel = new EntityModel<>(userDTO);
		return entityModel.add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());
	}

}
