package com.epam.esm.dal.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.entity.Purchase;
import com.epam.esm.transferobj.Pagination;

@Repository("purchaseDao")
@Transactional
public class PurchaseDaoImpl implements PurchaseDao {

	private static final String GET_PURCHASES_BY_USER_ID = "SELECT p FROM Purchase p JOIN p.user u WHERE u.id = :userId";
	private static final String GET_PURCHASE = "SELECT p FROM Purchase p JOIN p.user u WHERE p.id = :purchaseId AND u.id = :userId";
	private static final String PARAM_USER_ID = "userId";
	private static final String PARAM_PURCHASE_ID = "purchaseId";

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger LOG = LogManager.getLogger(PurchaseDaoImpl.class);

	@Autowired
	public PurchaseDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Purchase> findPurchsesByUserId(long userId, Pagination pagination) {
		return entityManager.createQuery(GET_PURCHASES_BY_USER_ID, Purchase.class).setParameter(PARAM_USER_ID, userId)
				.setFirstResult(pagination.getOffset()).setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public Purchase findPurchseById(long purchaseId, long userId) {
		List<Purchase> purchases = entityManager.createQuery(GET_PURCHASE, Purchase.class)
				.setParameter(PARAM_PURCHASE_ID, purchaseId).setParameter(PARAM_USER_ID, userId).getResultList();
		return ((purchases.isEmpty()) ? null : purchases.get(0));
	}

	@Override
	public Purchase addPurchase(Purchase purchase) {
		entityManager.persist(purchase);
		return purchase;
	}

}
