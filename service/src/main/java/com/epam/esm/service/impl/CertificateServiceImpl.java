package com.epam.esm.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dto.GiftCertificateCreateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateUpdateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ServiceValidationException;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.service.util.RequestOrderParamsChecker;
import com.epam.esm.service.validator.RequestFilterParamsValidator;
import com.epam.esm.service.validator.RequestOrderParamsValidator;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@Service
public class CertificateServiceImpl implements CertificateService {

	private CertificateDao certificateDao;
	private ModelMapper modelMapper;

	@Autowired
	public CertificateServiceImpl(CertificateDao certificateDao, ModelMapper modelMapper) {
		this.certificateDao = certificateDao;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<GiftCertificateGetDTO> getCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams)
			throws ServiceValidationException {
		boolean isFilterParamsValid = RequestFilterParamsValidator.validateFilterParams(filterParams);
		boolean isOrderParamsValid = RequestOrderParamsValidator.validateOrderParams(orderParams);
		if (!isFilterParamsValid || !isOrderParamsValid) {
			throw new ServiceValidationException("Request param's value is not valid");
		}
		List<GiftCertificate> certificates = certificateDao.findCertificates(filterParams, orderParams);
		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public GiftCertificateGetDTO getCertificate(long id) {
		GiftCertificate certificate = certificateDao.findCertificate(id);
		if (certificate == null) {
			return null;
		}
		return convertToDto(certificate);
	}

	@Override
	public GiftCertificateGetDTO saveCertificate(GiftCertificateCreateDTO certificate) {
		GiftCertificate certificateToAdd = convertToEntity(certificate);
		LocalDateTime creationTime = DateTimeFormatterISO.createAndformatDateTime();
		certificateToAdd.setCreationDate(creationTime);
		certificateToAdd.setLastUpdateDate(creationTime);
		GiftCertificate addedCertificate = certificateDao.addCertificate(certificateToAdd);
		return convertToDto(addedCertificate);
	}

	@Override
	public GiftCertificateGetDTO updateCertificate(long certificateId, GiftCertificateUpdateDTO certificate) {
		GiftCertificate certificateToUpdate = convertToEntity(certificate);
		certificateToUpdate.setLastUpdateDate(DateTimeFormatterISO.createAndformatDateTime());
		GiftCertificate updatedCertificate = certificateDao.updateCertificate(certificateId, certificateToUpdate);
		if (updatedCertificate == null) {
			return new GiftCertificateGetDTO();
		}
		return convertToDto(updatedCertificate);
	}

	@Override
	public int deleteCertificate(long id) {
		int affectedRows = certificateDao.deleteCertificate(id);
		return affectedRows;
	}

	private GiftCertificateGetDTO convertToDto(GiftCertificate giftCertificate) {
		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}
		GiftCertificateGetDTO certificateDTO = modelMapper.map(giftCertificate, GiftCertificateGetDTO.class);
		return certificateDTO;
	}

	private GiftCertificate convertToEntity(GiftCertificateCreateDTO certificateDTO) {
		GiftCertificate certificate = modelMapper.map(certificateDTO, GiftCertificate.class);
		return certificate;
	}
	
	private GiftCertificate convertToEntity(GiftCertificateUpdateDTO certificateDTO) {
		GiftCertificate certificate = modelMapper.map(certificateDTO, GiftCertificate.class);
		return certificate;
	}

}
