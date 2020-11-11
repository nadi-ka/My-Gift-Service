package com.epam.esm.rest;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IllegalOperationServiceException;

@RestController
@RequestMapping("/tags")
public class TagController {

	private static final Logger log = LogManager.getLogger(TagController.class);

	private TagService tagService;
	
	@Autowired
	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	/**
	 * GET method, which returns the List, which contains all tags from the
	 * Database;
	 */
	@GetMapping
	public List<TagDTO> getTags() {

		List<TagDTO> tags = tagService.getTags();

		return tags;
	}

	/**
	 * GET tag by the long Id; In case when the tag with the given Id is not found,
	 * the method returns Status Code = 404
	 */
	@GetMapping("/{tagId}")
	public TagDTO getTag(@PathVariable long tagId) {

		TagDTO theTag = tagService.getTag(tagId);

		if (theTag == null) {
			throw new NotFoundException("The tag wasn't found, id - " + tagId);
		}
		return theTag;
	}

	/**
	 * POST method which creates new tag; In case of success, the
	 * method returns Status Code = 200 and the response body contains 
	 * the tag with the generated Id
	 */
	@PostMapping
	public TagDTO addTag(@Valid @RequestBody TagDTO theTag) {

		TagDTO newTag = tagService.saveTag(theTag);

		return newTag;
	}

	/**
	 * PUT method which allows to change the tag's Name; In case of success, the
	 * method returns Status Code = 200;
	 */
	@PutMapping("/{tagId}")
	public TagDTO updateTag(@PathVariable long tagId, 
			@Valid @RequestBody TagDTO theTag) {

		// check if the tag exists;
		TagDTO tag = tagService.getTag(tagId);
		if (tag == null) {
			throw new NotFoundException("The tag with given Id wasn't found and can't be updated, id - " + tagId);
		}
		tagService.updateTag(theTag);

		return theTag;
	}

	/**
	 * DELETE tag by long Id; In case when the tag with the given Id is not found,
	 * the method returns Status Code = 200 Deleting the tags, which are bound with
	 * certificates are not allowed (bounded certificates must be deleted primarily)
	 */
	@DeleteMapping("/{tagId}")
	public ResponseEntity<?> deleteTag(@PathVariable long tagId) {

		// check if the tag exists;
		TagDTO tag = tagService.getTag(tagId);
		if (tag == null) {
			return ResponseEntity.status(HttpStatus.OK).body("The tag doesn't exist in base, id - " + tagId);
		}
		// tag was found and will be deleted;
		try {
			tagService.deleteTag(tagId);
		} catch (IllegalOperationServiceException e) {
			log.log(Level.WARN,
					"The tag couldn't be deleted as it's bounded with one or more certificates - tagId" + tagId, e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.OK).body("The tag was successfully deleted, id - " + tagId);
	}

}
