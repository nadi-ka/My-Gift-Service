package com.epam.esm.dal.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Purchase;

@Repository
@Transactional
public class PurchaseDaoImpl implements PurchaseDao {

	private static final String GET_PURCHASES_BY_USER_ID = "FROM Purchase WHERE Id_user = :userId";
	private static final String GET_PURCHASE_BY_ID = "FROM Purchase p WHERE p.id = :purchaseId";
	private static final String PARAM_USER_ID = "userId";
	private static final String PARAM_PURCHASE_ID = "purchaseId";

	private SessionFactory sessionFactory;
	
	private static final Logger LOG = LogManager.getLogger(PurchaseDaoImpl.class);

	@Autowired
	public PurchaseDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Purchase> findPurchsesByUserId(long userId, Pagination pagination) {
		return sessionFactory.getCurrentSession().createQuery(GET_PURCHASES_BY_USER_ID, Purchase.class)
				.setParameter(PARAM_USER_ID, userId).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public Purchase findPurchseById(long purchaseId) {	
		return sessionFactory.getCurrentSession().find(Purchase.class, purchaseId);
	}

	@Override
	public long addPurchase(Purchase purchase) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(purchase);
		return purchase.getId();
	}

}
