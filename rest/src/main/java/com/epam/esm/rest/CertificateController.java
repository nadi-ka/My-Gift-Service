package com.epam.esm.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.GiftCertificateCreateUpdateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.CertificateService;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.ParameterConstant;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

	private CertificateService certificateService;
	private MessageSource messageSource;

	@Autowired
	public CertificateController(CertificateService certificateService, MessageSource messageSource) {
		this.certificateService = certificateService;
		this.messageSource = messageSource;
	}

	/**
	 * GET certificate by the long Id;
	 * 
	 * @param certificateId
	 * @return {@link GiftCertificateGetDTO} (in case when the certificate with the
	 *         given Id is not found, the method returns Status Code = 404)
	 */
	@GetMapping("{certificateId}")
	public GiftCertificateGetDTO getSertificate(@PathVariable long certificateId) {
		GiftCertificateGetDTO giftCertificate = certificateService.getCertificate(certificateId);
		if (giftCertificate == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_FOUND_KEY),
					new Object[] { certificateId }, Locale.getDefault()));
		}
		return giftCertificate;
	}

	/**
	 * POST method which adds new certificate into the Database;
	 * 
	 * @param certificate The argument should contains the List of tags to bound the
	 *                    certificate with
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200 and the response body contains new generated
	 *         certificates's Id)
	 */
	@PostMapping
	public GiftCertificateGetDTO addCertificate(@Valid @RequestBody GiftCertificateCreateUpdateDTO certificate) {
		if (certificate.getTags() == null || certificate.getTags().isEmpty()) {
			throw new InvalidRequestParametersException(messageSource
					.getMessage((MessageKeyHolder.CERTIFICATE_INVALID_TAGS_KEY), null, Locale.getDefault()));
		}
		return certificateService.saveCertificate(certificate);
	}

	/**
	 * PUT method which allows to change the certificate's Name, Description, Price,
	 * Duration and the tags to bound with;
	 * 
	 * @param certificateId, certificate
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200)
	 */
	@PutMapping("{certificateId}")
	public GiftCertificateGetDTO updateCertificate(@PathVariable long certificateId,
			@Valid @RequestBody GiftCertificateCreateUpdateDTO certificate) {
		GiftCertificateGetDTO giftCertificateGetDTO = certificateService.getCertificate(certificateId);
		if (giftCertificateGetDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_UPDATED_KEY),
					new Object[] { certificateId }, Locale.getDefault()));
		}
		GiftCertificateGetDTO certificateDTO = certificateService.updateCertificate(certificate);
		return certificateDTO;
	}

	/**
	 * DELETE certificate by long Id;
	 * 
	 * @param certificateId
	 * @return {@link ResponseEntity} (in case when the certificate with the given
	 *         Id is not found, the method returns Status Code = 200 OK)
	 */
	@DeleteMapping("{certificateId}")
	public ResponseEntity<?> deleteCertificate(@PathVariable long certificateId) {
		GiftCertificateGetDTO certificate = certificateService.getCertificate(certificateId);
		if (certificate == null) {
			return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage(
					(MessageKeyHolder.CERTIFICATE_ABSENT_KEY), new Object[] { certificateId }, Locale.getDefault()));
		}
		certificateService.deleteCertificate(certificateId);
		return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage(
				(MessageKeyHolder.CERTIFICATE_DELETED_KEY), new Object[] { certificateId }, Locale.getDefault()));
	}

	/**
	 * @param accepts optional parameters tag, name, description, order, direction;
	 *                parameters could be used in conjunction;
	 * @return {@link List<GiftCertificateGetDTO>} in the case, when nothing was
	 *         found, returns empty list;
	 */
	@GetMapping
	public @ResponseBody List<GiftCertificateGetDTO> getCertificates(@RequestParam(required = false) String tag,
			@RequestParam(required = false) String name, @RequestParam(required = false) String description,
			@RequestParam(required = false, defaultValue = "date") String order,
			@RequestParam(required = false, defaultValue = "desc") String direction) {
		List<FilterParam> filterParams = new ArrayList<>();
		List<OrderParam> orderParams = new ArrayList<>();
		if (tag != null && !tag.isEmpty()) {
			filterParams.add(new FilterParam(ParameterConstant.TAG, tag));
		}
		if (name != null && !name.isEmpty()) {
			filterParams.add(new FilterParam(ParameterConstant.CERTIFICATE_NAME, name));
		}
		if (description != null && !description.isEmpty()) {
			filterParams.add(new FilterParam(ParameterConstant.DESCRIPTION, description));
		}
		orderParams.add(new OrderParam(order, direction));
		List<GiftCertificateGetDTO> certificates = certificateService.getCertificates(filterParams, orderParams);
		return certificates;
	}

}
