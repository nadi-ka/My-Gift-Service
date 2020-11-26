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

	private static final Logger log = LogManager.getLogger(PurchaseDaoImpl.class);

	private static final String GET_PURCHASES_BY_USER_ID = "FROM Purchase WHERE id_user = :userId";
	private static final String GET_PURCHASE_BY_ID = "FROM Purchase p WHERE p.id = :purchaseId";
	private static final String PARAM_USER_ID = "userId";
	private static final String PARAM_PURCHASE_ID = "purchaseId";

	private SessionFactory sessionFactory;

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
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Purchase> query = session.createQuery(GET_PURCHASE_BY_ID, Purchase.class);
			query.setParameter(PARAM_PURCHASE_ID, purchaseId);
			Purchase purchase = query.getSingleResult();
			return purchase;
		} catch (NoResultException e) {
			return new Purchase();
		}
	}

	@Override
	public long addPurchase(Purchase purchase) {
		Session session = sessionFactory.getCurrentSession();
		long purchaseId = (Long) session.save(purchase);
		return purchaseId;
	}

}
