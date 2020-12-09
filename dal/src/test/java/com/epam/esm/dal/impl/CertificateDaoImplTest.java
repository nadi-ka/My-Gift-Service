package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.config.DalSpringConfig;
import com.epam.esm.dal.util.SqlQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@SpringBootTest
@EnableAutoConfiguration
@SpringJUnitConfig(DalSpringConfig.class)
class CertificateDaoImplTest {

	@PersistenceContext
	private EntityManager entityManager;
	private SqlQueryBuilder builder;
	private GiftCertificate certificate;
	private LocalDateTime timeStamp;
	private static final String TAG_NAME_ROMANCE = "#Romance";
	private static final String NAME_CHOCOLATIER = "Master-class from chocolatier";
	private static final String NAME_SKYDIVING = "Skydiving";
	private static final String DESCRIPTION_CHOCOLATIER = "Two hours master-class for couple";
	private static final BigDecimal CERT_PRICE = new BigDecimal(92.90);
	private static final int CERT_DURATION = 100;
	private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String ZONE = "Europe/Minsk";
	private static final long ID_CERT_TO_UPDATE = 3;
	private static final long ID_ABSENT = 9999;

	@Autowired
	private TagDao tagDao = new TagDaoSql(entityManager);

	@Autowired
	private CertificateDao certificateDao = new CertificateDaoSql(entityManager, builder, tagDao);

	@BeforeEach
	public void setUp() {
		timeStamp = getTimestamp();
		List<Tag> tags = new ArrayList<Tag>();
		Tag tag = new Tag();
		tag.setName(TAG_NAME_ROMANCE);
		tags.add(tag);
		certificate = new GiftCertificate();
		certificate.setName(NAME_CHOCOLATIER);
		certificate.setDescription(DESCRIPTION_CHOCOLATIER);
		certificate.setPrice(CERT_PRICE);
		certificate.setCreationDate(timeStamp);
		certificate.setLastUpdateDate(timeStamp);
		certificate.setDuration(CERT_DURATION);
		certificate.setTags(tags);
	}

	@AfterEach
	public void tearDown() {
		timeStamp = null;
		certificate = null;
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#addCertificate(com.epam.esm.entity.GiftCertificate)}.
	 */
	@Test
	void testAddCertificate() {
		GiftCertificate createdCertificate = certificateDao.addCertificate(certificate);

		assertNotNull(createdCertificate);
		assertEquals(NAME_CHOCOLATIER, createdCertificate.getName());
		certificateDao.deleteCertificate(createdCertificate.getId());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#updateCertificate(com.epam.esm.entity.GiftCertificate)}.
	 */
	
	@Test
	void testUpdateCertificate() {
		GiftCertificate updatedCertificate = certificateDao.updateCertificate(ID_CERT_TO_UPDATE, certificate);

		assertNotNull(updatedCertificate);
		assertEquals(ID_CERT_TO_UPDATE, updatedCertificate.getId());
		assertEquals(NAME_CHOCOLATIER, updatedCertificate.getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#findCertificates(java.util.List, java.util.List, com.epam.esm.entity.Pagination)}.
	 */
	@Test
	void testfindCertificates() {
		Pagination pagination = new Pagination(2, 0);
		List<FilterParam> filterParams = new ArrayList<>();
		String filterParamName = "name";
		String filterParamVal = "Skydiving";
		filterParams.add(new FilterParam(filterParamName, filterParamVal));
		List<OrderParam> orderParams = new ArrayList<>();
		String orderParamName = "creationDate";
		String orderParamDirection = "desc";
		orderParams.add(new OrderParam(orderParamName, orderParamDirection));
		List<GiftCertificate> actualCertificates = certificateDao.findCertificates(filterParams, orderParams, pagination);

		assertNotNull(actualCertificates);
		assertTrue(actualCertificates.size() == 2);
		assertTrue(actualCertificates.get(0).getId() == 2);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#findCertificate(long)}.
	 */
	@Test
	void testFindCertificate_PositiveResult() {
		GiftCertificate certificateActual = certificateDao.findCertificate(1);

		assertNotNull(certificateActual);
		assertEquals(1, certificateActual.getId());
		assertEquals(NAME_SKYDIVING, certificateActual.getName());
	}

	@Test
	void testFindCertificate_NegativaResult_NotFound() {
		GiftCertificate certificate = certificateDao.findCertificate(ID_ABSENT);

		assertNull(certificate);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#deleteCertificate(long)}.
	 */
	@Test
	void testDeleteCertificate_PositiveResult_DeletedSuccessfully() {
		GiftCertificate createdCertificate = certificateDao.addCertificate(certificate);
		certificateDao.deleteCertificate(createdCertificate.getId());
		
		assertNull(certificateDao.findCertificate(createdCertificate.getId()));
	}
	
	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#getCertificatesTotalCost(java.util.List)}.
	 */
	@Test
	void getCertificatesTotalCost() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);
		Double sumExpected = 140.0;
		
		assertTrue(sumExpected == certificateDao.getCertificatesTotalCost(ids).doubleValue());
	}
	
	@Test
	void getCertificatesTotalCost_Not_Found() {
		List<Long> ids = new ArrayList<>();
		ids.add(ID_ABSENT);
		
		assertNull(certificateDao.getCertificatesTotalCost(ids));
	}
	
	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#certificatesExistForPurchase(java.util.List)}.
	 */
	@Test
	void certificatesExistForPurchase() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);

		assertTrue(certificateDao.certificatesExistForPurchase(ids));
	}
	
	@Test
	void certificatesExistForPurchase_Not_Found() {
		List<Long> ids = new ArrayList<>();
		ids.add(ID_ABSENT);

		assertFalse(certificateDao.certificatesExistForPurchase(ids));
	}
	
	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#findCertificatesByTags(java.util.List, com.epam.esm.entity.Pagination)}.
	 */
	@Test
	void findCertificatesByTags() {
		Long[] ids = {1L};
		int sizeExpected = 2;
		
		assertTrue(certificateDao.findCertificatesByTags(ids, new Pagination(2, 0)).size() == sizeExpected);
	}
	

	private LocalDateTime getTimestamp() {
		ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of(ZONE));
		LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
		zoneEuropeMinsk.format(formatter);
		return ldt;
	}

}
