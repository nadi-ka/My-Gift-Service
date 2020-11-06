package com.epam.esm.dal.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.UserDao;
import com.epam.esm.entity.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger log = LogManager.getLogger(UserDaoImpl.class);
	
	public void setSessionFactory(SessionFactory sessionFactory) {
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
		log.info("########################User: " + user) ;
		
		return user;
	}

	@Override
	public int deleteUser(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUser(long id, User user) {
		// TODO Auto-generated method stub
		return 0;
	}

}
