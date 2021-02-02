package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateCreateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateUpdateDTO;
import com.epam.esm.service.exception.ServiceValidationException;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.Pagination;

public interface CertificateService {

	List<GiftCertificateGetDTO> getCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams, Pagination pagination)
			throws ServiceValidationException;

	GiftCertificateGetDTO getCertificate(long id);

	GiftCertificateGetDTO saveCertificate(GiftCertificateCreateDTO certificate);

	GiftCertificateGetDTO updateCertificate(long certificateId, GiftCertificateUpdateDTO certificate);

	void deleteCertificate(long id);

	List<GiftCertificateGetDTO> getCertificatesByTags(Long[] tagIds, Pagination pagination);

}
