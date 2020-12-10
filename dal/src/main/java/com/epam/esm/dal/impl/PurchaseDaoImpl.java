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

	private static final String GET_PURCHASES_BY_USER_ID = "FROM Purchase WHERE Id_user = :userId";
	private static final String PARAM_USER_ID = "userId";

	@PersistenceContext
	private EntityManager entityManager;
	
	private static final Logger LOG = LogManager.getLogger(PurchaseDaoImpl.class);

	@Autowired
	public PurchaseDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Purchase> findPurchsesByUserId(long userId, Pagination pagination) {
		return entityManager.createQuery(GET_PURCHASES_BY_USER_ID, Purchase.class)
				.setParameter(PARAM_USER_ID, userId).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public Purchase findPurchseById(long purchaseId) {	
		return entityManager.find(Purchase.class, purchaseId);
	}

	@Override
	public Purchase addPurchase(Purchase purchase) {
		entityManager.persist(purchase);
		return purchase;
	}

}
