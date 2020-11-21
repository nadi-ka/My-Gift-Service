package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.CertificateDao;
import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.mapper.CertificateResultSetExtractor;
import com.epam.esm.dal.util.SqlQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@Repository
@Transactional
public class CertificateDaoSql implements CertificateDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SqlQueryBuilder sqlBuilder;
	private TagDao tagDao;
	private SessionFactory sessionFactory;

	private static final String SQL_FIND_CERTIFICATES_WITH_TAGS_BY_ID = "SELECT GiftCertificate.Id, "
			+ "GiftCertificate.Name, Description, Price, CreateDate, LastUpdateDate, Duration, "
			+ "Tag.Id, Tag.Name FROM GiftService.GiftCertificate JOIN GiftService.`Tag-Certificate` "
			+ "ON GiftCertificate.Id = `Tag-Certificate`.IdCertificate JOIN GiftService.Tag "
			+ "ON Tag.Id = `Tag-Certificate`.IdTag WHERE GiftCertificate.Id = (?)";
	private static final String SQL_ADD_CERTIFICATE = "INSERT INTO GiftService.GiftCertificate (Name, Description, "
			+ "Price, CreateDate, LastUpdateDate, Duration) VALUES (?,?,?,?,?,?);";
	private static final String SQL_INSERT_INTO_M2M = "INSERT INTO GiftService.`Tag-Certificate` VALUES (?,?);";
	private static final String SQL_UPDATE_CERTIFICATE = "Update GiftService.GiftCertificate set Name = (?), "
			+ "Description = (?), Price = (?), LastUpdateDate = (?), Duration = (?) where Id = (?);";
	private static final String SQL_DELETE_CERTIFICATE_BY_ID = "DELETE FROM GiftService.GiftCertificate WHERE Id = (?);";
	private static final String SQL_DELETE_FROM_M2M = "DELETE FROM GiftService.`Tag-Certificate` WHERE IdCertificate = (?);";
	
	private static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM GiftCertificate WHERE id = :certificateId";
	private static final String PARAM_CERTIFICATE_ID = "certificateId";

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
		long id = (Long) session.save(certificate);
		return session.get(GiftCertificate.class, id);
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
	public List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams) {
//		String sqlQuery = sqlBuilder.buildCertificatesQuery(filterParams, orderParams);
//		try {
//			return jdbcTemplate.query(sqlQuery, new CertificateResultSetExtractor());
//		} catch (DataAccessException e) {
//			return new ArrayList<GiftCertificate>();
//		}
		CriteriaQuery<GiftCertificate> query = sqlBuilder.buildCertificatesQuery(filterParams, orderParams, sessionFactory);
		List<GiftCertificate> certificates = sessionFactory.getCurrentSession().createQuery(query).getResultList();
		return certificates;
	}

	@Override
	public GiftCertificate findCertificate(long id) {
		return sessionFactory.getCurrentSession().get(GiftCertificate.class, id);
	}

	@Override
	public int deleteCertificate(long id) {
		return sessionFactory.getCurrentSession().createQuery(DELETE_CERTIFICATE_BY_ID).setParameter(PARAM_CERTIFICATE_ID, id)
				.executeUpdate();
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
