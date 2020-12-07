package com.epam.esm.dal.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;

@Repository("tagDao")
@Transactional
public class TagDaoSql implements TagDao {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger LOG = LogManager.getLogger(TagDaoSql.class);

	private static final String FIND_TAGS = "FROM Tag";
	private static final String FIND_CERTIFICATE_ID_BY_TAG_ID = "SELECT c "
			+ "FROM GiftCertificate c JOIN c.tags t WHERE t.id  = :tagId";
	private static final String FIND_TAG_BY_NAME = "FROM Tag WHERE name = :tagName";
	private static final String PARAM_TAG_ID = "tagId";
	private static final String PARAM_TAG_NAME = "tagName";
//	private static final String FIND_MOST_POPULAR_TAG = "FROM Tag t "
//			+ "JOIN GiftCertificate gc "
//			+ "JOIN Purchase p "
//			+ "JOIN User u "
//			+ "WHERE u.id = (SELECT t1.Id_user FROM "
//			+ "(SELECT p.Id_user, SUM(p.cost) totalCost "
//			+ "FROM Purchase p GROUP BY p.Id_user) t1 "
//			+ "WHERE t1.totalCost = (SELECT MAX(t2.totalCost) FROM "
//			+ "(SELECT p.Id_user, sum(p.cost) totalCost "
//			+ "FROM Purchase p GROUP BY p.Id_user) t2)) "
//			+ "GROUP BY t.id ORDER BY count(t.id) desc";
	
	private static final String FIND_MOST_POPULAR_TAG = "select t FROM Tag t "
			+ "JOIN t.certificates tc "
			+ "JOIN tc.GiftCertificate gc "
			+ "JOIN gc.purchases gp "
			+ "JOIN gp.Purchase p "
			+ "JOIN User u "
			+ "WHERE u.id = 1";

	@Autowired
	public TagDaoSql(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Tag addTag(Tag tag) {
		entityManager.persist(tag);
		return tag;
	}

	@Override
	public Tag updateTag(long tagId, Tag tag) {
		tag.setId(tagId);
		return (Tag) entityManager.merge(tag);
	}

	@Override
	public List<Tag> findAllTags(Pagination pagination) {
		return entityManager.createQuery(FIND_TAGS, Tag.class).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public Tag findTag(long id) {
		return entityManager.find(Tag.class, id);
	}

	@Override
	public void deleteTag(long id) {
		Tag tag = entityManager.find(Tag.class, id);
		entityManager.remove(tag);
	}

	@Override
	public boolean certificatesExistForTag(long tagId) {
		TypedQuery<GiftCertificate> query = entityManager.createQuery(FIND_CERTIFICATE_ID_BY_TAG_ID,
				GiftCertificate.class);
		query.setParameter(PARAM_TAG_ID, tagId);
		query.setMaxResults(1);
		return (!query.getResultList().isEmpty());
	}

	@Override
	public Tag findTagByName(String name) {
		List<Tag> tags = entityManager.createQuery(FIND_TAG_BY_NAME, Tag.class)
				.setParameter(PARAM_TAG_NAME, name).getResultList();
		if (tags.isEmpty()) {
			return null;
		}
		return tags.get(0);
	}

	@Override
	public Tag findMostPopularTagOfUserWithHighestCostOfAllPurchases() {
//		List<Tag> tags = entityManager.createNativeQuery(FIND_MOST_POPULAR_TAG, Tag.class)
//				.getResultList();
//		if (tags.isEmpty()) {
//			return null;
//		}
//		return tags.get(0);
		
//		return (Tag)entityManager.createQuery(FIND_MOST_POPULAR_TAG, Tag.class).setFirstResult(0)
//				.setMaxResults(1).getResultList();
		
		List<Tag> tags = entityManager.createQuery(FIND_MOST_POPULAR_TAG, Tag.class).setFirstResult(0)
				.setMaxResults(1).getResultList();
		return tags.get(0);
	}

}
