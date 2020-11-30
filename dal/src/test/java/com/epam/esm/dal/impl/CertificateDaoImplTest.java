package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
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

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(DalSpringConfig.class)
@EnableAutoConfiguration
class CertificateDaoImplTest {

	private SessionFactory sessionFactory;
	private SqlQueryBuilder builder;
	private GiftCertificate certificate;
	private LocalDateTime timeStamp;
	private static final String TAG_NAME_ROMANCE = "#Romance";
	private static final String NAME_CHOCOLATIER = "Master-class from chocolatier";
	private static final String NAME_SKYDIVING = "Skydiving";
	private static final String DESCRIPTION_CHOCOLATIER = "Two hours master-class for couple";
	private static final Double CERT_PRICE = 92.90;
	private static final int CERT_DURATION = 100;
	private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String ZONE = "Europe/Minsk";
	private static final long ID_CERT_TO_UPDATE = 3;
	private static final long ID_ABSENT = 9999;

	@Autowired
	private TagDao tagDao = new TagDaoSql(sessionFactory);

	@Autowired
	private CertificateDao certificateDao = new CertificateDaoSql(sessionFactory, builder, tagDao);

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
		
		assertTrue(certificateDao.deleteCertificate(createdCertificate.getId()) == 1);
	}

	@Test
	void testDeleteCertificate_NegativeResult_CertificateAbsent() {
		
		assertTrue(certificateDao.deleteCertificate(ID_ABSENT) == 0);
	}
	
	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#getSumCertificatesPrice(java.util.List)}.
	 */
	@Test
	void getSumCertificatesPrice() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);
		
		Double sumExpected = 140.0;
		assertEquals(sumExpected, certificateDao.getSumCertificatesPrice(ids));
	}
	
	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#getAmountOfCertificates(java.util.List)}.
	 */
	@Test
	void getAmountOfCertificates() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);

		assertEquals(ids.size(), certificateDao.getAmountOfCertificates(ids));
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
