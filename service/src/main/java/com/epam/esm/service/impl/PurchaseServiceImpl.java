package com.epam.esm.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.dal.UserDao;
import com.epam.esm.dto.GiftCertificateIdsOnlyDTO;
import com.epam.esm.dto.PurchaseDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.exception.EntityNotFoundServiceException;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.transferobj.Pagination;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	private PurchaseDao purchaseDao;
	private UserDao userDao;
	private ModelMapper modelMapper;
	private CertificateDao certificateDao;

	@Autowired
	public PurchaseServiceImpl(PurchaseDao purchaseDao, UserDao userDao, ModelMapper modelMapper, CertificateDao certificateDao) {
		this.purchaseDao = purchaseDao;
		this.userDao = userDao;
		this.modelMapper = modelMapper;
		this.certificateDao = certificateDao;
	}

	@Override
	public List<PurchaseDTO> getPurchasesByUserId(long userId, Pagination pagination) {
		return purchaseDao.findPurchsesByUserId(userId, pagination).stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public PurchaseDTO getPurchaseById(long purchaseId) {
		Purchase purchase = purchaseDao.findPurchseById(purchaseId);
		if (purchase == null) {
			return null;
		}
		return convertToDto(purchase);
	}

	@Override
	public PurchaseDTO savePurchase(long userId, List<GiftCertificateIdsOnlyDTO> certificates) {
		if (userDao.findUser(userId) == null) {
			throw new EntityNotFoundServiceException("User not exists, id=" + userId);
		}
		LocalDateTime creationTime = DateTimeFormatterISO.createAndformatDateTime();
		List<GiftCertificate> certificatesWithIds = certificates.stream().map(this::convertToEntity).collect(Collectors.toList());
		List<Long> certificateIds = formCertificateIdsList(certificatesWithIds);
		if (!certificateDao.certificatesExistForPurchase(certificateIds)) {
			throw new EntityNotFoundServiceException("One or more certificates to bound purchase with not exists");
		}
		Purchase purchase = new Purchase(creationTime, new User(userId), certificatesWithIds);
		BigDecimal cost = certificateDao.getCertificatesTotalCost(certificateIds);
		purchase.setCost(cost);
		Purchase createdPurchase = purchaseDao.addPurchase(purchase);
		createdPurchase.setCertificates(new ArrayList<GiftCertificate>());
		return convertToDto(createdPurchase);
	}

	private PurchaseDTO convertToDto(Purchase order) {
		return modelMapper.map(order, PurchaseDTO.class);
	}

	private GiftCertificate convertToEntity(GiftCertificateIdsOnlyDTO certificateDTO) {
		return modelMapper.map(certificateDTO, GiftCertificate.class);
	}
	
	private List<Long> formCertificateIdsList(List<GiftCertificate> certificatesWithIds) {
		List<Long> ids = new ArrayList<>();
		for(GiftCertificate certificate: certificatesWithIds) {
			Long id = certificate.getId();
			ids.add(id);
		}
		return ids;
	}

}
