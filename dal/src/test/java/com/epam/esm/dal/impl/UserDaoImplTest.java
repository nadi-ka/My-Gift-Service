package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.epam.esm.dal.UserDao;
import com.epam.esm.dal.config.DalSpringConfig;
import com.epam.esm.entity.User;
import com.epam.esm.entity.role.Role;

@SpringBootTest
@EnableAutoConfiguration
@SpringJUnitConfig(DalSpringConfig.class)
@ActiveProfiles("test")
class UserDaoImplTest {

	@PersistenceContext
	private EntityManager entityManager;
	private User user;
	private static final String USER_FIRST_NAME = "Ann";
	private static final String FIRST_USER_LAST_NAME = "Smith";
	private static final String USER_DATE_OF_BIRTH = "1987-05-05";
	private static final String USER_EMAIL = "ann87@gmail.com";
	private static final String USER_LOGIN = "Smith";
	private static final String USER_PASSWORD = "$2y$12$mtvrCDOczbEHT04GXOTwg.k02XtCNM40HJp38Gt4GMYHhKmB5N2Ws ";
	private static final Role USER_ROLE = Role.USER;
	private static final long ID_ABSENT = 9999;
	private static final String LOGIN_ABSENT = "77777";

	@Autowired
	private UserDao userDao = new UserDaoImpl(entityManager);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		user = new User(USER_FIRST_NAME, FIRST_USER_LAST_NAME, LocalDate.parse(USER_DATE_OF_BIRTH), USER_EMAIL,
				USER_LOGIN, USER_PASSWORD, USER_ROLE);
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
	 * {@link com.epam.esm.dal.impl.UserDaoImpl#addUser(com.epam.esm.entity.User)}.
	 */
	@Test
	void testAddUser() {

		assertEquals(USER_FIRST_NAME, userDao.addUser(user).getFirstName());
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.UserDaoImpl#findUser(long)}.
	 */
	@Test
	void testFindUser_Positive_Result() {

		assertEquals(FIRST_USER_LAST_NAME, userDao.findUser(1L).getLastName());
	}

	@Test
	void testFindUser_Not_Found() {

		assertNull(userDao.findUser(ID_ABSENT));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.UserDaoImpl#findUserByLogin(String)}.
	 */
	@Test
	void testFindUserByLogin_Positive_Result() {

		assertNotNull(userDao.findUserByLogin(USER_LOGIN));
	}

	@Test
	void testFindUserByLogin_NOT_FOUND() {

		assertNull(userDao.findUserByLogin(LOGIN_ABSENT));
	}

}
