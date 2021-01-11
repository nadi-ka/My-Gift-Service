package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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
import com.epam.esm.dal.UserDao;
import com.epam.esm.dto.GiftCertificateIdsOnlyDTO;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.config.ServiceSpringConfig;
import com.epam.esm.service.exception.EntityNotFoundServiceException;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.transferobj.Pagination;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(ServiceSpringConfig.class)
@SpringBootTest
@EnableAutoConfiguration
class PurchaseServiceImplTest {

	@Mock
	private PurchaseDao purchaseDao;

	@Mock
	private CertificateDao certificateDao;
	@Mock
	private UserDao userDao;
	private ModelMapper modelMapper;
	private Purchase purchase1;
	private Purchase purchase2;
	private List<Purchase> purchases;
	private static final BigDecimal PURCHASE_COST_FIRST = new BigDecimal(150.00);
	private static final BigDecimal PURCHASE_COST_SECOND = new BigDecimal(70.00);
	private static final long ID_ABSENT = 9999;

	@InjectMocks
	@Autowired
	private PurchaseService purchaseService = new PurchaseServiceImpl(purchaseDao, userDao, modelMapper,
			certificateDao);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		purchases = new ArrayList<>();
		purchase1 = new Purchase();
		purchase1.setUser(new User(2L));
		purchase1.setCreationDate(DateTimeFormatterISO.createAndformatDateTime());
		purchase1.setCost(PURCHASE_COST_FIRST);

		purchase2 = new Purchase();
		purchase2.setUser(new User(2L));
		purchase2.setCreationDate(DateTimeFormatterISO.createAndformatDateTime());
		purchase2.setCost(PURCHASE_COST_SECOND);

		purchases.add(purchase1);
		purchases.add(purchase2);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		purchases = null;
		purchase1 = null;
		purchase2 = null;
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.PurchaseServiceImpl#getPurchasesByUserId(long, com.epam.esm.transferobj.Pagination)}.
	 */
	@Test
	void testGetPurchasesByUserId_Positive_Result() {
		Mockito.when(purchaseDao.findPurchsesByUserId(Mockito.anyLong(), Mockito.any(Pagination.class)))
				.thenReturn(purchases);
		int sizeExpected = 2;

		assertTrue(purchaseService.getPurchasesByUserId(1L, new Pagination(2, 0)).size() == sizeExpected);
	}

	@Test
	void testGetPurchasesByUserId_Not_Found() {
		Mockito.when(purchaseDao.findPurchsesByUserId(Mockito.anyLong(), Mockito.any(Pagination.class)))
				.thenReturn(new ArrayList<Purchase>());

		assertTrue(purchaseService.getPurchasesByUserId(ID_ABSENT, new Pagination(2, 0)).isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.PurchaseServiceImpl#getPurchaseById(long)}.
	 */
	@Test
	void testGetPurchaseById_Positive_Result() {
		Mockito.when(purchaseDao.findPurchseById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(purchase1);

		assertTrue(PURCHASE_COST_FIRST.doubleValue() == purchaseService.getPurchaseById(1L, 1L).getCost().doubleValue());
	}

	@Test
	void testGetPurchaseById_Not_Found() {
		Mockito.when(purchaseDao.findPurchseById(ID_ABSENT, 1L)).thenReturn(null);

		assertNull(purchaseService.getPurchaseById(ID_ABSENT, 1L));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.PurchaseServiceImpl#savePurchase(long, java.util.List)}.
	 */
	@Test
	void testSavePurchase() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);
		Mockito.when(userDao.findUser(Mockito.anyLong())).thenReturn(new User());
		Mockito.when(certificateDao.certificatesExistForPurchase(Mockito.any())).thenReturn(true);
		Mockito.when(certificateDao.getCertificatesTotalCost(ids)).thenReturn(PURCHASE_COST_FIRST);
		Mockito.when(purchaseDao.addPurchase(Mockito.any(Purchase.class))).thenReturn(purchase1);

		assertNotNull(purchaseService.savePurchase(1L, getCertificatesWithIds()));
	}

	@Test
	void testSavePurchase_User_Not_Found() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);
		Mockito.when(userDao.findUser(Mockito.anyLong())).thenReturn(null);

		assertThrows(EntityNotFoundServiceException.class, 
				() -> purchaseService.savePurchase(ID_ABSENT, new ArrayList<GiftCertificateIdsOnlyDTO>()),
		           "Expected to throw, but it didn't");
	}

	private List<GiftCertificateIdsOnlyDTO> getCertificatesWithIds() {
		List<GiftCertificateIdsOnlyDTO> ids = new ArrayList<GiftCertificateIdsOnlyDTO>();
		ids.add(new GiftCertificateIdsOnlyDTO(1L));
		ids.add(new GiftCertificateIdsOnlyDTO(2L));
		return ids;
	}

}
