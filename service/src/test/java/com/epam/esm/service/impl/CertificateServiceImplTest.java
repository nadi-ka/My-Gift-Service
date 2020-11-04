package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

	@Mock
	private CertificateDao certificateDao;

	@InjectMocks
	private CertificateService certificateService = new CertificateServiceImpl(new ModelMapper());

	private static LocalDateTime stamp;

	@BeforeTestClass
	public static void setUpBeforeClass() throws Exception {
		
		stamp = DateTimeFormatterISO.createAndformatDateTime();
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#getCertificates(java.util.List, java.util.List)}.
	 */
	@Test
	void testGetCertificates() {

		List<FilterParam> filterParams = new ArrayList<FilterParam>();
		List<OrderParam> orderParams = new ArrayList<OrderParam>();
		filterParams.add(new FilterParam("tag", "Romance"));
		orderParams.add(new OrderParam("date", "desc"));

		Mockito.when(certificateDao.findCertificates(filterParams, orderParams)).thenReturn(certificates());
		List<GiftCertificateGetDTO> certificatesActual = certificateService.getCertificates(filterParams, orderParams);

		assertTrue(certificatesActual.size() == 2);
		assertEquals("Master-class from chocolatier", certificatesActual.get(0).getName());
		assertEquals("Yachting", certificatesActual.get(1).getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#getCertificate(long)}.
	 */
	@Test
	void testGetCertificate_PositiveResult() {

		GiftCertificate certificate = certificate();

		Mockito.when(certificateDao.findCertificate(1)).thenReturn(certificate);
		GiftCertificateGetDTO certificateActual = certificateService.getCertificate(1);

		assertEquals("Master-class from chocolatier", certificateActual.getName());
		assertTrue(certificateActual.getPrice() == 95.50);
	}

	@Test
	void testGetCertificate_NotFound() {

		Mockito.when(certificateDao.findCertificate(999)).thenReturn(null);
		GiftCertificateGetDTO certificateActual = certificateService.getCertificate(999);

		assertNull(certificateActual);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#saveCertificate(com.epam.esm.dto.GiftCertificateCreateUpdateDTO)}.
	 */
	@Test
	void testSaveCertificate() {

		Mockito.when(certificateDao.addCertificate(Mockito.any())).thenReturn(certificateFull());
		GiftCertificateGetDTO actual = certificateService.saveCertificate(certificateCreateUpdateDTO());

		assertNotNull(actual);
		assertEquals("Master-class from chocolatier", actual.getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#updateCertificate(com.epam.esm.dto.GiftCertificateCreateUpdateDTO)}.
	 */
	@Test
	void testUpdateCertificate_PositiveResult_Updated() {

		Mockito.when(certificateDao.updateCertificate(Mockito.any())).thenReturn(certificateFull());
		GiftCertificateGetDTO actual = certificateService.updateCertificate(certificateCreateUpdateDTO());

		assertNotNull(actual);
		assertEquals("Master-class from chocolatier", actual.getName());
	}
	
	@Test
	void testUpdateCertificate_NegativeResult_NotUpdated() {

		Mockito.when(certificateDao.updateCertificate(Mockito.any())).thenReturn(null);
		GiftCertificateGetDTO actual = certificateService.updateCertificate(certificateCreateUpdateDTO());

		assertNull(actual);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#deleteCertificate(long)}.
	 */
	@Test
	void testDeleteCertificate() {

		Mockito.when(certificateDao.deleteCertificate(1)).thenReturn(new int[] { 1, 1 });
		int[] affectedRows = certificateService.deleteCertificate(1);

		assertArrayEquals(new int[] { 1, 1 }, affectedRows);
	}

	private GiftCertificate certificate() {

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

	private List<GiftCertificate> certificates() {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();

		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(1, "#Sport"));
		tags.add(new Tag(2, "#Romance"));

		GiftCertificate certificate = new GiftCertificate();
		certificate.setName("Yachting");
		certificate.setDescription("Romantic journey for couple");
		certificate.setPrice(169.90);
		certificate.setCreationDate(stamp);
		certificate.setLastUpdateDate(stamp);
		certificate.setDuration(120);
		certificate.setTags(tags);

		certificates.add(certificate());
		certificates.add(certificate);

		return certificates;
	}


	private GiftCertificateCreateUpdateDTO certificateCreateUpdateDTO() {

		List<TagDTO> tags = new ArrayList<TagDTO>();
		tags.add(new TagDTO(2, "#Romance"));

		GiftCertificateCreateUpdateDTO certificate = new GiftCertificateCreateUpdateDTO();
		certificate.setName("Master-class from chocolatier");
		certificate.setDescription("Two hours master-class for couple");
		certificate.setPrice(95.50);
		certificate.setDuration(90);
		certificate.setTags(tags);

		return certificate;
	}

	private GiftCertificate certificateFull() {

		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(2, "#Romance"));

		GiftCertificate certificate = new GiftCertificate();
		certificate.setId(3);
		certificate.setName("Master-class from chocolatier");
		certificate.setDescription("Two hours master-class for couple");
		certificate.setPrice(95.50);
		certificate.setCreationDate(stamp);
		certificate.setLastUpdateDate(stamp);
		certificate.setDuration(90);
		certificate.setTags(tags);

		return certificate();
	}

}
