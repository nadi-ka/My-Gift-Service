package com.epam.esm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IllegalOperationServiceException;
import com.epam.esm.service.exception.ServiceValidationException;

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
	public List<TagDTO> getTags(Pagination pagination) {
		List<Tag> tags;
		tags = tagDao.findAllTags(pagination);
		return tags.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public TagDTO saveTag(TagDTO tag) {
		if (tagDao.findTagByName(tag.getName()) != null) {
			throw new ServiceValidationException("Not unique tag name"); 
		}
		if (tag.getId() != 0) {
			tag.setId(0);
		}
		Tag resultTag = tagDao.addTag(convertToEntity(tag));
		return convertToDto(resultTag);
	}

	@Override
	public TagDTO getTag(long id) {
		Tag tag = tagDao.findTag(id);
		if (tag == null) {
			return null;
		}
		return convertToDto(tag);
	}

	@Override
	public TagDTO updateTag(long tagId, TagDTO tag) {
		if (tagDao.findTagByName(tag.getName()) != null) {
			throw new ServiceValidationException("Not unique tag name"); 
		}
		Tag updatedTag = tagDao.updateTag(tagId, convertToEntity(tag));
		if (updatedTag == null) {
			return new TagDTO();
		}
		return convertToDto(updatedTag);
	}

	@Override
	public void deleteTag(long id) throws IllegalOperationServiceException {
		boolean tagBoundedWithCertificate = tagDao.certificatesExistForTag(id);
		if (tagBoundedWithCertificate) {
			throw new IllegalOperationServiceException(
					"The tag is bounded with one or more certififcates and coudn't be deleted, tagId - " + id);
		}
		tagDao.deleteTag(id);
	}
	
	@Override
	public TagDTO getMostPopularTagOfUserWithHighestCostOfAllPurchases() {
		Tag tag = tagDao.findMostPopularTagOfUserWithHighestCostOfAllPurchases();
		if (tag == null) {
			return new TagDTO();
		}
		return convertToDto(tag);
	}

	private TagDTO convertToDto(Tag tag) {
		return modelMapper.map(tag, TagDTO.class);
	}

	private Tag convertToEntity(TagDTO tagDTO) {
		return modelMapper.map(tagDTO, Tag.class);
	}

}
