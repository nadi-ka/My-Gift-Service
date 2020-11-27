package com.epam.esm.rest;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Pagination;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IllegalOperationServiceException;

@RestController
@RequestMapping("/tags")
public class TagController {

	private static final Logger LOG = LogManager.getLogger(TagController.class);

	private TagService tagService;
	private MessageSource messageSource;

	@Autowired
	public TagController(TagService tagService, MessageSource messageSource) {
		this.tagService = tagService;
		this.messageSource = messageSource;
	}

	/**
	 * GET method, which returns the List, which contains all tags from the
	 * Database;
	 * 
	 * @param pagination
	 * 
	 * @return {@link List<TagDTO>} in the case, when nothing was found, returns
	 *         empty list;
	 */
	@GetMapping
	public List<TagDTO> getTags(@Valid Pagination pagination) {
		List<TagDTO> tags = tagService.getTags(pagination);
		tags.forEach(tag -> tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel()));
		return tags;
	}

	/**
	 * GET tag by the long Id;
	 * 
	 * @param tagId
	 * @return {@link TagDTO} (in case when the tag with the given Id is not found,
	 *         the method returns Status Code = 404)
	 * @throws NotFoundException
	 */
	@GetMapping("{tagId}")
	public EntityModel<TagDTO> getTag(@PathVariable long tagId) {
		TagDTO tag = tagService.getTag(tagId);
		if (tag == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.TAG_NOT_FOUND_KEY),
					new Object[] { tagId }, Locale.getDefault()));
		}
		EntityModel<TagDTO> entityModel = new EntityModel<>(tag);
		return entityModel.add(linkTo(methodOn(TagController.class).getTag(tagId)).withSelfRel()
				.andAffordance(afford(methodOn(TagController.class).deleteTag(tagId)))
				.andAffordance(afford(methodOn(TagController.class).updateTag(tagId, null))));
	}

	/**
	 * POST method which creates new tag;
	 * 
	 * @param tag
	 * @return {@link TagDTO} (in case of success, the method returns Status Code =
	 *         200 and the response body contains the tag with the generated Id)
	 */
	@PostMapping
	public EntityModel<TagDTO> addTag(@Valid @RequestBody TagDTO tag) {
		TagDTO createdTag = tagService.saveTag(tag);
		EntityModel<TagDTO> entityModel = new EntityModel<>(createdTag);
		return entityModel.add(linkTo(methodOn(TagController.class).getTag(createdTag.getId())).withSelfRel()
				.andAffordance(afford(methodOn(TagController.class).deleteTag(createdTag.getId())))
				.andAffordance(afford(methodOn(TagController.class).updateTag(createdTag.getId(), null))));
	}

	/**
	 * PUT method which allows to change the tag's Name;
	 * 
	 * @param tagId, tag
	 * @return {@link TagDTO} (in case of success, the method returns Status Code =
	 *         200)
	 * @throws NotFoundException
	 */
	@PutMapping("{tagId}")
	public EntityModel<TagDTO> updateTag(@PathVariable long tagId, @Valid @RequestBody TagDTO tag) {
		TagDTO tagDTO = tagService.getTag(tagId);
		if (tagDTO == null) {
			throw new NotFoundException(messageSource.getMessage((MessageKeyHolder.TAG_NOT_UPDATED_KEY),
					new Object[] { tagId }, Locale.getDefault()));
		}
		TagDTO updatedTag = tagService.updateTag(tagId, tag);
		EntityModel<TagDTO> entityModel = new EntityModel<>(updatedTag);
		return entityModel.add(linkTo(methodOn(TagController.class).getTag(tagId)).withSelfRel()
				.andAffordance(afford(methodOn(TagController.class).deleteTag(tagId))));
	}

	/**
	 * DELETE tag by long Id; Deleting the tags, which are bound with certificates
	 * are not allowed (bounded certificates must be deleted primarily)
	 * 
	 * @param tagId
	 * @return {@link ResponseEntity} (in case when the tag with the given Id is not
	 *         found, the method returns Status Code = 200)
	 */
	@DeleteMapping("{tagId}")
	public ResponseEntity<?> deleteTag(@PathVariable long tagId) {
		TagDTO tag = tagService.getTag(tagId);
		if (tag == null) {
			return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage((MessageKeyHolder.TAG_ABSENT_KEY),
					new Object[] { tagId }, Locale.getDefault()));
		}
		try {
			tagService.deleteTag(tagId);
		} catch (IllegalOperationServiceException e) {
			LOG.log(Level.WARN,
					"The tag couldn't be deleted as it's bounded with one or more certificates - tagId" + tagId, e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource
					.getMessage((MessageKeyHolder.TAG_BOUNDED_KEY), new Object[] { tagId }, Locale.getDefault()));
		}
		return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage((MessageKeyHolder.TAG_DELETED_KEY),
				new Object[] { tagId }, Locale.getDefault()));
	}

	/**
	 * Get method to found the most popular tag of the user with the highest cost of
	 * all purchases
	 * 
	 * @return {@link TagDTO}
	 * @throws NotFoundException
	 */
	@GetMapping("/most-popular-tag")
	public TagDTO getMostPopularTagOfUserWithHighestCostOfPurchases() {
		TagDTO tagDTO = tagService.getMostPopularTagOfUserWithHighestCostOfAllPurchases();
		if (tagDTO.getId() == 0) {
			throw new NotFoundException(
					messageSource.getMessage((MessageKeyHolder.NOTHING_FOUND_KEY), null, Locale.getDefault()));
		}
		return tagDTO;
	}

}
