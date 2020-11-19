package com.epam.esm.dal.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

@Repository
@Transactional
public class TagDaoSql implements TagDao {

	private SessionFactory sessionFactory;

	private static final String FIND_TAGS = "FROM Tag";
	private static final String FIND_CERTIFICATE_ID_BY_TAG_ID = "SELECT c "
			+ "FROM GiftCertificate c JOIN c.tags t WHERE t.id  = :tagId";
	private static final String FIND_TAG_BY_NAME = "FROM Tag WHERE Name = :tagName;";
	private static final String DELETE_TAG_BY_ID = "DELETE FROM Tag WHERE Id = :tagId";
	private static final String PARAM_TAG_ID = "tagId";
	private static final String PARAM_TAG_NAME = "tagName";

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
		return sessionFactory.getCurrentSession().createQuery(DELETE_TAG_BY_ID).setParameter(PARAM_TAG_ID, id)
				.executeUpdate();
	}

	@Override
	public boolean certificatesExistForTag(long tagId) {
		Query<GiftCertificate> query = sessionFactory.getCurrentSession().createQuery(FIND_CERTIFICATE_ID_BY_TAG_ID,
				GiftCertificate.class);
		query.setParameter(PARAM_TAG_ID, tagId);
		query.setMaxResults(1);
		return (!query.getResultList().isEmpty());
	}

	@Override
	public Tag findTagByName(String name) {
		try {
			return sessionFactory.getCurrentSession().createQuery(FIND_TAG_BY_NAME, Tag.class)
					.setParameter(PARAM_TAG_NAME, name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
