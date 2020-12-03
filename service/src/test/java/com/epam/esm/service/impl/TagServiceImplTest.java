package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.config.ServiceSpringConfig;
import com.epam.esm.service.exception.IllegalOperationServiceException;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(ServiceSpringConfig.class)
@SpringBootTest
@EnableAutoConfiguration
class TagServiceImplTest {

	@Mock
	private TagDao tagDao;
	private ModelMapper modelMapper;
	private Tag tag1;
	private Tag tag2;
	private List<Tag> tags;
	private static final String NAME_SPORT = "#Sport";
	private static final String NAME_ROMANCE = "#Romance";
	private static final String NAME_SPA = "#SPA";
	private static final long ID_ABSENT = 9999;

	@InjectMocks
	@Autowired
	private TagService tagService = new TagServiceImpl(tagDao, modelMapper);

	@BeforeEach
	void setUp() {
		tag1 = new Tag(1, NAME_SPORT);
		tag2 = new Tag(2, NAME_ROMANCE);
		tags = new ArrayList<>();
		tags.add(tag1);
		tags.add(tag2);
	}

	@AfterEach
	public void tearDown() {
		tag1 = null;
		tag2 = null;
		tags = null;
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.TagServiceImpl#getTags(com.epam.esm.entity.Pagination)}.
	 */
	@Test
	void testGetTags() {
		Pagination pagination = new Pagination(2, 0);
		Mockito.when(tagDao.findAllTags(pagination)).thenReturn(tags);
		int sizeExpected = 2;

		assertTrue(tagService.getTags(pagination).size() == sizeExpected);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.TagServiceImpl#saveTag(com.epam.esm.dto.TagDTO)}.
	 */
	@Test
	void testSaveTag() {
		long savedTagId = 3;
		Mockito.when(tagDao.addTag(Mockito.any(Tag.class))).thenReturn(new Tag(savedTagId, NAME_SPA));
		TagDTO tagDTO = new TagDTO();
		tagDTO.setName(NAME_SPA);

		assertTrue(tagService.saveTag(tagDTO).getId() == savedTagId);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.TagServiceImpl#getTag(long)}.
	 */
	@Test
	void testGetTag_PositiveResult() {
		Mockito.when(tagDao.findTag(1)).thenReturn(tag1);

		assertEquals(NAME_SPORT, tagService.getTag(1).getName());
	}

	@Test
	void testGetTag_NotFound() {
		Mockito.when(tagDao.findTag(ID_ABSENT)).thenReturn(null);
		TagDTO tagActual = tagService.getTag(ID_ABSENT);

		assertNull(tagActual);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.TagServiceImpl#updateTag(long, com.epam.esm.dto.TagDTO)}.
	 */
	@Test
	void testUpdateTag_PositiveResult() {
		TagDTO tagDTO = new TagDTO();
		tagDTO.setName(NAME_SPA);
		Mockito.when(tagDao.updateTag(Mockito.anyLong(), Mockito.any(Tag.class))).thenReturn(new Tag(1, NAME_SPA));

		assertEquals(NAME_SPA, tagService.updateTag(1, tagDTO).getName());
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.TagServiceImpl#deleteTag(long)}.
	 */

	@Test
	void testDeleteTag_NotDeleted_AsBoundWithCertificate() {
		Mockito.when(tagDao.certificatesExistForTag(1)).thenReturn(true);
		IllegalOperationServiceException thrown = assertThrows(IllegalOperationServiceException.class,
				() -> tagService.deleteTag(1), "Expected deleteTag() to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("The tag is bounded with one or more certififcates"));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.service.impl.TagServiceImpl#getMostPopularTagOfUserWithHighestCostOfAllPurchases()}.
	 */
	@Test
	void testGetMostPopularTagOfUserWithHighestCostOfAllPurchases() {
		Mockito.when(tagDao.findMostPopularTagOfUserWithHighestCostOfAllPurchases()).thenReturn(tag1);

		assertEquals(NAME_SPORT, tagService.getMostPopularTagOfUserWithHighestCostOfAllPurchases().getName());
	}

}
