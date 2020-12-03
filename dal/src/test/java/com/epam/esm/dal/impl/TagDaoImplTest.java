package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.config.DalSpringConfig;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(DalSpringConfig.class)
@EnableAutoConfiguration
class TagDaoImplTest {

	private SessionFactory sessionFactory;
	private Tag tag;
	private static final String NAME_SPA = "#SPA";
	private static final String NAME_SPORT = "#Sport";
	private static final String NAME_ABSENT = "Something";
	private static final long ID_ABSENT = 9999;
	private static final long ID_TAG_TO_UPDATE = 2;

	@Autowired
	private TagDao tagDao = new TagDaoSql(sessionFactory);

	@BeforeEach
	public void setUp() {
		tag = new Tag();
		tag.setName(NAME_SPA);
	}

	@AfterEach
	public void tearDown() {		
		tag = null;
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#addTag(com.epam.esm.entity.Tag)}.
	 */
	@Test
	void testAddTag_PositiveResult() {
		Tag savedTag = tagDao.addTag(tag);

		assertNotNull(savedTag);
		assertEquals(NAME_SPA, tag.getName());
		tagDao.deleteTag(savedTag.getId());
	}

	@Test
	void testAddTag_Not_Created() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> tagDao.addTag(null),
				"Expected addTag() to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("attempt to create saveOrUpdate event with null entity"));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#updateTag(com.epam.esm.entity.Tag)}.
	 */
	@Test
	void testUpdateTag_PositiveResult() {
		Tag actualTag = tagDao.updateTag(ID_TAG_TO_UPDATE, tag);

		assertNotNull(actualTag);
		assertEquals(NAME_SPA, tag.getName());
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#findAllTags(com.epam.esm.entity.Pagination)}.
	 */
	@Test
	void testFindAllTags() {
		Pagination pagination = new Pagination(2, 0);
		List<Tag> actualTags = tagDao.findAllTags(pagination);

		assertNotNull(actualTags);
		assertTrue(actualTags.size() == 2);
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#findTag(long)}.
	 */
	@Test
	void testFindTag_PositiveResult() {
		Tag tag = tagDao.findTag(1);

		assertNotNull(tag);
		assertEquals(1, tag.getId());
		assertEquals(NAME_SPORT, tag.getName());
	}

	@Test
	void testFindTag_NegativеResult_notFound() {
		Tag tag = tagDao.findTag(ID_ABSENT);

		assertNull(tag);
	}

	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#deleteTag(long)}.
	 */
	@Test
	void testDeleteTag_PositiveResult_DeletedSuccessfully() {
		Tag createdTag = tagDao.addTag(tag);
		tagDao.deleteTag(createdTag.getId());
		
		assertNull(tagDao.findTag(createdTag.getId()));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#certificatesExistForTag(long)}.
	 */
	@Test
	void testCertificatesExistForTag_Found() {

		assertTrue(tagDao.certificatesExistForTag(1));
	}

	@Test
	void testCertificatesExistForTag_NotFound() {
		Tag createdTag = tagDao.addTag(tag);

		assertFalse(tagDao.certificatesExistForTag(createdTag.getId()));
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#findTagByName(String)}.
	 */
	@Test
	void testFindTagByName_PositiveResult() {
		Tag tag = tagDao.findTagByName(NAME_SPORT);

		assertNotNull(tag);
		assertEquals(NAME_SPORT, tag.getName());
	}

	@Test
	void testFindTagByName_NegativеResult_notFound() {
		Tag tag = tagDao.findTagByName(NAME_ABSENT);

		assertNull(tag);
	}

	/**
	 * Test method for
	 * {@link com.epam.esm.dal.impl.TagDaoSql#findMostPopularTagOfUserWithHighestCostOfAllPurchases()}.
	 */
	@Test
	void testfindMostPopularTagOfUserWithHighestCostOfAllPurchases() {

		assertNotNull(tagDao.findMostPopularTagOfUserWithHighestCostOfAllPurchases());
	}

}
