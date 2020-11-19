package com.epam.esm.dal.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.config.DalSpringConfig;
import com.epam.esm.entity.Tag;

@ActiveProfiles("test")
@DataJpaTest
@SpringJUnitConfig(DalSpringConfig.class)
@EnableAutoConfiguration
class TagDaoImplTest {
	
	private SessionFactory sessionFactory;

	@Autowired
	private TagDao tagDao = new TagDaoSql(sessionFactory);
//	private EmbeddedDatabase db;
//	
//	@BeforeAll
//
//	@BeforeEach
//	public void setUp() {
//		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//				.setName("testDB;DATABASE_TO_UPPER=false;CASE_INSENSITIVE_IDENTIFIERS=TRUE;IGNORECASE=TRUE;MODE=MySQL")
//				.addScript("create-db.sql").addScript("insert-data.sql").build();
//	}
//
//	@AfterEach
//	public void tearDown() {		
//		db.shutdown();
//	}
//
//	/**
//	 * Test method for
//	 * {@link com.epam.esm.dal.impl.TagDaoSql#addTag(com.epam.esm.entity.Tag)}.
//	 */
//	@Test
//	void testAddTag_PositiveResult() {
//		
//		TagDaoSql tagDao = getTagDao();
//		Tag tag = new Tag();
//		tag.setName("SPA");
//		Tag savedTag = tagDao.addTag(tag);
//		
//		assertNotNull(savedTag);
//		assertEquals(3, tag.getId());
//		assertEquals("SPA", tag.getName());
//	}
//	
////	@Test
////	void testAddTag_DataAccessExceptionExpected() {
////		
////		TagDaoSql tagDao = getTagDao();
////
////		DataAccessException thrown = assertThrows(DataAccessException.class, () -> tagDao.addTag(new Tag(3, null)),
////		"Expected addTag() to throw, but it didn't");
////
////		assertTrue(thrown.getMessage().contains("NULL not allowed for column \"Name\""));
////	}
////		
////	/**
////	 * Test method for
////	 * {@link com.epam.esm.dal.impl.TagDaoSql#updateTag(com.epam.esm.entity.Tag)}.
////	 */
////	@Test
////	void testUpdateTag_PositiveResult() {
////		
////		TagDaoSql tagDao = getTagDao();
////		Tag tag = new Tag();
////		tag.setName("SPA");
////		tagDao.updateTag(1, tag);
////		Tag actualTag = tagDao.findTag(1);
////		
////		assertNotNull(actualTag);
////		assertEquals("SPA", tag.getName());
////	}
////	
//////	@Test
//////	void testUpdateTag_NegativeResult_NotUpdated() {
//////		
//////		TagDaoSql tagDao = getTagDao();
//////		Tag tag = new Tag();
//////		tag.setName("SPA");
//////		int affectedRowsActual = tagDao.updateTag(999, tag);
//////
//////		assertEquals(0, affectedRowsActual);
//////	}
////		
////
	/**
	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#findAllTags()}.
	 */
	@Test
	void testFindAllTags() {

//		TagDaoSql tagDao = getTagDao();
		List<Tag> actualTags = tagDao.findAllTags();

		assertNotNull(actualTags);
//		assertEquals(2, actualTags.size());
	}
////
////	/**
////	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#findTag(long)}.
////	 */
////	@Test
////	void testFindTag_PositiveResult() {
////
////		TagDaoSql tagDao = getTagDao();
////		Tag tag = tagDao.findTag(1);
////
////		assertNotNull(tag);
////		assertEquals(1, tag.getId());
////		assertEquals("#Sport", tag.getName());
////	}
////
////	@Test
////	void testFindTag_NegativaResult_notFound() {
////
////		TagDaoSql tagDao = getTagDao();
////		Tag tag = tagDao.findTag(9999);
////		
////		assertNull(tag);
////
////	}
////
////	/**
////	 * Test method for {@link com.epam.esm.dal.impl.TagDaoSql#deleteTag(long)}.
////	 */
////	@Test
////	void testDeleteTag_PositiveResult_DeletedSuccessfully() {
////		
////		TagDaoSql tagDao = getTagDao();
////		tagDao.addTag(new Tag(3, "SPA"));
////		int affectedRowsActual = tagDao.deleteTag(3);
////		
////		assertEquals(1, affectedRowsActual);
////		
////	}
////	
////	@Test
////	void testDeleteTag_NegativeResult_TagAbsent() {
////		
////		TagDaoSql tagDao = getTagDao();
////		int affectedRowsActual = tagDao.deleteTag(999);
////		
////		assertEquals(0, affectedRowsActual);
////		
////	}
////
////	/**
////	 * Test method for
////	 * {@link com.epam.esm.dal.impl.TagDaoSql#certificatesExistForTag(long)}.
////	 */
////	@Test
////	void testCertificatesExistForTag_Found() {
////		
////		TagDaoSql tagDao = getTagDao();
////		boolean id = tagDao.certificatesExistForTag(1);
////		
////		assertTrue(id);
////	}
////	
////	@Test
////	void testCertificatesExistForTag_NotFound() {
////		
////		TagDaoSql tagDao = getTagDao();
////		tagDao.addTag(new Tag(3, "SPA"));
////		boolean id = tagDao.certificatesExistForTag(3);
////		
////		assertFalse(id);
////	}
////	
////	private TagDaoSql getTagDao() {
////		
////		JdbcTemplate template = new JdbcTemplate(db);
////		TagDaoSql tagDao = new TagDaoSql(template);
////		return tagDao;
////	}

}
