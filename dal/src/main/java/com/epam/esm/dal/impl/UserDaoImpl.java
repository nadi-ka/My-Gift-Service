package com.epam.esm.dal.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.UserDao;
import com.epam.esm.entity.User;

@Repository("userDao")
@Transactional
public class UserDaoImpl implements UserDao {

	private static final String FIND_USER_BY_LOGIN = "SELECT u FROM User u WHERE login = :userLogin";
	private static final String PARAM_USER_LOGIN = "userLogin";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public UserDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public User addUser(User user) {
		entityManager.persist(user);
		return user;
	}

	@Override
	public User findUser(long id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public User findUserByLogin(String login) {
		List<User> users = entityManager.createQuery(FIND_USER_BY_LOGIN, User.class)
				.setParameter(PARAM_USER_LOGIN, login).getResultList();
		return (!users.isEmpty() ? users.get(0) : null);
	}

}
