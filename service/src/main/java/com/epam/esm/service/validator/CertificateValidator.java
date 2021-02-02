package com.epam.esm.service.validator;

import java.math.BigDecimal;
import java.util.List;

import com.epam.esm.dto.GiftCertificateUpdateDTO;
import com.epam.esm.dto.TagDTO;

public class CertificateValidator {

	private static final int NAME_MAX_LENGTH = 45;
	private static final int DESCRIPTION_MAX_LENGTH = 100;
	private static final Double CERT_MAX_PRICE = 10000.0;
	private static final Integer CERT_MAX_DURATION = 1000;

	public boolean validateCertificate(GiftCertificateUpdateDTO certificate) {
		return validCertificateName(certificate.getName()) && validCertificateDescription(certificate.getDescription())
				&& validCertificatePrice(certificate.getPrice()) && validCertificateDuration(certificate.getDuration())
				&& validTags(certificate.getTags());
	}

	private boolean validCertificateName(String name) {
		return (name != null) && (!name.isEmpty()) && (name.length() <= NAME_MAX_LENGTH);
	}

	private boolean validCertificateDescription(String description) {
		return (description != null) && (!description.isEmpty()) && (description.length() <= DESCRIPTION_MAX_LENGTH);
	}

	private boolean validCertificatePrice(BigDecimal price) {
		return (price != null) && (price.doubleValue() > 0.1) && (price.doubleValue() < CERT_MAX_PRICE);
	}

	private boolean validCertificateDuration(Integer duration) {
		return (duration != null) && (duration > 1) && (duration < CERT_MAX_DURATION);
	}

	private boolean validTags(List<TagDTO> tags) {
		return (tags != null) && !tags.isEmpty();
	}

}
