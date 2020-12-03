package com.epam.esm.dal.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.UserDao;
import com.epam.esm.entity.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	private SessionFactory sessionFactory;
	
	@Autowired
	public  UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public User addUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(user);
		return user;
	}

	@Override
	public User findUser(long id) {
		return sessionFactory.getCurrentSession().get(User.class, id);
	}

}
