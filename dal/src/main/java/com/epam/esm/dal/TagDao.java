package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Tag;

public interface TagDao {
	
	Tag addTag(Tag tag);
	int updateTag(Tag tag);
	List<Tag> findAllTags();
	Tag findTag(long id);
	Tag findTagByName(String name);
	int deleteTag(long id);
	boolean certificatesExistForTag(long tagId);

}
