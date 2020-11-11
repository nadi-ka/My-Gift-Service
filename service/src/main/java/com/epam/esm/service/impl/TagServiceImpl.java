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

	private TagDao tagDao;
	private ModelMapper modelMapper;

	@Autowired
	public TagServiceImpl(TagDao tagDao, ModelMapper modelMapper) {
		this.tagDao = tagDao;
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
		boolean tagBoundedWithCertificate = tagDao.findCertificateIdByTagId(theId);
		if (tagBoundedWithCertificate) {
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
