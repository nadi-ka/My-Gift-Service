package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.Pagination;

public interface TagDao {

	Tag addTag(Tag tag);

	Tag updateTag(long id, Tag tag);

	List<Tag> findAllTags(Pagination pagination);

	Tag findTag(long id);

	Tag findTagByName(String name);

	void deleteTag(long id);

	boolean certificatesExistForTag(long tagId);
	
	Tag findMostPopularTagOfUserWithHighestCostOfAllPurchases();

}
