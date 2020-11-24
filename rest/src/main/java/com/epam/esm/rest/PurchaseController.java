package com.epam.esm.rest;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;

@Validated
@RestController
@RequestMapping
public class PurchaseController {

	private PurchaseService purchaseService;
	private UserService userService;

	@Autowired
	public PurchaseController(PurchaseService purchaseService, UserService userService) {
		this.purchaseService = purchaseService;
		this.userService = userService;
	}

	private static final Logger log = LogManager.getLogger(PurchaseController.class);

	/**
	 * GET all user's orders by the long Id;
	 * 
	 * @param userId
	 * @return {@link List<PurchaseDTO>} (in case when the user with given Id is not
	 *         found, the method returns Status Code = 404)
	 * @throws NotFoundException
	 */
	@GetMapping("/users/{userId}/purchases")
	public List<PurchaseDTO> getPurchases(@PathVariable long userId) {
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO == null) {
			throw new NotFoundException("The user wasn't found, id - " + userId);
		}
		return purchaseService.getPurchasesByUserId(userId);
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
			throw new NotFoundException("The purchase wasn't found, id - " + purchaseId);
		}
		return purchaseDTO;
	}

	/**
	 * The method adds new purchase for use;
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
			throw new InvalidRequestParametersException("The list with certificates cannot be null or empty");
		}
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO == null) {
			throw new NotFoundException("The user wasn't found, id - " + userId);
		}
		PurchaseDTO purchaseDTO = purchaseService.savePurchase(userId, certificates);
		if (purchaseDTO.getId() == 0) {
			throw new NotFoundException("Certificate to bound with wasn't found");
		}
		return purchaseDTO;
	}

}
