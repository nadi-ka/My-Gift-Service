package com.epam.esm.dal.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.util.SqlQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@Repository
@Transactional
public class CertificateDaoSql implements CertificateDao {

	private SqlQueryBuilder sqlBuilder;
	private TagDao tagDao;
	private SessionFactory sessionFactory;

	private static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM GiftCertificate WHERE id = :certificateId";
	private static final String GET_SUM_PRICE_OF_CERTIFICATES = "SELECT SUM(price) FROM GiftCertificate c WHERE c.id IN (?1)";
	private static final String GET_CERTIFICATES_AMOUNT_BY_IDS = "SELECT count(id) FROM GiftCertificate c WHERE c.id IN (?1)";
	private static final String PARAM_CERTIFICATE_ID = "certificateId";

	private static final Logger LOG = LogManager.getLogger(CertificateDaoSql.class);

	@Autowired
	public CertificateDaoSql(SessionFactory sessionFactory, SqlQueryBuilder builder, TagDao tagDao) {
		this.sessionFactory = sessionFactory;
		this.sqlBuilder = builder;
		this.tagDao = tagDao;
	}

	@Override
	public GiftCertificate addCertificate(GiftCertificate certificate) {
		Session session = sessionFactory.getCurrentSession();
		updateTagsBoundedWithCertificate(certificate);
		session.persist(certificate);
		return certificate;
	}

	@Override
	public GiftCertificate updateCertificate(long certificateId, GiftCertificate certificate) {
		if (certificate.getTags() != null && !certificate.getTags().isEmpty()) {
			updateTagsBoundedWithCertificate(certificate);
		}
		certificate.setId(certificateId);
		return (GiftCertificate) sessionFactory.getCurrentSession().merge(certificate);
	}

	@Override
	public List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams,
			Pagination pagination) {
		CriteriaQuery<GiftCertificate> query = sqlBuilder.buildCertificatesFilterOrderQuery(filterParams, orderParams,
				sessionFactory);
		return sessionFactory.getCurrentSession().createQuery(query).setFirstResult(pagination.getOffset())
				.setMaxResults(pagination.getLimit()).getResultList();
	}

	@Override
	public GiftCertificate findCertificate(long id) {
		return sessionFactory.getCurrentSession().get(GiftCertificate.class, id);
	}

	@Override
	public void deleteCertificate(long id) {
		Session session = sessionFactory.getCurrentSession();
		GiftCertificate certificate = session.find(GiftCertificate.class, id);
		session.remove(certificate);
	}

	@Override
	public Double getSumCertificatesPrice(List<Long> certificateIds) {
		try {
			return (Double) sessionFactory.getCurrentSession().createQuery(GET_SUM_PRICE_OF_CERTIFICATES)
					.setParameter(1, certificateIds).getSingleResult();
		} catch (NoResultException e) {
			return 0.0;
		}
	}

	@Override
	public Long getAmountOfCertificates(List<Long> certificateIds) {
		try {
			return (Long) sessionFactory.getCurrentSession().createQuery(GET_CERTIFICATES_AMOUNT_BY_IDS)
					.setParameter(1, certificateIds).getSingleResult();
		} catch (NoResultException e) {
			return 0L;
		}
	}

	@Override
	public List<GiftCertificate> findCertificatesByTags(Long[] tagIds, Pagination pagination) {
		CriteriaQuery<GiftCertificate> query = sqlBuilder.buildSearchCertificatesByTagsQuery(tagIds, sessionFactory);
		return sessionFactory.getCurrentSession().createQuery(query).setFirstResult(pagination.getOffset())
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
