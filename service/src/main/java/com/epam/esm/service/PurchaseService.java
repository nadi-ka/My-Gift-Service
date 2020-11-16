package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.PurchaseDTO;

public interface PurchaseService {
	
	List<PurchaseDTO> getPurchasesByUserId(long userId);
	
	PurchaseDTO savePurchase(long userId, long[] certificateId);

}
