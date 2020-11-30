package com.epam.esm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.epam.esm.dto.GiftCertificateCreateDTO;
import com.epam.esm.dto.GiftCertificateGetDTO;
import com.epam.esm.dto.GiftCertificateUpdateDTO;
import com.epam.esm.entity.Pagination;
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
	private static final Logger LOG = LogManager.getLogger(CertificateController.class);
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
	 * @throws NotFoundException
	 */
	@GetMapping("{certificateId}")
	public EntityModel<GiftCertificateGetDTO> getCertificate(@PathVariable long certificateId) {
		GiftCertificateGetDTO giftCertificate = certificateService.getCertificate(certificateId);
		if (giftCertificate == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_FOUND_KEY),
					new Object[] { certificateId }, LocaleContextHolder.getLocale()));
		}
		EntityModel<GiftCertificateGetDTO> entityModel = new EntityModel<>(giftCertificate);
		return entityModel
				.add(linkTo(methodOn(CertificateController.class).getCertificate(certificateId)).withSelfRel());
	}

	/**
	 * POST method which adds new certificate into the Database;
	 * 
	 * @param certificate (the argument should contains the list of tags to bound
	 *                    the certificate with)
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200 and the response body contains new certificates)
	 * @throws InvalidRequestParametersException
	 */
	@PostMapping
	public EntityModel<GiftCertificateGetDTO> addCertificate(@Valid @RequestBody GiftCertificateCreateDTO certificate) {
		if (certificate.getTags() == null || certificate.getTags().isEmpty()) {
			throw new InvalidRequestParametersException(messageSource
					.getMessage((MessageKeyHolder.CERTIFICATE_INVALID_TAGS_KEY), null, LocaleContextHolder.getLocale()));
		}
		GiftCertificateGetDTO createdCertificate = certificateService.saveCertificate(certificate);
		EntityModel<GiftCertificateGetDTO> entityModel = new EntityModel<>(createdCertificate);
		return entityModel.add(linkTo(methodOn(CertificateController.class).getCertificate(createdCertificate.getId()))
				.withSelfRel()
				.andAffordance(
						afford(methodOn(CertificateController.class).deleteCertificate(createdCertificate.getId())))
				.andAffordance(afford(
						methodOn(CertificateController.class).updateCertificate(createdCertificate.getId(), null))));
	}

	/**
	 * PUT method which allows to change the certificate's Name, Description, Price,
	 * Duration and the tags to bound with;
	 * 
	 * @param certificateId, certificate
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200 and updated entity in the response body)
	 * @throws NotFoundException
	 */
	@PutMapping("{certificateId}")
	public EntityModel<GiftCertificateGetDTO> updateCertificate(@PathVariable long certificateId,
			@Valid @RequestBody GiftCertificateUpdateDTO certificate) {
		GiftCertificateGetDTO giftCertificateGetDTO = certificateService.getCertificate(certificateId);
		if (giftCertificateGetDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_UPDATED_KEY),
					new Object[] { certificateId }, LocaleContextHolder.getLocale()));
		}
		certificate.setCreationDate(giftCertificateGetDTO.getCreationDate());
		GiftCertificateGetDTO updatedCertificate = certificateService.updateCertificate(certificateId, certificate);
		EntityModel<GiftCertificateGetDTO> entityModel = new EntityModel<>(updatedCertificate);
		return entityModel.add(linkTo(methodOn(CertificateController.class).getCertificate(updatedCertificate.getId()))
				.withSelfRel().andAffordance(
						afford(methodOn(CertificateController.class).deleteCertificate(updatedCertificate.getId()))));
	}

	/**
	 * PATCH method which allows to change the certificate's name, description,
	 * price, duration, bounded tags separately or in combination;
	 * 
	 * @param certificateId, patch
	 * @return {@link GiftCertificateGetDTO} (in case of success, the method returns
	 *         Status Code = 200 and updated entity in the response body)
	 * @throws NotFoundException, JsonPatchProcessingException
	 */
	@PatchMapping("{certificateId}")
	public EntityModel<GiftCertificateGetDTO> partialUpdateCertificate(@PathVariable long certificateId,
			@Valid @RequestBody JsonPatch patch) {
		try {
			GiftCertificateGetDTO certificate = certificateService.getCertificate(certificateId);
			if (certificate == null) {
				throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.CERTIFICATE_NOT_FOUND_KEY),
						new Object[] { certificateId }, LocaleContextHolder.getLocale()));
			}
			GiftCertificateGetDTO certificatePatched = applyPatchToCertificate(patch, certificate);
			GiftCertificateGetDTO updatedCertificate = certificateService.updateCertificate(certificateId,
					convertToCertificateUpdateDTO(certificatePatched));
			EntityModel<GiftCertificateGetDTO> entityModel = new EntityModel<>(updatedCertificate);
			return entityModel
					.add(linkTo(methodOn(CertificateController.class).getCertificate(updatedCertificate.getId()))
							.withSelfRel().andAffordance(afford(methodOn(CertificateController.class)
									.deleteCertificate(updatedCertificate.getId()))));
		} catch (JsonPatchException | JsonProcessingException e) {
			LOG.log(Level.ERROR,
					"Error when calling partialUpdateCertificate() from CertificateController, certificate wasn't updated, id - "
							+ certificateId,
					e);
			throw new JsonPatchProcessingException(
					messageSource.getMessage((MessageKeyHolder.CERTIFICATE_JSON_PATCH_ERROR),
							new Object[] { certificateId }, LocaleContextHolder.getLocale()));
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
					(MessageKeyHolder.CERTIFICATE_ABSENT_KEY), new Object[] { certificateId }, LocaleContextHolder.getLocale()));
		}
		certificateService.deleteCertificate(certificateId);
		return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage(
				(MessageKeyHolder.CERTIFICATE_DELETED_KEY), new Object[] { certificateId }, LocaleContextHolder.getLocale()));
	}

	/**
	 * @param accepts optional parameters tag, name, description, order, direction,
	 *                tagIds; parameters could be used in conjunction; Required
	 *                parameter is pagination;
	 * @return {@link List<GiftCertificateGetDTO>} in the case, when nothing was
	 *         found, returns empty list;
	 */
	@GetMapping
	public @ResponseBody List<GiftCertificateGetDTO> getCertificates(@RequestParam(required = false) String tag,
			@RequestParam(required = false) String name, @RequestParam(required = false) String description,
			@RequestParam(required = false, defaultValue = "creationDate") String order,
			@RequestParam(required = false, defaultValue = "desc") String direction,
			@RequestParam(required = false) Long[] tagIds, @Valid Pagination pagination) {
		if (tagIds != null && tagIds.length != 0) {
			for (int i = 0; i < tagIds.length; i++) {
			}
			return certificateService.getCertificatesByTags(tagIds, pagination);
		}
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
		try {
			List<GiftCertificateGetDTO> certificates = certificateService.getCertificates(filterParams, orderParams,
					pagination);
			certificates.forEach(certificate -> certificate.add(
					linkTo(methodOn(CertificateController.class).getCertificate(certificate.getId())).withSelfRel()));
			return certificates;
		} catch (ServiceValidationException e) {
			LOG.log(Level.ERROR, "Filter param's value is not valid", e);
			throw new ServiceValidationException(messageSource
					.getMessage((MessageKeyHolder.CERTIFICATE_INVALID_REQUEST_PARAM_KEY), null, LocaleContextHolder.getLocale()));
		}
	}

	private GiftCertificateGetDTO applyPatchToCertificate(JsonPatch patch, GiftCertificateGetDTO targetCertificate)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetCertificate, JsonNode.class));
		return objectMapper.treeToValue(patched, GiftCertificateGetDTO.class);
	}

	private GiftCertificateUpdateDTO convertToCertificateUpdateDTO(GiftCertificateGetDTO certificateGetDTO) {
		return new GiftCertificateUpdateDTO(certificateGetDTO.getId(), certificateGetDTO.getName(),
				certificateGetDTO.getDescription(), certificateGetDTO.getPrice(), certificateGetDTO.getDuration(),
				certificateGetDTO.getTags(), certificateGetDTO.getCreationDate());
	}

}
