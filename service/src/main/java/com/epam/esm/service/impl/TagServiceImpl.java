package com.epam.esm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IllegalOperationServiceException;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;

	@Autowired
	private ModelMapper modelMapper;
	
	public  TagServiceImpl() {}
	
	public TagServiceImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public List<TagDTO> getTags() {

		List<Tag> tags;
		tags = tagDao.findAllTags();

		return tags.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public TagDTO saveTag(TagDTO theTag) {

		Tag resultTag = tagDao.addTag(convertToEntity(theTag));

		return convertToDto(resultTag);
	}

	@Override
	public TagDTO getTag(long theId) {

		Tag tag = tagDao.findTag(theId);

		if (tag == null) {
			return null;
		}
		return convertToDto(tag);
	}

	@Override
	public int updateTag(TagDTO theTag) {

		 int affectedRows = tagDao.updateTag(convertToEntity(theTag));
		 
		 return affectedRows;

	}

	@Override
	public int deleteTag(long theId) throws IllegalOperationServiceException {

		// check if there is at least one certificate, bounded with the tag for delete
		// operation;
		// if the method returns cartificateId - the operation will be forbidden;
		// if the method returns cartificateId=0, the tag could be deleted;
		long certificateId = tagDao.findCertificateIdByTagId(theId);

		if (certificateId != 0) {
			throw new IllegalOperationServiceException(
					"The tag is bounded with one or more certififcates and coudn't be deleted, tagId - " + theId);
		}
		int affectedRows = tagDao.deleteTag(theId);
		return affectedRows;
	}

	private TagDTO convertToDto(Tag tag) {

		TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

		return tagDTO;
	}

	private Tag convertToEntity(TagDTO tagDTO) {

		Tag tag = modelMapper.map(tagDTO, Tag.class);

		return tag;
	}

}
