package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Purchase;

public interface PurchaseDao {
	
	List<Purchase> findPurchsesByUserId(long userId, Pagination pagination);
	
	long addPurchase(Purchase purchase);
	
	Purchase findPurchseById(long purchaseId);

}
