package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Purchase;

public interface PurchaseDao {
	
	List<Purchase> findPurchsesByUserId(long userId);
	
	long addPurchase(Purchase purchase);
	
	Purchase findPurchseById(long purchaseId);

}
