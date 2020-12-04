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
	private static final String FIND_MOST_POPULAR_TAG = "SELECT Tag.Id, Tag.Name, Tag.Created_on, Tag.Updated_on "
			+ "FROM Certificate_service.Tag "
			+ "JOIN Certificate_service.Tag_Certificate ON Tag.Id = Tag_Certificate.IdTag "
			+ "JOIN Certificate_service.GiftCertificate ON Tag_Certificate.IdCertificate = GiftCertificate.Id "
			+ "JOIN Certificate_service.Purchase_Certificate ON GiftCertificate.Id = Purchase_Certificate.Id_certificate "
			+ "JOIN Certificate_service.Purchase ON Purchase_Certificate.Id_order = Purchase.Id "
			+ "JOIN Certificate_service.User ON Purchase.Id_user = User.Id "
			+ "WHERE User.Id = (SELECT User.Id FROM Certificate_service.User "
			+ "ORDER BY (SELECT sum(Cost) FROM Certificate_service.Purchase "
			+ "WHERE Id_user = User.Id) desc LIMIT 1) GROUP BY Tag.Id "
			+ "ORDER BY count(Tag.Id) desc LIMIT 1";

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
		List<Tag> tags = entityManager.createNativeQuery(FIND_MOST_POPULAR_TAG, Tag.class)
				.getResultList();
		if (tags.isEmpty()) {
			return null;
		}
		return tags.get(0);
	}

}
