package com.epam.esm.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.PurchaseDao;
import com.epam.esm.dto.PurchaseDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.util.DateTimeFormatterISO;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired
	private PurchaseDao orderDao;

	@Autowired
	private ModelMapper modelMapper;

	private static final Logger log = LogManager.getLogger(PurchaseServiceImpl.class);

	public void setModelMapper(ModelMapper mapper) {
		this.modelMapper = mapper;
	}

	@Override
	public List<PurchaseDTO> getPurchasesByUserId(long userId) {

		List<Purchase> orders = orderDao.findPurchsesByUserId(userId);

		if (orders == null) {
			return null;
		}

		return orders.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public PurchaseDTO savePurchase(long userId, long[] certificateId) {

		LocalDateTime creationTime = DateTimeFormatterISO.createAndformatDateTime();

		User user = new User();
		user.setId(userId);

		List<GiftCertificate> certificates = formCertificateList(certificateId);

		Purchase purchase = new Purchase(creationTime, user, certificates);

		long purchaseId =  orderDao.addPurchase(purchase);
		
		Purchase savedPurchase = orderDao.findPurchseById(purchaseId);

		return convertToDto(savedPurchase);
	}

	private PurchaseDTO convertToDto(Purchase order) {

		PurchaseDTO orderDTO = modelMapper.map(order, PurchaseDTO.class);

		return orderDTO;
	}

	private Purchase convertToEntity(PurchaseDTO orderDTO) {

		Purchase order = modelMapper.map(orderDTO, Purchase.class);

		return order;
	}

	private List<GiftCertificate> formCertificateList(long[] certificateId) {

		List<GiftCertificate> certificates = new ArrayList<GiftCertificate>();

		for (int i = 0; i < certificateId.length; i++) {

			GiftCertificate certificateFromOrder = new GiftCertificate();
			certificateFromOrder.setId(certificateId[i]);

			certificates.add(certificateFromOrder);
		}
		return certificates;
	}

}
