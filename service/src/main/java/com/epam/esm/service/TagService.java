package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.exception.IllegalOperationServiceException;

public interface TagService {

	List<TagDTO> getTags();

	TagDTO getTag(long id);

	TagDTO saveTag(TagDTO tag);

	TagDTO updateTag(long tagId, TagDTO tag);

	int deleteTag(long id) throws IllegalOperationServiceException;

}
