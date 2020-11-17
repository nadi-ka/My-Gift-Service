package com.epam.esm.dal.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.UserDao;
import com.epam.esm.entity.User;

@Repository
public class UserDaoImpl implements UserDao {

	private SessionFactory sessionFactory;
	
	@Autowired
	public  UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public User addUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		long id = (Long) session.save(user);
		user.setId(id);
		return user;
	}

	@Override
	public List<User> findUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public User findUser(long id) {
		Session session = sessionFactory.getCurrentSession();
		User user = session.get(User.class, id);
		return user;
	}

}
