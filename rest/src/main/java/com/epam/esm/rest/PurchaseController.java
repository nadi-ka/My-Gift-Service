package com.epam.esm.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.epam.esm.dto.GiftCertificateIdsOnlyDTO;
import com.epam.esm.dto.PurchaseDTO;
import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.EntityNotFoundServiceException;
import com.epam.esm.transferobj.Pagination;

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
	@PreAuthorize("@userIdChecker.hasUserId(authentication, #userId) or hasRole('ROLE_ADMIN')")
	public List<PurchaseDTO> getPurchases(@PathVariable long userId, @Valid Pagination pagination) {
		if (userService.getUser(userId) == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.USER_NOT_FOUND_KEY),
					new Object[] { userId }, LocaleContextHolder.getLocale()));
		}
		List<PurchaseDTO> purchases = purchaseService.getPurchasesByUserId(userId, pagination);
		purchases.forEach(purchase -> purchase
				.add(linkTo(methodOn(PurchaseController.class).getPurchase(userId, purchase.getId())).withSelfRel()));
		return purchases;
	}

	/**
	 * GET purchase by the long Id;
	 * 
	 * @param purchaseId
	 * @return {@link PurchaseDTO} (in case when the purchase with the given Id is
	 *         not found, the method returns Status Code = 404)
	 * @throws NotFoundException
	 */
	@GetMapping("/users/{userId}/purchases/{purchaseId}")
	@PreAuthorize("@userIdChecker.hasUserId(authentication, #userId) or hasRole('ROLE_ADMIN')")
	public EntityModel<PurchaseDTO> getPurchase(@PathVariable long userId, @PathVariable long purchaseId) {
		PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(purchaseId, userId);
		if (purchaseDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.PURCHASE_NOT_FOUND_KEY),
					new Object[] { purchaseId }, LocaleContextHolder.getLocale()));
		}
		return new EntityModel<>(purchaseDTO)
				.add(linkTo(methodOn(PurchaseController.class).getPurchase(userId, purchaseId)).withSelfRel());
	}

	/**
	 * Add new purchase for user;
	 * 
	 * @param userId, certificates
	 * @return {@link PurchaseDTO} (in case of success, the method returns Status
	 *         Code = 200 and the response body contains new purchase;
	 * @throws NotFoundException (in case when the user with given id is not found)
	 */
	@PostMapping("/users/{userId}/purchases")
	@PreAuthorize("@userIdChecker.hasUserId(authentication, #userId) or hasRole('ADMIN')")
	public EntityModel<PurchaseDTO> addPurchase(@PathVariable long userId,
			@RequestBody List<@Valid GiftCertificateIdsOnlyDTO> certificates) {
		if (certificates.isEmpty()) {
			throw new InvalidRequestParametersException(messageSource.getMessage(
					(MessageKeyHolder.PURCHASE_EMPTY_CERTIFICATES_KEY), null, LocaleContextHolder.getLocale()));
		}
		try {
		PurchaseDTO purchaseDTO = purchaseService.savePurchase(userId, certificates);
		return new EntityModel<>(purchaseDTO)
				.add(linkTo(methodOn(PurchaseController.class).getPurchase(userId, purchaseDTO.getId())).withSelfRel());
		}catch (EntityNotFoundServiceException e) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.PURCHASE_NOT_CREATED),
					new Object[] { userId }, LocaleContextHolder.getLocale()));
		}
	}

}
