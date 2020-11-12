package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.epam.esm.dal.mapper.CertificateMapper;
import com.epam.esm.dal.mapper.CertificateWithTagsMapper;
import com.epam.esm.dal.util.DuplicateResultsRemover;
import com.epam.esm.dal.util.SqlQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.transferobj.FilterParam;
import com.epam.esm.transferobj.OrderParam;

@Repository
public class CertificateDaoSql implements CertificateDao {

	private final JdbcTemplate jdbcTemplate;
	private SqlQueryBuilder sqlBuilder;
	private TagDao tagDao;

	private static final String sqlFindCertificateById = "SELECT * FROM GiftService.GiftCertificate WHERE Id = (?)";
	private static final String sqlAddCertificate = "INSERT INTO GiftService.GiftCertificate (Name, Description, "
			+ "Price, CreateDate, LastUpdateDate, Duration) VALUES (?,?,?,?,?,?);";
	private static final String sqlInsertIntoM2M = "INSERT INTO GiftService.`Tag-Certificate` VALUES (?,?);";
	private static final String sqlUpdateCertificate = "Update GiftService.GiftCertificate set Name = (?), "
			+ "Description = (?), Price = (?), LastUpdateDate = (?), Duration = (?) where Id = (?);";
	private static final String sqlDeleteCertificateById = "DELETE FROM GiftService.GiftCertificate WHERE Id = (?);";
	private static final String sqlDeleteFromM2M = "DELETE FROM GiftService.`Tag-Certificate` WHERE IdCertificate = (?);";

	@Autowired
	public CertificateDaoSql(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}

	@Autowired
	public void setSqlBuilder(SqlQueryBuilder sqlBuilder) {
		this.sqlBuilder = sqlBuilder;
	}

	@Transactional
	@Override
	public GiftCertificate addCertificate(GiftCertificate certificate) {
		LocalDateTime lastUpdateDate = certificate.getLastUpdateDate();
		Timestamp lastUpdateTimestamp = ((lastUpdateDate == null) ? null : Timestamp.valueOf(lastUpdateDate));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String paramToReturn = "Id";
				PreparedStatement preparedStatement = con.prepareStatement(sqlAddCertificate,
						new String[] { paramToReturn });
				preparedStatement.setString(1, certificate.getName());
				preparedStatement.setString(2, certificate.getDescription());
				preparedStatement.setDouble(3, certificate.getPrice());
				preparedStatement.setTimestamp(4, Timestamp.valueOf(certificate.getCreationDate()));
				preparedStatement.setTimestamp(5, lastUpdateTimestamp);
				preparedStatement.setLong(6, certificate.getDuration());
				return preparedStatement;
			}
		}, keyHolder);
		long newSertificateId = keyHolder.getKey().longValue();
		certificate.setId(newSertificateId);
		updateTagsBoundedWithCertificate(certificate);
		for (Tag tag : certificate.getTags()) {
			jdbcTemplate.update(sqlInsertIntoM2M, tag.getId(), newSertificateId);
		}
		return certificate;
	}

	@Transactional
	@Override
	public GiftCertificate updateCertificate(GiftCertificate certificate) {
		int affectedRows = jdbcTemplate.update(sqlUpdateCertificate, certificate.getName(),
				certificate.getDescription(), certificate.getPrice(), certificate.getLastUpdateDate(),
				certificate.getDuration(), certificate.getId());
		if (certificate.getTags() != null && !certificate.getTags().isEmpty()) {
			jdbcTemplate.update(sqlDeleteFromM2M, certificate.getId());
			updateTagsBoundedWithCertificate(certificate);
			for (Tag tag : certificate.getTags()) {
				jdbcTemplate.update(sqlInsertIntoM2M, tag.getId(), certificate.getId());
			}
		}
		if (affectedRows == 0) {
			return null;
		}
		return certificate;
	}

	@Override
	public List<GiftCertificate> findCertificates(List<FilterParam> filterParams, List<OrderParam> orderParams) {
		String sqlQuery = sqlBuilder.buildCertificatesQuery(filterParams, orderParams);
		try {
			List<GiftCertificate> certificates = jdbcTemplate.query(sqlQuery, new CertificateWithTagsMapper());
			certificates = DuplicateResultsRemover.removeDuplicateResults(certificates);
			return certificates;
		} catch (DataAccessException e) {
			// nothing was found by the request;
			return new ArrayList<GiftCertificate>();
		}
	}

	@Override
	public GiftCertificate findCertificate(long id) {
		try {
			return jdbcTemplate.queryForObject(sqlFindCertificateById, new Object[] { id },
					new CertificateMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Transactional
	@Override
	public int[] deleteCertificate(long id) {
		int[] types = { Types.BIGINT };
		int affectedRows1 = jdbcTemplate.update(sqlDeleteFromM2M, new Object[] { id }, types);
		int affectedRows2 = jdbcTemplate.update(sqlDeleteCertificateById, new Object[] { id }, types);
		return new int[] { affectedRows1, affectedRows2 };
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
