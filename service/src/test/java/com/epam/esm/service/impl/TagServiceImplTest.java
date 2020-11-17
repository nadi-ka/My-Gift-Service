package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
	
//	@Mock
//	private TagDao tagDao;
//	
//	@InjectMocks
//	private TagService tagService = new TagServiceImpl(tagDao, new ModelMapper());
//
//	/**
//	 * Test method for {@link com.epam.esm.service.impl.TagServiceImpl#getTags()}.
//	 */
//	@Test
//	void testGetTags() {
//		
//		 Mockito.when(tagDao.findAllTags()).thenReturn(getListOfTags());
//		 List<TagDTO> actualList = tagService.getTags();
//		 
//	     assertTrue(actualList.size() == 2);
//	     assertEquals("#Romance", actualList.get(1).getName());
//	     assertTrue(actualList.get(0).getId() == 1);
//	}
//
//	/**
//	 * Test method for {@link com.epam.esm.service.impl.TagServiceImpl#saveTag(com.epam.esm.dto.TagDTO)}.
//	 */
//	@Test
//	void testSaveTag() {
//		
//		Tag tag = new Tag();
//		tag.setName("#SPA");
//		Tag savedTag = new Tag(3, "#SPA");
//		
//		Mockito.when(tagDao.addTag(tag)).thenReturn(savedTag);
//		
//		TagDTO tagDTO = new TagDTO();
//		tagDTO.setName("#SPA");
//		TagDTO actualTag = tagService.saveTag(tagDTO);
//		
//		assertTrue(actualTag.getId() == 3);
//	}
//	
//	/**
//	 * Test method for {@link com.epam.esm.service.impl.TagServiceImpl#getTag(long)}.
//	 */
//	@Test
//	void testGetTag_PositiveResult() {
//		
//		Tag tag = new Tag(1, "#Sport");
//		
//		Mockito.when(tagDao.findTag(1)).thenReturn(tag);
//		TagDTO tagActual = tagService.getTag(1);
//		
//		assertEquals("#Sport", tagActual.getName());
//		assertTrue(tagActual.getId() == 1);
//		
//	}
//	
//	@Test
//	void testGetTag_NotFound() {
//		
//		Mockito.when(tagDao.findTag(999)).thenReturn(null);
//		TagDTO tagActual = tagService.getTag(999);
//		
//		assertNull(tagActual);	
//	}
//
//	/**
//	 * Test method for {@link com.epam.esm.service.impl.TagServiceImpl#updateTag(com.epam.esm.dto.TagDTO)}.
//	 */
////	@Test
////	void testUpdateTag_PositiveResult() {
////		
////		Tag tag = new Tag();
////		tag.setName("#SPA");
////		TagDTO tagDTO = new TagDTO();
////		tagDTO.setName("#SPA");
////		
////		Mockito.when(tagDao.updateTag(1, tag)).thenReturn(1);
////		Mockito.when(tagDao.findTag(1)).thenReturn(new Tag(1, "#SPA"));
////		TagDTO actualTag = tagService.updateTag(1, tagDTO);
////		
////		assertNotNull(actualTag);
////		assertEquals("#SPA", actualTag.getName());
////	}
////	
////	@Test
////	void testUpdateTag_NegativeResult_NotFound() {
////		
////		Tag tag = new Tag();
////		tag.setName("#SPA");
////		TagDTO tagDTO = new TagDTO();
////		tagDTO.setName("#SPA");
////		
////		Mockito.when(tagDao.updateTag(999, tag)).thenReturn(0);
////		TagDTO actualTag = tagService.updateTag(999, tagDTO);
////		
////		assertNotNull(actualTag);
////		assertNull(actualTag.getName());
////	}
//
//	/**
//	 * Test method for {@link com.epam.esm.service.impl.TagServiceImpl#deleteTag(long)}.
//	 */
//	@Test
//	void testDeleteTag_SuccessfullyDeleted() {
//		
//		Mockito.when(tagDao.certificatesExistForTag(1)).thenReturn(false);
//		Mockito.when(tagDao.deleteTag(1)).thenReturn(1);
//		
//		int affectedRows = tagService.deleteTag(1);
//		
//		assertTrue(affectedRows == 1);
//	}
//	
//	@Test
//	void testDeleteTag_NotDeleted_AsBoundWithCertificate() {
//		
//		Mockito.when(tagDao.certificatesExistForTag(1)).thenReturn(true);
//		
//		IllegalOperationServiceException thrown = assertThrows(IllegalOperationServiceException.class, () -> tagService.deleteTag(1),
//				"Expected deleteTag() to throw, but it didn't");
//
//				assertTrue(thrown.getMessage().contains("The tag is bounded with one or more certififcates"));
//	}
//	
//	private List<Tag> getListOfTags() {
//		List<Tag> tags = new ArrayList<Tag>();
//		tags.add(new Tag(1, "#Sport"));
//		tags.add(new Tag(2, "#Romance"));
//		
//		return tags;
//	}
//	

}
