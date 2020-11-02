package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.exception.IllegalOperationServiceException;

public interface TagService {
	
	List<TagDTO> getTags();
	
	TagDTO getTag(long theId);

	TagDTO saveTag(TagDTO theTag);

	int updateTag(TagDTO theTag);

	int deleteTag(long theId) throws IllegalOperationServiceException;

}
