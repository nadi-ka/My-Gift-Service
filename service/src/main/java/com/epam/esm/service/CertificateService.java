package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.util.parameter.FilterParam;
import com.epam.esm.util.parameter.OrderParam;

public interface CertificateService {

	List<GiftCertificateGetDTO> getCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams);

	GiftCertificateGetDTO getCertificate(long theId);

	GiftCertificateGetDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate);

	GiftCertificateGetDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate);

	int[] deleteCertificate(long theId);

}
