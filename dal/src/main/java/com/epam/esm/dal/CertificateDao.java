package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

public interface CertificateDao {

	GiftCertificate addCertificate(GiftCertificate certificate);

	GiftCertificate updateCertificate(long certificateId, GiftCertificate certificate);

	List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams, Pagination pagination);

	GiftCertificate findCertificate(long id);

	int deleteCertificate(long id);
	
	Double getSumCertificatesPrice(List<Long> certificateIds);
	
	Long getAmountOfCertificates(List<Long> certificateIds);
	
	List<GiftCertificate> findCertificatesByTags(Long[] tagIds, Pagination pagination);
}
