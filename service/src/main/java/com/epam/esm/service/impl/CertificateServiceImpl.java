package com.epam.esm.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.service.util.RequestOrderParamsChecker;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@Service
public class CertificateServiceImpl implements CertificateService {

	@Autowired
	private CertificateDao certificateDao;

	@Autowired
	private ModelMapper modelMapper;

	public CertificateServiceImpl() {
	}

	public CertificateServiceImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public List<GiftCertificateGetDTO> getCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams) {
		
		// check and, if necessary, correct orderParams;
		orderParams = RequestOrderParamsChecker.checkAndCorrectOrderParams(orderParams);

		List<GiftCertificate> certificates = certificateDao.findCertificates(filterParams, orderParams);

		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public GiftCertificateGetDTO getCertificate(long theId) {

		GiftCertificate certificate = certificateDao.findCertificate(theId);

		if (certificate == null) {
			return null;
		}
		return convertToDto(certificate);
	}

	@Override
	public GiftCertificateGetDTO saveCertificate(GiftCertificateCreateUpdateDTO theCertificate) {

		GiftCertificate certificateToAdd = convertToEntity(theCertificate);

		// set the creation Date and Time(now) and format in accordance with ISO-8601

		LocalDateTime creationTime = DateTimeFormatterISO.createAndformatDateTime();
		certificateToAdd.setCreationDate(creationTime);
		certificateToAdd.setLastUpdateDate(creationTime);

		GiftCertificate addedCertificate = certificateDao.addCertificate(certificateToAdd);

		return convertToDto(addedCertificate);
	}

	@Override
	public GiftCertificateGetDTO updateCertificate(GiftCertificateCreateUpdateDTO theCertificate) {

		GiftCertificate certificateToUpdate = convertToEntity(theCertificate);

		// set LastUpdateDate and Time(now) and format in accordance with ISO-8601
		certificateToUpdate.setLastUpdateDate(DateTimeFormatterISO.createAndformatDateTime());

		GiftCertificate updatedCertificate = certificateDao.updateCertificate(certificateToUpdate);

		if (updatedCertificate == null) {
			return null;
		}

		return convertToDto(updatedCertificate);
	}

	@Override
	public int[] deleteCertificate(long theId) {

		int[] affectedRows = certificateDao.deleteCertificate(theId);

		return affectedRows;
	}

	private GiftCertificateGetDTO convertToDto(GiftCertificate giftCertificate) {

		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}

		GiftCertificateGetDTO certificateDTO = modelMapper.map(giftCertificate, GiftCertificateGetDTO.class);

		return certificateDTO;
	}

	private GiftCertificate convertToEntity(GiftCertificateCreateUpdateDTO giftDTO) {

		GiftCertificate certificate = modelMapper.map(giftDTO, GiftCertificate.class);

		return certificate;
	}

}
