package com.epam.esm.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.epam.esm.dal.TagDao;
import com.epam.esm.dal.constant.ColumnNameHolder;
import com.epam.esm.entity.Tag;

@Repository
public class TagDaoSql implements TagDao {

	private final JdbcTemplate jdbcTemplate;
	private RowMapper<Tag> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
		return new Tag(resultSet.getLong(ColumnNameHolder.TAG_ID), resultSet.getString(ColumnNameHolder.TAG_NAME));
	};

	private static final String SQL_FIND_TAGS = "SELECT * FROM GiftService.Tag;";
	private static final String SQL_FIND_TAG_BY_ID = "SELECT * FROM GiftService.Tag WHERE Id = (?)";
	private static final String SQL_FIND_TAG_BY_NAME = "SELECT * FROM GiftService.Tag WHERE Name = (?);";
	private static final String SQL_ADD_TAG = "INSERT INTO GiftService.Tag (Name) VALUES (?)";
	private static final String SQL_UPDATE_TAG = "Update GiftService.Tag set Name = (?) where Id = (?);";
	private static final String SQL_DELETE_TAG_BY_ID = "DELETE FROM  GiftService.Tag WHERE Id = (?);";
	private static final String SQL_FIND_CERTIFICATE_ID_BY_TAG_ID = "SELECT IdCertificate FROM  "
			+ "GiftService.`Tag-Certificate` WHERE IdTag = (?) LIMIT 1;";

	@Autowired
	public TagDaoSql(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Tag addTag(Tag tag) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String paramToReturn = "Id";
				PreparedStatement preparedStatement = con.prepareStatement(SQL_ADD_TAG, new String[] { paramToReturn });
				preparedStatement.setString(1, tag.getName());
				return preparedStatement;
			}
		}, keyHolder);
		long newTagId = keyHolder.getKey().longValue();
		tag.setId(newTagId);
		return tag;
	}

	@Override
	public int updateTag(long tagId, Tag tag) {
		return jdbcTemplate.update(SQL_UPDATE_TAG, tag.getName(), tagId);
	}

	@Override
	public List<Tag> findAllTags() {
		try {
			return jdbcTemplate.query(SQL_FIND_TAGS, ROW_MAPPER);
		} catch (DataAccessException e) {
			// nothing was found by the request
			return new ArrayList<Tag>();
		}
	}

	@Override
	public Tag findTag(long id) {
		try {
			return jdbcTemplate.queryForObject(SQL_FIND_TAG_BY_ID, new Object[] { id }, ROW_MAPPER);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public int deleteTag(long id) {
		int[] types = { Types.BIGINT };
		return jdbcTemplate.update(SQL_DELETE_TAG_BY_ID, new Object[] { id }, types);
	}

	@Override
	public boolean certificatesExistForTag(long tagId) {
		long certificateId;
		try {
			certificateId = (Long) jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE_ID_BY_TAG_ID, new Object[] { tagId },
					Long.class);
		} catch (DataAccessException e) {
			certificateId = 0;
		}
		return (certificateId != 0);
	}

	@Override
	public Tag findTagByName(String name) {
		Tag tag;
		try {
			tag = jdbcTemplate.queryForObject(SQL_FIND_TAG_BY_NAME, new Object[] { name }, ROW_MAPPER);
		} catch (DataAccessException e) {
			tag = null;
		}
		return tag;
	}

}
