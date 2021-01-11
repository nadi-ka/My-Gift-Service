package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.dto.GiftCertificateCreateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateUpdateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.config.ServiceSpringConfig;
import com.epam.esm.service.exception.IllegalOperationServiceException;
import com.epam.esm.service.exception.ServiceValidationException;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.Pagination;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(ServiceSpringConfig.class)
@SpringBootTest
@EnableAutoConfiguration
class CertificateServiceImplTest {

	@Mock
	private CertificateDao certificateDao;
	@Mock
	private PurchaseDao purchaseDao;
	private ModelMapper modelMapper;
	private LocalDateTime timeStamp;
	private GiftCertificate certificate1;
	private GiftCertificate certificate2;
	private List<GiftCertificate> certificates;
	private List<Tag> tags;
	private static final String TAG_NAME_ROMANSE = "#Romance";
	private static final String NAME_CHOCOLATIER = "Master-class from chocolatier";
	private static final String NAME_YACHTING = "YACHTING";
	private static final String DESCRIPTION_CHOCOLATIER = "Two hours master-class for couple";
	private static final String DESCRIPTION_YACHTING = "Romantic journey for couple";
	private static final BigDecimal CERT_PRICE = new BigDecimal(92.90);
	private static final int CERT_DURATION = 100;
	private static final long ID_ABSENT = 9999;

	@InjectMocks
	@Autowired
	private CertificateService certificateService = new CertificateServiceImpl(certificateDao, purchaseDao,
			modelMapper);

	@BeforeEach
	void setUp() {
		timeStamp = DateTimeFormatterISO.createAndformatDateTime();
		tags = new ArrayList<Tag>();
		certificates = new ArrayList<>();
		tags.add(new Tag(2, TAG_NAME_ROMANSE));
		certificate1 = new GiftCertificate();
		certificate1.setName(NAME_CHOCOLATIER);
		certificate1.setDescription(DESCRIPTION_CHOCOLATIER);
		certificate1.setPrice(CERT_PRICE);
		certificate1.setCreationDate(timeStamp);
		certificate1.setLastUpdateDate(timeStamp);
		certificate1.setDuration(CERT_DURATION);
		certificate1.setTags(tags);

		certificate2 = new GiftCertificate();
		certificate2.setName(NAME_YACHTING);
		certificate2.setDescription(DESCRIPTION_YACHTING);
		certificate2.setPrice(CERT_PRICE);
		certificate2.setCreationDate(timeStamp);
		certificate2.setLastUpdateDate(timeStamp);
		certificate2.setDuration(CERT_DURATION);
		certificate2.setTags(tags);

		certificates.add(certificate1);
		certificates.add(certificate2);
	}

