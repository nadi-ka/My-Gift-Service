package com.epam.esm.dal.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.entity.Purchase;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {
	
	private static final Logger log = LogManager.getLogger(PurchaseDaoImpl.class);
	
	private static final String hqlGetPurchasesByUserId = "FROM Purchase where id_user = :userId";
	private static final String hqlGetPurchaseById = "FROM Purchase where id = :purchaseId";
	private static final String parameterUserId = "userId";
	private static final String parameterPurchaseId = "purchaseId";
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public PurchaseDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Purchase> findPurchsesByUserId(long userId) {
		Session session = sessionFactory.getCurrentSession();
		Query<Purchase> query = session.createQuery(hqlGetPurchasesByUserId, Purchase.class);
		query.setParameter(parameterUserId, userId);
		List<Purchase> purchases = query.getResultList();
		return purchases;
	}
	
	@Override
	@Transactional
	public Purchase findPurchseById(long purchaseId) {
		Session session = sessionFactory.getCurrentSession();
		Query<Purchase> query = session.createQuery(hqlGetPurchaseById, Purchase.class);
		query.setParameter(parameterPurchaseId, purchaseId);
		Purchase purchase = query.getSingleResult();
		return purchase;
	}

	@Override
	@Transactional
	public long addPurchase(Purchase purchase) {
		Session session = sessionFactory.getCurrentSession();
		long purchaseId = (Long) session.save(purchase);
		return purchaseId;
	}

}
