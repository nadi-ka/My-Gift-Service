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
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.Pagination;

@Repository("tagDao")
@Transactional
public class TagDaoSql implements TagDao {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger LOG = LogManager.getLogger(TagDaoSql.class);

	private static final String FIND_TAGS = "FROM Tag";
	private static final String FIND_CERTIFICATE_FOR_TAG = "SELECT c "
			+ "FROM GiftCertificate c JOIN c.tags t WHERE t.id  = :tagId";
	private static final String FIND_TAG_BY_NAME = "SELECT t FROM Tag t WHERE name = :tagName";
	private static final String PARAM_TAG_ID = "tagId";
	private static final String PARAM_TAG_NAME = "tagName";
	private static final String FIND_MOST_POPULAR_TAG = "SELECT Tag.Id, Tag.Name, Tag.Created_on, Tag.Updated_on "
			+ "FROM Certificate_service.Tag "
			+ "JOIN Certificate_service.Tag_Certificate ON Tag.Id = Tag_Certificate.IdTag "
			+ "JOIN Certificate_service.GiftCertificate ON Tag_Certificate.IdCertificate = GiftCertificate.Id "
			+ "JOIN Certificate_service.Purchase_Certificate ON GiftCertificate.Id = Purchase_Certificate.Id_certificate "
			+ "JOIN Certificate_service.Purchase ON Purchase_Certificate.Id_order = Purchase.Id "
			+ "JOIN Certificate_service.User ON Purchase.Id_user = User.Id WHERE User.Id = " + "(SELECT t.Id_user FROM "
			+ "(SELECT Purchase.Id_user, sum(Purchase.Cost) AS totalCost "
			+ "FROM Certificate_service.Purchase GROUP BY Purchase.Id_user) t "
			+ "WHERE t.totalCost = (SELECT max(t1.totalCost) FROM "
			+ "(SELECT Purchase.Id_user, sum(Purchase.Cost) AS totalCost "
			+ "FROM Certificate_service.Purchase GROUP BY Purchase.Id_user) t1)) "
			+ "GROUP BY Tag.Id ORDER BY count(Tag.Id) desc";

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
		TypedQuery<GiftCertificate> query = entityManager.createQuery(FIND_CERTIFICATE_FOR_TAG, GiftCertificate.class)
				.setParameter(PARAM_TAG_ID, tagId).setMaxResults(1);
		return (!query.getResultList().isEmpty());
	}

	@Override
	public Tag findTagByName(String name) {
		List<Tag> tags = entityManager.createQuery(FIND_TAG_BY_NAME, Tag.class).setParameter(PARAM_TAG_NAME, name)
				.getResultList();
		if (tags.isEmpty()) {
			return null;
		}
		return tags.get(0);
	}

	@Override
	public Tag findMostPopularTagOfUserWithHighestCostOfAllPurchases() {
		List<Tag> tags = entityManager.createNativeQuery(FIND_MOST_POPULAR_TAG, Tag.class).setFirstResult(0)
				.setMaxResults(1).getResultList();
		return (tags.isEmpty() ? null : tags.get(0));
	}

}
