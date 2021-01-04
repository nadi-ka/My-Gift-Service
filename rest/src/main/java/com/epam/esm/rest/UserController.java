package com.epam.esm.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserLoginPasswordDTO;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.dto.UserWithJwt;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.security.jwt.JwtProvider;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.NotUniqueParameterServiceException;
import com.epam.esm.userdetails.CustomUserDetails;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;
	private MessageSource messageSource;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;
	private static final String BEARER = "Bearer";

	@Autowired
	public UserController(UserService userService, MessageSource messageSource, PasswordEncoder encoder,
			AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
		this.userService = userService;
		this.messageSource = messageSource;
		this.passwordEncoder = encoder;
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}

	/**
	 * POST method which creates new user;
	 * 
	 * @param user
	 * @return {@link UserDTO} (in case of success, the method returns Status Code =
	 *         200 and the response body contains new user)
	 * @throws NotUniqueParameterServiceException
	 */
	@PostMapping("/signup")
	public UserDTO signUp(@Valid @RequestBody UserRegisterDTO user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		try {
			return userService.saveUser(user);
		} catch (NotUniqueParameterServiceException e) {
			throw new NotUniqueParameterServiceException(
					messageSource.getMessage((MessageKeyHolder.USER_LOGIN_NOT_UNIQUE), new Object[] { user.getLogin() },
							LocaleContextHolder.getLocale()));
		}
	}

	/**
	 * POST method which logIn the user;
	 * 
	 * @param user
	 * @return {@link UserWithJwt} (in case of success, the method returns Status Code =
	 *         200 and the response body contains new user with valid token)
	 */
	@PostMapping("/login")
	public UserWithJwt logIn(@Valid @RequestBody UserLoginPasswordDTO user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String token = jwtProvider.generateToken(userDetails);
		return new UserWithJwt(userDetails.getId(), userDetails.getUsername(), userDetails.getFirstName(),
				userDetails.getLastName(), userDetails.getAuthorities(), BEARER, token);
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
		if (userDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.USER_NOT_FOUND_KEY),
					new Object[] { userId }, LocaleContextHolder.getLocale()));
		}
		EntityModel<UserDTO> entityModel = new EntityModel<>(userDTO);
		return entityModel.add(linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel());
	}

}
