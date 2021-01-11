package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Purchase;
import com.epam.esm.transferobj.Pagination;

public interface PurchaseDao {
	
	List<Purchase> findPurchsesByUserId(long userId, Pagination pagination);
	
	Purchase addPurchase(Purchase purchase);
	
	Purchase findPurchseById(long purchaseId, long userId);
	
	boolean purchaseExistsForCertificate(long certificateId);

}
