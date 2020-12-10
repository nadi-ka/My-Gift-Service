package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.dal.config.DalSpringConfig;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import com.epam.esm.transferobj.Pagination;

@SpringBootTest
@EnableAutoConfiguration
@SpringJUnitConfig(DalSpringConfig.class)
class PurchaseDaoImplTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	private Purchase purchase;
	private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String ZONE = "Europe/Minsk";
	private static final BigDecimal PURCHASE_COST = new BigDecimal(150.00);
	private static final long ID_ABSENT = 9999;
	
	@Autowired
	private PurchaseDao purchaseDao = new PurchaseDaoImpl(entityManager);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		purchase = new Purchase();
		purchase.setUser(new User(2L));
		purchase.setCreationDate(getTimestamp());
		purchase.setCost(PURCHASE_COST);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		purchase = null;
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.PurchaseDaoImpl#findPurchsesByUserId(long, com.epam.esm.transferobj.Pagination)}.
	 */
	@Test
	void testFindPurchsesByUserId_Positive_Result() {
		List<Purchase> purchases = purchaseDao.findPurchsesByUserId(1, new Pagination(2, 0));
		int sizeExpected = 2;
		
		assertNotNull(purchases);
		assertTrue(purchases.size() == sizeExpected);
	}
	
	@Test
	void testFindPurchsesByUserId_Not_Found() {
		
		assertTrue(purchaseDao.findPurchsesByUserId(ID_ABSENT, new Pagination(2, 0)).isEmpty());
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.PurchaseDaoImpl#findPurchseById(long)}.
	 */
	@Test
	void testFindPurchseById_Positive_Result() {

		assertTrue(PURCHASE_COST.doubleValue() == purchaseDao.findPurchseById(1).getCost().doubleValue());
	}
	
	@Test
	void testFindPurchseById_Not_Found() {

		assertNull(purchaseDao.findPurchseById(ID_ABSENT));
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.PurchaseDaoImpl#addPurchase(com.epam.esm.entity.Purchase)}.
	 */
	@Test
	void testAddPurchase() {
		
		assertNotNull(purchaseDao.addPurchase(purchase));
	}
	
	private LocalDateTime getTimestamp() {
		ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of(ZONE));
		LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
		zoneEuropeMinsk.format(formatter);
		return ldt;
	}

}