	@AfterEach
	public void tearDown() {
		timeStamp = null;
		tags = null;
		certificate1 = null;
		certificate2 = null;
		certificates = null;
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#getCertificates(java.util.List, java.util.List, com.epam.esm.transferobj.Pagination)}.
	 */
	@Test
	void testGetCertificates() throws ServiceValidationException {
		List<FilterParam> filterParams = new ArrayList<FilterParam>();
		String filterParamName = "name";
		String filterParamVal = "Skydiving";
		filterParams.add(new FilterParam(filterParamName, filterParamVal));
		List<OrderParam> orderParams = new ArrayList<OrderParam>();
		String orderParamName = "creationDate";
		String orderParamDirection = "desc";
		orderParams.add(new OrderParam(orderParamName, orderParamDirection));
		Pagination pagination = new Pagination(2, 0);
		Mockito.when(certificateDao.findCertificates(filterParams, orderParams, pagination)).thenReturn(certificates);
		List<GiftCertificateGetDTO> certificatesActual = certificateService.getCertificates(filterParams, orderParams,
				pagination);
		int expectedListSize = 2;

		assertTrue(certificatesActual.size() == expectedListSize);
		assertEquals(NAME_CHOCOLATIER, certificatesActual.get(0).getName());
		assertEquals(NAME_YACHTING, certificatesActual.get(1).getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#getCertificate(long)}.
	 */
	@Test
	void testGetCertificate_PositiveResult() {
		Mockito.when(certificateDao.findCertificate(1)).thenReturn(certificate1);

		assertEquals(NAME_CHOCOLATIER, certificateService.getCertificate(1).getName());
	}

	@Test
	void testGetCertificate_NotFound() {
		Mockito.when(certificateDao.findCertificate(ID_ABSENT)).thenReturn(null);
		GiftCertificateGetDTO certificateActual = certificateService.getCertificate(ID_ABSENT);

		assertNull(certificateActual);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#saveCertificate(com.epam.esm.dto.GiftCertificateCreateUpdateDTO)}.
	 */
	@Test
	void testSaveCertificate() {
		Mockito.when(certificateDao.addCertificate(Mockito.any())).thenReturn(certificate1);
		GiftCertificateGetDTO actual = certificateService.saveCertificate(getCertificateCreateDTO());

		assertNotNull(actual);
		assertEquals(NAME_CHOCOLATIER, actual.getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#updateCertificate(long, com.epam.esm.dto.GiftCertificateCreateUpdateDTO)}.
	 */
	@Test
	void testUpdateCertificate_PositiveResult_Updated() {
		Mockito.when(certificateDao.updateCertificate(Mockito.anyLong(), Mockito.any())).thenReturn(certificate1);
		GiftCertificateGetDTO actual = certificateService.updateCertificate(1, getCertificateUpdateDTO());

		assertNotNull(actual);
		assertEquals(NAME_CHOCOLATIER, actual.getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#deleteCertificate(long)}.
	 */
	@Test
	void testDeleteCertificate_NotDeleted_AsBoundWithPurchase() {
		Mockito.when(purchaseDao.purchaseExistsForCertificate(Mockito.anyLong())).thenReturn(true);
		IllegalOperationServiceException thrown = assertThrows(IllegalOperationServiceException.class,
				() -> certificateService.deleteCertificate(1L), "Expected deleteCertificate() to throw, but it didn't");
		
		assertTrue(thrown.getMessage().contains("The certificate is bounded with one or more purchases"));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.CertificateServiceImpl#getCertificatesByTags(Long[], com.epam.esm.transferobj.Pagination)}.
	 */
	@Test
	void testGetCertificatesByTags() {
		Mockito.when(certificateDao.findCertificatesByTags(Mockito.any(), Mockito.any(Pagination.class)))
				.thenReturn(certificates);
		Long[] ids = { 1L };
		List<GiftCertificateGetDTO> actual = certificateService.getCertificatesByTags(ids, new Pagination(2, 0));
		int sizeExpected = 2;

		assertTrue(actual.size() == sizeExpected);
	}

	private GiftCertificateCreateDTO getCertificateCreateDTO() {
		List<TagDTO> tags = new ArrayList<TagDTO>();
		tags.add(new TagDTO(2, TAG_NAME_ROMANSE));
		GiftCertificateCreateDTO certificate = new GiftCertificateCreateDTO();
		certificate.setName(NAME_CHOCOLATIER);
		certificate.setDescription(DESCRIPTION_CHOCOLATIER);
		certificate.setPrice(CERT_PRICE);
		certificate.setDuration(CERT_DURATION);
		certificate.setTags(tags);

		return certificate;
	}

	private GiftCertificateUpdateDTO getCertificateUpdateDTO() {
		List<TagDTO> tags = new ArrayList<TagDTO>();
		tags.add(new TagDTO(2, TAG_NAME_ROMANSE));
		GiftCertificateUpdateDTO certificate = new GiftCertificateUpdateDTO();
		certificate.setName(NAME_CHOCOLATIER);
		certificate.setDescription(DESCRIPTION_CHOCOLATIER);
		certificate.setPrice(CERT_PRICE);
		certificate.setDuration(CERT_DURATION);
		certificate.setCreationDate(timeStamp);
		certificate.setTags(tags);

		return certificate;
	}

}
