package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.service.exception.ServiceValidationException;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

public interface CertificateService {

	List<GiftCertificateGetDTO> getCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams)
			throws ServiceValidationException;

	GiftCertificateGetDTO getCertificate(long id);

	GiftCertificateGetDTO saveCertificate(GiftCertificateCreateUpdateDTO certificate);

	GiftCertificateGetDTO updateCertificate(long certificateId, GiftCertificateCreateUpdateDTO certificate);

	int[] deleteCertificate(long id);

}
