package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateWithIdDTO;
import com.epam.esm.dto.PurchaseDTO;

public interface PurchaseService {
	
	List<PurchaseDTO> getPurchasesByUserId(long userId);
	
	PurchaseDTO getPurchaseById(long purchaseId);
	
	PurchaseDTO savePurchase(long userId, List<GiftCertificateWithIdDTO> certificates);

}
