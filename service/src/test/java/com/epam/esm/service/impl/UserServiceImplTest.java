package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.epam.esm.dal.UserDao;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.entity.User;
import com.epam.esm.entity.role.Role;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.ServiceSpringConfig;
import com.epam.esm.service.exception.NotUniqueEmailException;
import com.epam.esm.service.exception.NotUniqueLoginException;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(ServiceSpringConfig.class)
@SpringBootTest
@EnableAutoConfiguration
class UserServiceImplTest {

	@Mock
	private UserDao userDao;
	private ModelMapper modelMapper;
	private User user;
	private static final String USER_FIRST_NAME = "Ann";
	private static final String USER_LAST_NAME = "Smith";
	private static final String USER_DATE_OF_BIRTH = "1987-05-05";
	private static final String USER_EMAIL = "ann87@gmail.com";
	private static final String USER_LOGIN = "Smith";
	private static final Role USER_ROLE = Role.USER;
	private static final String USER_PASSWORD = "$2y$12$mtvrCDOczbEHT04GXOTwg.k02XtCNM40HJp38Gt4GMYHhKmB5N2WS ";
	private static final long ID_ABSENT = 9999;

	@InjectMocks
	@Autowired
	private UserService userService = new UserServiceImpl(userDao, modelMapper);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		user = new User(USER_FIRST_NAME, USER_LAST_NAME, LocalDate.parse(USER_DATE_OF_BIRTH), USER_EMAIL, USER_LOGIN,
				USER_PASSWORD, USER_ROLE);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		user = null;
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.UserServiceImpl#saveUser(com.epam.esm.dto.UserDTO)}.
	 */
	@Test
	void testSaveUser_Successfully_saved() {
		Mockito.when(userDao.findUserByLogin(Mockito.anyString())).thenReturn(null);
		Mockito.when(userDao.findUserByLogin(Mockito.anyString())).thenReturn(null);
		Mockito.when(userDao.addUser(Mockito.any(User.class))).thenReturn(user);

		assertEquals(USER_LAST_NAME, userService.saveUser(getUserDTO()).getLastName());
	}

	@Test
	void testSaveUser_ExceptionThrown_AsLoginAlreadyExists() {
		Mockito.when(userDao.findUserByLogin(Mockito.anyString())).thenReturn(user);
		NotUniqueLoginException thrown = assertThrows(NotUniqueLoginException.class,
				() -> userService.saveUser(getUserDTO()), "Expected saveUser() to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("The login is already exists"));
	}
	
	@Test
	void testSaveUser_ExceptionThrown_AsEmailAlreadyExists() {
		Mockito.when(userDao.findUserByLogin(Mockito.anyString())).thenReturn(null);
		Mockito.when(userDao.findUserByEmail(Mockito.anyString())).thenReturn(user);
		NotUniqueEmailException thrown = assertThrows(NotUniqueEmailException.class,
				() -> userService.saveUser(getUserDTO()), "Expected saveUser() to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("The email is already exists"));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.UserServiceImpl#getUser(long)}.
	 */
	@Test
	void testGetUser_Positive_result() {
		Mockito.when(userDao.findUser(1L)).thenReturn(user);

		assertEquals(USER_LAST_NAME, userService.getUser(1L).getLastName());
	}

	@Test
	void testGetUser_Not_Found() {
		Mockito.when(userDao.findUser(ID_ABSENT)).thenReturn(null);

		assertNull(userService.getUser(ID_ABSENT));
	}

	private UserRegisterDTO getUserDTO() {
		return new UserRegisterDTO(USER_FIRST_NAME, USER_LAST_NAME, LocalDate.parse(USER_DATE_OF_BIRTH), USER_EMAIL,
				USER_LOGIN, USER_PASSWORD);
	}

}
