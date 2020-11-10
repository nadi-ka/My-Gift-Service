package com.epam.esm.dal.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.OrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

@Repository
public class OrderDaoImpl implements OrderDao {
	
	private static final Logger log = LogManager.getLogger(OrderDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<Order> findOrdersByUserId(long userId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		User user = session.get(User.class, userId);
		log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$: " + user);
		
		List<Order> orders = user.getOrders();
		log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$: " + orders);
		
//		for (Order order: orders) {
//			
//			List<GiftCertificate> certificates = order.getCertificates();
//		}
//		log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$: " + orders);
		
		return orders;
	}

}
