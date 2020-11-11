package com.epam.esm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.epam.esm.service.CertificateService;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.ParameterConstant;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

	private CertificateService certificateService;
	
	@Autowired
	public CertificateController(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * GET certificate by the long Id; In case when the certificate with the given
	 * Id is not found, the method returns Status Code = 404
	 */
	@GetMapping("/{certificateId}")
	public GiftCertificateGetDTO getSertificate(@PathVariable long certificateId) {

		GiftCertificateGetDTO giftCertificate = certificateService.getCertificate(certificateId);

		if (giftCertificate == null) {
			throw new NotFoundException("The certificate wasn't found, id - " + certificateId);
		}
		return giftCertificate;
	}

	/**
	 * POST method which adds new certificate into the Database; In case of success,
	 * the method returns Status Code = 200 and the response body contains new
	 * generated certificates's Id; The method also bounds new certificate with
	 * appropriate tags in M2M table; The argument (GiftCertificateCreateUpdateDTO
	 * theCertificate) should contains the List of tags to bound the certificate
	 * with
	 */
	@PostMapping
	public GiftCertificateGetDTO addCertificate(@Valid @RequestBody GiftCertificateCreateUpdateDTO theCertificate) {

		// the certificate can't exists without tag
		if (theCertificate.getTags() == null || theCertificate.getTags().isEmpty()) {
			throw new InvalidRequestParametersException("The certificate should be bounded at least with one tag");
		}

		GiftCertificateGetDTO certificateGetDTO = certificateService.saveCertificate(theCertificate);

		return certificateGetDTO;
	}

	/**
	 * PUT method which allows to change the certificate's Name, Description, Price,
	 * Duration and the tags to bound with; In case of success, the method returns
	 * Status Code = 200
	 */
	@PutMapping("/{certificateId}")
	public GiftCertificateGetDTO updateCertificate(@PathVariable long certificateId, 
			@Valid @RequestBody GiftCertificateCreateUpdateDTO theCertificate) {
		
		// check if the certificate with given Id exists;	
		GiftCertificateGetDTO existingCertficate = certificateService.getCertificate(certificateId);
		if (existingCertficate == null) {
			throw new NotFoundException("The certificate with given Id wasn't found, id - " + certificateId);
		}

		GiftCertificateGetDTO certificateDTO = certificateService.updateCertificate(theCertificate);

		return certificateDTO;
	}

	/**
	 * DELETE certificate by long Id; In case when the certificate with the given Id
	 * is not found, the method returns Status Code = 200 OK
	 */
	@DeleteMapping("/{certificateId}")
	public ResponseEntity<?> deleteCertificate(@PathVariable long certificateId) {

		// check if the certificate with given Id exists;
		GiftCertificateGetDTO certificate = certificateService.getCertificate(certificateId);
		if (certificate == null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body("The certificate doesn't exist in base, id - " + certificateId);
		}

		// certificate was found and will be deleted;
		certificateService.deleteCertificate(certificateId);

		return ResponseEntity.status(HttpStatus.OK)
				.body("The certificate was successfully deleted, id - " + certificateId);
	}

	/**
	 * GET method, which returns the List of GiftCertificates; accepts optional
	 * parameters tag, name, description, order, direction; parameters could be used
	 * in conjunction;
	 */
	@GetMapping
	public @ResponseBody List<GiftCertificateGetDTO> getCertificates(@RequestParam(required = false) String tag,
			@RequestParam(required = false) String name, @RequestParam(required = false) String description,
			@RequestParam(required = false, defaultValue = "date") String order,
			@RequestParam(required = false, defaultValue = "desc") String direction) {

		List<GiftCertificateGetDTO> certificates = new ArrayList<GiftCertificateGetDTO>();

		List<FilterParam> filterParams = new ArrayList<FilterParam>();
		List<OrderParam> orderParams = new ArrayList<OrderParam>();

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

		certificates = certificateService.getCertificates(filterParams, orderParams);

		return certificates;
	}

}
