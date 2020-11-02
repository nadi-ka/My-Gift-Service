package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

class CertificateDaoSqlTest {

	private EmbeddedDatabase db;

	@BeforeEach
	void setUp() {

		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.setName("testDB;DATABASE_TO_UPPER=false;IGNORECASE=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;MODE=MySQL")
				.addScript("create-db.sql").addScript("insert-data.sql").build();
	}

	@AfterEach
	void tearDown() {

		db.shutdown();
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#addCertificate(com.epam.esm.entity.GiftCertificate)}.
	 */
	@Test
	void testAddCertificate_PositiveResult() {

		CertificateDaoSql certificateDao = getCertificateDao();
		GiftCertificate savedCertificate = certificateDao.addCertificate(getCertificate());

		assertNotNull(savedCertificate);
		assertEquals(4, savedCertificate.getId());
		assertEquals("Master-class from chocolatier", savedCertificate.getName());

	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#updateCertificate(com.epam.esm.entity.GiftCertificate)}.
	 */
	@Test
	void testUpdateCertificate_PositiveResult() {

		CertificateDaoSql certificateDao = getCertificateDao();
		GiftCertificate certificate = getCertificate();
		certificate.setId(1);
		certificateDao.updateCertificate(certificate);
		GiftCertificate updatedCertificateActual = certificateDao.findCertificate(1);

		assertNotNull(updatedCertificateActual);
		assertEquals(1, updatedCertificateActual.getId());
		assertEquals("Master-class from chocolatier", updatedCertificateActual.getName());

	}

	@Test
	void testUpdateCertificate_NegativeResult_NotUpdated() {

		CertificateDaoSql certificateDao = getCertificateDao();
		GiftCertificate certificate = getCertificate();
		certificate.setId(9999);
		GiftCertificate updatedCertificate = certificateDao.updateCertificate(certificate);

		assertNull(updatedCertificate);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#findCertificates(java.util.List, java.util.List)}.
	 */

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#findCertificate(long)}.
	 */
	@Test
	void testFindCertificate_PositiveResult() {

		CertificateDaoSql certificateDao = getCertificateDao();
		GiftCertificate certificateActual = certificateDao.findCertificate(1);

		assertNotNull(certificateActual);
		assertEquals(1, certificateActual.getId());
		assertEquals("Skydiving", certificateActual.getName());

	}

	@Test
	void testFindCertificate_NegativaResult_notFound() {

		CertificateDaoSql certificateDao = getCertificateDao();
		GiftCertificate certificate = certificateDao.findCertificate(9999);

		assertNull(certificate);

	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.CertificateDaoSql#deleteCertificate(long)}.
	 */
	@Test
	void testDeleteCertificate_PositiveResult_DeletedSuccessfully() {

		CertificateDaoSql certificateDao = getCertificateDao();
		certificateDao.deleteCertificate(1);

		GiftCertificate certificate = certificateDao.findCertificate(1);

		assertNull(certificate);

	}

	@Test
	void testDeleteCertificate_NegativeResult_CertificateAbsent() {

		CertificateDaoSql certificateDao = getCertificateDao();
		int[] affectedRowsActual = certificateDao.deleteCertificate(9999);
		int[] expected = new int[] {0, 0};
		
		assertArrayEquals(expected, affectedRowsActual);

	}

	private CertificateDaoSql getCertificateDao() {

		JdbcTemplate template = new JdbcTemplate(db);
		CertificateDaoSql certificateDao = new CertificateDaoSql(template);
		return certificateDao;
	}

	private GiftCertificate getCertificate() {
		
		ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
		String formatPattern = "yyyy-MM-dd HH:mm:ss";

	    LocalDateTime stamp = zoneEuropeMinsk.toLocalDateTime();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
	    zoneEuropeMinsk.format(formatter);
				
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(2, "#Romance"));

		GiftCertificate certificate = new GiftCertificate();
		certificate.setName("Master-class from chocolatier");
		certificate.setDescription("Two hours master-class for couple");
		certificate.setPrice(95.50);
		certificate.setCreationDate(stamp);
		certificate.setLastUpdateDate(stamp);
		certificate.setDuration(90);
		certificate.setTags(tags);

		return certificate;
	}

}
