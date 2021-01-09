package com.epam.esm.dal.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.util.SqlQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;
import com.epam.esm.transferobj.Pagination;

@Repository("certificateDao")
@Transactional
public class CertificateDaoSql implements CertificateDao {

	private SqlQueryBuilder sqlBuilder;
	private TagDao tagDao;
	@PersistenceContext
	private EntityManager entityManager;

	private static final String GET_SUM_PRICE_OF_CERTIFICATES = "SELECT SUM(price) FROM GiftCertificate c WHERE c.id IN (?1)";
	private static final String GET_CERTIFICATES_AMOUNT_BY_IDS = "SELECT count(id) FROM GiftCertificate c WHERE c.id IN (?1)";

	@Autowired
	public CertificateDaoSql(EntityManager entityManager, SqlQueryBuilder builder, TagDao tagDao) {
		this.entityManager = entityManager;
		this.sqlBuilder = builder;
		this.tagDao = tagDao;
	}

	@Override
	public GiftCertificate addCertificate(GiftCertificate certificate) {
		updateTagsBoundedWithCertificate(certificate);
		entityManager.persist(certificate);
		return certificate;
	}

	@Override
	public GiftCertificate updateCertificate(long certificateId, GiftCertificate certificate) {
		if (certificate.getTags() != null && !certificate.getTags().isEmpty()) {
			updateTagsBoundedWithCertificate(certificate);
		}
		certificate.setId(certificateId);
		return (GiftCertificate) entityManager.merge(certificate);
	}

	@Override
	public List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams,
			Pagination pagination) {
		CriteriaQuery<GiftCertificate> query = sqlBuilder.buildCertificatesFilterOrderQuery(filterParams, orderParams,
				entityManager);
		return entityManager.createQuery(query).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public GiftCertificate findCertificate(long id) {
		return entityManager.find(GiftCertificate.class, id);
	}

	@Override
	public void deleteCertificate(long id) {
		GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);
		entityManager.remove(certificate);
	}

	@Override
	public BigDecimal getCertificatesTotalCost(List<Long> certificateIds) {
		return (BigDecimal) entityManager.createQuery(GET_SUM_PRICE_OF_CERTIFICATES).setParameter(1, certificateIds)
				.getSingleResult();
	}

	@Override
	public boolean certificatesExistForPurchase(List<Long> certificateIds) {
		return (Long) entityManager.createQuery(GET_CERTIFICATES_AMOUNT_BY_IDS).setParameter(1, certificateIds)
				.getSingleResult() == certificateIds.size();
	}

	@Override
	public List<GiftCertificate> findCertificatesByTags(Long[] tagIds, Pagination pagination) {
		CriteriaQuery<GiftCertificate> query = sqlBuilder.buildSearchCertificatesByTagsQuery(tagIds, entityManager);
		return entityManager.createQuery(query).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	private void updateTagsBoundedWithCertificate(GiftCertificate certificate) {
		for (Tag tag : certificate.getTags()) {
			Tag tagToBoundCertificateWith = tagDao.findTagByName(tag.getName());
			if (tagToBoundCertificateWith == null) {
				tagToBoundCertificateWith = tagDao.addTag(tag);
			}
			tag.setId(tagToBoundCertificateWith.getId());
		}
	}

}
