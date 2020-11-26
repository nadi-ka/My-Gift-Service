package com.epam.esm.rest;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.GiftCertificateWithIdDTO;
import com.epam.esm.dto.PurchaseDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Pagination;
import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;

@Validated
@RestController
@RequestMapping
public class PurchaseController {

	private PurchaseService purchaseService;
	private UserService userService;
	private MessageSource messageSource;

	@Autowired
	public PurchaseController(PurchaseService purchaseService, UserService userService, MessageSource messageSource) {
		this.purchaseService = purchaseService;
		this.userService = userService;
		this.messageSource = messageSource;
	}

	/**
	 * GET all user's purchases by the long Id;
	 * 
	 * @param userId, pagination
	 * @return {@link List<PurchaseDTO>} (in case when the user with given Id is not
	 *         found, the method returns Status Code = 404)
	 * @throws NotFoundException
	 */
	@GetMapping("/users/{userId}/purchases")
	public List<PurchaseDTO> getPurchases(@PathVariable long userId, @Valid Pagination pagination) {
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.USER_NOT_FOUND_KEY),
					new Object[] { userId }, Locale.getDefault()));
		}
		return purchaseService.getPurchasesByUserId(userId, pagination);
	}

	/**
	 * GET purchase by the long Id;
	 * 
	 * @param purchaseId
	 * @return {@link PurchaseDTO} (in case when the purchase with the given Id is
	 *         not found, the method returns Status Code = 404)
	 * @throws NotFoundException
	 */
	@GetMapping("/purchases/{purchaseId}")
	public PurchaseDTO getPurchase(@PathVariable long purchaseId) {
		PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(purchaseId);
		if (purchaseDTO.getId() == 0) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.PURCHASE_NOT_FOUND_KEY),
					new Object[] { purchaseId }, Locale.getDefault()));
		}
		return purchaseDTO;
	}

	/**
	 * Add new purchase for user;
	 * 
	 * @param userId, certificateIds
	 * @return {@link PurchaseDTO} (in case of success, the method returns Status
	 *         Code = 200 and the response body contains new purchase;
	 * @throws NotFoundException (in case when the user with given id 
	 * is not found)
	 */
	@PostMapping("/users/{userId}/purchases")
	public PurchaseDTO addPurchase(@PathVariable long userId,
			@RequestBody @NotEmpty List<@Valid GiftCertificateWithIdDTO> certificates) {
		if (certificates.isEmpty()) {
			throw new InvalidRequestParametersException(messageSource.getMessage((MessageKeyHolder.PURCHASE_EMPTY_CERTIFICATES_KEY),
					null, Locale.getDefault()));
		}
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.USER_NOT_FOUND_KEY),
					new Object[] { userId }, Locale.getDefault()));
		}
		PurchaseDTO purchaseDTO = purchaseService.savePurchase(userId, certificates);
		if (purchaseDTO.getId() == 0) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.PURCHASE_CERTIFICATE_NOT_FOUND_KEY),
					null, Locale.getDefault()));
		}
		return purchaseDTO;
	}

}
