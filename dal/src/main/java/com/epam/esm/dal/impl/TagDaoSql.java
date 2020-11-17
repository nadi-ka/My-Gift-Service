package com.epam.esm.dal.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.TagDao;
import com.epam.esm.entity.Tag;

@Repository
@Transactional
public class TagDaoSql implements TagDao {

	private SessionFactory sessionFactory;

	private static final String FIND_TAGS = "FROM Tag";
//	private static final String FIND_CERTIFICATE_ID_BY_TAG_ID = "SELECT IdCertificate "
//			+ "FROM `Tag-Certificate` WHERE IdTag = (?) LIMIT 1;";
	private static final String FIND_TAG_BY_NAME = "FROM Tag WHERE Name = :tagName;";
	private static final String DELETE_TAG_BY_ID = "DELETE FROM Tag WHERE Id = :tagId";

	private static Logger logger = LogManager.getLogger(TagDaoSql.class);

	@Autowired
	public TagDaoSql(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Tag addTag(Tag tag) {
		Session session = sessionFactory.getCurrentSession();
		long id = (Long) session.save(tag);
		tag.setId(id);
		return tag;
	}

	@Override
	public Tag updateTag(long tagId, Tag tag) {
		tag.setId(tagId);
		return (Tag) sessionFactory.getCurrentSession().merge(tag);
	}

	@Override
	public List<Tag> findAllTags() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery(FIND_TAGS, Tag.class).getResultList();
	}

	@Override
	public Tag findTag(long id) {
		return sessionFactory.getCurrentSession().get(Tag.class, id);
	}

	@Override
	public int deleteTag(long id) {
		return sessionFactory.getCurrentSession().createQuery(DELETE_TAG_BY_ID).setParameter("tagId", id)
				.executeUpdate();
	}

	@Override
	public boolean certificatesExistForTag(long tagId) {
		Tag tag = sessionFactory.getCurrentSession().get(Tag.class, tagId);
		return !tag.getCertificates().isEmpty();
	}

	@Override
	public Tag findTagByName(String name) {
		return sessionFactory.getCurrentSession().createQuery(FIND_TAG_BY_NAME, Tag.class).setParameter("tagName", name)
				.getSingleResult();
	}

}
