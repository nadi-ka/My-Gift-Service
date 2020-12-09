package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateIdsOnlyDTO;
import com.epam.esm.dto.PurchaseDTO;
import com.epam.esm.entity.Pagination;

public interface PurchaseService {
	
	List<PurchaseDTO> getPurchasesByUserId(long userId, Pagination pagination);
	
	PurchaseDTO getPurchaseById(long purchaseId);
	
	PurchaseDTO savePurchase(long userId, List<GiftCertificateIdsOnlyDTO> certificates);

}
