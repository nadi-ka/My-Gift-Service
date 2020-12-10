package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.exception.IllegalOperationServiceException;
import com.epam.esm.transferobj.Pagination;

public interface TagService {

	List<TagDTO> getTags(Pagination pagination);

	TagDTO getTag(long id);

	TagDTO saveTag(TagDTO tag);

	TagDTO updateTag(long tagId, TagDTO tag);

	void deleteTag(long id) throws IllegalOperationServiceException;
	
	TagDTO getMostPopularTagOfUserWithHighestCostOfAllPurchases();

}
