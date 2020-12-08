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
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ServiceValidationException;
import com.epam.esm.service.util.DateTimeFormatterISO;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.RequestParamsValidator;
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
	public List<GiftCertificateGetDTO> getCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams,
			Pagination pagination) {
		boolean isFilterParamsValid = RequestParamsValidator.validateFilterParams(filterParams);
		boolean isOrderParamsValid = RequestParamsValidator.validateOrderParams(orderParams);
		if (!isFilterParamsValid || !isOrderParamsValid) {
			throw new ServiceValidationException("Request param's values are not valid");
		}
		List<GiftCertificate> certificates = certificateDao.findCertificates(filterParams, orderParams, pagination);
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
		if (certificate.getId() != 0) {
			certificate.setId(0);
		}
		GiftCertificate certificateToAdd = convertToEntity(certificate);
		LocalDateTime creationTime = DateTimeFormatterISO.createAndformatDateTime();
		certificateToAdd.setCreationDate(creationTime);
		certificateToAdd.setLastUpdateDate(creationTime);
		GiftCertificate addedCertificate = certificateDao.addCertificate(certificateToAdd);
		return convertToDto(addedCertificate);
	}

	@Override
	public GiftCertificateGetDTO updateCertificate(long certificateId, GiftCertificateUpdateDTO certificate) {
		CertificateValidator validator = new CertificateValidator();
		if (!validator.validateCertificate(certificate)) {
			throw new ServiceValidationException("Certificate properties are not valid");
		}
		GiftCertificate certificateToUpdate = convertToEntity(certificate);
		certificateToUpdate.setLastUpdateDate(DateTimeFormatterISO.createAndformatDateTime());
		GiftCertificate updatedCertificate = certificateDao.updateCertificate(certificateId, certificateToUpdate);
		if (updatedCertificate == null) {
			return new GiftCertificateGetDTO();
		}
		return convertToDto(updatedCertificate);
	}

	@Override
	public void deleteCertificate(long id) {
		certificateDao.deleteCertificate(id);
	}

	@Override
	public List<GiftCertificateGetDTO> getCertificatesByTags(Long[] tagIds, Pagination pagination) {
		List<GiftCertificate> certificates = certificateDao.findCertificatesByTags(tagIds, pagination);
		return certificates.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private GiftCertificateGetDTO convertToDto(GiftCertificate giftCertificate) {
		if (giftCertificate.getTags() == null) {
			giftCertificate.setTags(Collections.emptyList());
		}
		return modelMapper.map(giftCertificate, GiftCertificateGetDTO.class);
	}

	private GiftCertificate convertToEntity(GiftCertificateCreateDTO certificateDTO) {
		return modelMapper.map(certificateDTO, GiftCertificate.class);
	}

	private GiftCertificate convertToEntity(GiftCertificateUpdateDTO certificateDTO) {
		return modelMapper.map(certificateDTO, GiftCertificate.class);
	}

}
