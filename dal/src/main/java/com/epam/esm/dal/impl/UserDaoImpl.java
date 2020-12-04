package com.epam.esm.dal.impl;

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
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	public  UserDaoImpl(EntityManager entityManager) {
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

}
