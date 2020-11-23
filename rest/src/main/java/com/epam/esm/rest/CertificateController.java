package com.epam.esm.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.GiftCertificateCreateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateUpdateDTO;
import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.JsonPatchProcessingException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ServiceValidationException;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.ParameterConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

	private CertificateService certificateService;
	private MessageSource messageSource;
	private static final Logger log = LogManager.getLogger(CertificateController.class);
	@Autowired
	ObjectMapper objectMapper;

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
	 * @param certificate (the argument should contains the list of tags to bound
	 *                    the certificate with)
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200 and the response body contains new certificates)
	 */
	@PostMapping
	public GiftCertificateGetDTO addCertificate(@Valid @RequestBody GiftCertificateCreateDTO certificate) {
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
	 *         Status Code = 200 and updated entity in the response body)
	 */
	@PutMapping("{certificateId}")
	public GiftCertificateGetDTO updateCertificate(@PathVariable long certificateId,
			@Valid @RequestBody GiftCertificateUpdateDTO certificate) {
		GiftCertificateGetDTO giftCertificateGetDTO = certificateService.getCertificate(certificateId);
		if (giftCertificateGetDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_UPDATED_KEY),
					new Object[] { certificateId }, Locale.getDefault()));
		}
		certificate.setCreationDate(giftCertificateGetDTO.getCreationDate());
		return certificateService.updateCertificate(certificateId, certificate);
	}

	/**
	 * PATCH method which allows to change the certificate's name, description,
	 * price, duration, bounded tags separately or in combination;
	 * 
	 * @param certificateId, patch
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200 and updated entity in the response body)
	 */
	@PatchMapping("{certificateId}")
	public GiftCertificateGetDTO partialUpdateCertificate(@PathVariable long certificateId,
			@RequestBody JsonPatch patch) {
		try {
			GiftCertificateGetDTO certificate = certificateService.getCertificate(certificateId);
			if (certificate == null) {
				throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_FOUND_KEY),
						new Object[] { certificateId }, Locale.getDefault()));
			}
			GiftCertificateGetDTO certificatePatched = applyPatchToCertificate(patch, certificate);
			return certificateService.updateCertificate(certificateId, certificatePatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			log.log(Level.ERROR,
					"Error when calling partialUpdateCertificate() from CertificateController, certificate wasn't updated, id - "
							+ certificateId,
					e);
			throw new JsonPatchProcessingException(
					messageSource.getMessage((MessageKeyHolder.CERTIFICATE_JSON_PATCH_ERROR),
							new Object[] { certificateId }, Locale.getDefault()));
		}
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
			@RequestParam(required = false) String order, @RequestParam(required = false) String direction) {
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

		List<GiftCertificateGetDTO> certificates;
		try {
			certificates = certificateService.getCertificates(filterParams, orderParams);
		} catch (ServiceValidationException e) {
			log.log(Level.ERROR, "Filter param's value is not valid", e);
			throw new ServiceValidationException(messageSource
					.getMessage((MessageKeyHolder.CERTIFICATE_INVALID_REQUEST_PARAM_KEY), null, Locale.getDefault()));
		}
		return certificates;
	}

	private GiftCertificateGetDTO applyPatchToCertificate(JsonPatch patch, GiftCertificateGetDTO targetCertificate)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetCertificate, JsonNode.class));
		return objectMapper.treeToValue(patched, GiftCertificateGetDTO.class);
	}

}
